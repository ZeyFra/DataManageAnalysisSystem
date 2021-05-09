package zeyfra.dmas.modules.contest.contest.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import zeyfra.dmas.core.api.PagingReqDTO;
import zeyfra.dmas.modules.contest.contest.dto.request.*;
import zeyfra.dmas.modules.contest.contest.dto.response.*;
import zeyfra.dmas.modules.contest.contest.listener.ContestProblemInfoListener;
import zeyfra.dmas.modules.contest.contest_problem.entity.ContestProblem;
import zeyfra.dmas.modules.contest.contest_problem.service.ContestProblemService;
import zeyfra.dmas.modules.contest.contest_problem.service.impl.ContestProblemServiceImpl;
import zeyfra.dmas.modules.contest.problem.entity.Problem;
import zeyfra.dmas.modules.contest.problem_member.entity.ProblemMember;
import zeyfra.dmas.modules.contest.problem_member.service.impl.ProblemMemberServiceImpl;
import zeyfra.dmas.modules.contest.contest.entity.Contest;
import zeyfra.dmas.modules.contest.contest.mapper.ContestMapper;
import zeyfra.dmas.modules.contest.contest.service.ContestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import zeyfra.dmas.modules.member.entity.Member;
import zeyfra.dmas.modules.contest.problem.service.ProblemService;
import zeyfra.dmas.modules.contest.problem.service.impl.ProblemServiceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 比赛表 服务实现类
 * </p>
 *
 * @author ZeyFra
 * @since 2021-04-11
 */
@Service
public class ContestServiceImpl extends ServiceImpl<ContestMapper, Contest> implements ContestService {

    private ContestProblemService contestProblemService;

    private ProblemService problemService;

    @Autowired
    public void setContestProblemService(ContestProblemService contestProblemService){
        this.contestProblemService = contestProblemService;
    }

    @Autowired
    public void setProblemService(ProblemService problemService){
        this.problemService = problemService;
    }


    @Override
    public IPage<Contest> paging(PagingReqDTO<Contest> reqDTO) {
        //创建分页对象
        Page page = new Page(reqDTO.getCurrent(), reqDTO.getSize());

        //转换结果
        return baseMapper.paging(page);
    }

    /**
     * 获取比赛信息
     */
    @Override
    public ContestInfoDTO getContestInfo(String contestId) {
        List<String> problemNameList = new ArrayList<>();
        QueryWrapper<Contest> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Contest::getContestId , Integer.valueOf(contestId));
        //根据比赛id获取该场比赛信息
        Contest contest = this.getOne(wrapper,false);
        //获取该场比赛得题目List
        List<Problem> problemList = baseMapper.getProblemList(contestId);
        //获取我院参赛人数
        Integer myTeamNum = baseMapper.getMyTeamNum(contestId);

        ContestInfoDTO contestInfoDTO = new ContestInfoDTO();

        //封装该场比赛相关题目信息
        List<ContestProblemInfo> problemInfoList = new ArrayList<>();
        int maxSubmitNum = 0;
        for(Problem problem : problemList){
            ContestProblemInfo contestProblemInfo = new ContestProblemInfo();
            contestProblemInfo.setProblemName(problem.getProblemName());
            contestProblemInfo.setProblemPassNum(problem.getProblemPassNum());
            contestProblemInfo.setProblemSubmitNum(problem.getProblemSubmitNum());
            if (problem.getProblemSubmitNum() > maxSubmitNum){
                maxSubmitNum = problem.getProblemSubmitNum();
            }
            problemNameList.add(problem.getProblemName());
            problemInfoList.add(contestProblemInfo);
        }
        contestInfoDTO.setProblemInfoList(problemInfoList);

        //封装比赛基本信息
        ContestInfo contestInfo = new ContestInfo();
        contestInfo.setContestName(contest.getContestName());
        contestInfo.setContestAkNum(contest.getContestAkNum());
        contestInfo.setContestName(contest.getContestName());
        contestInfo.setContestSite(contest.getContestSite());
        contestInfo.setContestTeamNum(contest.getContestTeamNum());
        contestInfo.setContestId(contest.getContestId());
        contestInfo.setContestTime(contest.getContestTime());
        contestInfo.setMaxSubmitNum(maxSubmitNum);
        contestInfo.setMyTeamNum(myTeamNum);

