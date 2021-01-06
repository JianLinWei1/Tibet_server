import com.alibaba.fastjson.JSON;
import com.mj.mainservice.MainApplication;
import com.mj.mainservice.entitys.access.AccessPerson;
import com.mj.mainservice.entitys.access.DeviceInfo;
import com.mj.mainservice.entitys.access.Doors;
import com.mj.mainservice.entitys.access.Translation;
import com.mj.mainservice.entitys.parking.ParkInfo;
import com.mj.mainservice.resposity.access.AccessPersonResposity;
import com.mj.mainservice.resposity.access.AccessRespository;
import com.mj.mainservice.service.access.AccessService;
import com.mj.mainservice.service.access.TranslationService;
import com.mj.mainservice.service.impl.UploadRecordServiceImpl;
import com.mj.mainservice.service.parking.UploadRecoedService;
import com.mj.mainservice.service.system.ISysAdminService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @auther JianLinWei
 * @date 2020-11-19 22:39
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class Test {

    @Autowired
    private ISysAdminService iSysAdminService;
    @Autowired
    private AccessService translationService;
    @Autowired
    private AccessRespository accessRespository;
    @Autowired
    private AccessPersonResposity personResposity;
    @Autowired
    private UploadRecoedService  uploadRecoedService;


    @org.junit.Test
    public  void test(){
       /* List<Integer>  doors = new ArrayList<>();
        for(int i = 1 ; i<= 4 ; i++){
            Doors doors1 = new Doors();
            doors1.setId(i);
            doors.add(i);
            //doors1.setName(String.valueOf(i));
        }

        AccessPerson  accessPerson = personResposity.findByPidEqualsAndAdvIdEqualsAndDoorsNum("702605", "DGD0380010082100164",doors );

        System.out.println(accessPerson);*/



    }


}
