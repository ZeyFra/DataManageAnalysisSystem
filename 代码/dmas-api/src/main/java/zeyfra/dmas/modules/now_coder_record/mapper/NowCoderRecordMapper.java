package zeyfra.dmas.modules.now_coder_record.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import zeyfra.dmas.modules.member.entity.Member;
import zeyfra.dmas.modules.now_coder_record.entity.NowCoderRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ZeyFra
 * @since 2021-04-14
 */
public interface NowCoderRecordMapper extends BaseMapper<NowCoderRecord> {

    /**
     * 分页
     * @param page
     * @return
     */
    IPage<NowCoderRecord> paging(Page page, String memberId);

}
