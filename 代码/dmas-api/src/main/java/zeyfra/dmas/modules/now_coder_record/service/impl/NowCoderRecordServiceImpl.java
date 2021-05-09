package zeyfra.dmas.modules.now_coder_record.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import zeyfra.dmas.core.api.PagingReqDTO;
import zeyfra.dmas.modules.now_coder_record.dto.request.QueryParamsDTO;
import zeyfra.dmas.modules.now_coder_record.entity.NowCoderRecord;
import zeyfra.dmas.modules.now_coder_record.mapper.NowCoderRecordMapper;
import zeyfra.dmas.modules.now_coder_record.service.NowCoderRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ZeyFra
 * @since 2021-04-14
 */
@Service
public class NowCoderRecordServiceImpl extends ServiceImpl<NowCoderRecordMapper, NowCoderRecord> implements NowCoderRecordService {

    @Override
    public IPage<NowCoderRecord> paging(PagingReqDTO<QueryParamsDTO> reqDTO) {
        //创建分页对象
        Page page = new Page(reqDTO.getCurrent(), reqDTO.getSize());

        //转换结果
        return baseMapper.paging(page,reqDTO.getParams().getMemberId());
    }

    @Override
    public List<List<String>> getMemeberAllNowCoderRecordForAnalysis(QueryParamsDTO paramsDTO) {
        List<List<String>> resultList = new ArrayList<>();

        QueryWrapper<NowCoderRecord> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(NowCoderRecord::getMemberId,paramsDTO.getMemberId());
        wrapper.lambda().orderByAsc(NowCoderRecord::getStartTime);
        List<NowCoderRecord> nowCoderRecordList = list(wrapper);


        DecimalFormat df = new DecimalFormat("0.00");
        for (NowCoderRecord nowCoderRecord : nowCoderRecordList) {
            List<String> item = new LinkedList<>();
            String time = nowCoderRecord.getStartTime();
            double rank = nowCoderRecord.getRank();
            double userCount = nowCoderRecord.getUserCount();
            String rankPercent ;
            if(rank != 0){
                rankPercent = df.format((rank / userCount)*100.0);
            }else {
                rankPercent = "0.00";
            }
            item.add(time);
            item.add(rankPercent);
            System.out.println(item.toString());
            resultList.add(item);
        }
        System.out.println(resultList.toString());
        return resultList;
    }
}
