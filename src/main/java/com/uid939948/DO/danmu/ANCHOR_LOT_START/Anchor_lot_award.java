package com.uid939948.DO.danmu.ANCHOR_LOT_START;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Anchor_lot_award {
    private int award_dont_popup;
    private String award_image;
    private String award_name;
    private int award_num;
    private String award_price_text;
    private int award_type;
    private List<Award_users> award_users;
    private long id;
    private int lot_status;
    private long ruid;
    private String url;
    private String web_url;
}
