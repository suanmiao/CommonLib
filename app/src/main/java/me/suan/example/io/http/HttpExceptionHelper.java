package me.suan.example.io.http;

import android.content.Context;
import android.util.Log;

import com.octo.android.robospice.exception.NetworkException;
import com.octo.android.robospice.exception.NoNetworkException;
import com.octo.android.robospice.persistence.exception.SpiceException;
import me.suan.example.component.SApplication;

import java.net.SocketTimeoutException;

import retrofit.RetrofitError;

/**
 * Created by suanmiao on 14/12/24.
 */
public class HttpExceptionHelper {
    private HttpExceptionHelper() {
    }

    public static final int STATUS_INTERNAL_ERROR = 500;
    public static final int STATUS_NOT_FOUND = 403;

    public static String getExceptionDescription(SpiceException exception) {
        Context context = SApplication.getAppContext();
        if (exception instanceof NoNetworkException) {
            return "no network";
        } else if (exception instanceof NetworkException) {
            if (exception.getCause() instanceof RetrofitError) {
                RetrofitError retrofitError = (RetrofitError) exception.getCause();
                if (retrofitError.getResponse() != null) {
                    int status = retrofitError.getResponse().getStatus();
                    if (status == STATUS_INTERNAL_ERROR) {
                        return "server error";
                    } else {
                        return "server error";
                    }
                } else if(retrofitError.getCause() instanceof SocketTimeoutException){
                    Log.e("SUAN", "exception timeout " + retrofitError.getCause());
                    return "timeout";
                }else{
                    return "network exception";
                }
            } else {
                return "network exception";
            }
        } else {
            return "local exception";
        }
    }
}
