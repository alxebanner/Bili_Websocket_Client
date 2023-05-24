package com.uid939948.DO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketAddress implements Serializable{
	private String group;

	private Short business_id;

	private Short refresh_row_factor;

	private Short refresh_rate;

	private Short max_delay;

	private String token;

	private List<HostServer> host_list;
}
