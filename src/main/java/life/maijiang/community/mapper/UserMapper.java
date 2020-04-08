package life.maijiang.community.mapper;

import life.maijiang.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Insert("insert into user(ACCOUNT_ID,NAME,TOKEN,AVATAR_URL,GMT_CREATE,GMT_MODIFIED) values(#{accountId},#{name},#{token},#{avatarUrl},#{gmtCreate},#{gmtModified})")
    public void insert(User user);

    @Select("select * from user where token = #{token} ")
    User findUserByToken(@Param("token") String token);
}
