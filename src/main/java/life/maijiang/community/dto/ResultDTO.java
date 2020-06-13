package life.maijiang.community.dto;

import life.maijiang.community.Exception.CustomizeErrorCode;
import life.maijiang.community.Exception.CustomizeException;
import lombok.Data;

@Data
public class ResultDTO<T> {
    private Integer code;
    private String message;
    private T data;

    public static ResultDTO errorOf(Integer code,String message){
        ResultDTO resultDTO =new ResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMessage(message);
        return  resultDTO;
    }

    public static ResultDTO errorOf(CustomizeErrorCode errorCode) {
        ResultDTO resultDTO =new ResultDTO();
        resultDTO.setCode(errorCode.getCode());
        resultDTO.setMessage(errorCode.getMessage());
        return  resultDTO;

    }

    public static ResultDTO okOf() {
        ResultDTO resultDTO =new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功");
        return  resultDTO;

    }
    public static <T> ResultDTO okOf(T t) {
        ResultDTO resultDTO =new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功");
        resultDTO.setData(t);
        return  resultDTO;

    }

    public static ResultDTO errorOf(CustomizeException exception) {
        ResultDTO resultDTO =new ResultDTO();
        resultDTO.setCode(exception.getCode());
        resultDTO.setMessage(exception.getMessage());
        return  resultDTO;
    }
}
