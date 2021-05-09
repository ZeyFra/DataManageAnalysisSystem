package zeyfra.dmas.modules.award.award_record.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.multipart.MultipartFile;
import zeyfra.dmas.modules.award.award_record.dto.request.AwardRecordUploadDTO;
import zeyfra.dmas.modules.award.award_record.dto.response.AwardDistributionDTO;
import zeyfra.dmas.modules.award.award_record.dto.response.AwardDistributionYearDTO;
import zeyfra.dmas.modules.award.award_record.dto.response.AwardRecordAndMember;
import zeyfra.dmas.modules.award.award_record.dto.response.MemberInfo;
import zeyfra.dmas.modules.award.award_record.entity.AwardRecord;
import zeyfra.dmas.modules.award.award_record.listener.AwardRecordListener;
import zeyfra.dmas.modules.award.award_record.mapper.AwardRecordMapper;
import zeyfra.dmas.modules.award.award_record.service.AwardRecordService;
import zeyfra.dmas.modules.award.award_record_member.entity.AwardRecordMember;
import zeyfra.dmas.modules.award.award_record_member.service.AwardRecordMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import zeyfra.dmas.modules.member.entity.Member;
import zeyfra.dmas.modules.member.service.MemberService;

import java.io.IOException;
import java.nio.file.WatchKey;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ZeyFra
 * @since 2021-04-20
 */
@Service
public class AwardRecordServiceImpl extends ServiceImpl<AwardRecordMapper, AwardRecord> implements AwardRecordService {

    private AwardRecordMemberService awardRecordMemberService;
    private MemberService memberService;

    @Lazy
    @Autowired
    public void setAwardRecordMemberService(AwardRecordMemberService awardRecordMemberService){
        this.awardRecordMemberService = awardRecordMemberService;
    }

    @Lazy
    @Autowired
    public void setMemberService(MemberService memberService){
        this.memberService = memberService;
    }

    @Override
    public Boolean uploadAwardRecord(MultipartFile file) throws IOException {
        List<AwardRecord> awardRecordList = new ArrayList<>();
        List<AwardRecordUploadDTO> uploadDTOList = new ArrayList<>();
        List<AwardRecordMember> recordMemberList = new ArrayList<>();
        List<Member> memberList = memberService.list();

        AwardRecordListener listener = new AwardRecordListener(uploadDTOList);
        EasyExcel.read(file.getInputStream(), AwardRecordUploadDTO.class, listener).sheet(0).doRead();
        uploadDTOList = listener.getUploadDTOList();
        for(AwardRecordUploadDTO item : uploadDTOList){
            AwardRecord temp = new AwardRecord();
            temp.setAwardingUnit(item.getAwardingUnit());
            temp.setAwardLevel(item.getAwardLevel());
            temp.setAwardName(item.getAwardName());
            temp.setAwardTime(item.getAwardTime());
            temp.setAwardType(item.getAwardType());
            temp.setAwardYear(item.getAwardYear());
            temp.setCertificateLevel(item.getCertificateLevel());
            temp.setTheSchoolRecognizesTheAwardLevel(item.getTheSchoolRecognizesTheAwardLevel());
            temp.setOrganizationalUnit(item.getOrganizationalUnit());
            this.save(temp);
            if(item.getAwardMember() == null){
                continue;

            }
            String memberNamesArray = item.getAwardMember();
            if("".equals(memberNamesArray)){
                continue;
            }
            String[] memberNames = memberNamesArray.split("、");
            for (String memberName : memberNames){
                String memberId;
                for(Member tempMember : memberList){
                    if (memberName.equals(tempMember.getMemberName())){
                        memberId = tempMember.getMemberId();
                        AwardRecordMember awardRecordMember = new AwardRecordMember();
                        awardRecordMember.setMemberId(memberId);
                        awardRecordMember.setAwardId(temp.getAwardId());
                        recordMemberList.add(awardRecordMember);
                        break;
                    }
                }


            }
        }
        return awardRecordMemberService.saveBatch(recordMemberList);
    }

