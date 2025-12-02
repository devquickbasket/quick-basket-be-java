package com.quickbasket.quickbasket.user.enums;

import lombok.Data;

public enum UserStatus {
    ACTIVE(200,"Active"),
    EMAIL_NOT_VERIFED(100,"Email not verified"),
    SUSPENDED(300,"Suspended"),
    NOT_ACTIVE(400,"Not Active");

    private final int code;
    private final String label;

    UserStatus(int code, String label) {
        this.code = code;
        this.label = label;
    }

    public int getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    public static UserStatus fromCode(int code) {
        for (UserStatus status : UserStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status code: " + code);
    }

}
