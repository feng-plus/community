package life.maijiang.community.controller;


import life.maijiang.community.dto.PaginationDTO;
import life.maijiang.community.model.User;
import life.maijiang.community.service.QuesionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class ProfileController {
    @Autowired
    QuesionService questionService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name="action")String action,
                          @RequestParam(name="page",defaultValue = "1")Integer page,
                          @RequestParam(name="size",defaultValue = "5")Integer size,
                          Model model,
                          HttpSession session){
        User user = (User)session.getAttribute("githubUser");
        if("questions".equals(action)){
           model.addAttribute("selection","questions");
           model.addAttribute("selectionName","我的提问");
        }else if("replies".equals(action)){
            model.addAttribute("selection","replies");
            model.addAttribute("selectionName","最新回复");
        }
        PaginationDTO paginationDTO = questionService.list(user.getId(),page,size);
        model.addAttribute("pagination",paginationDTO);
        return "profile";
    }
}
