package com.uid939948.DO.mySelf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoboterDanMu {
    private String danMu;

    /**
     * 弹幕等待时间 （秒）
     */
    private int waitingTime;

    /**
     * 弹幕集合
     */
    private List<String> list;

    /**
     * 是否随机播放弹幕
     */
    private Boolean isRandom;
}
