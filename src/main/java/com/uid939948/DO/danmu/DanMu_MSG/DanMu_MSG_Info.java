package com.uid939948.DO.danmu.DanMu_MSG;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 文字表情弹幕
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DanMu_MSG_Info {
    /**
     * 用户uid
     */
    private Long uid;

    /**
     * 用户名称
     */
    private String uname;

    /**
     * 弹幕
     */
    private String message;

    /**
     * 是否是 表情
     */
    private Boolean isEmoticon;

    /**
     * 是否佩戴勋章
     */
    private Boolean isFansMedal;

    /**
     * 勋章级别
     */
    private int medal_level;

    /**
     * 勋章名称
     */
    private String medal_name;

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

    /**
     * ul等级
     */
    private long ul_level;

    /**
     * 表情图片地址
     */
    private String emoticon_url;

    /**
     * 是否房管
     */
    private Boolean isManager;

    /**
     * 弹幕颜色
     * 提督颜色 #E17AFF
     * 舰长颜色 #00D1F1
     * 总督颜色  没见过
     *  无舰长时为空
     */
    private String danmuColor;

    /**
     * 头像地址
     */
    private String faceUrl;

    /**
     * 是否 系统表情标识 包含秒啊 有点东西（给前端显示方便）
     * 为1 是用户表情  为0 是系统表情
     */
    private Boolean bulge_display;

    /**
     * 是否包含系统表情
     * 默认为false
     */
    private Boolean isHaveSystemEmoji = false;

    /**
     * 系统表情+ 自定义表情 列表
     */
    private List<String> emojiList;

    /**
     * 荣耀等级
     */
    private int honorLevel;
}
