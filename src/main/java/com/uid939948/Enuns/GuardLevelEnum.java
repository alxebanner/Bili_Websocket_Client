package com.uid939948.Enuns;

/**
 * 直播状态
 * 0为不直播  1为直播中 2为轮播
 */
public enum GuardLevelEnum {
    LEVEL_ZERO(0, ""),
    LEVEL_ONE(1, "总督"),
    LEVEL_TWO(2, "提督"),
    LEVEL_THREE(3, "舰长");

    private int id;
    private String name;

    GuardLevelEnum(int id, String name) {
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
        GuardLevelEnum[] carTypeEnums = values();
        for (GuardLevelEnum enumTest : carTypeEnums) {
            if (enumTest.id == id) {
                return enumTest.getName();
            }
        }
        return null;
    }

    public static Integer getId(String countryName) {
        GuardLevelEnum[] carTypeEnums = values();
        for (GuardLevelEnum enumTest : carTypeEnums) {
            if (enumTest.getName().equals(countryName)) {
                return enumTest.getId();
            }
        }
        return null;
    }

}
