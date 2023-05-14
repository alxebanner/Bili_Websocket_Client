package com.uid939948.DO.danmu.ANCHOR_LOT_START;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Award_users {
    private long uid;
    private String uname;
    private String face;
    private int level;
    private long color;
    private long bag_id;
    private int gift_id;
    private int num;
}
