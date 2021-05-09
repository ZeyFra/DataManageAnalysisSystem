package zeyfra.dmas.modules.contest.contest.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModelProperty;
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
import zeyfra.dmas.modules.contest.contest.dto.request.*;
import zeyfra.dmas.modules.contest.contest.dto.response.ContestMemberDetail;
import zeyfra.dmas.modules.contest.contest.entity.Contest;
import zeyfra.dmas.modules.contest.contest.dto.response.ContestInfoDTO;
import zeyfra.dmas.modules.contest.contest.dto.response.ContestMemberColumnDTO;
import zeyfra.dmas.modules.contest.contest.service.ContestService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 比赛表 前端控制器
 * </p>
 *
 * @author ZeyFra
 * @since 2021-04-11
 */
@RestController
@RequestMapping("/dmas/api/contest")
public class ContestController extends BaseController {

    private ContestService contestService;

    @Autowired
    public void setContestService(ContestService contestService){
        this.contestService = contestService;
    }

    /**
     * 上传比赛数据
     * @return ApiRest<IPage<Contest>>
     */
    @ApiOperation(value = "Excel格式上传比赛数据")
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public ApiRest uploadContest(ContestUploadInfoRequestDTO reqDTO, MultipartFile file) throws IOException {
        Boolean uploadResult = contestService.uploadContest(reqDTO, file);
        return uploadResult ? super.success() : super.failure("保存失败！");

    }

    /**
     * 获取比赛分页list
     * @return ApiRest<IPage<Contest>>
     */
    @ApiOperation(value = "获取比赛列表的分页list")
    @RequestMapping(value = "/pageContestList", method = RequestMethod.GET)
    public ApiRest<IPage<Contest>> pageContestList(PagingReqDTO<Contest> reqDTO){
        //分页查询并转换
        IPage<Contest> page = contestService.paging(reqDTO);
        return super.success(page);
    }

    /**
     * 获取比赛信息
     */
    @ApiOperation(value = "获取比赛得信息")
    @RequestMapping(value = "/getContestInfo", method = RequestMethod.GET)
    public ApiRest<ContestInfoDTO> getContestInfo(ContestInfoRequestDTO requestDTO){
        ContestInfoDTO contestInfo = contestService.getContestInfo(requestDTO.getContestId());
        return super.success(contestInfo);
    }

    /**
     * 获取比赛详情页面表格得动态column
     */
    @ApiOperation(value = "获取比赛详情页面表格得动态column")
    @RequestMapping(value = "/getContestMemberColumnList", method = RequestMethod.GET)
    public ApiRest<List<ContestMemberColumnDTO>> getContestMemberColumnList(ContestInfoRequestDTO requestDTO){
        List<ContestMemberColumnDTO> list = contestService.getContestMemberColumnLsit(requestDTO.getContestId());
        return super.success(list);
    }

    /**
     * 获取比赛详情页面表格得数据List
     */
    @ApiOperation(value = "获取比赛详情页面表格得数据List")
    @RequestMapping(value = "/getContestMemberDetailList", method = RequestMethod.GET)
    public ApiRest getContestMemberDetailList(ContestInfoRequestDTO requestDTO){
        List<ContestMemberDetail> contestMemberDetailList = contestService.getContestMemberDetailList(requestDTO.getContestId());
        return  super.success(contestMemberDetailList);
    }

    /**
     * 根据参数查询比赛日期
     */
    @ApiOperation(value = "根据条件搜索比赛")
    @RequestMapping(value = "/queryContest", method = RequestMethod.POST)
    public ApiRest<IPage<Contest>> queryContest(@RequestBody PagingReqDTO<ContestQueryParamsDTO> reqDTO){
        IPage<Contest> page = contestService.queryContest(reqDTO);
        return  super.success(page);
    }

    /**
     * 根据参数查询比赛日期
     */
    @ApiOperation(value = "修改比赛信息")
    @RequestMapping(value = "/updateContest", method = RequestMethod.POST)
    public ApiRest<IPage<Contest>> updateContest(@RequestBody ContestUpdateParamsDTO reqDTO){
        Boolean save = contestService.updateContest(reqDTO);
        if(save) {
            return super.success();
        }else {
            return super.failure("保存失败！");
        }
    }

    /**
     * 根据比赛id进行级联删除
     */
    @ApiOperation(value = "根据比赛id进行级联删除")
    @RequestMapping(value = "/deleteContest", method = RequestMethod.DELETE)
    public ApiRest<IPage<Contest>> deleteContest(@RequestBody ContestDeleteParamsDTO reqDTO){
        Boolean delete = contestService.deleteContest(reqDTO);
        if(delete) {
            return super.success();
        }else {
            return super.failure("删除失败！");
        }
    }

    /**
     * 根据比赛id进行级联删除
     */
    @ApiOperation(value = "下载比赛数据上传模板")
    @RequestMapping(value = "/getTemplate", method = RequestMethod.GET)
    public void getTemplate(HttpServletResponse response) throws IOException {

        ServletOutputStream out = null;
        try {
            InputStream fis = Thread.currentThread().getContextClassLoader().getResourceAsStream("templates/比赛数据上传模板.xlsx");
            assert fis != null;
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            response.setContentType("application/force-download");
            String fileName = java.net.URLEncoder.encode("比赛数据上传模板", "UTF-8");
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

}

