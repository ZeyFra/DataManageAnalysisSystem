package zeyfra.dmas;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import zeyfra.dmas.modules.member.dto.response.NowCoderInfoDTO;
import zeyfra.dmas.modules.now_coder_record.entity.NowCoderRecord;
import zeyfra.dmas.modules.now_coder_record.service.NowCoderRecordService;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static zeyfra.dmas.core.utils.BeanMapper.copy;

//@SpringBootTest
class DmasApplicationTests {

    public class a{
        public String a;
        public String b;
    }

    public class b{
        public String a;
        public String b;
    }

    @Test
    void contextLoads() {
        System.out.println(TimeUnit.MILLISECONDS.toHours(7200000L));
    }

    private NowCoderRecordService nowCoderRecordService;

    @Autowired
    public void setNowCoderRecordService(NowCoderRecordService nowCoderRecordService){
        this.nowCoderRecordService = nowCoderRecordService;
    }

    @Test
    void test2(){
        QueryWrapper<NowCoderRecord> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(NowCoderRecord::getMemberId,"18202112");
        wrapper.lambda().orderByDesc(NowCoderRecord::getStartTime);
        List<NowCoderRecord> oldNowCoderRecordList = nowCoderRecordService.list(wrapper);

        for(NowCoderRecord temp : oldNowCoderRecordList){
            System.out.println(temp.toString());
        }
    }

    @Test
    void test3(){
        String str1 = "2019-03-01 19:00:00";
        String str2 = "2019-03-01 20:00:00";
        String str3 = "2021-03-01 19:00:00";
        String str4 = "2021-03-02 19:00:00";
        System.out.println(str1.compareTo(str2));
        System.out.println(str2.compareTo(str3));
        System.out.println(str3.compareTo(str4));
    }

    @Test
    void test4(){
        Map<String, String> map = new HashMap<>();
        map.put("1","a");
        map.put("2","b");
        map.put("3","c");
        map.put("6","a");
        System.out.println(map.toString());
        String s = map.get("1");
        System.out.println(map.toString());
        map.replace("1","c");
        System.out.println(map.toString());
    }

    @Test
    public void test5(){
        int max =123;
        int length = (max + "").length();
        System.out.println(length);
       // max = (int) (() * (Math.pow(length-1,10)));

        System.out.println();

    }
}
