package zeyfra.dmas.modules.member.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import zeyfra.dmas.core.api.PagingReqDTO;
import zeyfra.dmas.core.utils.MemberAnalysisComparator;
import zeyfra.dmas.modules.award.award_record.entity.AwardRecord;
import zeyfra.dmas.modules.award.award_record.service.AwardRecordService;
import zeyfra.dmas.modules.award.award_record_member.entity.AwardRecordMember;
import zeyfra.dmas.modules.award.award_record_member.service.AwardRecordMemberService;
import zeyfra.dmas.modules.member.dto.request.MemberRequestDTO;
import zeyfra.dmas.modules.member.dto.request.MemberSearchRequestDTO;
import zeyfra.dmas.modules.member.dto.request.MemberUpdateRequestDTO;
import zeyfra.dmas.modules.member.dto.request.MemberUploadDataDTO;
import zeyfra.dmas.modules.member.dto.response.*;
import zeyfra.dmas.modules.member.entity.Member;
import zeyfra.dmas.modules.member.listener.ExamScoreListener;
import zeyfra.dmas.modules.member.listener.MemberDataListener;
import zeyfra.dmas.modules.member.mapper.MemberMapper;
import zeyfra.dmas.modules.member.service.MemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import zeyfra.dmas.modules.now_coder_record.entity.NowCoderRecord;
import zeyfra.dmas.modules.now_coder_record.service.NowCoderRecordService;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author ZeyFra
 * @since 2021-04-11
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

    private NowCoderRecordService nowCoderRecordService;
    private AwardRecordMemberService awardRecordMemberService;
    private AwardRecordService awardRecordService;

    @Autowired
    public void setNowCoderRecordService(NowCoderRecordService nowCoderRecordService){
        this.nowCoderRecordService = nowCoderRecordService;
    }

    @Autowired
    public void setAwardRecordMemberService(AwardRecordMemberService awardRecordMemberService) {
        this.awardRecordMemberService = awardRecordMemberService;
    }

    @Autowired
    public void setAwardRecordService(AwardRecordService awardRecordService) {
        this.awardRecordService = awardRecordService;
    }

    @Override
    public IPage<Member> paging(PagingReqDTO<Member> reqDTO) {

        //创建分页对象
        Page page = new Page(reqDTO.getCurrent(), reqDTO.getSize());

        //转换结果

        return baseMapper.paging(page);
    }

    @Override
    public IPage<Member> queryMemberByMemberId(PagingReqDTO<MemberSearchRequestDTO> reqDTO) {
        //创建分页对象
        Page page = new Page(reqDTO.getCurrent(), reqDTO.getSize());
        String memberId = reqDTO.getParams().getMemberId();
        String isRetire = reqDTO.getParams().getIsRetire();
        if(!("".equals(memberId)) && !("".equals(isRetire))){
            return baseMapper.queryMemberByMemberIdAndStae(page,memberId,Integer.valueOf(isRetire));

        }else if("".equals(memberId)){
            return baseMapper.queryMemberByState(page,Integer.valueOf(isRetire));
        }else{
            return baseMapper.queryMemberByMemberId(page,memberId);
        }
    }

    @Override
    public boolean updateMember(MemberUpdateRequestDTO reqDTO) {

        QueryWrapper<Member> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Member::getMemberId, reqDTO.getMemberId());
        Member member = this.getOne(wrapper);
        member.setMemberId(reqDTO.getMemberId());
        member.setMemberName(reqDTO.getMemberName());
        member.setTelephone(reqDTO.getTelephone());
        member.setGender(reqDTO.getGender());
        member.setQq(reqDTO.getQq());
        member.setIsRetire(reqDTO.getIsRetire());
        return this.updateById(member);
    }

    @Override
    public boolean uploadMember(MultipartFile file) {
        List<Member> memberList = new ArrayList<>();
        List<NowCoderRecord> recordList = new ArrayList<>();
        MemberDataListener listener = new MemberDataListener(memberList);
        try {
            EasyExcel.read(file.getInputStream(), MemberUploadDataDTO.class, listener).sheet(0).doRead();
            memberList = listener.getMemberList();
            List<Member> list = list();
            List<Member> finalMemberList = memberList;
            list.forEach(item -> finalMemberList.removeIf(temp -> temp.getMemberId().equals(item.getMemberId())));

            for(Member item : finalMemberList){
                if("无".equals(item.getNowCoderId())){
                    continue;
                }
                List<NowCoderInfoDTO> nowCoderInfoList = crawNowCoderInfo(item.getNowCoderId());
                packageNowCoderRecord(item, recordList, nowCoderInfoList);

                nowCoderRecordService.saveBatch(recordList);
                recordList.clear();
            }
            return this.saveBatch(finalMemberList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public List<NowCoderInfoDTO> crawNowCoderInfo(String nowCoderId) throws IOException {
        int pageCount = getPageCount(nowCoderId);
        List<NowCoderInfoDTO> list = new ArrayList<>();
        for(int i = 1; i <= pageCount;i++){
            String url = "https://ac.nowcoder.com/acm-heavy/acm/contest/profile/contest-joined-history?token=" +
                    "&uid=" + nowCoderId + "&page=" + i +
                    "&onlyJoinedFilter=true&searchContestName=&onlyRatingFilter=false&contestEndFilter=true";
            Document document = Jsoup.connect(url)
                    .header("Accept", "*/*")
                    .header("Accept-Encoding", "gzip, deflate")
                    .header("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
                    .header("Content-Type", "application/json;charset=UTF-8")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0")
                    .timeout(10000).ignoreContentType(true).get();
            Element body = document.body();
            JSONObject jsonObject = new JSONObject(body.text());
            try {
                JSONArray jsonArray =  jsonObject.getJSONObject("data").getJSONArray("dataList");
                for (int j = 0; j < jsonArray.length(); j++) {
                    NowCoderInfoDTO nowCoderInfoDTO = new NowCoderInfoDTO();
                    // 封装一个NewCoderInfoDTO对象
                    JSONObject tempJsonObject = jsonArray.getJSONObject(j);
                    nowCoderInfoDTO.setContestId(String.valueOf(tempJsonObject.getLong("contestId")));
                    nowCoderInfoDTO.setContestName(tempJsonObject.getString("contestName"));
                    nowCoderInfoDTO.setTeamName(tempJsonObject.getString("teamName"));
                    nowCoderInfoDTO.setProblemCount(tempJsonObject.getInt("problemCount"));
                    nowCoderInfoDTO.setAcceptedCount(tempJsonObject.getInt("acceptedCount"));
                    nowCoderInfoDTO.setRank(tempJsonObject.getInt("rank"));
                    nowCoderInfoDTO.setUserCount(tempJsonObject.getInt("userCount"));
                    nowCoderInfoDTO.setContestLogoUrl(tempJsonObject.getJSONObject("settingInfo").getString("logoUrl"));
                    nowCoderInfoDTO.setStartTime(timeStamp2Date(String.valueOf(tempJsonObject.get("startTime"))));
                    nowCoderInfoDTO.setEndTime(timeStamp2Date(String.valueOf(tempJsonObject.get("endTime"))));
                    nowCoderInfoDTO.setContestDuration(String.valueOf(tempJsonObject.getLong("contestDuration")/3600000));
                    // 添加到list
                    list.add(nowCoderInfoDTO);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return list;
    }

    @Override
    public List<ExamScoreInfoDTO> getExamDetailList(String memberId, String memberName) {

        List<ExamScoreInfoDTO> list = new LinkedList<>();
        if(memberId.substring(0,2).matches("17|18|19|20")){
            String fileName = "exam-score/" + getExamScoreFileName(memberId);
            InputStream file = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            ExamScoreListener listener = new ExamScoreListener(list, memberId);
            EasyExcel.read(file,ExamScoreInfoDTO.class,listener).sheet(0).doRead();
            list = listener.getList();
        }


        return list;
    }

    @Override
    public List<NowCoderRecord> getNowCoderDetailByMemberId(String memberId) {
        QueryWrapper<NowCoderRecord> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(NowCoderRecord::getMemberId, memberId);
        wrapper.lambda().orderByDesc(NowCoderRecord::getStartTime);
        return nowCoderRecordService.list(wrapper);
    }

    @Override
    public boolean updateAllNowCoderRecord() throws IOException {
        List<Member> memberList = this.list();

        for(Member item : memberList){
            if("无".equals(item.getNowCoderId())){
                continue;
            }
            String memberId = item.getMemberId();
            String nowCoderId = item.getNowCoderId();
            List<NowCoderRecord> newNowCoderRecordList = new ArrayList<>();

            List<NowCoderInfoDTO> nowCoderInfoList = crawNowCoderInfo(nowCoderId);
            packageNowCoderRecord(item, newNowCoderRecordList, nowCoderInfoList);

            QueryWrapper<NowCoderRecord> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(NowCoderRecord::getMemberId,memberId);
            wrapper.lambda().orderByDesc(NowCoderRecord::getStartTime);
            List<NowCoderRecord> oldNowCoderRecordList = nowCoderRecordService.list(wrapper);

            if(oldNowCoderRecordList.size() == 0){
                nowCoderRecordService.saveBatch(newNowCoderRecordList);
            }else {
                String latestStartTime = oldNowCoderRecordList.get(0).getStartTime();
                newNowCoderRecordList.removeIf(temp -> temp.getStartTime().compareTo(latestStartTime) <= 0);
                nowCoderRecordService.saveBatch(newNowCoderRecordList);
            }
        }
        return true;
    }

    @Override
    public MemberAnalysisPage pagingMemberAnalysisInfo(PagingReqDTO<Member> reqDTO) {
        //创建分页对象
        Page page = new Page(reqDTO.getCurrent(), reqDTO.getSize());
        IPage<Member> memberPage = baseMapper.paging(page);
        return packageMemberAnalysisPage(memberPage);
    }

    @Override
    public MemberAnalysisPage queryMemberAnalysisByMemberId(PagingReqDTO<MemberSearchRequestDTO> reqDTO) {

        //创建分页对象
        Page page = new Page(reqDTO.getCurrent(), reqDTO.getSize());
        String memberId = reqDTO.getParams().getMemberId();
        String isRetire = reqDTO.getParams().getIsRetire();
        IPage<Member> memberPage;
        if(!("".equals(memberId)) && !("".equals(isRetire))){
            memberPage = baseMapper.queryMemberByMemberIdAndStae(page,memberId,Integer.valueOf(isRetire));

        }else if("".equals(memberId)){
            memberPage = baseMapper.queryMemberByState(page,Integer.valueOf(isRetire));
        }else{
            memberPage = baseMapper.queryMemberByMemberId(page,memberId);
        }



        return packageMemberAnalysisPage(memberPage);
    }

    @Override
    public MemberAnalysisDetailInfos getMemberAnalysisInfos(MemberRequestDTO reqDTO) {
        List<ExamScoreInfoDTO> examDetailList = getExamDetailList(reqDTO.getMemberId(), reqDTO.getMemberName());
        // 三个数组分别记录通识类学科、基础学科、专业学科的分数分布情况
        // 0: 0-60 | 1: 60-70 | 2: 70-80 | 3: 80-90 | 4: 90-100
        int[] generalCourse = new int [5];
        int[] basicCourse = new int [5];
        int[] professionalCourse = new int [5];
        String generalEducationPlatform = "通识教育平台课";
        String basicPlatform = "学科基础平台课";
        String professional = "专业";

        computeExamScoreDistribution(generalCourse, basicCourse, professionalCourse, generalEducationPlatform, basicPlatform, professional, examDetailList);
        List<Integer> generalCourseScores = new LinkedList<>();
        List<Integer> basicCourseScores = new LinkedList<>();
        List<Integer> professionalCourseScores = new LinkedList<>();
        for(int num : generalCourse){
            generalCourseScores.add(num);
        }
        for(int num : basicCourse){
            basicCourseScores.add(num);
        }
        for(int num : professionalCourse){
            professionalCourseScores.add(num);
        }
        return new MemberAnalysisDetailInfos(generalCourseScores,basicCourseScores,professionalCourseScores);
    }

    @Override
    public List<MemberDistributionDTO> getAllMemeberDistribution() {
        List<Member> memberList = list();
        List<MemberDistributionDTO> result = new LinkedList<>();
        Map<String, Integer> gradeMap = new HashMap<>();
        for (Member member : memberList) {
            String grade = member.getMemberId().substring(0, 2);
            if(gradeMap.containsKey(grade)){
                Integer count = gradeMap.get(grade);
                count++;
                gradeMap.replace(grade,count);
            }else {
                gradeMap.put(grade,1);
            }
        }
        gradeMap.forEach((key,value) -> {
            MemberDistributionDTO dto = new MemberDistributionDTO(key,value);
            result.add(dto);
        });
        return result;
    }

    @Override
    public AllMemberExamInfoDTO getMemeberAllExamDetailForAnalysis() {

        // 三个数组分别记录通识类学科、基础学科、专业学科的分数分布情况
        // 0: 0-60 | 1: 60-70 | 2: 70-80 | 3: 80-90 | 4: 90-100
        int[] generalCourse = new int [5];
        int[] basicCourse = new int [5];
        int[] professionalCourse = new int [5];
        String generalEducationPlatform = "通识教育平台课";
        String basicPlatform = "学科基础平台课";
        String professional = "专业";

        List<Member> memberList = list();

        for (Member member : memberList) {
            List<ExamScoreInfoDTO> examDetailList = getExamDetailList(member.getMemberId(), member.getMemberName());
            computeExamScoreDistribution(generalCourse, basicCourse, professionalCourse, generalEducationPlatform, basicPlatform, professional, examDetailList);
        }
        List<Integer> generalCourseScores = new LinkedList<>();
        List<Integer> basicCourseScores = new LinkedList<>();
        List<Integer> professionalCourseScores = new LinkedList<>();
        int max = -1;
        for(int num : generalCourse){
            if(num > max){
                max = num;
            }
            generalCourseScores.add(num);
        }
        for(int num : basicCourse){
            if(num > max){
                max = num;
            }
            basicCourseScores.add(num);
        }
        for(int num : professionalCourse){
            if(num > max){
                max = num;
            }
            professionalCourseScores.add(num);
        }
        int length = (max + "").length();
        int pow = (int)Math.pow(10, length - 1);
        return new AllMemberExamInfoDTO(generalCourseScores,basicCourseScores,professionalCourseScores,(max / pow + 1) * pow);
    }

    private void computeExamScoreDistribution(int[] generalCourse, int[] basicCourse, int[] professionalCourse, String generalEducationPlatform, String basicPlatform, String professional, List<ExamScoreInfoDTO> examDetailList) {
        for (ExamScoreInfoDTO examScoreInfoDTO : examDetailList) {
            String courseType = examScoreInfoDTO.getCourseType();
            if(courseType.equals(generalEducationPlatform)){
                int scope = getCourseScoreScope(examScoreInfoDTO.getScore());
                generalCourse[scope]++;
            }
            if(courseType.equals(basicPlatform)){
                int scope = getCourseScoreScope(examScoreInfoDTO.getScore());
                basicCourse[scope]++;
            }
            if(courseType.contains(professional)){
                int scope = getCourseScoreScope(examScoreInfoDTO.getScore());
                professionalCourse[scope]++;
            }
        }
    }


    private MemberAnalysisPage packageMemberAnalysisPage(IPage<Member> memberIPage){

        List<MemberAnalysisBasicInfos> memberAnalysisBasicInfosList = new ArrayList<>();
        MemberAnalysisPage resPage = new MemberAnalysisPage();
        resPage.setCurrent(memberIPage.getCurrent());
        resPage.setSize(memberIPage.getSize());
        resPage.setTotal(memberIPage.getTotal());
        List<Member> memberList = memberIPage.getRecords();
        int nationalPrizeNum = 0;
        int provincialPrizeNum = 0;

        for (Member member : memberList) {
            MemberAnalysisBasicInfos memberAnalysisBasicInfos = new MemberAnalysisBasicInfos();
            memberAnalysisBasicInfos.setMemberId(member.getMemberId());
            memberAnalysisBasicInfos.setMemberName(member.getMemberName());
            memberAnalysisBasicInfos.setNowCoderId(member.getNowCoderId());
            memberAnalysisBasicInfos.setGender(member.getGender());
            memberAnalysisBasicInfos.setIsRetire(member.getIsRetire());

            QueryWrapper<AwardRecordMember> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(AwardRecordMember::getMemberId,member.getMemberId());
            List<AwardRecordMember> awardRecordMemberList = awardRecordMemberService.list(wrapper);

            for (AwardRecordMember awardRecordMember : awardRecordMemberList) {
                QueryWrapper<AwardRecord> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(AwardRecord::getAwardId,awardRecordMember.getAwardId());
                AwardRecord one = awardRecordService.getOne(queryWrapper);
                String awardLevel = one.getAwardLevel();
                if(awardLevel.contains("国家")){
                    nationalPrizeNum++;
                }else if(awardLevel.contains("省级")){
                    provincialPrizeNum++;
                }
            }
            memberAnalysisBasicInfos.setNationalPrizeNum(nationalPrizeNum);
            memberAnalysisBasicInfos.setProvincialPrizeNum(provincialPrizeNum);
            nationalPrizeNum = 0;
            provincialPrizeNum = 0;
            memberAnalysisBasicInfosList.add(memberAnalysisBasicInfos);
        }
        memberAnalysisBasicInfosList.sort(new MemberAnalysisComparator());
        resPage.setRecords(memberAnalysisBasicInfosList);
        return resPage;
    }

    private int getCourseScoreScope(double score){
        if(score < 60){
            return 0;
        }else if(score >= 60 && score < 70){
            return 1;
        }else if(score >= 70 && score < 80){
            return 2;
        }else if(score >= 80 && score < 90){
            return 3;
        }else {
            return 4;
        }
    }

    /**
     * 封装NowCoderRecord对象
     * @param item
     * @param newNowCoderRecordList
     * @param nowCoderInfoList
     */
    private void packageNowCoderRecord(Member item, List<NowCoderRecord> newNowCoderRecordList, List<NowCoderInfoDTO> nowCoderInfoList) {
        for(NowCoderInfoDTO temp : nowCoderInfoList) {
            NowCoderRecord nowCoderRecord = new NowCoderRecord();
            nowCoderRecord.setMemberId(item.getMemberId());
            nowCoderRecord.setContestId(temp.getContestId());
            nowCoderRecord.setContestName(temp.getContestName());
            nowCoderRecord.setLogoUrl(temp.getContestLogoUrl());
            nowCoderRecord.setTeamName(temp.getTeamName());
            nowCoderRecord.setProblemCount(temp.getProblemCount());
            nowCoderRecord.setAcceptCount(temp.getAcceptedCount());
            nowCoderRecord.setRank(temp.getRank());
            nowCoderRecord.setUserCount(temp.getUserCount());
            nowCoderRecord.setContestDuration(temp.getContestDuration());
            nowCoderRecord.setStartTime(temp.getStartTime());
            nowCoderRecord.setEndTime(temp.getEndTime());
            newNowCoderRecordList.add(nowCoderRecord);
        }
    }

    /**
     * 根据牛客id获取总页数
     * @param nowCoderId
     * @return
     * @throws IOException
     */
    private static int getPageCount(String nowCoderId) throws IOException {
        String url = "https://ac.nowcoder.com/acm-heavy/acm/contest/profile/contest-joined-history?token=" +
                "&uid=" + nowCoderId + "&page=1" +
                "&onlyJoinedFilter=true&searchContestName=&onlyRatingFilter=false&contestEndFilter=true";
        Document document = Jsoup.connect(url).timeout(10000).ignoreContentType(true).get();
        JSONObject jsonObject = new JSONObject(document.body().text());
        return jsonObject.getJSONObject("data")
                .getJSONObject("pageInfo").getInt("pageCount");
    }

    /**
     * 时间格式化
     * @param time
     * @return
     */
    private static String timeStamp2Date(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date;
        if (13 == time.length()) {
            date = sdf.format(new Date(Long.parseLong(time)));
        } else {
            date = sdf.format(new Date(Integer.parseInt(time) * 1000L));
        }
        return date;
    }

    /**
     * 获取对应年级的成绩xlsx文件
     * @param memberId
     * @return
     */
    private static String getExamScoreFileName(String memberId){
        StringBuilder fileName = new StringBuilder();

        if(memberId.matches("^16[0-9]{6}$")){
            fileName.append(2016);
        }else if(memberId.matches("^17[0-9]{6}$")){
            fileName.append(2017);
        }else if(memberId.matches("^18[0-9]{6}$")){
            fileName.append(2018);
        }else if(memberId.matches("^19[0-9]{6}$")){
            fileName.append(2019);
        }else if(memberId.matches("^20[0-9]{6}$")){
            fileName.append(2020);
        }
        fileName.append("级成绩列表");
        fileName.append(".xlsx");
        return fileName.toString();
    }

    public List<ExamScoreInfoDTO> getAllMemebrExamDetailList(List<Member> memberList) {

        List<ExamScoreInfoDTO> list = new LinkedList<>();
        for (Member member : memberList) {
            if(member.getMemberId().substring(0,2).matches("17|18|19|20")){
                String memberId = member.getMemberId();
                String fileName = "exam-score/" + getExamScoreFileName(memberId);
                InputStream file = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
                ExamScoreListener listener = new ExamScoreListener(list, memberId);
                EasyExcel.read(file,ExamScoreInfoDTO.class,listener).sheet(0).doRead();
                list.addAll(listener.getList());
            }
        }
        return list;
    }

}
