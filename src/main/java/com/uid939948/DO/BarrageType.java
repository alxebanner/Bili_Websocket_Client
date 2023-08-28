package com.uid939948.DO;

import com.uid939948.Conf.MainConf;
import lombok.Data;

@Data
public class BarrageType {
    private Short mode;
    private Long color;
    private Short length = 20;
    private Long room_id = MainConf.ROOMID;
}
