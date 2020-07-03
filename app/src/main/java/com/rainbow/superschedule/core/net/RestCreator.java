package com.rainbow.superschedule.core.net;

import com.rainbow.superschedule.core.App;
import com.rainbow.superschedule.core.net.cookie.CookieManager;
import com.rainbow.superschedule.core.net.cookie.PersistentCookieStore;

import java.util.ArrayList;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * SuperSchedule
 * Created By Rainbow on 2020/5/5.
 */
public class RestCreator {

    public static WeakHashMap<String, Object> getParams() {
        return ParamsHolder.params;
    }

    private static class ParamsHolder {
        static WeakHashMap<String, Object> params = new WeakHashMap<>();
    }

    private static class RetrofitHolder {
        private static final String BASE_URL = "http://119.23.174.48:8006/";

        private static final Retrofit RETROFIT_CLIENT = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(OkHttpHolder.OK_HTTP_CLIENT)
                .build();
    }

    @SuppressWarnings("all")
    private static final class OkHttpHolder {
        private static final int TIME_OUT = 60;
        private static final ArrayList<Interceptor> INTERCEPTORS = new ArrayList<>();
        private static final OkHttpClient OK_HTTP_CLIENT = addInterceptors()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .cookieJar(new CookieManager(new PersistentCookieStore(App.getContext())))
                .build();

        private static OkHttpClient.Builder addInterceptors() {
            OkHttpClient.Builder BUILDER = new OkHttpClient.Builder();
            if (INTERCEPTORS != null && !INTERCEPTORS.isEmpty()) {
                for (int i = 0; i < INTERCEPTORS.size(); i++) {
                    BUILDER.addInterceptor(INTERCEPTORS.get(i));
                }
            }
            return BUILDER;
        }
    }

    private static final class RestServiceHolder {
        private static final RestService REST_SERVICE =
                RetrofitHolder.RETROFIT_CLIENT.create(RestService.class);
    }

    public static RestService getRestService() {
        return RestServiceHolder.REST_SERVICE;
    }
}
