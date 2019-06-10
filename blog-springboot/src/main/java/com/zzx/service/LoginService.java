package com.zzx.service;

import com.zzx.dao.LoginDao;
import com.zzx.dao.UserDao;
import com.zzx.model.pojo.Login;
import com.zzx.model.pojo.User;
import com.zzx.utils.DateUtil;
import com.zzx.utils.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;


@Service
public class LoginService {

    @Autowired
    private LoginDao loginDao;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private DateUtil dateUtil;

    @Autowired
    private RequestUtil requestUtil;

    /**
     * 保存登录信息
     * @param user
     */
    @Transactional
    public void saveLoginInfo(User user) {

        user = userDao.findUserByName(user.getName());
        //删除原纪录
        loginDao.deleteLoginByUserId(user.getId());
        //新增一条
        Login login = new Login();
        login.setUser(user);
        login.setIp(requestUtil.getIpAddress(request));
        login.setTime(dateUtil.getCurrentDate());
        loginDao.saveLogin(login);
    }

}
