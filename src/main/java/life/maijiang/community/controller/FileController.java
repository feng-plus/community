package life.maijiang.community.controller;

import life.maijiang.community.dto.FileDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class FileController {
//http://localhost:8887/publish/php/upload.php?guid=1592404175513
    //@GetMapping("/publish/php/upload.php/{guid}")
    @ResponseBody
    @RequestMapping(value = "/publish/php/upload.php",method = RequestMethod.POST)
    public FileDTO upload(Long guid){
        FileDTO fileDTO = new FileDTO();
        fileDTO.setSuccess(1);
        fileDTO.setUrl("/images/logo.png");
        fileDTO.setMessage("成功");
        System.out.println(guid + "图片上传成功了");
        return fileDTO;
    }
}
