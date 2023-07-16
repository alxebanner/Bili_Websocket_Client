package com.uid939948.DO.Login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 扫码登录二维码实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUrl {
    private String oauthKey;
    private String url;
}
