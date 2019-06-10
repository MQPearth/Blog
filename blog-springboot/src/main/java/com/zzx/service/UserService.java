package com.zzx.service;


import com.zzx.config.JwtConfig;
import com.zzx.dao.CodeDao;
import com.zzx.dao.MailkeyDao;
import com.zzx.dao.RoleDao;
import com.zzx.dao.UserDao;
import com.zzx.model.entity.Result;
import com.zzx.model.entity.StatusCode;
import com.zzx.model.pojo.Code;
import com.zzx.model.pojo.Mailkey;
import com.zzx.model.pojo.Role;
import com.zzx.model.pojo.User;
import com.zzx.utils.DateUtil;
import com.zzx.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private CodeDao codeDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private MailkeyDao mailkeyDao;

    @Autowired
    private RoleService roleService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private DateUtil dateUtil;

    @Autowired
    private JwtConfig jwtConfig;


    /**
     * 登录
     * 返回token，用户名，用户角色
     *
     * @param user
     * @return
     */
    public Map login(User user) {
        Map<String, Object> map = new HashMap<>();

        try {
            UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(user.getName(), user.getPassword());
            final Authentication authentication = authenticationManager.authenticate(upToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final UserDetails userDetails = this.loadUserByUsername(user.getName());
            final String token = jwtTokenUtil.generateToken(userDetails);
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            List<String> roles = new ArrayList<>();
            for (GrantedAuthority authority : authorities) {
                roles.add(authority.getAuthority());
            }
            map.put("token", jwtConfig.getPrefix() + token);
            map.put("name", user.getName());
            map.put("roles", roles);
            return map;
        } catch (AuthenticationException e) {
            //认证失败，不返回token
            return null;
        }

    }

    /**
     * 注册
     *
     * @param userToAdd
     */
    @Transactional
    public void register(User userToAdd, String mailCode, String inviteCode) throws RuntimeException {

        //查询邮箱验证码是否有效
        Mailkey mailkey = mailkeyDao.findMailkeyByCodeAndMail(mailCode, userToAdd.getMail());

        //无效 throw 异常
        if (mailkey == null || dateUtil.dateDiffMinute(mailkey.getSendTime()) > 5)
            throw new RuntimeException("验证码无效");

        //有效
        //查询邀请码是否有效
        Code code = codeDao.findCodeById(inviteCode);

        if (code == null || code.getState() != 0)
            //无效 throw 异常
            throw new RuntimeException("邀请码无效");

        //有效 保存用户
        final String username = userToAdd.getName();
        if (userDao.findUserByName(username) != null) {
            throw new RuntimeException("用户名已存在");
        }

        if (userDao.findUserByMail(userToAdd.getMail()) != null)
            throw new RuntimeException("邮箱已使用");

        List<Role> roles = new ArrayList<>(1);
        roles.add(roleService.findRoleByName("USER"));
        userToAdd.setRoles(roles);//新注册用户赋予USER权限

        final String rawPassword = userToAdd.getPassword();
        userToAdd.setPassword(encoder.encode(rawPassword));//加密密码
        userToAdd.setState(1);//正常状态
        userDao.saveUser(userToAdd);//保存角色
        //保存该用户的所有角色

        for (Role role : roles) {
            roleDao.saveUserRoles(userToAdd.getId(), role.getId());
        }
        // 更新邀请码状态 删除验证码
        code.setUser(userToAdd);
        code.setState(1);
        codeDao.updateCode(code);
        mailkeyDao.deleteMailkeyByMail(userToAdd.getMail());
    }


    /**
     * 根据用户名查询用户及用户角色
     *
     * @param name
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userDao.findUserByName(name);
        if (user == null) {  //查询不到用户时，判定这是个非法token，但是仍然返回 不抛异常
            return new org.springframework.security.core.userdetails.User("NORMAL", "NORMAL", null);
            //也可将异常抛出
//            throw new UsernameNotFoundException("USER NOT FOUND");
        }

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        //用于添加用户的权限。将用户权限添加到authorities
        if ((user.getState() == 1)) {
            List<Role> roles = roleDao.findUserRoles(user.getId());
            for (Role role : roles) {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            }
        } else {  //该用户被封禁
            authorities.add(new SimpleGrantedAuthority("NORMAL"));
        }
        //查询到了用户，即用户进行了操作或登录动作，记录在登录表
        loginService.saveLoginInfo(user);
        return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(), authorities);

    }

    /**
     * 封禁或解禁用户
     *
     * @param id
     * @param state
     */
    public void updateUserState(Integer id, Integer state) {
        User user = new User();
        user.setId(id);
        user.setState(state);
        userDao.updateUserState(user); //更改用户状态
//        //删除/增加用户user权限
//
//        if (state == 0) {
//            //删除该用户的角色
//        } else {
//
//        }
//        Role role = roleDao.findRoleByName("USER");

    }


    /**
     * 创建管理员
     *
     * @param user
     */
    public void createAdmin(User user) {

        final String username = user.getName();
        if (userDao.findUserByName(username) != null)
            throw new RuntimeException("用户名已存在");

        user.setRoles(roleService.findAllRole());//管理员默认拥有所有权限
        final String rawPassword = user.getPassword();
        user.setPassword(encoder.encode(rawPassword));//加密密码
        user.setState(1);//正常状态
        userDao.saveUser(user);//保存角色

        //保存该用户的所有角色
        List<Role> roles = user.getRoles();
        for (Role role : roles) {
            roleDao.saveUserRoles(user.getId(), role.getId());
        }
    }

    /**
     * 修改用户密码
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @param code        邮箱验证码
     */
    @Transactional
    public void updateUserPassword(String oldPassword, String newPassword, String code) {
        //校验原密码
        String name = jwtTokenUtil.getUsernameFromRequest(request);
        User user = new User();
        user.setName(name);

        user = userDao.findUserByName(user.getName()); //
        if (!encoder.matches(oldPassword, user.getPassword()))
            throw new RuntimeException("密码错误");

        //校验邮箱验证码
        Mailkey mailkey = mailkeyDao.findMailkeyByCodeAndMail(code, user.getMail());
        if (mailkey == null || dateUtil.dateDiffMinute(mailkey.getSendTime()) > 5)
            throw new RuntimeException("验证码无效");
        //更新密码
        user.setPassword(encoder.encode(newPassword));
        userDao.updateUserPasswordById(user);
        //删除验证码
        mailkeyDao.deleteMailkeyByMail(user.getMail());

    }

    /**
     * 更新用户邮箱
     *
     * @param newMail     新邮箱
     * @param oldMailCode 旧邮箱验证码
     * @param newMailCode 新邮箱验证码
     */
    @Transactional
    public void updateUserMail(String newMail, String oldMailCode, String newMailCode) {

        //获取向旧邮箱发出的验证码
        String userName = jwtTokenUtil.getUsernameFromRequest(request);
        User user = userDao.findUserByName(userName);
        Mailkey mailkey = mailkeyDao.findMailkeyByCodeAndMail(oldMailCode, user.getMail());

        //与用户输入的旧邮箱验证码进行匹配
        if (mailkey == null || dateUtil.dateDiffMinute(mailkey.getSendTime()) > 5)
            throw new RuntimeException("旧邮箱无效验证码");

        //检查新邮箱是否已存在
        if (userDao.findUserByMail(newMail) != null)
            throw new RuntimeException("此邮箱已使用");
        //获取向新邮箱发出的验证码
        mailkey = mailkeyDao.findMailkeyByCodeAndMail(newMailCode, newMail);
        //校验新邮箱验证码
        if (mailkey == null || dateUtil.dateDiffMinute(mailkey.getSendTime()) > 5)
            throw new RuntimeException("新邮箱无效验证码");

        String oldMail = user.getMail();
        user.setMail(newMail);
        //更新用户邮箱信息
        userDao.updateUserMailById(user);
        //删除两个验证码
        mailkeyDao.deleteMailkeyByMail(oldMail);
        mailkeyDao.deleteMailkeyByMail(user.getMail());


    }

    /**
     * 重置密码
     *
     * @param userName
     * @param mailCode
     * @param newPassword
     */
    @Transactional
    public void forgetPassword(String userName, String mailCode, String newPassword) {
        User user = userDao.findUserByName(userName);
        if (user == null)
            throw new RuntimeException("用户名不存在");

        Mailkey mailkey = mailkeyDao.findMailkeyByCodeAndMail(mailCode, user.getMail());

        //与用户输入的邮箱验证码进行匹配
        if (mailkey == null || dateUtil.dateDiffMinute(mailkey.getSendTime()) > 5)
            throw new RuntimeException("无效验证码");
        user.setPassword(encoder.encode(newPassword));
        userDao.updateUserPasswordById(user);//更新密码
        mailkeyDao.deleteMailkeyByMail(user.getMail());//删除发送的验证码

    }

    /**
     * 根据用户名分页搜索用户
     *
     * @param userName
     * @return
     */
    public List<User> searchUserByName(String userName, Integer page, Integer showCount) {
        return userDao.searchUserByName(userName, (page - 1) * showCount, showCount);
    }

    /**
     * 分页查询用户
     *
     * @param page
     * @param showCount
     * @return
     */
    public List<User> findUser(Integer page, Integer showCount) {
        return userDao.findUser((page - 1) * showCount, showCount);
    }

    /**
     * 查询用户数
     *
     * @return
     */
    public Long getUserCount() {
        return userDao.getUserCount();
    }


    /**
     * 查询用户邮箱
     *
     * @return
     */
    public String findUserMail() {
        User user = userDao.findUserByName(jwtTokenUtil.getUsernameFromRequest(request));
        return user.getMail();
    }

    /**
     * 模糊查询用户名 返回记录数
     *
     * @param userName
     * @return
     */
    public Long getUserCountByName(String userName) {
        return userDao.getUserCountByName(userName);
    }
}
