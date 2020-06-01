package life.maijiang.community.mapper;

import life.maijiang.community.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Insert("insert into user(ACCOUNT_ID,NAME,TOKEN,AVATAR_URL,GMT_CREATE,GMT_MODIFIED) values(#{accountId},#{name},#{token},#{avatarUrl},#{gmtCreate},#{gmtModified})")
    public void insert(User user);

    @Select("select * from user where token = #{token} ")
    User findUserByToken(@Param("token") String token);

    @Select("select * from user where id = #{id}")
    User findById(@Param("id") Integer id);

    @Select("select * from user where account_id = #{accountId}")
    User findByAccountId(@Param("accountId")String accountId);

    @Update("update user set NAME=#{name},TOKEN=#{token},AVATAR_URL=#{avatarUrl},GMT_MODIFIED=#{gmtModified} where id=#{id}")
    void update(User user);
}
