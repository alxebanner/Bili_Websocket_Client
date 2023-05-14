package com.uid939948.Enuns;

/**
 * 直播状态
 * 0为不直播  1为直播中 2为轮播
 */
public enum InteractWordEnum {

    ENTER(1, "进入"),
    ATTENTION(2, "关注"),
    SHARE(3, "分享");

    private int id;
    private String name;

    InteractWordEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getCountryValue(Integer id) {
        InteractWordEnum[] carTypeEnums = values();
        for (InteractWordEnum enumTest : carTypeEnums) {
            if (enumTest.id == id) {
                return enumTest.getName();
            }
        }
        return null;
    }

    public static Integer getId(String countryName) {
        InteractWordEnum[] carTypeEnums = values();
        for (InteractWordEnum enumTest : carTypeEnums) {
            if (enumTest.getName().equals(countryName)) {
                return enumTest.getId();
            }
        }
        return null;
    }

}
