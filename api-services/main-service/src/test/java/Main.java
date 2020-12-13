import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

    }
}
