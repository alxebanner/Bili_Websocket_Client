package com.uid939948.DO;

import lombok.Data;
@Data
public class Gift implements Cloneable{
	private static Gift gift = new Gift();
	// 礼物id
	private Integer giftId;
	// 礼物类型(未知)
	private Short giftType;
	// 礼物名称
	private String giftName;
	// 赠送礼物数量
	private Integer num;
	// 赠送人
	private String uname;
	// 赠送人头像
	private String face;
	// 赠送人舰长 等级 0 1总督 2提督 3舰长
	private Short guard_level;
	// 赠送人uid
	private Long uid;
	// 赠送时间
	private Long timestamp;
	// 赠送类型
	private String action;
	// 单价
	private Integer price;
	// 瓜子类型 silver银:gold金//经工具过滤 0为银 1为金
	private Short coin_type;
	// 总价格
	private Long total_coin;

	private MedalInfo medal_info;

	public Gift() {
		super();
		// TODO 自动生成的构造函数存根
	}

	public static Gift getGift() {
		try {
			return (Gift) gift.clone();
		} catch (CloneNotSupportedException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return new Gift();
	}

	public static Gift getGift(Integer giftId, Short giftType, String giftName, Integer num, String uname, String face,
			Short guard_level, Long uid, Long timestamp, String action, Integer price, Short coin_type,
			Long total_coin,MedalInfo medal_info) {
		try {
			Gift g = (Gift) gift.clone();
			g.giftId = giftId;
			g.giftType = giftType;
			g.giftName = giftName;
			g.num = num;
			g.uname = uname;
			g.face = face;
			g.guard_level = guard_level;
			g.uid = uid;
			g.timestamp = timestamp;
			g.action = action;
			g.price = price;
			g.coin_type = coin_type;
			g.total_coin = total_coin;
			g.medal_info = medal_info;
			return g;
		} catch (CloneNotSupportedException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return new Gift();
	}


}
