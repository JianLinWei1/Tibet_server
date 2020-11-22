import com.alibaba.fastjson.JSON;
import com.mj.mainservice.MainApplication;
import com.mj.mainservice.entitys.access.Translation;
import com.mj.mainservice.service.access.AccessService;
import com.mj.mainservice.service.access.TranslationService;
import com.mj.mainservice.service.system.ISysAdminService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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


    @org.junit.Test
    public  void test(){
       /* List<String>  strings = iSysAdminService.getChildByUerIds("526788978314186752");
        System.out.println(strings);
        System.out.println("输出"+strings.size());*/
       List<Translation>  translations = new ArrayList<>();
       Translation translation = new Translation();
       translation.setUserId("1");
       translation.setName("拉萨2");
       translation.setDvName("701门禁");
       translation.setSn("DGD9340019072202658");
       translation.setPersonId("lasa2");
       translation.setIcCard("3450074351");
       translation.setTime(LocalDateTime.now());
       translations.add(translation);
       translationService.upload(translations);

    }


}
