package com.uid939948.DO;

import com.uid939948.Until.FastJsonUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FristSecurityData {
    //用户uid 默认无登陆状态为0 非必选
    private Long uid = 0L;
    //房间号 必选
    private Long roomid;
    //协议版本 目前为2
    private Integer protover = 2;
    //平台 可以为web
    private String platform = "web";
    //客户端版本 已知1.11.0 1.8.5 1.5.15 1.13.1 无用
    private String clientver = "1.14.0";
    //未知 可以是2
    private Integer type = 2;
    //用户标识  通过接口https://api.live.bilibili.com/room/v1/Danmu/getConf?room_id=房间号&platform=pc&player=web获取 在里面为token
    private String key;


    public FristSecurityData(Long roomid, String key) {
        super();
        this.roomid = roomid;
        this.key = key;
    }

    public FristSecurityData(Long uid, Long roomid, String key) {
        super();
        this.uid = uid;
        this.roomid = roomid;
        this.key = key;
    }

    public String toJson() {
        return FastJsonUtils.toJson(this);
    }


}
