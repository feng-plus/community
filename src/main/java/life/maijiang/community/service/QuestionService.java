package life.maijiang.community.service;

import life.maijiang.community.Exception.CustomizeErrorCode;
import life.maijiang.community.Exception.CustomizeException;
import life.maijiang.community.dto.PaginationDTO;
import life.maijiang.community.dto.QuestionDTO;
import life.maijiang.community.mapper.QuestionMapper;
import life.maijiang.community.mapper.UserMapper;
import life.maijiang.community.model.Question;
import life.maijiang.community.model.QuestionExample;
import life.maijiang.community.model.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    UserMapper userMapper;


    public PaginationDTO list(Integer page, Integer size){
        //offset = page -1
        //容错处理
        PaginationDTO pagination = new PaginationDTO();

        //questionMapper.count();
        Integer toltalCount =(int)questionMapper.countByExample(new QuestionExample());
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
        //List<Question> questions =  questionMapper.list(offset,size);
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(new QuestionExample(), new RowBounds(offset, size));
        List<QuestionDTO> questionDTOS = new ArrayList<QuestionDTO>();
        for(Question question : questions){
           User user = userMapper.selectByPrimaryKey(question.getCreator());
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
        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userId);
        Integer toltalCount =(int)questionMapper.countByExample(new QuestionExample());
        //Integer toltalCount = questionMapper.countByUserId(userId);
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
        QuestionExample example1 = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userId);
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(example1, new RowBounds(offset, size));
        //<Question> questions =  questionMapper.listByUserId(userId,offset,size);
        List<QuestionDTO> questionDTOS = new ArrayList<QuestionDTO>();
        for(Question question : questions){
            User user = userMapper.selectByPrimaryKey(question.getCreator());
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
        //Question question = questionMapper.getById(id);
        Question question = questionMapper.selectByPrimaryKey(id);
        if(question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if(question.getId() == null){
            //执行新增操作
            System.out.println("新增提问");
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(System.currentTimeMillis());
            //questionMapper.create(question);
            questionMapper.insert(question);
        }else{
            //执行修改操作
            System.out.println("修改提问");
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            //questionMapper.update(question);

            QuestionExample example = new QuestionExample();
            example.createCriteria().andIdEqualTo(question.getId());
            int updated = questionMapper.updateByExampleSelective(updateQuestion, example);
            if(updated !=1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }
}
