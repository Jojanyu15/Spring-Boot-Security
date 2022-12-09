package co.epam.securitytask.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    @RequestMapping("/user")
    public String user() {
        return "user";
    }

    @RequestMapping("/info")
    public String info() {
        return "info";
    }

    @RequestMapping("/about")
    public String about() {
        return "about";
    }

    @RequestMapping("/admin")
    public String admin() {
        return "admin";
    }

    @RequestMapping("/403.html")
    public String forbidden() {
        return "403";
    }
}
