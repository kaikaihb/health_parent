package com.likai.security;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hello")
public class HelloController {

    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('add')")
    public String add(){
        System.out.println("add...");
        return null;
    }

    @RequestMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete(){
        System.out.println("delete...");
        return null;
    }
}
