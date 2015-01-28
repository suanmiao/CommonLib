package me.suanmiao.common.component;

import me.suanmiao.common.util.DateUtil;
import me.suanmiao.common.util.FileUtils;
import me.suanmiao.common.util.helper.FileHelper;

/**
 * Created by suanmiao on 15/1/2.
 */
public class SExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static final String EXCEPTION_FILE_NAME = "crash.log";
    private Thread.UncaughtExceptionHandler exceptionHandler;

    public SExceptionHandler(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        this.exceptionHandler = uncaughtExceptionHandler;
    }

    public SExceptionHandler() {

    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        String content = getExceptionContent(ex);
        writeToFile(content);
        if (exceptionHandler != null) {
            exceptionHandler.uncaughtException(thread, ex);
        }
    }

    public static void writeToFile(String content) {
        String path = FileHelper.getAppRootDirectory() + EXCEPTION_FILE_NAME;
        FileUtils.saveTextFile(path, content);
    }

    private String getExceptionContent(Throwable ex) {
        String time = "time : " + DateUtil.getDate(System.currentTimeMillis(), DateUtil.FORMATE_MM_DD_HH_MM_SS);
        String cause = "cause : " + (ex.getCause() == null ? "" : ex.getCause().toString());
        String message = "message : " + ex.getMessage();

        String err = "";
        ex.printStackTrace();
        StackTraceElement[] stack = ex.getStackTrace();
        if (stack != null) {
            for (int i = 0; i < stack.length; i++) {
                err += ("\tat ");
                err += (stack[i].toString());
                err += ("\n");
            }

        }

        ex.printStackTrace();
        return time + "\n" + cause + "\n" + message + "\n"+err;
    }
}
