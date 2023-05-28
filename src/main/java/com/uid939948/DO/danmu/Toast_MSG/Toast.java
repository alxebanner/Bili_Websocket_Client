package com.uid939948.DO.danmu.Toast_MSG;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 上舰后消息推送实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Toast {
    private boolean anchor_show;
    private String color;
    private int dmscore;
    private int effect_id;
    private long end_time;
    private int face_effect_id;
    private int gift_id;
    private int guard_level;
    private int is_show;
    private int num;
    private int op_type;
    private String payflow_id;
    private long price;
    private String role_name;
    private int room_effect_id;
    private long start_time;
    private int svga_block;
    private int target_guard_count;
    private String toast_msg;
    private long uid;
    private String unit;
    private boolean user_show;
    private String username;
}
