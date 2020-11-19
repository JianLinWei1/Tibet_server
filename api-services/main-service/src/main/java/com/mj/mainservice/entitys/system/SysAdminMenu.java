package com.mj.mainservice.entitys.system;

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
public class SysAdminMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 管理员id
     */
    private String adminId;

    /**
     * 菜单id
     */
    private String menuId;


}
