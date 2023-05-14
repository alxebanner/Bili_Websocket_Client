package com.uid939948.DO.danmu.GuardBuy_MSG;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuardBuy {
    private long uid;

    private String username;

    private int guard_level;

    private int num;

    private int price;

    private int gift_id;

    private String gift_name;

    private String start_time;

    private String end_time;

//    "uid":175635059,
//            "username":"水禾禾禾",
//            "guard_level":3,
//            "num":1,
//            "price":198000,
//            "gift_id":10003,
//            "gift_name":"舰长",
//            "start_time":1682179214,
//            "end_time":1682179214
}
