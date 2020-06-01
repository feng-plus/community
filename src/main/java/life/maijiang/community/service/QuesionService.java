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

        Integer toltalPage;
        if(toltalCount%size==0){
            toltalPage = toltalCount/size;
        }else{
            toltalPage = toltalCount/size + 1 ;
        }
        if(page < 1 ){
            page = 1;
        }
        if(page > toltalPage){
            page = toltalPage;
        }
        pagination.setPaginations(toltalPage,page);

        Integer offset = size*(page - 1);
        List<Question> questions =  questionMapper.list(offset,size);
        List<QuestionDTO> questionDTOS = new ArrayList<QuestionDTO>();
        for(Question question : questions){
           User user = userMapper.findById(question.getCreator());
           QuestionDTO questionDTO = new QuestionDTO();
            questionDTO.setUser(user);
            //System.out.println(questionDTO.toString());
           //赋值question属性值到questionDTO
           BeanUtils.copyProperties(question,questionDTO);
           questionDTOS.add(questionDTO);
        }
        pagination.setQuestionDTOS(questionDTOS);

       //Integer
        return pagination;
    }

    public PaginationDTO list(Integer userId, Integer page, Integer size) {
        //offset = page -1
        //容错处理
        PaginationDTO pagination = new PaginationDTO();
        Integer toltalCount = questionMapper.countByUserId(userId);
        Integer toltalPage;
        if(toltalCount%size==0){
            toltalPage = toltalCount/size;
        }else{
            toltalPage = toltalCount/size + 1 ;
        }
        if(page < 1 ){
            page = 1;
        }
        if(page > toltalPage){
            page = toltalPage;
        }
        pagination.setPaginations(toltalPage,page);
        Integer offset = size*(page - 1);

        List<Question> questions =  questionMapper.listByUserId(userId,offset,size);
        List<QuestionDTO> questionDTOS = new ArrayList<QuestionDTO>();
        for(Question question : questions){
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            questionDTO.setUser(user);
            //System.out.println(questionDTO.toString());
            //赋值question属性值到questionDTO
            BeanUtils.copyProperties(question,questionDTO);
            questionDTOS.add(questionDTO);
        }
        pagination.setQuestionDTOS(questionDTOS);

        //Integer
        return pagination;
    }

    public QuestionDTO getById(Integer id) {
        Question question = questionMapper.getById(id);
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        User user = userMapper.findById(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }
}
