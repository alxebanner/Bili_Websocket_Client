package com.uid939948.Service.impl;

import com.uid939948.Controller.DanmuWebsocket;
import com.uid939948.DO.danmu.DanMu_MSG.DanMu_MSG_Info;
import com.uid939948.DO.danmu.DanmuVO.DanmuDto;
import com.uid939948.DO.danmu.Send_Gift.SimpleGift;
import com.uid939948.DO.danmu.Toast_MSG.Toast;
import com.uid939948.Service.TestMessageService;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

@Service
@Slf4j
public class TestMessageServiceImpl implements TestMessageService {
    @Resource
    DanmuWebsocket danmuWebsocket;

    @Override
    public void danmuMessage(String name, String faceUrl, String message, int guard_level) {
        if (StringUtils.isEmpty(name)) {
            name = "测试用户";
        }

        if (StringUtils.isEmpty(faceUrl)) {
            faceUrl = "https://i0.hdslb.com/bfs/face/8aa83c6494e43641a8c16b86e2774f335bcad8d0.jpg";
        }

        if (StringUtils.isEmpty(message)) {
            message = "测试弹幕";
        }
        if (StringUtils.isEmpty(String.valueOf(guard_level))) {
            guard_level = 0;
        }

        DanMu_MSG_Info danMuMsgInfo = new DanMu_MSG_Info();
        danMuMsgInfo.setMessage(message);
        danMuMsgInfo.setUname(name);
        danMuMsgInfo.setIsEmoticon(false);
        danMuMsgInfo.setIsHaveSystemEmoji(false);
        danMuMsgInfo.setIsManager(false);
        danMuMsgInfo.setFaceUrl(faceUrl);
        danMuMsgInfo.setIsFansMedal(false);
        danMuMsgInfo.setUl_level(49);

        if (0 != guard_level) {
            // #00D1F1为舰长颜色
            danMuMsgInfo.setDanmuColor("#00D1F1");
        } else {
            danMuMsgInfo.setDanmuColor("noColor");
        }

        // log.info("测试文字弹幕 " + danMuMsgInfo.getUname() + " " + danMuMsgInfo.getDanmuColor());

        // 发送弹幕
        danmuWebsocket_sendMessage(danMuMsgInfo, "danmu");
    }

    @Override
    public void bulge_displayEmoticonMessage(String name, String faceUrl, String message, int guard_level, String EmoticonUrl) {


    }

    @Override
    public void EmoticonMessage(String name, String faceUrl, String message, int guard_level, String EmoticonUrl) {

    }

    @Override
    public void giftMessage(String name, String num, String message, int guard_level, String giftName) {

        if (StringUtils.isEmpty(name)) {
            name = "测试用户";
        }

        if (StringUtils.isEmpty(num)) {
            num = "1";
        }

        int numTemp;
        try {
            numTemp = Integer.valueOf(num);
        } catch (NumberFormatException e) {
            log.warn("输入数字有问，暂时为1");
            numTemp = 1;
        }

        if (StringUtils.isEmpty(message)) {
            message = "测试弹幕";
        }
        if (StringUtils.isEmpty(String.valueOf(guard_level))) {
            guard_level = 0;
        }

        if (StringUtils.isEmpty(giftName)) {
            giftName = "小电视飞船";
        }

        SimpleGift simpleGift = new SimpleGift();
        simpleGift.setUname(name);
        simpleGift.setNum(numTemp);
        simpleGift.setGiftName(giftName);

        danmuWebsocket_sendMessage(simpleGift, "gift");
        //{"cmd":"SEND_GIFT","data":{"action":"投喂","batch_combo_id":"1c8a7b39-af71-4a6b-9aa8-9b8947e5d609","batch_combo_send":{"action":"投喂","batch_combo_id":"1c8a7b39-af71-4a6b-9aa8-9b8947e5d609","batch_combo_num":1,"blind_gift":{"blind_gift_config_id":55,"from":0,"gift_action":"爆出","gift_tip_price":106000,"original_gift_id":32369,"original_gift_name":"至尊盲盒","original_gift_price":100000},"gift_id":32362,"gift_name":"马戏之王","gift_num":1,"send_master":null,"uid":552667853,"uname":"黑听の居居"},"beatId":"0","biz_source":"Live","blind_gift":{"blind_gift_config_id":55,"from":0,"gift_action":"爆出","gift_tip_price":106000,"original_gift_id":32369,"original_gift_name":"至尊盲盒","original_gift_price":100000},"broadcast_id":0,"coin_type":"gold","combo_resources_id":3,"combo_send":{"action":"投喂","combo_id":"7af3862d-8de0-4fcb-b8aa-f18a8cb3138f","combo_num":1,"gift_id":32362,"gift_name":"马戏之王","gift_num":1,"send_master":null,"uid":552667853,"uname":"黑听の居居"},"combo_stay_time":12,"combo_total_coin":106000,"crit_prob":0,"demarcation":3,"discount_price":106000,"dmscore":120,"draw":0,"effect":2,"effect_block":0,"face":"https://i2.hdslb.com/bfs/face/5c8420d2b23ee3223b99b17633cb2966f4f3d228.jpg","face_effect_id":0,"face_effect_type":0,"float_sc_resource_id":2,"giftId":32362,"giftName":"马戏之王","giftType":0,"gold":0,"guard_level":3,"is_first":true,"is_join_receiver":false,"is_naming":false,"is_special_batch":0,"magnification":1,"medal_info":{"anchor_roomid":0,"anchor_uname":"","guard_level":3,"icon_id":0,"is_lighted":1,"medal_color":398668,"medal_color_border":6809855,"medal_color_end":6850801,"medal_color_start":398668,"medal_level":25,"medal_name":"被融化","special":"","target_id":629601798},"name_color":"#00D1F1","num":1,"original_gift_name":"","price":106000,"rcost":4207567,"receive_user_info":{"uid":629601798,"uname":"野比大融official"},"remain":0,"rnd":"1681001819111300001","send_master":null,"silver":0,"super":0,"super_batch_gift_num":1,"super_gift_num":1,"svga_block":0,"switch":true,"tag_image":"","tid":"1681001819111300001","timestamp":1681001819,"top_list":null,"total_coin":100000,"uid":552667853,"uname":"黑听の居居"}}
    }

    @Override
    public void toastMessage(String name, String faceUrl, String message, int guard_level, String type) {
        if (StringUtils.isEmpty(name)) {
            name = "测试用户";
        }
        if (StringUtils.isEmpty(faceUrl)) {
            faceUrl = "https://i0.hdslb.com/bfs/face/8aa83c6494e43641a8c16b86e2774f335bcad8d0.jpg";
        }

        if (StringUtils.isEmpty(message)) {
            message = "测试弹幕";
        }
        if (StringUtils.isEmpty(String.valueOf(guard_level))) {
            guard_level = 0;
        }

        Toast toast = new Toast();
        toast.setNum(1);
        toast.setUid(939948);
        toast.setGuard_level(3);
        toast.setUsername(name);
        toast.setFaceUrl(faceUrl);
        danmuWebsocket_sendMessage(toast, "toast");
    }

    /**
     * 输出弹幕
     *
     * @param object 弹幕类型
     * @param type   类型
     */
    private void danmuWebsocket_sendMessage(Object object, String type) {
        try {
            danmuWebsocket.sendMessage(DanmuDto.toJson(type, object));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}