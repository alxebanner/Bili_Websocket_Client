/**
  * Copyright 2023 bejson.com 
  */
package com.uid939948.DO.danmu.like;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fans_medal {
    /**
     * 勋章所属房间
     */
    private long anchor_roomid;

    /**
     * 级别
     * 0 无
     * 1 总督
     * 2 提督
     * 3 舰长
     */
    private int guard_level;

    private int icon_id;

    /**
     * 是否点亮
     * 0 无勋章 或未冷
     * 1 点亮
     */
    private int is_lighted;

    /**
     * 勋章颜色
     */
    private long medal_color;

    private long medal_color_border;

    private long medal_color_end;

    private long medal_color_start;

    /**
     * 勋章级别
     */
    private int medal_level;

    /**
     * 勋章名称
     */
    private String medal_name;

    /**
     * 勋章分数（ 包含氪金
     */
    private long score;

    private String special;
    /**
     * 勋章所属主播UID
     */
    private long target_id;

}