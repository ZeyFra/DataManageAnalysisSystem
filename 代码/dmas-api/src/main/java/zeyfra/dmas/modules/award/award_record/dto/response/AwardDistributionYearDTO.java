package zeyfra.dmas.modules.award.award_record.dto.response;

import lombok.Data;

/**
 * @author ZeyFra
 * @date 2021/4/27 15:37
 */
@Data
public class AwardDistributionYearDTO {

    private String year;
    private int num;

    public AwardDistributionYearDTO() {
    }

    public AwardDistributionYearDTO(String year, int num) {
        this.year = year;
        this.num = num;
    }
}
