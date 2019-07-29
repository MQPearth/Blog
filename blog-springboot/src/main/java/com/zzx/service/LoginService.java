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
     *
     * @param user
     */
    @Transactional
    public void saveLoginInfo(User user) {

        user = userDao.findUserByName(user.getName());
        Login login = new Login();
        login.setUser(user);//绑定用户
        login.setIp(requestUtil.getIpAddress(request));//获取操作ip
        login.setTime(dateUtil.getCurrentDate());//操作时间
        loginDao.updateLogin(login);

    }

}
