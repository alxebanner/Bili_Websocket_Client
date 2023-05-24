package com.uid939948.DO.Face;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomInfo {

    private long room_id;
    private long uid;
    private int area_id;
    private int live_status;
    private String live_url;
    private int parent_area_id;
    private String title;
    private String parent_area_name;
    private String area_name;
    private String live_time;
    private String description;
    private String tags;
    private int attention;
    private int online;
    private int short_id;
    private String uname;
    private String cover;
    private String background;
    private int join_slide;
    private long live_id;
    private String live_id_str;
}
