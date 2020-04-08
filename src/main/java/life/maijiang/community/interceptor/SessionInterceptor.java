package life.maijiang.community.interceptor;


import life.maijiang.community.mapper.UserMapper;
import life.maijiang.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义拦截器
 */
@Component
public class SessionInterceptor implements HandlerInterceptor {

    @Autowired
    UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取当前用户cookie信息
        Cookie[] cookies = request.getCookies();
        if(cookies != null && cookies.length != 0){
            //遍历cookie
            for (Cookie cookie : cookies) {
                if("token".equals(cookie.getName())){
                    //根据name获取value
                    String token = cookie.getValue();
                    //然后根据token查询用户信息
                    User user = userMapper.findUserByToken(token);
                    if(user != null){
                        //将用户信息存入session
                        request.getSession().setAttribute("githubUser",user);
                    }
                    break;
                }
            }
        }
        //System.out.println("拦截器执行了");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
