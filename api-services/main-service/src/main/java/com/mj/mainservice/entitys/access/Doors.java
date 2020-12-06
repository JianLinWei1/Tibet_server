package com.mj.mainservice.entitys.access;

import lombok.Data;

/**
 * @auther JianLinWei
 * @date 2020-12-02 22:37
 */
@Data
public class Doors {
    private Integer id;
    private String name;

    public Doors() {
        super();
    }
    public Doors(int id ,String name){
        this.id = id ;
        this.name = name;
    }
}
