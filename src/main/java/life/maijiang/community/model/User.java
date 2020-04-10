package life.maijiang.community.model;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String accountId;
    private String name;
    private String token;
    private String avatarUrl;
    private Long gmtCreate;
    private Long gmtModified;


}
