package cn.wolfcode.p2p.base.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
/**
 * 视频预约的时间段
 */
public class OrderTime extends BaseDomain{
    //开始时间:9:00
    private String begin;
    //结束时间:9:30
    private String end;

}