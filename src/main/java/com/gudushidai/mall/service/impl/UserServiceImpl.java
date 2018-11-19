package com.gudushidai.mall.service.impl;

import com.gudushidai.mall.dao.UserMapper;
import com.gudushidai.mall.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.gudushidai.mall.dao.UserDao;
import com.gudushidai.mall.entity.User;
import com.gudushidai.mall.service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findById(int id) {
        return userDao.getOne(id);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userDao.findAll(pageable);
    }

    @Override
    public List<User> findAllExample(Example<User> example) {
        return userDao.findAll(example);
    }

    @Override
    public void update(User user) {
        userDao.save(user);
    }

    @Override
    public int create(User user) {
        Integer id = 0;
        try{
            id = userDao.save(user).getId();
        }catch (Exception e){
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public void delById(int id) {
        userDao.delete(id);
    }

    /**
     * 根据用户名查询
     *
     * @param username
     * @return
     */
    @Override
    public List<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    /**
     * 根据手机号查询
     *
     * @param phone
     * @return
     */
    @Override
    public List<User> findByUserphone(String phone) {
        return userDao.findByPhone(phone);
    }

    @Override
    public List<User> findByUseremail(String email) {
        return userDao.findByEmail(email);
    }

    /**
     * 检查登录
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public User checkLogin(String username, String password) {
        try {
            password = Md5Util.EncoderByMd5(password);
        }catch (Exception e){
            e.printStackTrace();
        }
        //根据用户名密码查询
        User user = userDao.findByUsernameAndPassword(username, password);
        if(user!=null){
            return user;
        }
        //根据手机号和密码查询
        user = userMapper.findByUserPhoneAndPassword(username,password);
        return user;
    }


}
