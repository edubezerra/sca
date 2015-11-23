/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.aries.blog.controller;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author ariestania.winda
 */

public class BaseController {
    
    public static final String PARAM_BASE_URL = "baseURL";
    
    public String getBaseURL(HttpServletRequest request){
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
    
}
