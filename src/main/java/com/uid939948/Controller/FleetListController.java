package com.uid939948.Controller;


import com.uid939948.Conf.CenterSetConf;
import com.uid939948.Conf.MainConf;
import com.uid939948.DO.UserInfoData.UserInfo;
import com.uid939948.DO.danmu.Send_Gift.GiftConfigData;
import com.uid939948.Result.Result;
import com.uid939948.Result.ResultCode;
import com.uid939948.Service.ClientService;
import com.uid939948.Service.ConfigService;
import com.uid939948.Service.SetService;
import com.uid939948.Service.UserInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
//@RequestMapping("/fleet")
public class FleetListController {
    @Autowired
    @Lazy
    private ClientService clientService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private SetService setService;

    @ResponseBody
    @PostMapping(value = "/connectRoom")
    public Result saveConfigUid(String roomId) {
        Result result;

        if (StringUtils.isEmpty(roomId)) {
            result = new Result(ResultCode.PARAM_IS_BLANK, "error");
        } else {
            try {
                clientService.startConnService(Long.parseLong(roomId));
                configService.saveConfigRoomId(roomId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            result = new Result(ResultCode.SUCCESS, "ok");
        }
        return result;
    }

    @ResponseBody
    @GetMapping(value = "/getConfigUid")
    public Result getConfigUid() {
        return new Result(ResultCode.SUCCESS, configService.getConfig());
    }


    @ResponseBody
    @GetMapping(value = "/getSet")
    public Result get() {
        return new Result(ResultCode.SUCCESS, MainConf.centerSetConf);
    }

    @ResponseBody
    @GetMapping(value = "/getUserInfo")
    public Result getUserInfo(String roomId) {
        UserInfo userInfo = userInfoService.getUserInfo(roomId);
        return new Result(ResultCode.SUCCESS, userInfo);
    }

    @ResponseBody
    @GetMapping(value = "/getGiftData")
    public Result getGiftData(String uid) {
        List<GiftConfigData> list = userInfoService.getGiftData(Long.parseLong(uid));
        return new Result(ResultCode.SUCCESS, list);
    }

    @ResponseBody
    @PostMapping(value = "/saveSet")
    public Result saveSet(Boolean isAnchorLot, Boolean isSilverGift, Boolean isSystemEmoji, Boolean isUserEmoj,
                          int danmuHeight, int danmuFont, String danmuColor, int userNameFont, String userNameColor,
                          String face_backgroundUrl,String username_backgroundUrl,String commonDanmu_backgroundUrl,
                          String fleetDanmu_backgroundUrl,String pendant_backgroundUrl) {
        CenterSetConf centerSetConf = MainConf.centerSetConf;
        centerSetConf.setIsAnchorLot(isAnchorLot);
        centerSetConf.setIsSilverGift(isSilverGift);
        centerSetConf.setIsSystemEmoji(isSystemEmoji);
        centerSetConf.setIsUserEmoji(isUserEmoj);
        centerSetConf.setDanmuHeight(danmuHeight);
        centerSetConf.setDanmuFont(danmuFont);
        centerSetConf.setDanmuColor(danmuColor);
        centerSetConf.setUserNameFont(userNameFont);
        centerSetConf.setUserNameColor(userNameColor);

        centerSetConf.setFace_backgroundUrl(face_backgroundUrl);
        centerSetConf.setUsername_backgroundUrl(username_backgroundUrl);
        centerSetConf.setCommonDanmu_backgroundUrl(commonDanmu_backgroundUrl);
        centerSetConf.setFleetDanmu_backgroundUrl(fleetDanmu_backgroundUrl);
        centerSetConf.setPendant_backgroundUrl(pendant_backgroundUrl);

        setService.changeSet(centerSetConf);
        return new Result(ResultCode.SUCCESS, MainConf.centerSetConf);
    }

    @ResponseBody
    @GetMapping(value = "/startTestSet")
    public Result startTestSet() {
        clientService.starTestService();
        return new Result(ResultCode.SUCCESS, MainConf.centerSetConf);
    }

    @ResponseBody
    @GetMapping(value = "/closeTestSet")
    public Result closeTestSet() {
        clientService.closeTestSet();
        return new Result(ResultCode.SUCCESS, MainConf.centerSetConf);
    }


    @ResponseBody
    @PostMapping(value = "/sendSet")
    public Result get1() {
        return new Result(ResultCode.SUCCESS, MainConf.centerSetConf);
    }

}