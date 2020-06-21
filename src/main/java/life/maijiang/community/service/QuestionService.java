package life.maijiang.community.service;

import life.maijiang.community.Exception.CustomizeErrorCode;
import life.maijiang.community.Exception.CustomizeException;
import life.maijiang.community.dto.PaginationDTO;
import life.maijiang.community.dto.QuestionDTO;
import life.maijiang.community.dto.QuestionQueryDTO;
import life.maijiang.community.mapper.QuestionExtMapper;
import life.maijiang.community.mapper.QuestionMapper;
import life.maijiang.community.mapper.UserMapper;
import life.maijiang.community.model.Question;
import life.maijiang.community.model.QuestionExample;
import life.maijiang.community.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    QuestionExtMapper questionExtMapper;

    @Autowired
    UserMapper userMapper;


    public PaginationDTO list(String search,Integer page, Integer size){
        if(StringUtils.isNotBlank(search)){
            String[] split = StringUtils.split(search, ' ');
            search = Arrays.stream(split).collect(Collectors.joining("|"));
        }

        //offset = page -1
        //容错处理
        PaginationDTO<QuestionDTO> pagination = new PaginationDTO<QuestionDTO>();

        //questionMapper.count();

        //QuestionExample questionExample = new QuestionExample();
        //questionExample.createCriteria().andTitleLike(search);
        //Integer toltalCount =(int)questionMapper.countByExample(questionExample);
        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);
        Integer toltalCount =questionExtMapper.countBySreach(questionQueryDTO);

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
        //QuestionExample example = new QuestionExample();
        //example.createCriteria().andTitleLike(search);
        //example.setOrderByClause("gmt_create desc");
        //List<Question> questions = questionMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));
        QuestionQueryDTO questionQueryDTO1 = new QuestionQueryDTO();
        questionQueryDTO1.setSearch(search);
        questionQueryDTO1.setPage(page);
        questionQueryDTO1.setSize(size);
        List<Question> questions = questionExtMapper.selectBySearch(questionQueryDTO1);
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
        pagination.setData(questionDTOS);

       //Integer
        return pagination;
    }

    public PaginationDTO list(String search,Long userId, Integer page, Integer size) {
        //offset = page -1
        //容错处理
        PaginationDTO pagination = new PaginationDTO();
        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userId)
        .andTitleLike(search);
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
        example.createCriteria().andCreatorEqualTo(userId).andTitleLike(search);
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
        pagination.setData(questionDTOS);

        //Integer
        return pagination;
    }

    public QuestionDTO getById(Long id) {
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
            question.setLikeCount(0);
            question.setViewCount(0);
            question.setCommentCount(0);
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

    public void inView(Long id) {
        Question updateQuestion = new Question();
        updateQuestion.setId(id);
        updateQuestion.setViewCount(1);
        questionExtMapper.incView(updateQuestion);
    }

    public List<QuestionDTO> selectRelated(QuestionDTO queryDTO) {
        if(StringUtils.isBlank(queryDTO.getTag())){
            return new ArrayList<>();
        }
        String[] split = StringUtils.split(queryDTO.getTag(), ',');
        String regexpTag = Arrays.stream(split).collect(Collectors.joining("|"));
        System.out.println(regexpTag);
        Question question = new Question();
        question.setId(queryDTO.getId());
        question.setTag(regexpTag);

        List<Question> questions = questionExtMapper.selectRelated(question);
        List<QuestionDTO> questionDTOs = questions.stream().map(question1 -> {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question1,questionDTO);
            return questionDTO;
        }).collect(Collectors.toList());

        return questionDTOs;
    }
}
