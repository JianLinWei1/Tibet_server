import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.mj.mainservice.util.camera.entitys.Condition;
import com.mj.mainservice.util.camera.entitys.QueryCondition;
import com.mj.mainservice.util.camera.util.UniUtil;
import org.checkerframework.checker.units.qual.C;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * @auther JianLinWei
 * @date 2020-11-19 23:02
 */
public class Main {

    public static void main(String[] args) throws IOException {
        /*File file1 = FileUtil.newFile("upload"+File.separator+""+System.currentTimeMillis()+".csv");
        if(!file1.exists())
            file1.createNewFile();*/

        String  base = Base64.getEncoder().encodeToString("admin".getBytes());
        String sign = SecureUtil.md5(base + "oc9z0Idmj82fN99QKBoq" + SecureUtil.md5("Admin123"));
        System.out.println(sign);

        //49.4.85.77:8088
        UniUtil loginUtil = new UniUtil();
       String token =   loginUtil.login("49.4.85.77" ,"admin" ,"Admin123");
        System.out.println(token);
         loginUtil.getCameraInfo("49.4.85.77" , token);
    }
}
