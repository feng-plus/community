package life.maijiang.community.cache;

import life.maijiang.community.dto.TagDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TagCache {

    public static List<TagDTO> getTags(){
        List<TagDTO> tagDTOS = new ArrayList<TagDTO>();
        TagDTO program = new TagDTO();
        program.setCategoryName("开发语言");
        program.setTagList(Arrays.asList("js","php","css","c++","c","java","cobol","html5"));
        tagDTOS.add(program);

        TagDTO framework = new TagDTO();
        framework.setCategoryName("开发框架");
        framework.setTagList(Arrays.asList("spring boot","spring","spring mvc","struts2","koa","express"));
        tagDTOS.add(framework);

        TagDTO server = new TagDTO();
        server.setCategoryName("服务器");
        server.setTagList(Arrays.asList("linux","apache","负载均衡"));
        tagDTOS.add(server);

        TagDTO db = new TagDTO();
        db.setCategoryName("数据库");
        db.setTagList(Arrays.asList("mysql"));
        tagDTOS.add(db);

        TagDTO kit = new TagDTO();
        kit.setCategoryName("开发工具");
        kit.setTagList(Arrays.asList("idea"));
        tagDTOS.add(kit);

        return tagDTOS;
    }

    public static String filterInvalid(String tags){
        String[] split = tags.split(",");
        List<TagDTO> tagDTOList = getTags();

        List<String> tagList = tagDTOList.stream().flatMap(tag -> tag.getTagList().stream()).collect(Collectors.toList());
        String invalid = Arrays.stream(split).filter(t -> !tagList.contains(t)).collect(Collectors.joining(","));
        return invalid;
    }
}
