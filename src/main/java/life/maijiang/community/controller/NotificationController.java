package life.maijiang.community.controller;

import life.maijiang.community.dto.NotificationDTO;
import life.maijiang.community.enums.NotificationTypeEnum;
import life.maijiang.community.mapper.NotificationMapper;
import life.maijiang.community.model.User;
import life.maijiang.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;
import javax.sound.midi.Soundbank;

@Controller
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notification/{id}")
    public String notification(@PathVariable(name="id")Long id,
                               HttpSession session){
        User user = (User)session.getAttribute("githubUser");
        if(user == null){
            return "redirect:/";
        }
        NotificationDTO notificationDTO = notificationService.read(id,user);
        if(NotificationTypeEnum.REPLY_COMMENT.getType() == notificationDTO.getType()
           || NotificationTypeEnum.REPLY_QUESTION.getType() == notificationDTO.getType()){
            System.out.println("点击成功了");
            return "redirect:/question/" + notificationDTO.getOuterid();
        }else{
            return "redirect:/";
        }
    }
}
