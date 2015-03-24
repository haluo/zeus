package com.soa.zeus.service.dao.impl;

import com.soa.zeus.service.dao.IUserDao;
import com.soa.zeus.service.domain.User;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Created by shizhizhong on 15/2/10.
 */
@Repository
public class UserDao extends BaseDao implements IUserDao{


    @Override
    public User get(Map<String, Object> map) {
        return (User)queryForObject("User.get", map);
    }


}
