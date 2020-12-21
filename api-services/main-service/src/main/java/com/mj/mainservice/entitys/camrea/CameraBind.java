package com.mj.mainservice.entitys.camrea;

import com.jian.common.util.PageHelper;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

/**
 * Created by MrJan on 2020/12/21
 */

@Data
public class CameraBind  extends PageHelper {
    private String admin ;
    private String pw ;
    @Id
    private String userId;
    @Transient
    private String name;
    private String serverIp;
}
