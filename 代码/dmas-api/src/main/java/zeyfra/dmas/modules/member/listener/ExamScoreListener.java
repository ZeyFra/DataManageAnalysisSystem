package zeyfra.dmas.modules.member.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zeyfra.dmas.modules.member.dto.response.ExamScoreInfoDTO;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author ZeyFra
 * @date 2021/4/12 21:55
 */
public class ExamScoreListener  extends AnalysisEventListener<ExamScoreInfoDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExamScoreListener.class);

    private final List<ExamScoreInfoDTO> list;

    private final String memberId;

    private static final String GENERAL_EDUCATION_PLATFORM_COURSE_TYPE = "通识教育平台课";
    private static final String GENERAL_EDUCATION = "通识教育";

    public ExamScoreListener(List<ExamScoreInfoDTO> list, String memberId) {
        this.list = list;
        this.memberId = memberId;
    }


    @Override
    public void invoke(ExamScoreInfoDTO data, AnalysisContext context) {
        Pattern pattern = Pattern.compile(GENERAL_EDUCATION);
        String memberId = data.getMemberId();
        if(this.memberId.equals(memberId)){
            String courseType = data.getCourseType();
            if(pattern.matcher(courseType).find()){
                if(GENERAL_EDUCATION_PLATFORM_COURSE_TYPE.equals(courseType)){
                    list.add(data);
                }
            }else {
                list.add(data);
            }
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

        LOGGER.info("所有数据解析完成！");
        LOGGER.info("一共解析到："+list.size()+"条数据");
    }

    public List<ExamScoreInfoDTO> getList(){
        return list;
    }

}
