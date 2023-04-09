package com.wzw.his.common.dto.sms;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@ToString
public class SmsSkdParam implements Serializable {

    @ApiModelProperty(value = "医生id")
    private Long staffId;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "起始时间")
    private Date startDate;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "截止时间")
    private Date endDate;
    @ApiModelProperty(value = "午别")
    private Integer noon;
    @ApiModelProperty(value = "科室id")
    private Long deptId;
    @ApiModelProperty(value = "挂号级别id")
    private Long registrationRankId;
}
