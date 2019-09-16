package com.nowcoder.community.controller;

import com.nowcoder.community.entity.HostHolder;
import com.nowcoder.community.entity.Message;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.MessageService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path = "/letter/list", method = RequestMethod.GET)
    public String getLetterList(Model model, Page page) {
        User user = hostHolder.getUser();

        // 分页信息
        page.setLimit(5);
        page.setPath("/letter/list");
        page.setRows(messageService.findConversationCount(user.getId()));
        // 会话列表
        List<Message> conversationList = messageService.findConversations(
                user.getId(), page.getOffset(), page.getLimit());

        List<Map<String, Object>> conversations = new ArrayList<>();
        if (conversations != null) {
            for (Message message : conversationList) {
                Map<String, Object> map = new HashMap<>();
                map.put("conversation", message);
                map.put("letterCount", messageService.findLetterCount(message.getConversationId()));
                map.put("unreadCount", messageService.findLetterUnreadCount(
                        user.getId(), message.getConversationId()));
                int targetId = user.getId() == message.getFromId() ? message.getToId() : message.getFromId();
                map.put("target", userService.findUserById(targetId));

                conversations.add(map);
            }
        }

        model.addAttribute("conversations", conversations);

//        // 查询未读消息数量
//        int letterUnreadCount = messageService.findLetterUnreadCount(user.getId(), null);
//        model.addAttribute("letterUnreadCount", letterUnreadCount);

        return "/site/letter";
    }

    @RequestMapping(path = "/letter/detail/{conversationId}", method = RequestMethod.GET)
    public String getLetterDetail(@PathVariable("conversationId") String conversationId, Page page, Model model) {

        page.setRows(messageService.findLetterCount(conversationId));
        page.setPath("/letter/detail/" + conversationId);
        page.setLimit(5);

        User target = null;

        List<Message> messages = messageService.findLetters(conversationId, page.getOffset(), page.getLimit());
        List<Map<String, Object>> letters = new ArrayList<>();
        List<Integer> unreadMsg = new ArrayList<>();
        if (messages != null) {
            for (Message message : messages) {
                if (target == null) {
                    int targetId = hostHolder.getUser().getId() == message.getFromId() ? message.getToId() : message.getFromId();
                    target = userService.findUserById(targetId);
                }

                Map<String, Object> letter = new HashMap<>();
                letter.put("message", message);
                letter.put("user", userService.findUserById(message.getFromId()));
                letters.add(letter);

                if (message.getStatus() == 0 && message.getToId() == hostHolder.getUser().getId()) {
                    unreadMsg.add(message.getId());
                }
            }
        }

        if (!unreadMsg.isEmpty()) {
            messageService.readMessage(unreadMsg);
        }


        model.addAttribute("letters", letters);
        model.addAttribute("target", target);
        return "/site/letter-detail";
    }

    @RequestMapping(path = "/letter/send", method = RequestMethod.POST)
    @ResponseBody // 异步请求
    public String addMessage(String toName, String content) {
        User toUser = userService.findUserByName(toName);
        if (toUser == null) {
            return CommunityUtil.getJSONString(1, "The user is not exist");
        }
        int targetId = toUser.getId();

        User user = hostHolder.getUser();
        if (user == null) {
            return CommunityUtil.getJSONString(403, "You haven't login!");
        }

        Message message = new Message();
        message.setFromId(user.getId());
        message.setToId(targetId);
        String conversationId = targetId < user.getId() ?
                targetId + "_" + user.getId() : user.getId() + "_" + targetId;
        message.setConversationId(conversationId);
        message.setContent(content);
        message.setStatus(0);
        message.setCreateTime(new Date());

        messageService.addMessage(message);
        return CommunityUtil.getJSONString(0, "Send message successfully");
    }
}
