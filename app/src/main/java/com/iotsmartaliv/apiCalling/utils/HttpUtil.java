package com.iotsmartaliv.apiCalling.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.iotsmartaliv.R;
import com.iotsmartaliv.apiCalling.models.ErrorObject;
import com.iotsmartaliv.constants.Constant;

import org.json.JSONObject;

public class HttpUtil {
    private static Logger logger = new Logger(HttpUtil.class.getSimpleName());

    /**
     * This method returns a Json object for handling Force update error
     *
     * @return
     */
    public static JSONObject getServerErrorJsonObject(Context context) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.ErrorClass.STATUS, 505);
            jsonObject.put(Constant.ErrorClass.CODE, 3000);
            jsonObject.put(Constant.ErrorClass.MESSAGE, context.getString(R.string.server_not_available));
            jsonObject.put(Constant.ErrorClass.DEVELOPER_MESSAGE, context.getString(R.string.server_not_available));
        } catch (Exception e) {
            logger.error(e);
        }
        return jsonObject;
    }

    /**
     * This method returns a Json object for handling Force update error
     *
     * @return
     */
    public static ErrorObject getServerErrorPojo(Context context) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(getServerErrorJsonObject(context).toString(), ErrorObject.class);
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    }
}
