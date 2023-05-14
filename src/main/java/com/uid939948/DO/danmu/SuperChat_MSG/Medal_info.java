package com.uid939948.DO.danmu.SuperChat_MSG;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Medal_info {
    private long anchor_roomid;
    private String anchor_uname;
    private int guard_level;
    private int icon_id;
    private int is_lighted;
    private String medal_color;
    private long medal_color_border;
    private long medal_color_end;
    private long medal_color_start;
    private int medal_level;
    private String medal_name;
    private String special;
    private long target_id;
}
