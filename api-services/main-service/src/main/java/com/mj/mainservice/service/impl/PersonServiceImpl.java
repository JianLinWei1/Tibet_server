package com.mj.mainservice.service.impl;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.text.csv.CsvUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.jian.common.entity.AntdTree;
import com.jian.common.util.ResultUtil;

import com.mj.mainservice.entitys.access.AccessPerson;
import com.mj.mainservice.entitys.access.Translation;
import com.mj.mainservice.entitys.parking.ParkingUserInfo;
import com.mj.mainservice.entitys.person.Department;
import com.mj.mainservice.entitys.person.PersonInfo;
import com.mj.mainservice.mapper.SysAdminMapper;
import com.mj.mainservice.resposity.access.AccessPersonResposity;
import com.mj.mainservice.resposity.department.DepartmentResposity;
import com.mj.mainservice.resposity.parking.ParkingPersonResposity;
import com.mj.mainservice.resposity.person.PersonRepository;
import com.mj.mainservice.service.person.PersonService;
import com.mj.mainservice.vo.person.ImportPersonVo;
import com.mj.mainservice.vo.person.PersonInfoVo;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Log4j2
public class PersonServiceImpl implements PersonService {
    //    @Resource
//    private MongoTemplate mongoTemplate;
    @Resource
    private PersonRepository personRepository;
    @Resource
    private MongoTemplate mongoTemplate;
    @Resource
    private AccessPersonResposity accessPersonResposity;
    @Resource
    private ParkingPersonResposity parkingPersonResposity;
    @Resource
    private DepartmentResposity departmentResposity;
    @Resource
    private SysAdminMapper adminMapper;
    @Value("${server.port}")
    private String port;


    @Override
    @Transactional
    public ResultUtil insertPerson(PersonInfo info) {
        try {
            if (personRepository.findById(info.getId()).isPresent())
                return new ResultUtil(-1, "该ID已存在");
            PersonInfo personInfo1 = personRepository.findByAccessIdEquals(info.getAccessId());
            if (personInfo1 != null)
                return new ResultUtil(-1, "该门禁卡号已经被注册");
            info.setCreateTime(LocalDateTime.now(ZoneId.systemDefault()));
            personRepository.insert(info);
            return ResultUtil.ok();
        } catch (Exception e) {
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }

    }

