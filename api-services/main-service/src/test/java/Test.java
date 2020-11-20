import com.alibaba.fastjson.JSON;
import com.mj.mainservice.MainApplication;
import com.mj.mainservice.service.system.ISysAdminService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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


    @org.junit.Test
    public  void test(){
       /* List<String>  strings = iSysAdminService.getChildByUerIds("526788978314186752");
        System.out.println(strings);
        System.out.println("输出"+strings.size());*/

    }


}
