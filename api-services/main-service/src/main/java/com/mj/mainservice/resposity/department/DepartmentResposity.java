package com.mj.mainservice.resposity.department;


import com.mj.mainservice.entitys.person.Department;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.stereotype.Repository;

/**
 * @auther JianLinWei
 * @date 2020-11-21 19:56
 */
@Repository
public interface DepartmentResposity extends MongoRepository<Department , String> {

    Page<Department>   findAll(Example<Department> example , Query query, Pageable pageable);

    Department  findByNameEqualsAndUserIdEquals(String name , String userId);
}
