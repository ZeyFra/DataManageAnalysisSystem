package zeyfra.dmas.modules.member.controller;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import zeyfra.dmas.core.api.ApiRest;
import zeyfra.dmas.core.api.PagingReqDTO;
import zeyfra.dmas.core.controller.BaseController;
import zeyfra.dmas.modules.member.dto.request.MemberRequestDTO;
import zeyfra.dmas.modules.member.dto.request.MemberSearchRequestDTO;
import zeyfra.dmas.modules.member.dto.request.MemberUpdateRequestDTO;
import zeyfra.dmas.modules.member.dto.response.*;
import zeyfra.dmas.modules.member.entity.Member;
import zeyfra.dmas.modules.member.service.MemberService;

import zeyfra.dmas.modules.now_coder_record.entity.NowCoderRecord;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author ZeyFra
 * @since 2021-04-11
 */
@RestController
@RequestMapping("/dmas/api/member")
public class MemberController extends BaseController {

    private MemberService memberService;

    @Autowired
    public void setUserService(MemberService memberService){
        this.memberService = memberService;
    }


    /**
     * 获取队员分页list
     * @return ApiRest<IPage<User>>
     */
    @ApiOperation(value = "获取队员分页list")
    @RequestMapping(value = "/pageMemberList", method = RequestMethod.GET)
    public ApiRest<IPage<Member>> pageList(PagingReqDTO<Member> reqDTO){
        //分页查询并转换
        IPage<Member> page = memberService.paging(reqDTO);
        return super.success(page);
    }

    /**
     * 根据条件查询队员分页list
     * @return ApiRest<IPage<User>>
     */
    @ApiOperation(value = "根据搜索条件查询队员")
    @RequestMapping(value = "/queryMemberByMemberId", method = RequestMethod.POST)
    public ApiRest<IPage<Member>> queryMemberByUserName(@RequestBody PagingReqDTO<MemberSearchRequestDTO> reqDTO){
        //分页查询并转换
        IPage<Member> page = memberService.queryMemberByMemberId(reqDTO);
        return super.success(page);
    }

    /**
     * 修改队员个人资料
     * @return ApiRest<IPage<User>>
     */
    @ApiOperation(value = "修改队员个人资料信息")
    @RequestMapping(value = "/updateMember", method = RequestMethod.PUT)
    public ApiRest<IPage<Member>> updateMember(@RequestBody MemberUpdateRequestDTO reqDTO){
        boolean save = memberService.updateMember(reqDTO);
        if(save) {
            return super.success();
        }else {
            return super.failure("保存失败！");
        }
    }

    /**
     * 上传队员数据
     * @return ApiRest<IPage<Contest>>
     */
    @ApiOperation(value = "上传队员Excel数据")
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public ApiRest uploadContest(MultipartFile file) {
        Boolean uploadResult = memberService.uploadMember(file);
        return uploadResult ? super.success() : super.failure("保存失败！");

    }

