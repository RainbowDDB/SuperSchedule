package com.rainbow.superschedule.core.net.cookie;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * Latte-Core
 * Cookie缓存接口
 * Created By Rainbow on 2019/4/30.
 */
public interface CookieStore {

    /**
     * 添加cookie
     */
    void add(HttpUrl httpUrl, Cookie cookie);

    /**
     * 添加指定httpUrl cookie集合
     */
    void add(HttpUrl httpUrl, List<Cookie> cookies);

    /**
     * 根据HttpUrl从缓存中读取cookie集合
     */
    List<Cookie> get(HttpUrl httpUrl);

    /**
     * 获取全部缓存cookie
     */
    List<Cookie> getCookies();

    /**
     * 移除指定httpUrl cookie集合
     */
    boolean remove(HttpUrl httpUrl, Cookie cookie);

    /**
     * 移除所有cookie
     */
    boolean removeAll();
}
