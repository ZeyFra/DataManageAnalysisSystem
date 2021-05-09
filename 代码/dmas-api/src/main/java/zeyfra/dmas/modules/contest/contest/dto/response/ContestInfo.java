package zeyfra.dmas.modules.contest.contest.dto.response;

import lombok.Data;
import zeyfra.dmas.modules.contest.contest.entity.Contest;

/**
 * @author ZeyFra
 * @date 2021/4/11 20:13
 */
@Data
public class ContestInfo extends Contest {

    public Integer myTeamNum;
    public Integer maxSubmitNum;

}
