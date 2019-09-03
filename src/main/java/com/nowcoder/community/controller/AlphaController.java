package com.nowcoder.community.controller;

import com.nowcoder.community.CommunityApplication;
import com.nowcoder.community.service.AlphaService;
import com.nowcoder.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/alpha")
@ContextConfiguration(classes = CommunityApplication.class)
public class AlphaController {

    @Autowired
    private AlphaService alphaService;

    @RequestMapping("hello")
    @ResponseBody
    public String sayHello() {
        return "Hello world";
    }

    @RequestMapping("bean")
    @ResponseBody
    public String getDate() {
        return alphaService.find();
    }

    @RequestMapping("http")
    public void http(HttpServletRequest request, HttpServletResponse response) {
//        get data from request
        System.out.println(request.getMethod());
        System.out.println(request.getServletPath());
        Enumeration<String> enumeration = request.getHeaderNames();

        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            String value = request.getHeader(name);
            System.out.println(name + ": " + value);
        }

        System.out.println(request.getParameter("code"));

//        return response to client
        response.setContentType("text/html;charset-utf-8");
        try (
                PrintWriter printWriter = response.getWriter()
        ) {
            printWriter.write("<h1> response success </h1>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Get Request
    // /students?current=1&limit=20
    @RequestMapping(path = "/students", method = RequestMethod.GET)
    @ResponseBody
    public String getStudents(
            @RequestParam(name = "current", required = false, defaultValue = "1") int current,
            @RequestParam(name = "limit", required = false, defaultValue = "30") int limit) {
        System.out.println(current);
        System.out.println(limit);
        return "some students";
    }

    // /student/123
    @RequestMapping(value = "/student/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getStudent(@PathVariable("id") int id) {
        System.out.println(id);
        return "A student";
    }

    // POST request
    @RequestMapping(path = "/student", method = RequestMethod.POST)
    @ResponseBody
    public String savetudent(String name, int age) {
        System.out.println(name);
        System.out.println(age);
        return "success";
    }

    // method1 use ModelandView
    @RequestMapping(path = "/teacher", method = RequestMethod.GET)
    public ModelAndView getTeacher() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("name", "zhangsan");
        modelAndView.addObject("age", 30);
        modelAndView.setViewName("/demo/view");
        return modelAndView;

    }

    // method2
    @RequestMapping(path = "school", method = RequestMethod.GET)
    public String getSchool(Model model) {
        model.addAttribute("name", "nus");
        model.addAttribute("age", 90);

        // set template path to the return value
        return "/demo/view";
    }

    // JSON data, 异步请求
    // Java object -> JSON(跨语言) -> JS object
    @RequestMapping(path = "/emp", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getEmp() {
        Map<String, Object> emp = new HashMap<>();
        emp.put("name", "zhang");
        emp.put("age", 30);
        emp.put("salary", 900000);
        return emp;
    }

    @RequestMapping(path = "/cookie/set", method = RequestMethod.GET)
    @ResponseBody
    public String setCookie(HttpServletResponse response) {
        // Generate new Cookie from javax.servlet.http.Cookie;
        Cookie cookie = new Cookie("code", CommunityUtil.generateUUID());
        // 设置cookie的有效路径，/community/alpha 及其子路径
        cookie.setPath("/community/alpha");
        // set cookie's lifetime
        cookie.setMaxAge(60 * 10);
        response.addCookie(cookie);
        return "set cookie successfully";
    }

    @RequestMapping(path = "/cookie/send", method = RequestMethod.GET)
    @ResponseBody
    // get the cookie from the request
    public String sendCookie(@CookieValue("code") String code) {
        return "send cookie successfully";
    }

    @RequestMapping(path = "/session/set", method = RequestMethod.GET)
    @ResponseBody
    // HttpSession Spring可以自动注入
    public String setSession(HttpSession session) {
        session.setAttribute("id", 1);
        session.setAttribute("name", "yifan");
        return "set Successfully";
    }

    @RequestMapping(path = "/session/get", method = RequestMethod.GET)
    @ResponseBody
    public String getSession(HttpSession session) {
        System.out.println(session.getAttribute("id"));
        System.out.println(session.getAttribute("name"));
        return (String) session.getAttribute("name");
    }

    @RequestMapping(path = "/ajax", method = RequestMethod.POST)
    @ResponseBody
    public String ajax(String name, int age) {
        System.out.println(name);
        System.out.println(age);
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("age", age);
        return CommunityUtil.getJSONString(0, "success", map);
    }
}
