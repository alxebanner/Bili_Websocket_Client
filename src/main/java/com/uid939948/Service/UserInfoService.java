package com.uid939948.Service;

import com.uid939948.DO.UserInfoData.UserInfo;
import com.uid939948.DO.danmu.Send_Gift.GiftConfigData;

import java.util.List;

public interface UserInfoService {
	/**
	 * 获取用户新
	 *
	 * @param roomId 房间号
	 * @return 用户信息
	 */
	UserInfo getUserInfo(String roomId);

	/**
	 * 获取礼物立碑
	 *
	 * @param uid 用户有uid
	 * @return 礼物列表
	 */
	List<GiftConfigData> getGiftData(long uid);

	/**
	 * 获取系统表情 名称
	 *
	 * @return 系统表情
	 */
	List<String> getSystemEmojiList();
}