    @Override
    public List<AwardRecordAndMember> getAwardList() {

        List<AwardRecordAndMember> resultList = new ArrayList<>();

        QueryWrapper<AwardRecord> wrapper = new QueryWrapper<>();
        wrapper.lambda().orderByDesc(AwardRecord::getAwardTime);
        List<AwardRecord> awardRecordList = list(wrapper);

        //遍历所有获奖
        for (AwardRecord awardRecord : awardRecordList) {

            QueryWrapper<AwardRecordMember> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(AwardRecordMember::getAwardId, awardRecord.getAwardId());
            List<AwardRecordMember> recordMemberList = awardRecordMemberService.list(queryWrapper);

            AwardRecordAndMember temp = new AwardRecordAndMember();

            temp.setAwardId(awardRecord.getAwardId());
            temp.setAwardName(awardRecord.getAwardName());
            temp.setAwardType(awardRecord.getAwardType());
            temp.setOrganizationalUnit(awardRecord.getOrganizationalUnit());
            temp.setAwardingUnit(awardRecord.getAwardingUnit());
            temp.setAwardLevel(awardRecord.getAwardLevel());
            temp.setAwardYear(awardRecord.getAwardYear());
            temp.setAwardTime(awardRecord.getAwardTime());
            temp.setCertificateLevel(awardRecord.getCertificateLevel());
            temp.setTheSchoolRecognizesTheAwardLevel(awardRecord.getTheSchoolRecognizesTheAwardLevel());
            List<MemberInfo> memberInfoList = new ArrayList<>();
            // 获奖-队员 记录
            for (AwardRecordMember awardRecordMember : recordMemberList) {

                QueryWrapper<Member> memberQueryWrapper = new QueryWrapper<>();
                memberQueryWrapper.lambda().eq(Member::getMemberId, awardRecordMember.getMemberId());
                List<Member> list = memberService.list(memberQueryWrapper);
                //找出和本厂比赛相关的队员

                for (Member member : list) {
                    MemberInfo memberInfo = new MemberInfo();
                    memberInfo.setMemberId(member.getMemberId());
                    memberInfo.setMemberName(member.getMemberName());
                    memberInfo.setNowCoderId(member.getNowCoderId());
                    memberInfoList.add(memberInfo);
                }


            }
            temp.setAwardMembers(memberInfoList);
            resultList.add(temp);
        }
        
        return resultList;
    }

    @Override
    public List<AwardRecord> getAwardRecordByMemberId(String memberId) {
        List<AwardRecord> recordList = new ArrayList<>();

        QueryWrapper<AwardRecordMember> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(AwardRecordMember::getMemberId,memberId);
        List<AwardRecordMember> awardRecordMemberList = awardRecordMemberService.list(wrapper);

        for (AwardRecordMember awardRecordMember : awardRecordMemberList) {
            QueryWrapper<AwardRecord> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(AwardRecord::getAwardId,awardRecordMember.getAwardId());
            AwardRecord one = this.getOne(queryWrapper);
            recordList.add(one);
        }
        return recordList;
    }

    @Override
    public AwardDistributionDTO getAllRecordDistribution() {
        List<AwardRecord> awardRecordList = list();

        int nationalLevel1 = 0;
        int nationalLevel2 = 0;
        int nationalLevel3 = 0;
        int provincialLevel1 = 0;
        int provincialLevel2 = 0;
        int provincialLevel3 = 0;

        for (AwardRecord awardRecord : awardRecordList) {
            String awardLevel = awardRecord.getTheSchoolRecognizesTheAwardLevel();
            if(awardLevel.contains("国家")){
                if(awardLevel.contains("一等奖")){
                    nationalLevel1++;
                }else if(awardLevel.contains("二等奖")){
                    nationalLevel2++;
                }else if(awardLevel.contains("三等奖")){
                    nationalLevel3++;
                }else if(awardLevel.contains("特等奖")){
                    provincialLevel1++;
                }
            }else if(awardLevel.contains("省级")){
                if(awardLevel.contains("一等奖")){
                    provincialLevel1++;
                }else if(awardLevel.contains("二等奖")){
                    provincialLevel2++;
                }else if(awardLevel.contains("三等奖")){
                    provincialLevel3++;
                }else if(awardLevel.contains("特等奖")){
                    provincialLevel1++;
                }
            }
        }
        return new AwardDistributionDTO(nationalLevel1,
                nationalLevel2,nationalLevel3,provincialLevel1,
                provincialLevel2,provincialLevel3);
    }

    @Override
    public List<AwardDistributionYearDTO> getAllRecordYearDistribution() {

        List<AwardDistributionYearDTO> result = new LinkedList<>();

        List<AwardRecord> awardRecordList = list();
        Map<String, Integer> yearMap = new HashMap<>();
        for (AwardRecord awardRecord : awardRecordList) {
            String awardYear = awardRecord.getAwardYear();
            if(yearMap.containsKey(awardYear)){
                Integer count = yearMap.get(awardYear);
                count++;
                yearMap.replace(awardYear,count);
            }else {
                yearMap.put(awardYear,1);
            }
        }

        yearMap.forEach((year,awardNum) -> {
            AwardDistributionYearDTO awardDistributionYearDTO = new AwardDistributionYearDTO(year,awardNum);
            result.add(awardDistributionYearDTO);
        });
        result.sort(Comparator.comparing(AwardDistributionYearDTO::getYear));
        return result;
    }
}
