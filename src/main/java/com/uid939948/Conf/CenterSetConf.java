package com.uid939948.Conf;

import com.uid939948.Until.FastJsonUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户配置
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CenterSetConf implements Serializable {
    /**
     * 房间号
     */
    private Long roomid = 0l;

    /**
     * 是否开启系统表情
     * 单个表情 比如 [花]
     */
    private Boolean isSystemEmoji = true;

    /**
     * 是否显示用户自定义表情
     *  todo 待开发 用户自定义表情
     */
    private Boolean isUserEmoji = true;

    /**
     * 是否显示礼物
     * todo 待开发  前端 自定义表情，后端已完成
     */
    private Boolean isGift = true;

    /**
     * 是否显示银瓜子礼物
     */
    private Boolean isSilverGift = true;

    /**
     * 显示最低金瓜子金额
     */
    private String minGoldPrice = "0.1";

    /**
     * 是否屏蔽天选消息
     */
    private Boolean isAnchorLot = true;

    /**
     * 是否屏蔽红包消息
     */
    private Boolean isRedPocket = true;

    /**
     * 是否开启不掉线线程
     */
    private Boolean isOnLine = true;

    /**
     * 进房 消息推送
     */
    private Boolean isEnterMessage = false;

    /**
     * 关注 消息推送
     */
    private Boolean isAttentionMessage = false;
    /**
     * 分享 消息推送
     */
    private Boolean isShareMessage = false;

    /**
     * 手机双击点赞 消息推送
     * LIKE_INFO_V3_CLICK
     */
    private Boolean isLikeMessage = false;

    /**
     * 手机双击点赞数量 消息推送
     * LIKE_INFO_V3_CLICK
     */
    private Boolean isLikeNumMessage = false;

    /**
     * 弹幕高度
     * 默认为980px
     */
    private int danmuHeight = 980;

    /**
     * 弹幕 字体大小
     */
    private int danmuFont = 32;

    /**
     * 弹幕字体颜色
     */
    private String danmuColor = "#FF60AF";

    /**
     * 用户名 字体大小
     */
    private int userNameFont = 30;

    /**
     * 用户名 字体颜色
     */
    private String userNameColor = "#D2B48C";

    /**
     * 用户 头像弯曲弧度 0px到50px
     */
    private String user_border_radius = "50";

    /**
     * 用户 头像高度
     */
    private String user_height = "100";

    /**
     * 用户 头像宽度
     */
    private String user_width = "100";

    /**
     * 用户 头像高度
     */
    private String user_top = "20";

    /**
     * 用户 头像宽度
     */
    private String user_left = "0";


    // 普通用户头像
    /**
     * 普通用户头像框背景图片地址
     */
    private String face_backgroundUrl = "https://s1.ax1x.com/2023/05/14/p9cTod0.png";
    // 纪录默认位置   private String face_backgroundUrl = "../../danmuCss/top/舰长头像框.png";

    /**
     * 普通用户 头像框背景图片宽度
     */
    private String face_common_Width = "130";

    /**
     * 普通用户头像框背景图片高度
     */
    private String face_common_Height = "130";

    /**
     * 普通用户 竖直头像偏移量
     */
    private String face_common_top = "-106";

    /**
     * 普通用户 水平向左头像偏移量
     */
    private String face_common_left = "-15";

    // 舰队头像
    /**
     * 舰队头像框背景图片地址
     */
    private String face_Top_backgroundUrl = "https://s1.ax1x.com/2023/05/26/p9qEDud.png";

    /**
     * 舰队用户头像框背景图片宽度
     */
    private String face_top_Width = "150";

    /**
     * 舰队用户 头像框背景图片高度
     */
    private String face_top_Height = "129";

    /**
     * 舰队用户 竖直头像偏移量
     */
    private String face_top_top = "-96";

    /**
     * 舰队用户 水平向左头像偏移量
     */
    private String face_top_left = "-17";

    /**
     * 用户名背景图片地址
     */
    private String username_backgroundUrl = "https://s1.ax1x.com/2023/05/14/p9cHAhT.png";
//    private String username_backgroundUrl = "../../danmuCss/top/观众用户名称.png";

    /**
     * 普通观众 背景图片地址
     */
    private String commonDanmu_backgroundUrl = "https://s1.ax1x.com/2023/05/14/p9cHdHI.png";
//    private String commonDanmu_backgroundUrl = "../../danmuCss/fans/底板-粉丝.png";

    /**
     * 舰队人员 背景图片地址
     */
    private String fleetDanmu_backgroundUrl = "https://s1.ax1x.com/2023/05/14/p9cbJI0.png";
//    private String fleetDanmu_backgroundUrl = "../../danmuCss/top/底板-舰长用.png";

    /**
     * 弹幕右下角地址
     */
    private String pendant_backgroundUrl = "https://s1.ax1x.com/2023/05/14/p9cbNGT.png";
//    private String pendant_backgroundUrl = "../../danmuCss/top/雪花.png";

    public String toJson() {
        return FastJsonUtils.toJson(this);
    }
}