        contestInfoDTO.setContestInfo(contestInfo);

        //封装题目名称集合
        contestInfoDTO.setProblemNameList(problemNameList);

        List<Integer> problemPassInfo = problemService.problemPassInfo(contestId);

        contestInfoDTO.setProblemPassInfo(problemPassInfo);

        return contestInfoDTO;
    }

    @Override
    public List<ContestMemberColumnDTO> getContestMemberColumnLsit(String contestId) {

        List<ContestMemberColumnDTO> list = new ArrayList<>();

        //1、学号、姓名、rank、[题目名]
        ContestMemberColumnDTO userId = new ContestMemberColumnDTO();
        userId.setProperty("memberId");
        userId.setPropertyName("学号");
        list.add(userId);

        ContestMemberColumnDTO realName = new ContestMemberColumnDTO();
        realName.setProperty("memberName");
        realName.setPropertyName("姓名");
        list.add(realName);

        ContestMemberColumnDTO rank = new ContestMemberColumnDTO();
        rank.setProperty("rank");
        rank.setPropertyName("积分/排名");
        list.add(rank);

        //获取本场比赛得题目名
        List<String> problemNameList = baseMapper.getContestProblemName(contestId);

        for(String problemName : problemNameList){
            ContestMemberColumnDTO memberColumn = new ContestMemberColumnDTO();
            memberColumn.setPropertyName(problemName);
            memberColumn.setProperty(problemName.toLowerCase());
            list.add(memberColumn);
        }

        return list;
    }

    @Override
    public List<ContestMemberDetail> getContestMemberDetailList(String contestId) {

        List<ContestMemberDetail> list = new ArrayList<>();

        //本场比赛得所有题目id
        List<Problem> problemList = baseMapper.getProblemList(contestId);

        List<Member> contestMemberList = baseMapper.getContestMemberList(contestId);


        //本场比赛得用户：学号、姓名

        for(Member member : contestMemberList){
            //封装
            ContestMemberDetail contestMemberDetail = new ContestMemberDetail();
            contestMemberDetail.setMemberId(member.getMemberId());
            contestMemberDetail.setMemberName(member.getMemberName());
            contestMemberDetail.setRank(baseMapper.getRank(contestId,member.getMemberId()));
            for(Problem problem : problemList){
                //根据题目id和学号找出对应的用户的答题情况
                ProblemMember problemMember = baseMapper.getProblemMember(problem.getProblemId(), member.getMemberId());
                String problemName = problem.getProblemName();
                switch (problemName){
                    case "A" :
                        if(problemMember.getResult() == null){
                            contestMemberDetail.setA("");
                        }else {
                            contestMemberDetail.setA(problemMember.getResult());
                        }
                        break;
                    case "B" :
                        if(problemMember.getResult() == null){
                            contestMemberDetail.setB("");
                        }else {
                            contestMemberDetail.setB(problemMember.getResult());
                        }
                        break;
                    case "C" :
                        if(problemMember.getResult() == null){
                            contestMemberDetail.setC("");
                        }else {
                            contestMemberDetail.setC(problemMember.getResult());
                        }
                        break;
                    case "D" :
                        if(problemMember.getResult() == null){
                            contestMemberDetail.setD("");
                        }else {
                            contestMemberDetail.setD(problemMember.getResult());
                        }
                        break;
                    case "E" :
                        if(problemMember.getResult() == null){
                            contestMemberDetail.setE("");
                        }else {
                            contestMemberDetail.setE(problemMember.getResult());
                        }
                        break;
                    case "F" :
                        if(problemMember.getResult() == null){
                            contestMemberDetail.setF("");
                        }else {
                            contestMemberDetail.setF(problemMember.getResult());
                        }
                        break;
                    case "G" :
                        if(problemMember.getResult() == null){
                            contestMemberDetail.setG("");
                        }else {
                            contestMemberDetail.setG(problemMember.getResult());
                        }
                        break;
                    case "H" :
                        if(problemMember.getResult() == null){
                            contestMemberDetail.setH("");
                        }else {
                            contestMemberDetail.setH(problemMember.getResult());
                        }
                        break;
                    case "I" :
                        if(problemMember.getResult() == null){
                            contestMemberDetail.setI("");
                        }else {
                            contestMemberDetail.setI(problemMember.getResult());
                        }
                        break;
                    case "J" :
                        if(problemMember.getResult() == null){
                            contestMemberDetail.setJ("");
                        }else {
                            contestMemberDetail.setJ(problemMember.getResult());
                        }
                        break;
                    case "K" :
                        if(problemMember.getResult() == null){
                            contestMemberDetail.setK("");
                        }else {
                            contestMemberDetail.setK(problemMember.getResult());
                        }
                        break;
                    case "L" :
                        if(problemMember.getResult() == null){
                            contestMemberDetail.setL("");
                        }else {
                            contestMemberDetail.setL(problemMember.getResult());
                        }
                        break;
                    case "M" :
                        if(problemMember.getResult() == null){
                            contestMemberDetail.setM("");
                        }else {
                            contestMemberDetail.setM(problemMember.getResult());
                        }
                        break;
                    case "N" :
                        if(problemMember.getResult() == null){
                            contestMemberDetail.setN("");
                        }else {
                            contestMemberDetail.setN(problemMember.getResult());
                        }
                        break;
                    case "O" :
                        if(problemMember.getResult() == null){
                            contestMemberDetail.setO("");
                        }else {
                            contestMemberDetail.setO(problemMember.getResult());
                        }
                        break;
                    default:
                        break;
                }
            }
            list.add(contestMemberDetail);
        }

        return list;
    }

    @Override
    public IPage<Contest> queryContest(PagingReqDTO<ContestQueryParamsDTO> reqDTO) {

        //分页查询并转换
        //创建分页对象
        Page page = new Page(reqDTO.getCurrent(), reqDTO.getSize());
        String contestName = reqDTO.getParams().getContestName();
        String contestTime = reqDTO.getParams().getContestTime();
        if(!("".equals(contestName)) && !("".equals(contestTime)) && contestTime != null){
            return baseMapper.queryContest(page,contestName,contestTime);
        }else if(contestTime == null || "".equals(contestTime)){
            return baseMapper.queryContestByContestName(page,contestName);
        }else{
            return baseMapper.queryContestByContestTime(page,contestTime);
        }
    }

    @Override
    public Boolean updateContest(ContestUpdateParamsDTO reqDTO) {

        Contest contest = this.getById(reqDTO.getContestId());
        contest.setContestTime(reqDTO.getContestTime());
        contest.setContestTeamNum(reqDTO.getContestTeamNum());
        contest.setContestName(reqDTO.getContestName());
        contest.setContestSite(reqDTO.getContestSite());
        contest.setContestAkNum(reqDTO.getContestAkNum());
        return this.updateById(contest);

    }

    @Override
    public Boolean deleteContest(ContestDeleteParamsDTO paramsDTO) {

        String contestId = paramsDTO.getContestId();
        // 获取该场比赛的题目id
        QueryWrapper<ContestProblem> cpWrapper = new QueryWrapper<>();
        cpWrapper.lambda().eq(ContestProblem::getContestId, contestId);
        List<ContestProblem> contestProblemList = contestProblemService.list(cpWrapper);
        for(ContestProblem contestProblem : contestProblemList){
            problemService.removeById(contestProblem.getProblemId());
        }
        // 删除比赛
        return this.removeById(contestId);

    }

    @Override
    public Boolean uploadContest(ContestUploadInfoRequestDTO reqDTO, MultipartFile file) {
        // 1、保存比赛信息
        Contest contest = new Contest();
        contest.setContestAkNum(reqDTO.getContestAkNum());
        contest.setContestTime(reqDTO.getContestTime());
        contest.setContestSite(reqDTO.getContestSite());
        contest.setContestName(reqDTO.getContestName());
        contest.setContestTeamNum(reqDTO.getContestTeamNum());
        this.save(contest);
        // 2、保存题目信息、题目和比赛关系，入比赛id，
        // 先保存题目，获取每个题目id
        // 根据比赛id和题目id保存题目和比赛的关系、
        // 内部调用EasyExcel保存问题和队员的关系
        try {
            EasyExcel.read(file.getInputStream(), ContestProblemInfoRequestDTO.class,
                    new ContestProblemInfoListener(new ProblemServiceImpl(),new ProblemMemberServiceImpl(),
                            new ContestProblemServiceImpl(),file.getInputStream(),
                            contest.getContestId())).sheet(1).doRead();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
