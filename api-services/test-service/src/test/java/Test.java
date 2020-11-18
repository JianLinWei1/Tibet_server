import com.google.inject.internal.asm.$Type;
import com.jian.testservice.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootTest
public class Test {

    @Autowired
    TestService service;



    @org.junit.Test
    public  void  test(){

    }
}
