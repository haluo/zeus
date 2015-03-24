package com.soa.zeus.service.dao;

import com.soa.zeus.service.domain.User;

import java.util.Map;

/**
 * Created by shizhizhong on 15/2/10.
 */
public interface IUserDao {

    public User get( Map<String, Object> map);
}
