package com.rainbow.superschedule.core.net.callback;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * SuperSchedule
 * Created By Rainbow on 2020/5/5.
 */
public class RequestCallback implements Callback<String> {

    private ISuccess iSuccess;
    private IFailure iFailure;
    private IRequest iRequest;
    private IError iError;

    public RequestCallback(ISuccess success, IFailure failure, IRequest request, IError error) {
        this.iError = error;
        this.iFailure = failure;
        this.iRequest = request;
        this.iSuccess = success;
    }

    @Override
    public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
        if (response.isSuccessful()) {
            if (call.isExecuted()) {
                if (iSuccess != null) {
                    iSuccess.onSuccess(response.body());
                }
            }
        } else {
            if (iError != null) {
                iError.onError(response.code(), response.message());
            }
        }

        if (iRequest != null) {
            iRequest.onRequestEnd();
        }
    }

    @Override
    public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
        if (iFailure != null) {
            Log.e("RestClient", t.toString());
            try {
                throw t;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            iFailure.onFailure();
        }
        if (iRequest != null) {
            iRequest.onRequestEnd();
        }
    }
}
