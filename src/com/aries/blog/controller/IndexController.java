package com.aries.blog.controller;


import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ariestania.winda
 */

@Controller
public class IndexController extends BaseController{
    
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(ModelMap model, HttpServletRequest request){
        model.addAttribute(PARAM_BASE_URL, getBaseURL(request));
        return "index";
    }
    
    @RequestMapping(value = "/userlist", method = RequestMethod.GET)
    public String userList(ModelMap model, HttpServletRequest request){
        model.addAttribute(PARAM_BASE_URL, getBaseURL(request));
        return "users";
    }
    
}
