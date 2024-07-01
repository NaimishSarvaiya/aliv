package com.iotsmartaliv.constants;

import android.util.Log;

import com.iotsmartaliv.connection.DemoHttps;
import com.iotsmartaliv.model.DeviceBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This activity class is used to carry URL and request for web-api call.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class Request {
    public static final String URL_EMAILREGIST = "https://www.doormaster.me:9099/doormaster/app/users?method=email";
    private static final String SERVER_URL = "https://www.doormaster.me:9099/doormaster/connection";
    private static final String REQ_DEV_URL = "https://www.doormaster.me:9099/doormaster/users/userinfo/rid";

    // request login
    public static JSONObject login(String username, String pwd) throws JSONException {
        final JSONObject login_json = new JSONObject();
        login_json.put("username", username);
        login_json.put("password", pwd);

        return DemoHttps.connectPost(SERVER_URL, login_json);
    }

    // request signup
    public static JSONObject signUp(String accessToken, String strUserName, String strEmailId, String password, String isTermuse) throws JSONException {
        final JSONObject signUp_json = new JSONObject();
        signUp_json.put("access_token", accessToken);
        signUp_json.put("nickname", strUserName);
        signUp_json.put("email", strEmailId);
        signUp_json.put("password", password);
        signUp_json.put("language", isTermuse);

        return DemoHttps.connectPost(URL_EMAILREGIST, signUp_json);
    }

    //Request all device information
    public static ArrayList<DeviceBean> reqDeviceList(String client_id) throws JSONException {
        String param = "client_id=" + client_id;

        JSONObject req_dev_ret = DemoHttps.connectGet(REQ_DEV_URL, param);

        if (req_dev_ret == null || req_dev_ret.isNull("ret")) {
            return null;
        }
        int ret = req_dev_ret.getInt("ret");
        Log.e("Doormaster sdk", "reqDeviceList error [ " + req_dev_ret.toString() + " ]");

        if (ret == 0) {
            JSONArray data = req_dev_ret.getJSONArray("data");
            if (data != null) {
                return resolveData(data);
            } else {
                Log.e("Doormaster sdk", "reqDeviceList  data null ");
                return null;        //data is null
            }
        } else {
            return null;
        }
    }

    /**
     * Resolve the server requested device list data.
     *
     * @param data
     * @return
     * @throws JSONException
     */
    private static ArrayList<DeviceBean> resolveData(JSONArray data) throws JSONException {
        ArrayList<DeviceBean> deviceList = new ArrayList<DeviceBean>();
        for (int i = 0; i < data.length(); i++) {
            JSONArray readerArray = data.getJSONObject(i).getJSONArray("reader");
            for (int j = 0; j < readerArray.length(); j++) {
                JSONObject readerObj = readerArray.getJSONObject(j);
                if (readerObj.isNull("reader_sn")
                        || readerObj.isNull("reader_mac")
                        || readerObj.isNull("dev_type")
                        || readerObj.isNull("privilege")
                        || readerObj.isNull("verified")
                        || readerObj.isNull("open_type")
                        || readerObj.isNull("ekey")) {
                    continue;
                }

                int privilege = readerObj.getInt("privilege");
                String reader_sn = readerObj.getString("reader_sn");
                String dev_mac = readerObj.getString("reader_mac");
                String ekey = readerObj.getString("ekey");
                int dev_type = readerObj.getInt("dev_type");
                //Device operation / effective time / distance / operation mode
                String start_date = readerObj.getString("start_date");
                String end_date = readerObj.getString("end_date");
                int use_count = readerObj.getInt("use_count");
                int verified = readerObj.getInt("verified");
                int open_type = readerObj.getInt("open_type");

                DeviceBean device = new DeviceBean();
                device.setDevSn(reader_sn);
                device.setDevMac(dev_mac.toUpperCase());
                device.setDevType(dev_type);
                device.seteKey(ekey);
                device.setPrivilege(privilege);
                device.setOpenType(open_type);
                device.setVerified(verified);
                device.setStartDate(start_date);
                device.setEndDate(end_date);
                device.setUseCount(use_count);
                deviceList.add(device);
            }
        }

        return deviceList;
    }
}