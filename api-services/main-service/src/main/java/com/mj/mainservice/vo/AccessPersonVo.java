package com.mj.mainservice.vo;


import com.mj.mainservice.entitys.access.AccessPerson;
import lombok.Data;

import java.util.List;

/**
 * Created by MrJan on 2020/10/20 16:42
 */

@Data
public class AccessPersonVo extends AccessPerson {
    private List<Object>  pids;

}
