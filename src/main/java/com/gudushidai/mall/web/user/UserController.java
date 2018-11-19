package com.gudushidai.mall.web.user;

import com.gudushidai.mall.entity.User;
import com.gudushidai.mall.entity.pojo.ResultBean;
import com.gudushidai.mall.service.UserService;
import com.gudushidai.mall.service.exception.LoginException;
import com.gudushidai.mall.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 打开注册页面
     *
     * @return
     */
    @RequestMapping("/toRegister.html")
    public String toRegister() {
        return "mall/user/register";
    }

    /**
     * 打开登录页面
     *
     * @return
     */
    @RequestMapping("/toLogin.html")
    public String toLogin() {
        return "mall/user/login";
    }

    /**
     * 登录
     *
     * @param username
     * @param password
     */
    @RequestMapping("/login.do")
    public void login(String username,
                      String password,
                      HttpServletRequest request,
                      HttpServletResponse response) throws IOException {
        User user = userService.checkLogin(username, password);
        if (user != null) {
            //登录成功 重定向到首页
            request.getSession().setAttribute("user", user);
            response.sendRedirect("/mall/index.html");
        } else {
            throw new LoginException("登录失败！ 用户名或者密码错误");
        }

    }

    /**
     * 注册
     */
    @RequestMapping("/register.do")
    public void register(String username,
                         String password,
                         String name,
                         String phone,
                         String email,
                         String addr,
                         HttpServletResponse response) throws IOException {
        User user = new User();
        user.setUsername(username);
        user.setPhone(phone);
        try {
            password = Md5Util.EncoderByMd5(password);
        }catch (Exception e){
            e.printStackTrace();
        }
        user.setPassword(password);
        user.setName(name);
        user.setEmail(email);
        user.setAddr(addr);
        userService.create(user);
//        if(result == 0){
//            response.sendRedirect("/mall/user/toRegister.html?regist=0&username="+username+"&password="+password+"&name="+name+"&phone="+phone+"&email="+email+"&addr="+addr);
//            return;
//        }
        // 注册完成后重定向到登录页面
        response.sendRedirect("/mall/user/toLogin.html");
    }

    /**
     * 登出
     */
    @RequestMapping("/logout.do")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().removeAttribute("user");
        response.sendRedirect("/mall/index.html");
    }

    /**
     * 验证用户名是否唯一
     * @param username
     * @return
     */
    @ResponseBody
    @RequestMapping("/checkUsername.do")
    public ResultBean<Boolean> checkUsername(String username){
        List<User> users = userService.findByUsername(username);
        if (users==null||users.isEmpty()){
            return new ResultBean<>(true);
        }
        return new ResultBean<>(false);
    }

    /**
     * 验证手机号是否唯一
     * @param phone
     * @return
     */
    @ResponseBody
    @RequestMapping("/checkUserphone.do")
    public ResultBean<Boolean> checkUserphone(String phone){
        List<User> users = userService.findByUserphone(phone);
        if (users==null||users.isEmpty()){
            return new ResultBean<>(true);
        }
        return new ResultBean<>(false);
    }

    /**
     * 验证邮箱是否唯一
     * @param email
     * @return
     */
    @ResponseBody
    @RequestMapping("/checkUseremail.do")
    public ResultBean<Boolean> checkUseremail(String email){
        List<User> users = userService.findByUseremail(email);
        if (users==null||users.isEmpty()){
            return new ResultBean<>(true);
        }
        return new ResultBean<>(false);
    }

    /**
     * 如发生错误 转发到这页面
     *
     * @param response
     * @param request
     * @return
     */
    @RequestMapping("/error.html")
    public String error(HttpServletResponse response, HttpServletRequest request) {
        return "error";
    }
}
