package me.suanmiao.example.io.http;

/**
 * Created by suanmiao on 14/12/6.
 */
public class BaseFormResult {
    public static final int STATUS_NONE = -1;
    public static final int STATUS_OK = 0;
    public static final int STATUS_NEED_TOKEN = 1;
    public static final int STATUS_INVALID_TOKEN = 2;
    public static final int STATUS_PERMISSION_DENIED = 3;
    private int status = STATUS_NONE;
    private String description;

    public int getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public boolean isStatusOK() {
        return status == STATUS_OK;
    }

}
