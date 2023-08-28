package com.uid939948.DO.PinYin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 弹幕分成单个字体解析
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PinYin_barrage {
    /**
     * 弹幕
     */
    private String message;

    /**
     * 拼音
     */
    private String pinYin;
}
