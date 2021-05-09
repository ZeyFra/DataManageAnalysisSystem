package zeyfra.dmas.modules.award.award_record.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author ZeyFra
 * @date 2021/4/21 21:12
 */
@Data
public class AwardRecordAndMember {

    private Integer awardId;

    private String organizationalUnit;

    private String awardYear;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date awardTime;

    private String awardType;

    private String awardName;

    private String awardLevel;

    private String certificateLevel;

    private String theSchoolRecognizesTheAwardLevel;

    private String awardingUnit;

    private List<MemberInfo> awardMembers;


}
