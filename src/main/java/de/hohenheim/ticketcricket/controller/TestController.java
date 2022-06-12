package de.hohenheim.ticketcricket.controller;

import de.hohenheim.ticketcricket.model.entity.TestUser;
import de.hohenheim.ticketcricket.model.service.TestUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class TestController {

    @Autowired
    private TestUserService service;

    @PostMapping ("/addUser")
    public String getNewUser(@ModelAttribute("user") TestUser user){
        service.saveUser(user);
        return "redirect:/list";
    }


    @GetMapping ("/input")
    public ModelAndView inputNewUserForm(){
        ModelAndView mav = new ModelAndView("inputview");
        TestUser user = new TestUser();
        mav.addObject("user", user);
        return mav;
    }

    @GetMapping({"/showUsers", "/", "/list"})
    public ModelAndView showUsers(){
        ModelAndView mv = new ModelAndView("home");
        List<TestUser> users = service.findAllUsers();
        mv.addObject("testUsers", users);
        return mv;
    }
}
