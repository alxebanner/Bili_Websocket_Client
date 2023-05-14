package com.uid939948.DO.danmu.like;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 点赞 类推送
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeInfo {
    private int show_area;
    /**
     * 互动级别
     * 1 进入
     * 2 关注
     * 3 分享
     * 4
     * 6 点赞
     */
    private int msg_type;
    private String like_icon;
    /**
     * 用户UID
     */
    private long uid;
    /**
     * 文本
     * 为主播点赞了
     */
    private String like_text;
    /**
     * 用户名称
     */
    private String uname;
    private String uname_color;
    private Integer[] identities;

    /**
     * 勋章
     */
    private Fans_medal fans_medal;
    private Contribution_info contribution_info;
    private int dmscore;
}
