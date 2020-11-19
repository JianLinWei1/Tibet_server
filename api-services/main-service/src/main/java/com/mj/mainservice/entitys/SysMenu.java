package com.mj.mainservice.entitys;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author JianLinWei
 * @since 2020-09-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysMenu implements Serializable {

    private static final long serialVersionUID = 1L;

     @TableId
    private String id ;

    /**
     * 路由
     */
    private String router;

    /**
     * 路由名称
     */
    private String name;

    /**
     * 图标
     */
    private String icon;

    /**
     * 权限
     */
    private String permission;


    private String authority;

    /**
     * 父id
     */
    private String parentId;


}
