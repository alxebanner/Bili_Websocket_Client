package com.uid939948.DO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomInit {
	/**
	 * 房间长号
	 */
	private Long room_id;
	/**
	 * 房间短号
	 */
	private Long short_id;
	/**
	 * 用户uid
	 */
	private Long uid;
	private Short need_p2p;
	private Boolean is_hidden;
	private Boolean is_locked;
	private Boolean is_portrait;
	//0为不直播  1为直播中 2为轮播
	private int live_status;
	private int hidden_till;
	private int lock_till;
	private Boolean encrypted;
	private Boolean pwd_verified;
	private Long live_time;
	private int room_shield;
	private int is_sp;
	private int special_type;

//	{"code":0,"msg":"ok","message":"ok","data":{"room_id":1029,"short_id":139,"uid":43536,"need_p2p":0,"is_hidden":false,"is_locked":false,"is_portrait":false,"live_status":1,"hidden_till":0,"lock_till":0,"encrypted":false,"pwd_verified":false,"live_time":1679607787,"room_shield":1,"is_sp":0,"special_type":0}}
}
