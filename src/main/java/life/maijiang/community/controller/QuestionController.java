package life.maijiang.community.controller;

import life.maijiang.community.dto.QuestionDTO;
import life.maijiang.community.service.QuesionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class QuestionController {
    @Autowired
     QuesionService quesionService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name="id")Integer id, Model model){
       QuestionDTO questionDTO =  quesionService.getById(id);
       model.addAttribute("question",questionDTO);
        return "question";
    }
}
