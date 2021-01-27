package com.jamestiotio.sentienterprize.gravatar;

public enum GravatarDefaultImage {
    GRAVATAR_ICON(""),

    IDENTICON("identicon"),

    MONSTERID("monsterid"),

    WAVATAR("wavatar"),

    HTTP_404("404"),
    
    MYSTERY_MAN("mm"),
    
    RETRO("retro"),
    
    BLANK("blank");

    private final String code;

    private GravatarDefaultImage(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}