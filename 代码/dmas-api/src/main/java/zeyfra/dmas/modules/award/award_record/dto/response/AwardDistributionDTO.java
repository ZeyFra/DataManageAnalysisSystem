package zeyfra.dmas.modules.award.award_record.dto.response;

import lombok.Data;

/**
 * @author ZeyFra
 * @date 2021/4/26 20:15
 */
@Data
public class AwardDistributionDTO {

    private Integer nationalLevel1;
    private Integer nationalLevel2;
    private Integer nationalLevel3;
    private Integer provincialLevel1;
    private Integer provincialLevel2;
    private Integer provincialLevel3;

    public AwardDistributionDTO(Integer nationalLevel1, Integer nationalLevel2,
                                Integer nationalLevel3, Integer provincialLevel1,
                                Integer provincialLevel2, Integer provincialLevel3) {
        this.nationalLevel1 = nationalLevel1;
        this.nationalLevel2 = nationalLevel2;
        this.nationalLevel3 = nationalLevel3;
        this.provincialLevel1 = provincialLevel1;
        this.provincialLevel2 = provincialLevel2;
        this.provincialLevel3 = provincialLevel3;
    }

}
