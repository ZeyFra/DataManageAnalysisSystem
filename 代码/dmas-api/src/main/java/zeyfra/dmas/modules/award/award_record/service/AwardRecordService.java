package zeyfra.dmas.modules.award.award_record.service;

import org.springframework.web.multipart.MultipartFile;
import zeyfra.dmas.modules.award.award_record.dto.response.AwardDistributionDTO;
import zeyfra.dmas.modules.award.award_record.dto.response.AwardDistributionYearDTO;
import zeyfra.dmas.modules.award.award_record.entity.AwardRecord;
import zeyfra.dmas.modules.award.award_record.dto.response.AwardRecordAndMember;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ZeyFra
 * @since 2021-04-20
 */
public interface AwardRecordService extends IService<AwardRecord> {

    Boolean uploadAwardRecord(MultipartFile file) throws IOException;

    List<AwardRecordAndMember> getAwardList();

    List<AwardRecord> getAwardRecordByMemberId(String memberId);

    AwardDistributionDTO getAllRecordDistribution();

    List<AwardDistributionYearDTO> getAllRecordYearDistribution();
}
