package com.iotsmartaliv.apiAndSocket.listeners;


import com.iotsmartaliv.apiAndSocket.models.ErrorObject;

/**
 * This interface is use for Retrofit response callback on calling activity.
 *
 * @param <T> is class type that is return object.
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 3/5/19 :May : 2019 on 14 : 53.
 */
public interface RetrofitListener<T> {
    void onResponseSuccess(T sucessRespnse, String apiFlag);

    void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag);
}
