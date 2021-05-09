package zeyfra.dmas.modules.contest.contest.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zeyfra.dmas.modules.contest.contest_member.service.ContestMemberService;
import zeyfra.dmas.modules.contest.problem.entity.Problem;
import zeyfra.dmas.modules.contest.problem_member.entity.ProblemMember;
import zeyfra.dmas.modules.contest.problem_member.service.ProblemMemberService;
import zeyfra.dmas.modules.contest.contest.dto.request.ContestDataDTO;
import zeyfra.dmas.modules.contest.contest_member.entity.ContestMember;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZeyFra
 * @date 2021/4/11 20:18
 */
public class ContestDataListener extends AnalysisEventListener<ContestDataDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContestDataListener.class);

    private final ContestMemberService contestMemberService;
    private final ProblemMemberService problemMemberService;

    public ContestDataListener(ContestMemberService contestMemberService, ProblemMemberService problemMemberService, List<Problem> problemList, Integer contestId) {
        this.contestMemberService = contestMemberService;
        this.problemMemberService = problemMemberService;
        this.contestId = contestId;
        this.problemList = problemList;
    }
    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 5;
    private final List<ContestDataDTO> list = new ArrayList<ContestDataDTO>();
    private List<Problem> problemList = new ArrayList<>();
    private final List<ContestMember> contestUserList = new ArrayList<>();
    private final List<ProblemMember> problemUserList = new ArrayList<>();
    private final Integer contestId;

    @SneakyThrows
    @Override
    public void invoke(ContestDataDTO data, AnalysisContext context) {

        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        LOGGER.info("解析到一条数据:{}",data.toString());
        if(data.getMemberId() != null && data.getMemberName() != null){
            list.add(data);
        }
        if (list.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            list.clear();
        }

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        LOGGER.info("所有数据解析完成！");
    }


    private void saveData() {
        LOGGER.info("{}条数据，开始存储数据库！", list.size());
        // 1、保存比赛用户信息
        for (ContestDataDTO dataDTO : list) {
            ContestMember contestMember = new ContestMember();
            contestMember.setContestId(contestId);
            contestMember.setMemberId(dataDTO.getMemberId());
            contestMember.setRank(dataDTO.getRank());
            contestUserList.add(contestMember);
        }
        if(!contestUserList.isEmpty()){
            contestMemberService.saveBatch(contestUserList);
        }
        contestUserList.clear();

        // 2、保存题目用户信息
        for (ContestDataDTO contestDataDTO : list) {
            // 每一行比赛数据
            for (int j = 0; j < problemList.size(); j++) {
                //对每一个题目添加题目用户信息
                ProblemMember problemMember = new ProblemMember();
                problemMember.setMemberId(contestDataDTO.getMemberId());
                problemMember.setProblemId(problemList.get(j).getProblemId());
                switch (j) {
                    case 0:
                        problemMember.setResult(contestDataDTO.getA());
                        break;
                    case 1:
                        problemMember.setResult(contestDataDTO.getB());
                        break;
                    case 2:
                        problemMember.setResult(contestDataDTO.getC());
                        break;
                    case 3:
                        problemMember.setResult(contestDataDTO.getD());
                        break;
                    case 4:
                        problemMember.setResult(contestDataDTO.getE());
                        break;
                    case 5:
                        problemMember.setResult(contestDataDTO.getF());
                        break;
                    case 6:
                        problemMember.setResult(contestDataDTO.getG());
                        break;
                    case 7:
                        problemMember.setResult(contestDataDTO.getH());
                        break;
                    case 8:
                        problemMember.setResult(contestDataDTO.getI());
                        break;
                    case 9:
                        problemMember.setResult(contestDataDTO.getJ());
                        break;
                    case 10:
                        problemMember.setResult(contestDataDTO.getK());
                        break;
                    case 11:
                        problemMember.setResult(contestDataDTO.getL());
                        break;
                    case 12:
                        problemMember.setResult(contestDataDTO.getM());
                        break;
                    case 13:
                        problemMember.setResult(contestDataDTO.getN());
                        break;
                    default:
                        problemMember.setResult(contestDataDTO.getO());
                        break;
                }
                problemUserList.add(problemMember);
            }
            problemMemberService.saveBatch(problemUserList);
            problemUserList.clear();
        }
    }

}
