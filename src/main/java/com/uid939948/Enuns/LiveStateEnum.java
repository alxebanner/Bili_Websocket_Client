package com.uid939948.Enuns;

/**
 * 直播状态
 * 0为不直播  1为直播中 2为轮播
 */
public enum LiveStateEnum {
    CLOSE(0, "未开播"),
    OPEN(1, "直播中"),
    ROTATION(2, "轮播");

    private int id;
    private String state;

    LiveStateEnum(int id, String state) {
        this.id = id;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setState(String state) {
        this.state = state;
    }

    public static String getCountryValue(Integer id) {
        LiveStateEnum[] carTypeEnums = values();
        for (LiveStateEnum enumTest : carTypeEnums) {
            if (enumTest.id == id) {
                return enumTest.getState();
            }
        }
        return null;
    }

    public static Integer getId(String countryName) {
        LiveStateEnum[] carTypeEnums = values();
        for (LiveStateEnum enumTest : carTypeEnums) {
            if (enumTest.getState().equals(countryName)) {
                return enumTest.getId();
            }
        }
        return null;
    }
}
