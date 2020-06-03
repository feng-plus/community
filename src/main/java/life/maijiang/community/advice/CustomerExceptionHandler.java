package life.maijiang.community.advice;

import life.maijiang.community.Exception.CustomizeException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class CustomerExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    ModelAndView handle(HttpServletRequest request, Throwable tx, Model model){
        HttpStatus status = getStatus(request);
        if(tx instanceof CustomizeException){
            model.addAttribute("message",tx.getMessage());
        }else{
            model.addAttribute("message","服务冒烟了，请稍后重试一下？");
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
