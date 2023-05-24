package com.uid939948.DO.FansMember;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 粉丝团勋章
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FansMemberInfo {
    private int user_rank;
    private long uid;
    private String name;
    private String face;
    private long score;
    private String medal_name;
    private int level;
    private long target_id;
    private String special;
    private int guard_level;
    private long medal_color_start;
    private long medal_color_end;
    private long medal_color_border;
    private String guard_icon;
    private String honor_icon;
}
