import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.mj.mainservice.entitys.parking.ParkInfo;
import com.mj.mainservice.util.camera.entitys.Condition;
import com.mj.mainservice.util.camera.entitys.QueryCondition;
import com.mj.mainservice.util.camera.util.UniUtil;
import org.checkerframework.checker.units.qual.C;
import org.junit.platform.commons.util.StringUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @auther JianLinWei
 * @date 2020-11-19 23:02
 */
public class Main {

    public static void main(String[] args) throws IOException {
        /*File file1 = FileUtil.newFile("upload"+File.separator+""+System.currentTimeMillis()+".csv");
        if(!file1.exists())
            file1.createNewFile();*/

       /* String hex =Long.toHexString(4280476129l);
        System.out.println(hex);
        long num = 4280476129l&0x00FFFFFF;
        System.out.println(num);
        System.out.println(Long.toHexString(num));


        String  base = Base64.getEncoder().encodeToString("admin".getBytes());
        String sign = SecureUtil.md5(base + "oc9z0Idmj82fN99QKBoq" + SecureUtil.md5("Admin123"));
        System.out.println(sign);

        //49.4.85.77:8088
        UniUtil loginUtil = new UniUtil();
       String token =   loginUtil.login("Tingjiguan" ,"Admin123z");
        System.out.println(token);
         loginUtil.getResInfo(token ,0 , 10 ,"1");*/

        ExecutorService executorService = Executors.newCachedThreadPool();
        //测试心跳
        executorService.execute(()->{
            ParkInfo parkInfo = new ParkInfo();
            parkInfo.setSerialno("d85b1269-8f94257");
            while (true){
               String s= HttpRequest.post("http://localhost:8080/api/main/lj/heartBeat?serialno=d85b1269-8f94257").execute().body();
               if(StringUtils.isNotBlank(s))
                System.out.println("*****1*** 获取心跳"+s);
            }

        });

     executorService.execute(()->{
            ParkInfo parkInfo = new ParkInfo();
            parkInfo.setSerialno("d85b1269-8f942257");
            while (true){
                String s= HttpRequest.post("http://localhost:8080/api/main/lj/heartBeat?serialno=d85b1269-8f942257").execute().body();
                if(StringUtils.isNotBlank(s))
                    System.out.println("*****2*** 获取心跳"+s);
            }

        });
        executorService.execute(()->{
            ParkInfo parkInfo = new ParkInfo();
            parkInfo.setSerialno("d85b1269-8f942257");
            while (true){
                String s= HttpRequest.post("http://localhost:8080/api/main/lj/heartBeat?serialno=d85b1269-8f942257").execute().body();
                if(StringUtils.isNotBlank(s))
                    System.out.println("*****2*** 获取心跳"+s);
            }

        });
    }
}
