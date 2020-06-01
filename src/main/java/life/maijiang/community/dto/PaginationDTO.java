package life.maijiang.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO {
    private List<QuestionDTO> questionDTOS;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;
    private Integer page;
    private List<Integer> pages = new ArrayList<>();
    private Integer toltalPage;

    public void setPaginations(Integer toltalPage, Integer page) {
        this.toltalPage=toltalPage;
        this.page=page;
        pages.add(page);
        for(int i=1;i<=3;i++){
            if(page - i > 0){
                pages.add(0,page-i);
            }
            if(page + i <= toltalPage){
                pages.add(page +i);
            }
        }
        //是否展示上一页
        if(page == 1){
            showPrevious = false;
        }else{
            showPrevious = true;
        }
        //是否展示下一页
        if(page == toltalPage){
            showNext = false;
        }else{
            showNext = true;
        }
        //是否展示首页
        if(pages.contains(1)){
            showFirstPage = false;
        }else{
            showFirstPage = true;
        }
        //是否展示尾页
        if(pages.contains(toltalPage)){
            showEndPage = false;
        }else{
            showEndPage = true;
        }
    }
}
