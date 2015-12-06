/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.aries.blog.service;

import com.aries.blog.model.User;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ariestania.winda
 */

@Service("userService")
@Transactional
public class UserService {
    
    @Autowired
    private SessionFactory sessionFactory;
    
    public List<User> getAllUser(){
        return sessionFactory.getCurrentSession()
                .createQuery("from User")
                .list();
    }
    
    
    
}
