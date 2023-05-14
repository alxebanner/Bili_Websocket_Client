package com.uid939948.DO.danmu.ANCHOR_LOT_START;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Anchor_lot {
    private String asset_icon;
    private String asset_icon_webp;
    private String award_image;
    private String award_name;
    private int award_num;
    private String award_price_text;
    private int award_type;
    private int cur_gift_num;
    private long current_time;
    private String danmu;
    private List<Danmu_new> danmu_new;
    private int danmu_type;
    private int gift_id;
    private String gift_name;
    private int gift_num;
    private int gift_price;
    private int goaway_time;
    private int goods_id;
    private long id;
    private int is_broadcast;
    private int join_type;
    private int lot_status;
    private int max_time;
    private String require_text;
    private int require_type;
    private int require_value;
    private long room_id;
    private int send_gift_ensure;
    private int show_panel;
    private int start_dont_popup;
    private int status;
    private int time;
    private String url;
    private String web_url;
}