    @Override
    public ResultUtil queryPersonsList(PersonInfo info) {
        try {
            PageRequest pageRequest = PageRequest.of(info.getPage() - 1, info.getLimit(), Sort.by(Sort.Direction.DESC, "createTime"));
            List<PersonInfo> personInfos = new ArrayList<PersonInfo>();
            long total = 0;
//            if (StringUtils.isNotEmpty(info.getId()) || StringUtils.isNotEmpty(info.getCarId()) ||
//                    StringUtils.isNotEmpty(info.getName()) || StringUtils.isNotEmpty(info.getAccessId()) || info.getRole() != null) {
            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withMatcher("id", ExampleMatcher.GenericPropertyMatchers.exact())
                    .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains())
                    .withMatcher("accessId", ExampleMatcher.GenericPropertyMatchers.contains())
                    .withMatcher("idCard", ExampleMatcher.GenericPropertyMatchers.contains())
                    .withMatcher("department", ExampleMatcher.GenericPropertyMatchers.contains())
                    .withMatcher("userId", ExampleMatcher.GenericPropertyMatchers.exact())
                    .withIgnorePaths("page", "limit", "accessPw")
                    .withNullHandler(ExampleMatcher.NullHandler.IGNORE)
                    .withIgnoreNullValues();


            Example<PersonInfo> example = Example.of(info, matcher);

            Page<PersonInfo> personInfos1 = personRepository.findAll(example, pageRequest);

            personInfos = personInfos1.toList();
            total = personInfos1.getTotalElements();
//            } else {
//                Page<PersonInfo> personInfos2 = personRepository.findAll(pageRequest);
//                personInfos = personInfos2.toList();
//                total = personInfos2.getTotalElements();
//            }


            ResultUtil resultUtil = new ResultUtil();
            resultUtil.setCode(0);
            resultUtil.setCount(total);
            resultUtil.setData(personInfos);
            return resultUtil;

        } catch (Exception e) {
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }

    }

    @Override
    public ResultUtil queryPersonsListByName(PersonInfo info, List<String> childs) {
        try {
            if (StringUtils.isEmpty(info.getName()))
                return ResultUtil.ok();
            childs.add(info.getUserId());
            List<PersonInfo> personInfos1 = personRepository.findAllByNameContainsAndUserIdIn(info.getName(), childs);


            ResultUtil resultUtil = new ResultUtil();
            resultUtil.setCode(0);

            resultUtil.setData(personInfos1);
            return resultUtil;

        } catch (Exception e) {
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResultUtil editPerson(PersonInfoVo info) {
        try {
            PersonInfo personInfo = new PersonInfo();
            BeanUtils.copyProperties(info, personInfo);
            if (StringUtils.isNotEmpty(info.getOid()))
                personRepository.deleteById(info.getOid());

            personRepository.save(personInfo);


            return ResultUtil.ok();
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResultUtil(-1, e.getMessage());
        }

    }

    @Override
    @Transactional
    public ResultUtil delPerson(List<String> ids) {
        try {
            List<String> dels = new ArrayList<>();
            ids.stream().forEach(id -> {
                List<AccessPerson> accessPerson = accessPersonResposity.findAllByPidEquals(id);
                if (accessPerson.size() > 0) {
                    dels.add(id);
                    return;
                }
                List<ParkingUserInfo> parkingUserInfos = parkingPersonResposity.findAllByPersonIdEquals(id);
                if (parkingUserInfos.size() > 0) {
                    dels.add(id);
                    return;
                }
                personRepository.deleteById(id);
            });
            if (dels.size() > 0)
                return new ResultUtil(-2, "存在已下发人员，请先删除从门禁设备/车辆删除,人员ID:" + JSON.toJSONString(dels));

            return ResultUtil.ok();
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResultUtil(-1, e.getMessage());
        }
    }

    @Override
    public List<PersonInfo> quryPersonListNoPage(PersonInfo personInfo) {
        try {


//            if (StringUtils.isNotEmpty(info.getId()) || StringUtils.isNotEmpty(info.getCarId()) ||
//                    StringUtils.isNotEmpty(info.getName()) || StringUtils.isNotEmpty(info.getAccessId()) || info.getRole() != null) {
            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withMatcher("id", ExampleMatcher.GenericPropertyMatchers.exact())
                    .withMatcher("userId", ExampleMatcher.GenericPropertyMatchers.exact())
                    //.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                    .withIgnorePaths("page", "limit", "accessPw")
                    .withNullHandler(ExampleMatcher.NullHandler.IGNORE);
            Example<PersonInfo> example = Example.of(personInfo, matcher);
            Query query = new Query();
            query.addCriteria(Criteria.byExample(example));

            List<PersonInfo> personInfos = personRepository.findAll(example);

            return personInfos;

        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    @Override
    public PersonInfo getPersonById(String id) {
        try {
            PersonInfo personInfo = personRepository.findById(id).get();
            return personInfo;
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }


    @Override
    public ResultUtil exportPerson(List<PersonInfoVo> personInfoVo) {
        try {

            personInfoVo.stream().forEach(p -> {
                try {
                    if (p.getPhoto() != null)
                        p.setPhotoUrl(new URL("http://localhost:" + port + "/" + p.getPhoto().replace("\\", "/")));
                } catch (Exception e) {
                    log.error(e);
                }
                StringBuilder carIds = new StringBuilder();
                if (p.getCarId() != null && p.getCarId().size() > 0)
                    p.getCarId().stream().forEach(c -> {
                        carIds.append(c);
                        carIds.append(";");
                    });
                p.setCarIds(carIds.toString());
            });

            String path = System.currentTimeMillis() + ".xlsx";
            EasyExcel.write("upload" + File.separator + path, PersonInfoVo.class).sheet("sheet").doWrite(personInfoVo);
            return new ResultUtil(0, path, "");
        } catch (Exception e) {
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }
    }

    @Override
    public ResultUtil importPerson(MultipartFile file, String userId) {
        try {
            File file1 = FileUtil.newFile("upload" + File.separator + "" + System.currentTimeMillis() + ".csv");
            if (!file1.exists())
                file1.createNewFile();

            CsvReader reader = CsvUtil.getReader();
            FileUtils.copyInputStreamToFile(file.getInputStream(), file1);

            List<CsvRow> rows = reader.read(file1).getRows();
            rows.remove(0);
            //List<ImportPersonVo>  personVos = reader.read(ResourceUtil.getUtf8Reader(file1.getAbsolutePath()), ImportPersonVo.class);
            rows.stream().forEach(c -> {
                List<String> strings = c.getRawList();
                PersonInfo personInfo = new PersonInfo();
                personInfo.setName(strings.get(0));
                personInfo.setId(strings.get(1));
                personInfo.setDepartment(strings.get(2));
                personInfo.setCreateTime(LocalDateTime.now());
                Optional<PersonInfo> optional = personRepository.findById(personInfo.getId());
                if (optional.isPresent())
                    return;

                Department department = departmentResposity.findByNameEqualsAndUserIdEquals(personInfo.getDepartment(), userId);
                if (department == null) {
                    department = new Department();
                    department.setNickName(adminMapper.selectById(userId).getNickName());
                    department.setName(personInfo.getDepartment());
                    department.setUserId(userId);
                    departmentResposity.save(department);
                }
                personInfo.setUserId(userId);
                personRepository.save(personInfo);


            });
            return ResultUtil.ok();
        } catch (Exception e) {
            log.error(e);
            return new ResultUtil(-1, e.getMessage());
        }
    }

    @Override
    public ResultUtil getPersonTree(String userId) {
        try {
           PersonInfo personInfo = new PersonInfo();
           personInfo.setUserId(userId);
           ExampleMatcher  matcher = ExampleMatcher.matching().withIgnorePaths("page","limit" ,"accessPw")
                   .withMatcher("userId", ExampleMatcher.GenericPropertyMatchers.exact());
           Example<PersonInfo>  example = Example.of(personInfo);
           List<PersonInfo> list = personRepository.findAll(example);
           List<AntdTree>  all =  new ArrayList<>();
            AntdTree antdTree = new AntdTree();
            antdTree.setKey("0");
            antdTree.setTitle("人员");
            List<AntdTree> child = new ArrayList<>();
            list.stream().forEach(p -> {
                AntdTree antdTree1 = new AntdTree();
                antdTree1.setKey(p.getId());
                antdTree1.setTitle(p.getName());
                child.add(antdTree1);
            });
            antdTree.setChildren(child);
            all.addAll(child);
            ResultUtil r = new ResultUtil();
            r.setCode(0);
            r.setData(all);
            return  r;
        }catch (Exception e){
            log.error(e);
            return  new ResultUtil(-1, e.getMessage());
        }
    }
}
