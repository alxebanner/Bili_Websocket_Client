package com.uid939948.DO.danmu.Red_Pocket;

import com.uid939948.DO.danmu.Send_Gift.pojo.Medal_info;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Red_Pocket_Info {
    private String uname;
    private int num;
    private int gift_id;
    private long start_time;
    private long uid;
    private long lot_id;
    private int price;
    private int wait_num;
    private Medal_info medal_info;
    private String action;
    private String gift_name;
    private String name_color;
    private long current_time;
}
