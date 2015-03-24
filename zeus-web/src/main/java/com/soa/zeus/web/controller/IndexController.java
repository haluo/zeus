package com.soa.zeus.web.controller;

import com.soa.zeus.service.domain.User;
import com.soa.zeus.service.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * Created by shizhizhong on 15/2/10.
 */
@Controller
public class IndexController extends BaseController{


    @Resource
    private IUserService userService;

    @RequestMapping(value = "/")
    public ModelAndView getUser(){
        ModelAndView mv = new ModelAndView("/index");
        User user = userService.getById(1);
        mv.addObject("user", user);
        return  mv;
    }
}
