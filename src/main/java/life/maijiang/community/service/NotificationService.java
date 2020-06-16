package life.maijiang.community.service;

import life.maijiang.community.Exception.CustomizeErrorCode;
import life.maijiang.community.Exception.CustomizeException;
import life.maijiang.community.dto.NotificationDTO;
import life.maijiang.community.dto.PaginationDTO;
import life.maijiang.community.dto.QuestionDTO;
import life.maijiang.community.enums.NotificationStatusEnum;
import life.maijiang.community.enums.NotificationTypeEnum;
import life.maijiang.community.mapper.NotificationMapper;
import life.maijiang.community.model.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class NotificationService {
    @Autowired
    private NotificationMapper notificationMapper;

    //分页查询最新回复内容
    public PaginationDTO list(Long userId, Integer page, Integer size) {
        //offset = page -1
        //容错处理
        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<NotificationDTO>();
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(userId);
        Integer toltalCount =(int)notificationMapper.countByExample(new NotificationExample());
        //Integer toltalCount = questionMapper.countByUserId(userId);
        Integer toltalPage;
        if(toltalCount%size==0){
            toltalPage = toltalCount/size;
        }else{
            toltalPage = toltalCount/size + 1 ;
        }
        if(page < 1 ){
            page = 1;
        }
        if(page > toltalPage){
            page = toltalPage;
        }
        paginationDTO.setPaginations(toltalPage,page);
        Integer offset = size*(page - 1);
        NotificationExample example1 = new NotificationExample();
        example1.createCriteria().andReceiverEqualTo(userId);
        example1.setOrderByClause("gmt_create desc");
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(example1, new RowBounds(offset, size));

        if(notifications.size() == 0){
            return paginationDTO;
        }
        List<NotificationDTO> notificationDTOS = new ArrayList<NotificationDTO>();
        for (Notification notification:notifications) {
           NotificationDTO notificationDTO = new NotificationDTO();
           BeanUtils.copyProperties(notification,notificationDTO);
           notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
           notificationDTOS.add(notificationDTO);
           paginationDTO.setData(notificationDTOS);
        }

        return paginationDTO;
    }
    //根据用户id读取通知表，count出未读数
    public Long unreadCount(Long userId) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(userId).andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(notificationExample);
    }

    //根据用户点击，将状态更新为已读
    public NotificationDTO read(Long id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if(notification == null){
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }

        if(!Objects.equals(notification.getReceiver(),user.getId())){
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_ERROR);
        }
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);
        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification,notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDTO;
    }
}
