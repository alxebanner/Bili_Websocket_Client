package com.uid939948.DO.danmu.DanmuVO;

import com.uid939948.Until.FastJsonUtils;
import lombok.Data;

import java.io.Serializable;

/**
 * 推送给前端的实体类
 */
@Data
public class DanmuDto implements Cloneable{
    private String type;
    private Object result;

    private static DanmuDto danmuDto = new DanmuDto();

    public static String toJson(String type,Object result) {
        try {
            DanmuDto ws = (DanmuDto) danmuDto.clone();
            ws.setType(type);
            ws.setResult(result);
            return FastJsonUtils.toJson(ws);
        } catch (Exception e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
        return "";
    }
}
