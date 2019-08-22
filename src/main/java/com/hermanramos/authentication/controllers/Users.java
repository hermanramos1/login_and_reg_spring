package com.hermanramos.authentication.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hermanramos.authentication.models.User;
import com.hermanramos.authentication.services.UserService;

@Controller
public class Users {
	@Autowired
	private UserService uS;
    
    @RequestMapping("/registration")
    public String registerForm(@ModelAttribute("user") User user) {
        return "users/registrationPage.jsp";
    }
    @RequestMapping("/login")
    public String login() {
        return "users/loginPage.jsp";
    }
        
    @PostMapping("/registration")
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result, HttpSession session) {
    	if(result.hasErrors()) {
    		return "users/registrationPage.jsp";
    	}else {
    		uS.registerUser(user);
    		session.setAttribute("user", user);
    		return "redirect:/home";
    	}
    }
    
    @PostMapping("/login")
    public String loginUser(@RequestParam("email") String email, @RequestParam("password") String password, Model model, HttpSession session) {
        if(uS.authenticateUser(email, password) == false) {
        	model.addAttribute("error", "Email or password not found");
        	return "users/loginPage.jsp";
        } else if (uS.authenticateUser(email, password) == true) {
        		Object user = uS.findByEmail(email);
        		session.setAttribute("user", user);
        	}
        	return "redirect:/home";
        }

    
    @RequestMapping("/home")
    public String home(HttpSession session, Model model) {
        if(session.getAttribute("user") != null) {
        	Object user = session.getAttribute("user");
        	model.addAttribute(user);
        	return "users/homePage.jsp";
        }else {
        	return "redirect:/registration";
        }
    }
    @RequestMapping("/logout")
    public String logout(HttpSession session) {
    	session.removeAttribute("user");
    	return "redirect:/login";
    }
    
}