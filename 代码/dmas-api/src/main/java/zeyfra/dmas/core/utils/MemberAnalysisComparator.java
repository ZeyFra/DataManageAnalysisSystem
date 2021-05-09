package zeyfra.dmas.core.utils;

import zeyfra.dmas.modules.member.dto.response.MemberAnalysisBasicInfos;

import java.util.Comparator;

/**
 * @author ZeyFra
 * @date 2021/4/24 16:56
 */
public class MemberAnalysisComparator implements Comparator<MemberAnalysisBasicInfos> {

    @Override
    public int compare(MemberAnalysisBasicInfos o1, MemberAnalysisBasicInfos o2) {
        int o1PrizeNum = o1.getNationalPrizeNum() + o1.getProvincialPrizeNum();
        int o2PrizeNum = o2.getNationalPrizeNum() + o2.getProvincialPrizeNum();
        return Integer.compare(o2PrizeNum, o1PrizeNum);
    }
}
