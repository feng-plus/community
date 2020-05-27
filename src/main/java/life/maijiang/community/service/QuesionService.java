package life.maijiang.community.service;

import life.maijiang.community.dto.PaginationDTO;
import life.maijiang.community.dto.QuestionDTO;
import life.maijiang.community.mapper.QuestionMapper;
import life.maijiang.community.mapper.UserMapper;
import life.maijiang.community.model.Question;
import life.maijiang.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuesionService {
    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    UserMapper userMapper;


    public PaginationDTO list(Integer page, Integer size){
        //offset = page -1
        //容错处理
        PaginationDTO pagination = new PaginationDTO();
        Integer toltalCount = questionMapper.count();
        pagination.setPaginations(toltalCount,page,size);
        if(page < 1 ){
            page = 1;
        }
        if(page > pagination.getToltalPage()){
            page = pagination.getToltalPage();
        }
        Integer offset = size*(page - 1);

        List<Question> questions =  questionMapper.list(offset,size);
        List<QuestionDTO> questionDTOS = new ArrayList<QuestionDTO>();
        for(Question question : questions){
           User user = userMapper.findById(question.getCreator());
           QuestionDTO questionDTO = new QuestionDTO();
            questionDTO.setUser(user);
            System.out.println(questionDTO.toString());
           //赋值question属性值到questionDTO
           BeanUtils.copyProperties(question,questionDTO);
           questionDTOS.add(questionDTO);
        }
        pagination.setQuestionDTOS(questionDTOS);

       //Integer
        return pagination;
    }
}
