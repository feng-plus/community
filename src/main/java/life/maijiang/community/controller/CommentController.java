package life.maijiang.community.controller;

import life.maijiang.community.Exception.CustomizeErrorCode;
import life.maijiang.community.dto.CommentCreateDTO;

import life.maijiang.community.dto.CommentDTO;
import life.maijiang.community.dto.ResultDTO;
import life.maijiang.community.enums.CommentTypeEnum;
import life.maijiang.community.model.Comment;
import life.maijiang.community.model.User;
import life.maijiang.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ResponseBody
    //@RequestMapping(value = "/comment",method = RequestMethod.POST)
    @PostMapping("/comment")
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("githubUser");
        //用户未登录，不能操作
        if(user == null){
            return  ResultDTO.errorOf(CustomizeErrorCode.NO_LOGON);
        }
        if(commentCreateDTO == null || StringUtils.isBlank(commentCreateDTO.getContent())){
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }

        System.out.println("postMan测试");
        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setCommentator(1L);
        comment.setLikeCount(0L);
        commentService.insert(comment);
        return ResultDTO.okOf();
    }

    @ResponseBody
    @RequestMapping(value = "/comment/{id}",method = RequestMethod.GET)
    public ResultDTO<List<CommentDTO>> comments(@PathVariable(name="id")Long id){
        List<CommentDTO> commentDTOS = commentService.listByTargetId(id, CommentTypeEnum.COMMENT.getType());
        return ResultDTO.okOf(commentDTOS);
    }
}
