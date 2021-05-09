package zeyfra.dmas.modules.award.award_record.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zeyfra.dmas.modules.award.award_record.dto.request.AwardRecordUploadDTO;
import zeyfra.dmas.modules.member.listener.MemberDataListener;

import java.util.List;

/**
 * @author ZeyFra
 * @date 2021/4/20 22:08
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AwardRecordListener extends AnalysisEventListener<AwardRecordUploadDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemberDataListener.class);
    private List<AwardRecordUploadDTO> uploadDTOList;


    public AwardRecordListener(List<AwardRecordUploadDTO> list) {
        this.uploadDTOList = list;
    }

    @Override
    public void invoke(AwardRecordUploadDTO awardRecordUploadDTO, AnalysisContext analysisContext) {
        LOGGER.info("解析到一条数据："+awardRecordUploadDTO.toString());
        uploadDTOList.add(awardRecordUploadDTO);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        LOGGER.info("全部数据解析完毕！共解析到："+uploadDTOList.size()+"条数据！");
    }
}
