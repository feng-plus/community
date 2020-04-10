package life.maijiang.community.controller;

import life.maijiang.community.mapper.QuestionMapper;
import life.maijiang.community.model.Question;
import life.maijiang.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {
    @Autowired
    QuestionMapper questionMapper;

    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(@RequestParam("title") String title,
                            @RequestParam("description") String  description,
                            @RequestParam("tag") String tag ,
                            HttpServletRequest request,
                            Model model){
        System.out.println("执行了发布功能");
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);

        if(title == null || "".equals(title)){
            model.addAttribute("error","标题不能为空");
            return "publish";
        }
        if(description == null || "".equals(description)){
            model.addAttribute("error","问题补充不能为空");
            return "publish";
        }
        if(tag == null || "".equals(tag)){
            model.addAttribute("error","标签不能为空");
            return "publish";
        }
        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(System.currentTimeMillis());
        //获取用户信息
        User user =(User)request.getSession().getAttribute("githubUser");
        if(user == null){
            model.addAttribute("error","用户未登录，请先登录");
            return "publish";
        }
        //将id赋值给creator
        question.setCreator(user.getId());
        //执行新增操作
        questionMapper.create(question);
        return "redirect:/";
    }

}
