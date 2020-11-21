import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @auther JianLinWei
 * @date 2020-11-19 23:02
 */
public class Main {

    public static void main(String[] args) {
        List<String> s = new ArrayList<>();
        s.add("1");
        s.add("2");s.add("3");

         String s1 = s.toString();
        System.out.println(s1);

    }
}
