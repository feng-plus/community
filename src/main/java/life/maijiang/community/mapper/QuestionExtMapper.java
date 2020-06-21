package life.maijiang.community.mapper;

import life.maijiang.community.dto.QuestionQueryDTO;
import life.maijiang.community.model.Question;

import java.util.List;


public interface QuestionExtMapper {
    //自己扩展的方法
    int incView(Question record);

    int incCommentCount(Question record);

    List<Question> selectRelated(Question question);

    Integer countBySreach(QuestionQueryDTO questionQueryDTO);

    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO1);
}