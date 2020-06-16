package life.maijiang.community.Exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode{

    QUESTION_NOT_FOUND(2001,"你找的问题不存在了,要不换个试试？"),
    TARGET_PARENT_BOT_FOUND(2002,"未选中任何问题或评论进行回复"),
    NO_LOGON(2003,"当前操作需要登录，请登录后重试"),
    SYSTEM_ERROR(2004,"服务冒烟了，请稍后重试一下？"),
    TYPE_PARAM_WRONG(2005,"评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006,"回复的评论不存在了,要不换个试试？"),
    CONTENT_IS_EMPTY(2007,"输入的内容不能为空"),
    READ_NOTIFICATION_ERROR(2008,"非法操作别人的通知"),
    NOTIFICATION_NOT_FOUND(2009,"通知不翼而飞了");
    @Override
    public String getMessage(){
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    private Integer code;
    private String message;
    CustomizeErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
