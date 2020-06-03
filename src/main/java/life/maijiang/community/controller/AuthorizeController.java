package life.maijiang.community.controller;

import life.maijiang.community.dto.AccessTokenDTO;
import life.maijiang.community.dto.GithubUser;

import life.maijiang.community.mapper.UserMapper;
import life.maijiang.community.model.User;
import life.maijiang.community.provider.GithubProvider;
import life.maijiang.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.GroupSequence;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    GithubProvider githubProvider;


    @Autowired
    UserService userService;
    @Value("${github.client.id}")
    String clientId;
    @Value("${github.client.secret}")
    String clientSecret;
    @Value("${github.redirect.uri}")
    String redirectUri;

    @GetMapping("/callback")
    public String callback(@RequestParam(name="code")String code, @RequestParam(name="state")String state,
                           HttpServletResponse response){
        //携带code发起post请求，得到access_toke
        System.out.println(code+"    "+state);
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        //携带access_toke发起get请求
        GithubUser githubUser = githubProvider.getGithubUser(accessToken);

        System.out.println(githubUser);
        //将得到的用户信息存入session
        if(githubUser != null && githubUser.getId() != null){
            //创建用户，存入数据库
            User user = new User();
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setName(githubUser.getName());
            user.setToken(UUID.randomUUID().toString());
            user.setAvatarUrl(githubUser.getAvatar_url());
            userService.createOrUpdate(user);
            //将Token存储到前端cookie,用于作为登录状态判断
            response.addCookie(new Cookie("token",user.getToken()));
            return "redirect:/";
        }else{
            return "redirect:/";
        }
    }

    /**
     * 退出功能
     * @return
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response){
        //移除session
        request.getSession().removeAttribute("githubUser");
        //移除Cookie方法
        Cookie newCookie = new Cookie("token",null);
        newCookie.setMaxAge(1);
        response.addCookie(newCookie);

        return "redirect:/";
    }
}
