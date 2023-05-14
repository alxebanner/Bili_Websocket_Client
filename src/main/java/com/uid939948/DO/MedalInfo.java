package com.uid939948.DO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedalInfo implements Serializable{
	

	//勋章头图标id 如果没有就0 如果有就指定id
	private Long icon_id;
	//不知道什么id
	private Long target_id;
	//特殊？？？不知道什么
	private String special;
	//勋章所属主播名字
	private String anchor_uname;
	//勋章所属主播房间
	private String anchor_roomid;
	//勋章等级
	/**
	 * 勋章等级
	 */
	private Short medal_level;
	/**
	 * 勋章名字
	 */
	private String medal_name;
	/**
	 * 勋章颜色
	 */
	private String medal_color;

	/**
	 * 勋章点亮情况
	 * 0 为无勋章 或 未点亮
	 * 1 为点亮
	 */
	private Integer is_lighted = 0;

	/**
	 * 舰队等级
	 * 0 无
	 * 1 总督
	 * 2 提督
	 * 3 舰长
	 */
	private Short guard_level = 0;

}
