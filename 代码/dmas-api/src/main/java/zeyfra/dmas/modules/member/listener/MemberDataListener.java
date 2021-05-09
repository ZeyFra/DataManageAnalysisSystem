package zeyfra.dmas.modules.member.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zeyfra.dmas.modules.member.dto.request.MemberUploadDataDTO;
import zeyfra.dmas.modules.member.entity.Member;

import java.util.List;

import static zeyfra.dmas.core.utils.ObjectUtils.checkObjectFieldIsNull;

/**
 * @author ZeyFra
 * @date 2021/4/11 22:07
 */
public class MemberDataListener extends AnalysisEventListener<MemberUploadDataDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemberDataListener.class);
    private final List<Member> memberList;

    public MemberDataListener(List<Member> memberList){
        this.memberList = memberList;
    }

    public List<Member> getMemberList(){
        return memberList;
    }

    @Override
    public void invoke(MemberUploadDataDTO dataDTO, AnalysisContext analysisContext) {

        try {
            LOGGER.info("解析到一条数据："+dataDTO.toString());
            if(!checkObjectFieldIsNull(dataDTO)){
                Member member = new Member();
                member.setMemberId(dataDTO.getMemberId());
                member.setMemberName(dataDTO.getMemberName());
                member.setGender(dataDTO.getGender());
                member.setQq(dataDTO.getQq());
                member.setTelephone(dataDTO.getTelephone());
                member.setNowCoderId(dataDTO.getNowCoderId());
                memberList.add(member);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        LOGGER.info("全部数据解析完毕！");
        LOGGER.info("解析到"+memberList.size()+"条数据");
    }
}
