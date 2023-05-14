package com.uid939948.DO.temp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 图片临时文件， 保存在本地
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacePicture {
    /**
     * 用户uid
     */
    private Long uid;
    /**
     * 头像地址
     */
    private String faceUrl;

    /**
     * 发言次数
     */
    private int count;

    /**
     * 最近更新的时间戳
     */
    private Long timestamp;


}