    /**
     * 下载比赛数据上传模板
     * @param response
     * @throws IOException
     */
    @ApiOperation(value = "下载比赛数据上传模板")
    @RequestMapping(value = "/getTemplate", method = RequestMethod.GET)
    public void getTemplate(HttpServletResponse response) throws IOException {

        ServletOutputStream out = null;
        try {
            InputStream fis = Thread.currentThread().getContextClassLoader().getResourceAsStream("templates/队员数据上传模板.xlsx");
            assert fis != null;
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            response.setContentType("application/force-download");
            String fileName = java.net.URLEncoder.encode("队员数据上传模板", "UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");
            out = response.getOutputStream();
            workbook.write(out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            assert out != null;
            out.close();
        }
    }

    /**
     * 根据学号和牛客id爬取学生信息
     * @param requestDTO
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "根据学号获取学生学科信息")
    @RequestMapping(value = "/getExamDetailList", method = RequestMethod.POST)
    public ApiRest crawNewCoderInfo(@RequestBody MemberRequestDTO requestDTO) {
        List<ExamScoreInfoDTO> examScoreInfoDTOList = memberService.getExamDetailList(requestDTO.getMemberId(), requestDTO.getMemberName());
        return success(examScoreInfoDTOList);
    }



    @ApiOperation(value = "导出学生牛客训练赛成绩和学科成绩为Excel")
    @RequestMapping(value = "/exportToExcecl", method = RequestMethod.POST)
    public void exportToExcecl(@RequestBody MemberRequestDTO requestDTO, HttpServletResponse response) throws IOException {
        ServletOutputStream os = null;
        os = response.getOutputStream();
        List<ExamScoreInfoDTO> examDetailList = memberService.getExamDetailList(requestDTO.getMemberId(), requestDTO.getMemberName());
        List<NowCoderRecord> nowCoderRecordList = memberService.getNowCoderDetailByMemberId(requestDTO.getMemberId());
        ExcelWriter writer = EasyExcel.write(os).build();
        WriteSheet practiceScoreSheet = EasyExcel.writerSheet(0, "牛客训练赛成绩").head(NowCoderRecord.class).build();
        writer.write(nowCoderRecordList,practiceScoreSheet);
        WriteSheet examScoreSheet = EasyExcel.writerSheet(1, "学科考试成绩").head(ExamScoreInfoDTO.class).build();
        writer.write(examDetailList,examScoreSheet);
        writer.finish();
        os.close();
    }

    /**
     * 更新所有成员牛客网训练赛情况
     * @return ApiRest
     * @throws IOException
     */
    @ApiOperation(value = "更新所有成员牛客网训练赛情况")
    @RequestMapping(value = "/updateAllNowCoderRecord", method = RequestMethod.PUT)
    public ApiRest updateAllNowCoderRecord() throws IOException {
        long startTime = System.currentTimeMillis();
        boolean result = memberService.updateAllNowCoderRecord();
        long endTime = System.currentTimeMillis();
        return result ? success((endTime-startTime)/1000): failure("更新失败！请检查网络连接是否通畅！");
    }

    /**
     * 获取队员分析信息
     * @return ApiRest
     * @throws IOException
     */
    @ApiOperation(value = "获取队员分析信息")
    @RequestMapping(value = "/pagingMemberAnalysisInfo", method = RequestMethod.GET)
    public ApiRest<MemberAnalysisPage> pagingMemberAnalysisInfo(PagingReqDTO<Member> reqDTO) throws IOException {
        MemberAnalysisPage response = memberService.pagingMemberAnalysisInfo(reqDTO);
        return success(response);
    }

    /**
     * 根据搜索条件队员分析基本信息
     * @return ApiRest<IPage<User>>
     */
    @ApiOperation(value = "根据搜索条件队员分析基本信息")
    @RequestMapping(value = "/queryMemberAnalysisByMemberId", method = RequestMethod.POST)
    public ApiRest<MemberAnalysisPage> queryMemberAnalysisByMemberId(@RequestBody PagingReqDTO<MemberSearchRequestDTO> reqDTO){
        //分页查询并转换
        MemberAnalysisPage response = memberService.queryMemberAnalysisByMemberId(reqDTO);
        return super.success(response);
    }


    /**
     * 获取可视化分析部分所需信息（三类学科成绩、获奖情况）
     * @return ApiRest<IPage<User>>
     */
    @ApiOperation(value = "获取可视化分析所需信息（三类学科成绩分布情况、获奖情况）")
    @RequestMapping(value = "/getMemberAnalysisInfos", method = RequestMethod.GET)
    public ApiRest<MemberAnalysisDetailInfos> getMemberAnalysisInfos(MemberRequestDTO reqDTO){
        //分页查询并转换
        MemberAnalysisDetailInfos response = memberService.getMemberAnalysisInfos(reqDTO);
        return super.success(response);
    }

    /**
     * 获取成员分布数据
     * @return ApiRest<IPage<User>>
     */
    @ApiOperation(value = "获取成员分布数据")
    @RequestMapping(value = "/getAllMemeberDistribution", method = RequestMethod.GET)
    public ApiRest<List<MemberDistributionDTO>> getAllMemeberDistribution(){
        //分页查询并转换
        List<MemberDistributionDTO> responseData = memberService.getAllMemeberDistribution();
        return super.success(responseData);
    }

    /**
     * 获取成员分布数据
     * @return ApiRest<IPage<User>>
     */
    @ApiOperation(value = "获取所有队员三类学科成绩分布情况")
    @RequestMapping(value = "/getMemeberAllExamDetailForAnalysis", method = RequestMethod.GET)
    public AllMemberExamInfoDTO getMemeberAllExamDetailForAnalysis(){
        //分页查询并转换
        AllMemberExamInfoDTO memeberAllExamDetailForAnalysis = memberService.getMemeberAllExamDetailForAnalysis();
        return memeberAllExamDetailForAnalysis;
    }
}

