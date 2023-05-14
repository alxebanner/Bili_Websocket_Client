package com.uid939948.DO.WebsocketConfig;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebsocketConfigInfo {
    private Long roomId;

    private int is_self_starting;
}
