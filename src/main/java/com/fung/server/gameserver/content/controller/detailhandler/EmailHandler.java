package com.fung.server.gameserver.content.controller.detailhandler;

import com.fung.server.gameserver.content.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author skytrc@163.com
 * @date 2020/7/21 16:28
 */
@Component
public class EmailHandler extends BaseInstructionHandler{

    @Autowired
    private EmailService emailService;

    @Override
    public String handler(List<String> ins) throws InterruptedException {
        String remove = ins.remove(0);
        switch (remove){
            case("create"): return create(ins.remove(0));
            case("write"): return write(ins);
            case("checkmailbox"): return checkMailBox();
            case("checkemail"): return checkEmail(ins.remove(0));
            case("send"): return sendEmail(ins.remove(0));
            default: return "邮件指令错误";
        }
    }

    public String create(String recipientId) {
        return emailService.createEmail(getChannelId(), recipientId);
    }

    public String write(List<String> list) {
        if (list.size() < 3) {
            return "邮件编写命令错误";
        }
        return emailService.writeContent(getChannelId(), list.remove(0), list.remove(0), list.remove(0));
    }

    public String delete(String emailId) {
        return emailService.deleteResEmail(getChannelId(), emailId);
    }

    public String checkMailBox() {
        return emailService.checkMailBox(getChannelId());
    }

    public String checkEmail(String emailId) {
        return emailService.checkAEmail(getChannelId(), emailId);
    }

    public String sendEmail(String emailId) {
        return emailService.send(getChannelId(), emailId);
    }
}
