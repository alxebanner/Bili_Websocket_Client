package com.uid939948.DO.mySelf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Roboter {
    private Long uid;
    private String name;

//    private RoboterDanMu roboterDanMu;

    private List<RoboterDanMu> list;
}
