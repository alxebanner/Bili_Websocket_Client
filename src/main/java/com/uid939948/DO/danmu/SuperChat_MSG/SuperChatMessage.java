package com.uid939948.DO.danmu.SuperChat_MSG;

import com.uid939948.DO.Gift;
import com.uid939948.DO.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 醒目留言
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuperChatMessage {
    private String background_bottom_color;
    private String background_color;
    private String background_color_end;
    private String background_color_start;
    private String background_icon;
    private String background_image;
    private String background_price_color;
    private double color_point;
    private int dmscore;
    private long end_time;
    private Gift gift;
    private long id;
    private int is_ranked;
    private int is_send_audit;
    private Medal_info medal_info;
    private String message;
    private String message_font_color;
    private String message_trans;
    private int price;
    private int rate;
    private long start_time;
    private int time;
    private String token;
    private int trans_mark;
    private long ts;
    private long uid;
    private UserInfo user_info;
}
