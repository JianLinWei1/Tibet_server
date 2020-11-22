package com.mj.mainservice.entitys.person;

import com.jian.common.util.PageHelper;
import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * @auther JianLinWei
 * @date 2020-11-21 19:49
 */
@Data
public class Department  extends PageHelper {
    @Id
    private String id ;
    private String name;
    private String nickName;
    private String userId;
}
