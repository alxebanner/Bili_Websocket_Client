package com.uid939948.DO.UserInfoData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    /**
     * 房间号
     */
    private Long roomId;

    /**
     * 用户uid
     */
    private Long uid;

    /**
     * 直播标题
     */
    private String title;

    /**
     * 直播封面地址
     */
    private String cover;

    /**
     * 开播状态
     */
    private String liveStatus;

//    /**
//     * 多少热人看过
//     */
//    private String text_large;


    private String face;

    private String name;


    /**
     * 个性签名
     */
    private String sign;
}


