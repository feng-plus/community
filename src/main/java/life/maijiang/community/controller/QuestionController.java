package life.maijiang.community.controller;

import life.maijiang.community.dto.CommentDTO;
import life.maijiang.community.dto.QuestionDTO;
import life.maijiang.community.enums.CommentTypeEnum;
import life.maijiang.community.model.Question;
import life.maijiang.community.service.CommentService;
import life.maijiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    QuestionService quesionService;

    @Autowired
    CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name="id")Long id, Model model){
        QuestionDTO questionDTO = quesionService.getById(id);
        //进行相关问题模糊查询
        List<QuestionDTO> relatedQuestions = quesionService.selectRelated(questionDTO);
        List<CommentDTO> comments = commentService.listByTargetId(id, CommentTypeEnum.QUESTION.getType());
        //累加锐图书
        quesionService.inView(id);
        model.addAttribute("question", questionDTO);
        model.addAttribute("comments", comments);
        model.addAttribute("relatedQuestions", relatedQuestions);
        return "question";
    }
}
