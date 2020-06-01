package life.maijiang.community.service;

import life.maijiang.community.mapper.UserMapper;
import life.maijiang.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;


    public void createOrUpdate(User user) {
      User dbUser =  userMapper.findByAccountId(user.getAccountId());
      if(dbUser == null){
          user.setGmtCreate(System.currentTimeMillis());
          user.setGmtModified(System.currentTimeMillis());
          userMapper.insert(user);
      }else{
          //更新
          dbUser.setGmtModified(System.currentTimeMillis());
          dbUser.setName(user.getName());
          dbUser.setAvatarUrl(user.getAvatarUrl());
          dbUser.setToken(user.getToken());
          userMapper.update(user);
      }
    }
}
