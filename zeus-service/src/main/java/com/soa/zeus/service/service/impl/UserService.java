package com.soa.zeus.service.service.impl;

import com.soa.zeus.service.dao.IUserDao;
import com.soa.zeus.service.domain.User;
import com.soa.zeus.service.service.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * Created by shizhizhong on 15/2/10.
 */
@Service
public class UserService implements IUserService {

    @Resource
    private IUserDao userDao;


    @Override
    public User getById(final Integer id) {
        return userDao.get(new HashMap<String, Object>(){
            {
                put("id", id);
            }
        });
    }
}
