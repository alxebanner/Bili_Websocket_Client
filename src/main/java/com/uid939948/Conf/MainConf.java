package com.uid939948.Conf;

import com.uid939948.DO.UserBarrageType;
import com.uid939948.DO.UserCookie;
import com.uid939948.DO.UserInfoData.UserInfo;
import com.uid939948.DO.danmu.ANCHOR_LOT_START.Anchor_lot;
import com.uid939948.DO.danmu.Send_Gift.GiftConfigData;
import com.uid939948.DO.dianGe.MusicInfo;
import com.uid939948.DO.temp.FacePicture;
import com.uid939948.WebSocketClient.WebSocketProxy;
import com.uid939948.thread.core.HeartByteThread;
import com.uid939948.thread.core.HeartOnlineThread;
import com.uid939948.thread.core.ParseMessageThread;
import com.uid939948.thread.core.TestThread;

import java.util.*;

public class MainConf {
    //websocket客户端主线程
    public static WebSocketProxy webSocketProxy;

    //心跳线程
    public static HeartByteThread heartByteThread;

    // 处理信息分类线程
    public static ParseMessageThread parseMessageThread;

    /**
     * 前端显示心跳
     */
    public static HeartOnlineThread heartOnlineThread;

    // 心跳包 16进制
    public final static String heartByte = "0000001f0010000100000002000000015b6f626a656374204f626a6563745d";
    // 包头长
    public final static char packageHeadLength = 16;
    // 验证包协议类型
    public final static int firstPackageType = 7;
    // 心跳包协议类型
    public final static int heartPackageType = 2;
    //心跳包&验证包协议版本
    public final static char packageVersion = 1;
    // 心跳包&验证包的尾巴其他
    public final static int packageOther = 1;

    /**
     * 房间人气
     */
    public static Long ROOM_POPULARITY = 1L;

    /**
     * 弹幕库地址，
     * 此处随便初始化一个
     */
    public static String URL = "wss://broadcastlv.chat.bilibili.com:2245/sub";

    // 主播uid
    public static Long UID = null;

    // 处理弹幕包集合
    public final static Vector<String> resultStrs = new Vector<String>(100);

    /**
     * 直播状态 0不直播 1直播 2轮播
     */
    public static int lIVE_STATUS = 0;
//    public static String USERCOOKIE = "DedeUserID__ckMd5=9518835468e33460;SESSDATA=63651ffc%2C1704103286%2C8c171*71;bili_jct=726a82f532d3551d4dee029d88a48e68;sid=nm8lednh;DedeUserID=939948";

    public static Long USERCOOKIE_UID = 939948L;

    public static String USERCOOKIE = "DedeUserID=939948;DedeUserID__ckMd5=9518835468e33460;Expires=1708693173;SESSDATA=4ace4b62,1708693173,2a913*81fkN7bL3uYg0xsLIkMy2WkS1KPH5Pqh3WHu1KJ4wV_bqLaPcimgAUMMXZBOU-nnNYly_kXgAAMAA;bili_jct=2babeac9be9e71ec562c6674fc7aee88;" +
            "=https%3A%2F%2Fwww.bilibili.com";


    /**
     * 真实房间号  长号
     */
    public static Long ROOMID = null;

    /**
     * 真实房间号  短号
     */
    public static Long SHORTROOMID = null;

    /**
     * 用戶设置
     */
    public static CenterSetConf centerSetConf;

    public static Long ROOMID_SAFE = null;

    /**
     * 用户Cookie 暂时废弃
     */
    public static UserCookie COOKIE = null;

    /**
     * 房间观看人数
     */
    public static Long ROOM_WATCHER = 0L;

    /**
     * 总点赞人数
     * click_count
     */
    public static Long ROOM_CLICK = 0L;

    /**
     * 礼物列表
     */
    public static List<GiftConfigData> giftList;

    /**
     * 礼物列表
     */
    public static Map<Integer, GiftConfigData> giftMap;

    /**
     * 表情图片地址列表
     */
    public static List<FacePicture> facePictureList = new ArrayList<>();

    /**
     * 无表情的用户uid
     * 用于线程定期获取
     */
    public static List<FacePicture> noFaceUidList = new ArrayList<>();

    /**
     * 最近一次查询用户头像时间
     */
    public static long lastTimeFace = 0;

    /**
     * 用户信息
     */
    public static UserInfo userInfo;

    /**
     * 当前天选
     */
    public static Anchor_lot anchorLot;

    /**
     * 天选过期时间
     */
    public static long anchorLot_endTime;

    /**
     * 测试模式线程
     */
    public static TestThread testThread;

    /**
     * 音乐列表
     */
    public static List<MusicInfo> musicInfoList;

    /**
     * 加班时间
     */
    public static int overtime = 12;

    /**
     * 截止剩余时间
     */
    public static Date endDate;

    /**
     * 发送弹幕类型
     */
    public static UserBarrageType userBarrageType;

    /**
     * 发送弹幕最大长度
     */
    public static int BarrageMaxLength;

    /**
     * 发送弹幕颜色
     */
    public static Long BarrageColor;

    /**
     * 保存的用户id
     */
    public static List<String> userList;

    /**
     * 首页最后刷新时间
     */
    public static long lastRoomInitTime = 0L;
}
