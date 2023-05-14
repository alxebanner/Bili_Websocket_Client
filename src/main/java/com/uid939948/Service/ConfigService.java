package com.uid939948.Service;

import com.uid939948.DO.WebsocketConfig.WebsocketConfigInfo;

public interface ConfigService {
	Boolean saveConfigUid();

	/**
	 * 保存房间号
	 *
	 * @param roomId 房间号
	 * @return 是否保存成功
	 */
	Boolean saveConfigRoomId(String roomId);

	WebsocketConfigInfo getConfig();;

}
