package zeyfra.dmas.modules.member.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author ZeyFra
 * @since 2021-04-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Member对象", description="用户表")
public class Member implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "队员id(学号)")
    @TableId(value = "member_id")
    private String memberId;

    @ApiModelProperty(value = "队员姓名")
    private String memberName;

    @ApiModelProperty(value = "性别")
    private Boolean gender;

    @ApiModelProperty(value = "电话号码")
    private String telephone;

    @ApiModelProperty(value = "QQ号")
    private String qq;

    @ApiModelProperty(value = "已退休")
    private Boolean isRetire;

    @ApiModelProperty(value = "牛客网id")
    private String nowCoderId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createdTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


}
