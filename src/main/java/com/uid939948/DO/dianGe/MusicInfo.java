package com.uid939948.DO.dianGe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MusicInfo {
    /**
     * 歌曲名字
     */
    private String musicName;

    /**
     * 点歌人姓名
     */
    private String musicId;

    /**
     * 音乐来源
     */
    private String musicSource;

    /**
     * 点歌人uid
     */
    private Long uid;

    /**
     * 点歌人名称
     */
    private String name;

    /**
     * 是否安全（非黑名单判断）
     */
    private Boolean isSafe;

    private int num;
}
