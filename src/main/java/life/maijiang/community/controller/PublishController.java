package life.maijiang.community.controller;

import life.maijiang.community.cache.TagCache;
import life.maijiang.community.dto.QuestionDTO;
import life.maijiang.community.mapper.QuestionMapper;
import life.maijiang.community.model.Question;
import life.maijiang.community.model.User;
import life.maijiang.community.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {
    @Autowired
    QuestionService questionService;

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name="id")Long id,
                       Model model){
        QuestionDTO questionDTO = questionService.getById(id);
        model.addAttribute("title",questionDTO.getTitle());
        model.addAttribute("description",questionDTO.getDescription());
        model.addAttribute("tag",questionDTO.getTag());
        model.addAttribute("id",questionDTO.getId());
        model.addAttribute("tags", TagCache.getTags());
        return "publish";
    }

    @GetMapping("/publish")
    public String publish(Model model){
        model.addAttribute("tags", TagCache.getTags());
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(@RequestParam(name="title",required = false) String title,
                            @RequestParam(name="description",required = false) String  description,
                            @RequestParam(name="tag",required = false) String tag ,
                            @RequestParam(name="id",required = false) Long id ,
                            HttpServletRequest request,
                            Model model){
        System.out.println("执行了发布功能");
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        model.addAttribute("tags", TagCache.getTags());

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
        String invlidTag = TagCache.filterInvalid(tag);
        if(StringUtils.isNotBlank(invlidTag)){
            model.addAttribute("error","输入非法标签"+invlidTag);
            return "publish";
        }

        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setId(id);

        //获取用户信息
        User user =(User)request.getSession().getAttribute("githubUser");
        if(user == null){
            model.addAttribute("error","用户未登录，请先登录");
            return "publish";
        }
        //将id赋值给creator
        question.setCreator(user.getId());
        //执行新增操作或者修改
        questionService.createOrUpdate(question);
        //questionMapper.create(question);
        return "redirect:/";
    }

}
