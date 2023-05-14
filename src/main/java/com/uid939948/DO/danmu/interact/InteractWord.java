/**
  * Copyright 2023 bejson.com 
  */
package com.uid939948.DO.danmu.interact;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InteractWord {
    private Contribution contribution;
    private int core_user_type;
    private int dmscore;
    private Fans_medal fans_medal;
    private List<Integer> identities;
    private int is_spread;
    private int msg_type;
    private int privilege_type;
    private long roomid;
    private long score;
    private String spread_desc;
    private String spread_info;
    private int tail_icon;
    private long timestamp;
    private long trigger_time;
    private long uid;
    private String uname;
    private String uname_color;
}