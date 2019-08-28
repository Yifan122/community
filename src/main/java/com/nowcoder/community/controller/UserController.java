package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.HostHolder;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${community.path.domain}")
    private String domain;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @LoginRequired
    @RequestMapping(path = "/setting", method = RequestMethod.GET)
    public String userSetting() {
        return "/site/setting";
    }

    @LoginRequired
    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model) {

        if (headerImage == null) {
            model.addAttribute("error", "Please choose a image file");
            return "/setting";
        }

        String fileName = headerImage.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);

        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "It's not a right format");
            return "/setting";
        }

        fileName = CommunityUtil.generateUUID() + "." + suffix;

        // Generate new File to store the content
        File dest = new File(uploadPath + "/" + fileName);

        try {
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("Fail to upload image: " + e.getMessage());
            throw new RuntimeException("Fail to upload image, exception in Server", e);
        }

        // update the head image (path in web)
        // http://8080/community/user/header/filename.png
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header/" + fileName;

        if (userService.updateHeader(user.getId(), headerUrl) < 1) {
            logger.error("Update database fail");
            throw new RuntimeException("Update database fail");
        }

        return "redirect:/index";
    }

    // Sever will response to binary file, so the return type is void
    @RequestMapping(path = "/header/{fileName}", method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName") String fileName,
                          HttpServletResponse response) {
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        fileName = uploadPath + "/" + fileName;

        response.setContentType("image/" + suffix);

        try (
                OutputStream os = response.getOutputStream();
                FileInputStream fis = new FileInputStream(fileName)
        ) {

            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("Fail to fetch the header image" + e.getMessage());
        }

    }

}
