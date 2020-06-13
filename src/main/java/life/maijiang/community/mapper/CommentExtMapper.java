package life.maijiang.community.mapper;

import life.maijiang.community.model.Comment;
import life.maijiang.community.model.CommentExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface CommentExtMapper {
    int incCommentCount(Comment record);
}