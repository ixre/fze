package net.fze.common.http;

public enum ContentType {
    NONE(""),
    FORM("application/x-www-form-urlencoded"),
    FILES("multipart/form-data"),
    JSON("application/json");

    private final String _encType;

    private ContentType(String encType){
        this._encType = encType;
    }

    public String getEncodeType() {
        return this._encType;
    }
}
