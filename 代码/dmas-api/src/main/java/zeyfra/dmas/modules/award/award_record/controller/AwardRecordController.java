package zeyfra.dmas.modules.award.award_record.controller;


import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import zeyfra.dmas.core.api.ApiRest;
import zeyfra.dmas.core.controller.BaseController;
import zeyfra.dmas.modules.award.award_record.dto.response.AwardDistributionDTO;
import zeyfra.dmas.modules.award.award_record.dto.response.AwardDistributionYearDTO;
import zeyfra.dmas.modules.award.award_record.dto.response.AwardRecordAndMember;
import zeyfra.dmas.modules.award.award_record.entity.AwardRecord;
import zeyfra.dmas.modules.award.award_record.service.AwardRecordService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ZeyFra
 * @since 2021-04-20
 */
@RestController
@RequestMapping("/dmas/api/award")
public class AwardRecordController extends BaseController {

    AwardRecordService awardRecordService;

    @Autowired
    public void setAwardRecordService(AwardRecordService awardRecordService){
        this.awardRecordService = awardRecordService;
    }


    /**
     * 添加荣誉
     * @return ApiRest<IPage<Contest>>
     */
    @ApiOperation(value = "以Excel文件格式添加荣誉奖项")
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public ApiRest uploadContest(MultipartFile file) throws IOException {
        Boolean uploadResult = awardRecordService.uploadAwardRecord(file);
        return uploadResult ? super.success() : super.failure("保存失败！");

    }

    /**
     * 获取荣誉奖项List
     * @return ApiRest<IPage<Contest>>
     */
    @ApiOperation(value = "获取荣誉奖项List")
    @RequestMapping(value = "/getAwardList",method = RequestMethod.GET)
    public ApiRest getAwardList(){
        List<AwardRecordAndMember> awardRecordAndMemberList = awardRecordService.getAwardList();
        return success(awardRecordAndMemberList);

    }

    /**
     * 根据学号查询获奖记录List
     * @return ApiRest<List<AwardRecord>>
     */
    @ApiOperation(value = "根据学号查询获奖记录List")
    @RequestMapping(value = "/getAwardRecordByMemberId",method = RequestMethod.GET)
    public ApiRest<List<AwardRecord>> getAwardRecordByMemberId(HttpServletRequest request){
        List<AwardRecord> awardRecordList = awardRecordService.getAwardRecordByMemberId(request.getParameter("memberId"));
        return success(awardRecordList);

    }

    /**
     * 获取所获奖项分布情况
     * @return ApiRest<List<AwardRecord>>
     */
    @ApiOperation(value = "获取所获奖项分布情况")
    @RequestMapping(value = "/getAllRecordDistribution",method = RequestMethod.GET)
    public ApiRest<AwardDistributionDTO> getAllRecordDistribution(){
        AwardDistributionDTO resultData = awardRecordService.getAllRecordDistribution();
        return success(resultData);

    }

    /**
     * 获取每年所获奖项分布情况
     * @return ApiRest<List<AwardDistributionYearDTO>>
     */
    @ApiOperation(value = "获取每年所获奖项分布情况")
    @RequestMapping(value = "/getAllRecordYearDistribution",method = RequestMethod.GET)
    public ApiRest<List<AwardDistributionYearDTO>> getAllRecordYearDistribution(){
        List<AwardDistributionYearDTO> resultData = awardRecordService.getAllRecordYearDistribution();
        return success(resultData);

    }
}

