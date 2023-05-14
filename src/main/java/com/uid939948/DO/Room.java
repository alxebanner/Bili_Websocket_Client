package com.uid939948.DO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    /**
     * 房间号
     * 这里只保存长号
     */
    private String roomId;
    //主播uid
    /**
     * uid号码
     */
    private String uid;
    /**
     * 主播公告
     */
    private String content;
    /**
     * 公告发布时间
     */
    private String time;
    /**
     * 状态
     */
    private String statue;
    /**
     * 名称
     */
    private String uname;
}
