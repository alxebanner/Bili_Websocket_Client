package com.uid939948.Controller;

import com.uid939948.DO.Login.LoginUrl;
import com.uid939948.Http.HttpRoomUtil;
import com.uid939948.Result.Result;
import com.uid939948.Result.ResultCode;
import com.uid939948.config.DanmujiInitConfig;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 登录页面接口
 */
@Controller
@Lazy
public class WebController {

    @Resource
    private DanmujiInitConfig danmujiInitConfig;

    @ResponseBody
    @GetMapping(value = "/getQrcode")
    public Result getQrcode() {
        LoginUrl loginUrl = HttpRoomUtil.httpGetQrcode();
        return new Result(ResultCode.SUCCESS, loginUrl);
    }

    @ResponseBody
    @PostMapping(value = "/loginCheck")
    public Result loginCheck(String oauthKey) {
//        if (req.getSession().getAttribute("auth") != null)
//            return null;
//        JSONObject jsonObject = null;
//        String oauthKey = (String) req.getSession().getAttribute("auth");
//        LoginData loginData = new LoginData();
//        loginData.setOauthKey(oauthKey);
//        String jsonString = HttpUserData.httpPostCookie(loginData);
//        jsonObject = JSONObject.parseObject(jsonString);
//        if (jsonObject != null) {k
//            if (jsonObject.getBoolean("status")) {
//                danmujiInitConfig.init();
////                checkService.init();
//                if (PublicDataConf.USER != null) {
//                    req.getSession().setAttribute("status", "login");
//                }
//            }
//        }
//        return jsonObject;

        String str = "true";

        danmujiInitConfig.init();

        return new Result(ResultCode.SUCCESS, str);
    }

}
