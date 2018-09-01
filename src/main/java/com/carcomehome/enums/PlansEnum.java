package com.carcomehome.enums;

/**
 * Created by ComeHomeCar
 */
public enum PlansEnum {

    SERUSER(1, "SerUser"),
    SERPROVIDER(2, "SerProvider");


    private final int id;

    private final String planName;

    PlansEnum(int id, String planName) {
        this.id = id;
        this.planName = planName;
    }

    public int getId() {
        return id;
    }

    public String getPlanName() {
        return planName;
    }
}
