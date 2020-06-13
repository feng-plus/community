package life.maijiang.community.advice;

import com.alibaba.fastjson.JSON;
import life.maijiang.community.Exception.CustomizeErrorCode;
import life.maijiang.community.Exception.CustomizeException;
import life.maijiang.community.dto.ResultDTO;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
public class CustomerExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    ModelAndView handle(HttpServletRequest request, Throwable tx, Model model, HttpServletResponse response){
        //HttpStatus status = getStatus(request);
        String contentType=request.getContentType();
        System.out.println(contentType);
        if("application/json".equals(contentType)){
            //返回JSON
            ResultDTO resultDTO=null;
            if(tx instanceof CustomizeException){
                resultDTO =  ResultDTO.errorOf((CustomizeException) tx);
            }else{
                resultDTO =  ResultDTO.errorOf(CustomizeErrorCode.SYSTEM_ERROR);
            }
            PrintWriter writer = null;
            try {
                response.setContentType("application/json");
                response.setStatus(200);
                response.setCharacterEncoding("UTF-8");
                writer = response.getWriter();
                writer.write(JSON.toJSONString(resultDTO));
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            //返回到错误页面
            if(tx instanceof CustomizeException){
                model.addAttribute("message",tx.getMessage());
            }else{
                model.addAttribute("message","服务冒烟了，请稍后重试一下？");
            }
        }
        return new ModelAndView("error");
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if(statusCode ==null){
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }else{
            return HttpStatus.valueOf(statusCode);
        }
    }
}
