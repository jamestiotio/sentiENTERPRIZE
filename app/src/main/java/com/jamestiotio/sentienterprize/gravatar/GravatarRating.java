package com.jamestiotio.sentienterprize.gravatar;

public enum GravatarRating {
    GENERAL_AUDIENCES("g"),

    PARENTAL_GUIDANCE_SUGGESTED("pg"),

    RESTRICTED("r"),

    EXPLICIT("x");

    private String code;

    private GravatarRating(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}