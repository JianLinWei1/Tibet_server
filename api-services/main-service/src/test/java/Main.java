import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @auther JianLinWei
 * @date 2020-11-19 23:02
 */
public class Main {

    public static void main(String[] args) throws IOException {
        File file1 = FileUtil.newFile("upload"+File.separator+""+System.currentTimeMillis()+".csv");
        if(!file1.exists())
            file1.createNewFile();

    }
}
