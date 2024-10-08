package com.iotsmartaliv.utils;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.iotsmartaliv.BuildConfig;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.model.CountryPhoneFormat;
import com.iotsmartaliv.utils.faceenroll.ConnectionManager;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.sentry.Sentry;
import io.sentry.SentryEvent;
import io.sentry.SentryLevel;
import io.sentry.protocol.Message;
import io.sentry.protocol.User;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okio.Buffer;
import retrofit2.Response;

public class Util {

    public  static  MutableLiveData<Boolean> value = new MutableLiveData<Boolean>();
    public static void showToast(Context context, String message){
        Toast.makeText(context,message, Toast.LENGTH_LONG).show();
    }
    public interface NetworkCheckCallback {
        void onNetworkCheckComplete(boolean isAvailable);
    }
    public static void checkInternet(Context context, NetworkCheckCallback callback) {
        ConnectionManager.performApiCallWithNetworkCheckExtra(context, callback);
    }

//    public static Boolean checkInternet(Context context){
//        ConnectionManager.performApiCallWithNetworkCheck(context, false);
//        return ConnectionManager.isNetworkAvailable;
//    }

//    public static void check(Context context){
//        ConnectionManager.performApiCallWithNetworkCheck(context, false);
//        value.setValue(ConnectionManager.isNetworkAvailable);
//    }

    public static void firebaseEvent(String eventName,Context context, String url, String userName,String userId,int errorCode
//            , String parameters,String apiType
    ){
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.URL, url);
//        bundle.putString(Constant.APITYPE, apiType);
//        bundle.putString(Constant.PARAMETER, parameters);
        bundle.putString(Constant.USERNAME,userName );
        bundle.putString(Constant.USERID,userId );
        bundle.putInt(Constant.ERRORCODE,errorCode );
        mFirebaseAnalytics.logEvent(eventName, bundle);
    }

    public static boolean isValidPhoneNumber(String phoneNumber, String countryCode) {
        List<CountryPhoneFormat> list = new ArrayList<>();
        list.add(new CountryPhoneFormat("+1",10, 10));   // USA, Canada
        list.add(new CountryPhoneFormat("+44",10, 10));  // UK
        list.add(new CountryPhoneFormat("+61",9, 9));    // Australia
        list.add(new CountryPhoneFormat("+91",10, 10));  // India
        list.add(new CountryPhoneFormat("+81",10, 11));  // Japan
        list.add(new CountryPhoneFormat("+49",10, 11));  // Germany
        list.add(new CountryPhoneFormat("+86",11, 11));  // China
        list.add(new CountryPhoneFormat("+33",9, 9));    // France
        list.add(new CountryPhoneFormat("+39",9, 10));   // Italy
        list.add(new CountryPhoneFormat("+7",10, 10));   // Russia
        list.add(new CountryPhoneFormat("+34",9, 9));    // Spain
        list.add(new CountryPhoneFormat("+55",10, 11));  // Brazil
        list.add(new CountryPhoneFormat("+27",9, 9));    // South Africa
        list.add(new CountryPhoneFormat("+31",9, 9));    // Netherlands
        list.add(new CountryPhoneFormat("+47",8, 8));    // Norway
        list.add(new CountryPhoneFormat("+46",7, 13));   // Sweden
        list.add(new CountryPhoneFormat("+41",9, 12));   // Switzerland
        list.add(new CountryPhoneFormat("+30",10, 10));  // Greece
        list.add(new CountryPhoneFormat("+52",10, 10));  // Mexico
        list.add(new CountryPhoneFormat("+48",9, 9));    // Poland
        list.add(new CountryPhoneFormat("+20",10, 10));  // Egypt
        list.add(new CountryPhoneFormat("+66",9, 10));   // Thailand
        list.add(new CountryPhoneFormat("+63",10, 10));  // Philippines
        list.add(new CountryPhoneFormat("+62",10, 12));  // Indonesia
        list.add(new CountryPhoneFormat("+65",8, 8));    // Singapore
        list.add(new CountryPhoneFormat("+92",10, 10));  // Pakistan
        list.add(new CountryPhoneFormat("+94",9, 9));    // Sri Lanka
        list.add(new CountryPhoneFormat("+254",9, 9));   // Kenya
        list.add(new CountryPhoneFormat("+82",9, 11));   // South Korea
        list.add(new CountryPhoneFormat("+353",9, 9));   // Ireland
        list.add(new CountryPhoneFormat("+372",7, 12));  // Estonia
        list.add(new CountryPhoneFormat("+358",7, 12));  // Finland
        list.add(new CountryPhoneFormat("+351",9, 9));   // Portugal
        list.add(new CountryPhoneFormat("+27",9, 9));    // South Africa
        list.add(new CountryPhoneFormat("+234",7, 10));  // Nigeria
        list.add(new CountryPhoneFormat("+92",10, 11));  // Pakistan
        list.add(new CountryPhoneFormat("+880",10, 10)); // Bangladesh
        list.add(new CountryPhoneFormat("+213",9, 9));   // Algeria
        list.add(new CountryPhoneFormat("+244",9, 9));   // Angola
        list.add(new CountryPhoneFormat("+54",10, 10));  // Argentina
        list.add(new CountryPhoneFormat("+374",8, 8));   // Armenia
        list.add(new CountryPhoneFormat("+297",7, 7));   // Aruba
        list.add(new CountryPhoneFormat("+43",10, 10));  // Austria
        list.add(new CountryPhoneFormat("+973",8, 8));   // Bahrain
        list.add(new CountryPhoneFormat("+880",10, 10)); // Bangladesh
        list.add(new CountryPhoneFormat("+32",9, 9));    // Belgium
        list.add(new CountryPhoneFormat("+975",7, 8));   // Bhutan
        list.add(new CountryPhoneFormat("+591",8, 8));   // Bolivia
        list.add(new CountryPhoneFormat("+387",8, 9));   // Bosnia and Herzegovina
        list.add(new CountryPhoneFormat("+267",7, 8));   // Botswana
        list.add(new CountryPhoneFormat("+359",7, 9));   // Bulgaria
        list.add(new CountryPhoneFormat("+226",8, 8));   // Burkina Faso
        list.add(new CountryPhoneFormat("+257",8, 8));   // Burundi
        list.add(new CountryPhoneFormat("+855",8, 9));   // Cambodia
        list.add(new CountryPhoneFormat("+237",8, 8));   // Cameroon
        list.add(new CountryPhoneFormat("+238",7, 7));   // Cape Verde
        list.add(new CountryPhoneFormat("+236",8, 8));   // Central African Republic
        list.add(new CountryPhoneFormat("+235",8, 8));   // Chad
        list.add(new CountryPhoneFormat("+56",9, 9));    // Chile
        list.add(new CountryPhoneFormat("+57",10, 10));  // Colombia
        list.add(new CountryPhoneFormat("+243",9, 9));   // Democratic Republic of Congo
        list.add(new CountryPhoneFormat("+242",9, 9));   // Republic of the Congo
        list.add(new CountryPhoneFormat("+506",8, 8));   // Costa Rica
        list.add(new CountryPhoneFormat("+225",8, 8));   // Côte d'Ivoire
        list.add(new CountryPhoneFormat("+385",8, 12));  // Croatia
        list.add(new CountryPhoneFormat("+357",8, 8));   // Cyprus
        list.add(new CountryPhoneFormat("+420",9, 9));   // Czech Republic
        list.add(new CountryPhoneFormat("+45",8, 8));    // Denmark
        list.add(new CountryPhoneFormat("+253",6, 8));   // Djibouti
        list.add(new CountryPhoneFormat("+670",7, 8));   // East Timor
        list.add(new CountryPhoneFormat("+593",8, 9));   // Ecuador
        list.add(new CountryPhoneFormat("+20",9, 10));   // Egypt
        list.add(new CountryPhoneFormat("+503",8, 8));   // El Salvador
        list.add(new CountryPhoneFormat("+240",6, 9));   // Equatorial Guinea
        list.add(new CountryPhoneFormat("+291",7, 7));   // Eritrea
        list.add(new CountryPhoneFormat("+372",7, 12));  // Estonia
        list.add(new CountryPhoneFormat("+251",9, 9));   // Ethiopia
        list.add(new CountryPhoneFormat("+679",7, 7));   // Fiji
        list.add(new CountryPhoneFormat("+358",7, 12));  // Finland
        list.add(new CountryPhoneFormat("+33",9, 9));    // France
        list.add(new CountryPhoneFormat("+241",7, 7));   // Gabon
        list.add(new CountryPhoneFormat("+220",7, 7));   // Gambia
        list.add(new CountryPhoneFormat("+995",8, 9));   // Georgia
        list.add(new CountryPhoneFormat("+49",10, 11));  // Germany
        list.add(new CountryPhoneFormat("+233",9, 9));   // Ghana
        list.add(new CountryPhoneFormat("+30",10, 10));  // Greece
        list.add(new CountryPhoneFormat("+502",8, 8));   // Guatemala
        list.add(new CountryPhoneFormat("+224",9, 9));   // Guinea
        list.add(new CountryPhoneFormat("+245",7, 7));   // Guinea-Bissau
        list.add(new CountryPhoneFormat("+592",7, 7));   // Guyana
        list.add(new CountryPhoneFormat("+509",8, 8));   // Haiti
        list.add(new CountryPhoneFormat("+504",8, 8));   // Honduras
        list.add(new CountryPhoneFormat("+36",8, 9));    // Hungary
        list.add(new CountryPhoneFormat("+354",7, 7));   // Iceland
        list.add(new CountryPhoneFormat("+91",10, 10));  // India
        list.add(new CountryPhoneFormat("+62",10, 12));  // Indonesia
        list.add(new CountryPhoneFormat("+98",10, 10));  // Iran
        list.add(new CountryPhoneFormat("+964",10, 10)); // Iraq
        list.add(new CountryPhoneFormat("+353",9, 9));   // Ireland
        list.add(new CountryPhoneFormat("+972",9, 10));  // Israel
        list.add(new CountryPhoneFormat("+39",9, 10));   // Italy
        list.add(new CountryPhoneFormat("+225",8, 8));   // Ivory Coast
        list.add(new CountryPhoneFormat("+81",10, 11));  // Japan
        list.add(new CountryPhoneFormat("+962",9, 9));   // Jordan
        list.add(new CountryPhoneFormat("+7",10, 10));   // Kazakhstan
        list.add(new CountryPhoneFormat("+254",9, 9));   // Kenya
        list.add(new CountryPhoneFormat("+686",8, 8));   // Kiribati
        list.add(new CountryPhoneFormat("+383",8, 8));   // Kosovo
        list.add(new CountryPhoneFormat("+965",8, 8));   // Kuwait
        list.add(new CountryPhoneFormat("+996",9, 9));   // Kyrgyzstan
        list.add(new CountryPhoneFormat("+856",9, 10));  // Laos
        list.add(new CountryPhoneFormat("+371",8, 8));   // Latvia
        list.add(new CountryPhoneFormat("+961",7, 8));   // Lebanon
        list.add(new CountryPhoneFormat("+266",8, 8));   // Lesotho
        list.add(new CountryPhoneFormat("+231",7, 8));   // Liberia
        list.add(new CountryPhoneFormat("+218",9, 9));   // Libya
        list.add(new CountryPhoneFormat("+423",7, 9));   // Liechtenstein
        list.add(new CountryPhoneFormat("+370",8, 8));   // Lithuania
        list.add(new CountryPhoneFormat("+352",8, 9));   // Luxembourg
        list.add(new CountryPhoneFormat("+853",8, 8));   // Macau
        list.add(new CountryPhoneFormat("+389",8, 9));   // North Macedonia
        list.add(new CountryPhoneFormat("+261",9, 10));  // Madagascar
        list.add(new CountryPhoneFormat("+265",7, 9));   // Malawi
        list.add(new CountryPhoneFormat("+60",9, 10));   // Malaysia
        list.add(new CountryPhoneFormat("+960",7, 7));   // Maldives
        list.add(new CountryPhoneFormat("+223",8, 8));   // Mali
        list.add(new CountryPhoneFormat("+356",8, 8));   // Malta
        list.add(new CountryPhoneFormat("+692",7, 7));   // Marshall Islands
        list.add(new CountryPhoneFormat("+222",8, 8));   // Mauritania
        list.add(new CountryPhoneFormat("+230",7, 8));   // Mauritius
        list.add(new CountryPhoneFormat("+52",10, 10));  // Mexico
        list.add(new CountryPhoneFormat("+373",8, 8));   // Moldova
        list.add(new CountryPhoneFormat("+377",8, 8));   // Monaco
        list.add(new CountryPhoneFormat("+976",8, 8));   // Mongolia
        list.add(new CountryPhoneFormat("+382",8, 9));   // Montenegro
        list.add(new CountryPhoneFormat("+212",9, 9));   // Morocco
        list.add(new CountryPhoneFormat("+258",9, 9));   // Mozambique
        list.add(new CountryPhoneFormat("+95",7, 9));    // Myanmar
        list.add(new CountryPhoneFormat("+264",7, 9));   // Namibia
        list.add(new CountryPhoneFormat("+674",7, 7));   // Nauru
        list.add(new CountryPhoneFormat("+977",10, 10)); // Nepal
        list.add(new CountryPhoneFormat("+31",9, 9));    // Netherlands
        list.add(new CountryPhoneFormat("+687",6, 7));   // New Caledonia
        list.add(new CountryPhoneFormat("+64",8, 10));   // New Zealand
        list.add(new CountryPhoneFormat("+505",8, 8));   // Nicaragua
        list.add(new CountryPhoneFormat("+227",8, 8));   // Niger
        list.add(new CountryPhoneFormat("+234",10, 10)); // Nigeria
        list.add(new CountryPhoneFormat("+47",8, 8));    // Norway
        list.add(new CountryPhoneFormat("+968",8, 8));   // Oman
        list.add(new CountryPhoneFormat("+92",10, 10));  // Pakistan
        list.add(new CountryPhoneFormat("+680",7, 7));   // Palau
        list.add(new CountryPhoneFormat("+970",8, 9));   // Palestine
        list.add(new CountryPhoneFormat("+507",8, 8));   // Panama
        list.add(new CountryPhoneFormat("+675",8, 8));   // Papua New Guinea
        list.add(new CountryPhoneFormat("+595",9, 9));   // Paraguay
        list.add(new CountryPhoneFormat("+51",9, 9));    // Peru
        list.add(new CountryPhoneFormat("+63",10, 10));  // Philippines
        list.add(new CountryPhoneFormat("+48",9, 9));    // Poland
        list.add(new CountryPhoneFormat("+351",9, 9));   // Portugal
        list.add(new CountryPhoneFormat("+974",8, 8));   // Qatar
        list.add(new CountryPhoneFormat("+40",9, 9));    // Romania
        list.add(new CountryPhoneFormat("+7",10, 10));   // Russia
        list.add(new CountryPhoneFormat("+250",9, 9));   // Rwanda
        list.add(new CountryPhoneFormat("+685",5, 7));   // Samoa
        list.add(new CountryPhoneFormat("+378",10, 10)); // San Marino
        list.add(new CountryPhoneFormat("+239",6, 6));   // Sao Tome and Principe
        list.add(new CountryPhoneFormat("+966",9, 9));   // Saudi Arabia
        list.add(new CountryPhoneFormat("+221",9, 9));   // Senegal
        list.add(new CountryPhoneFormat("+381",8, 9));   // Serbia
        list.add(new CountryPhoneFormat("+248",7, 7));   // Seychelles
        list.add(new CountryPhoneFormat("+232",8, 8));   // Sierra Leone
        list.add(new CountryPhoneFormat("+65",8, 8));    // Singapore
        list.add(new CountryPhoneFormat("+421",9, 9));   // Slovakia
        list.add(new CountryPhoneFormat("+386",9, 9));   // Slovenia
        list.add(new CountryPhoneFormat("+677",7, 7));   // Solomon Islands
        list.add(new CountryPhoneFormat("+252",7, 8));   // Somalia
        list.add(new CountryPhoneFormat("+27",9, 9));    // South Africa
        list.add(new CountryPhoneFormat("+82",9, 11));   // South Korea
        list.add(new CountryPhoneFormat("+211",9, 9));   // South Sudan
        list.add(new CountryPhoneFormat("+34",9, 9));    // Spain
        list.add(new CountryPhoneFormat("+94",9, 9));    // Sri Lanka
        list.add(new CountryPhoneFormat("+249",9, 10));  // Sudan
        list.add(new CountryPhoneFormat("+597",6, 7));   // Suriname
        list.add(new CountryPhoneFormat("+268",8, 8));   // Swaziland
        list.add(new CountryPhoneFormat("+46",7, 13));   // Sweden
        list.add(new CountryPhoneFormat("+41",9, 12));   // Switzerland
        list.add(new CountryPhoneFormat("+963",9, 9));   // Syria
        list.add(new CountryPhoneFormat("+886",9, 10));  // Taiwan
        list.add(new CountryPhoneFormat("+992",9, 9));   // Tajikistan
        list.add(new CountryPhoneFormat("+255",9, 9));   // Tanzania
        list.add(new CountryPhoneFormat("+66",9, 9));    // Thailand
        list.add(new CountryPhoneFormat("+228",8, 8));   // Togo
        list.add(new CountryPhoneFormat("+676",5, 5));   // Tonga
        list.add(new CountryPhoneFormat("+216",8, 8));   // Tunisia
        list.add(new CountryPhoneFormat("+90",10, 10));  // Turkey
        list.add(new CountryPhoneFormat("+993",8, 8));   // Turkmenistan
        list.add(new CountryPhoneFormat("+688",6, 6));   // Tuvalu
        list.add(new CountryPhoneFormat("+256",9, 9));   // Uganda
        list.add(new CountryPhoneFormat("+380",9, 9));   // Ukraine
        list.add(new CountryPhoneFormat("+971",9, 9));   // United Arab Emirates
        list.add(new CountryPhoneFormat("+44",10, 10));  // United Kingdom
        list.add(new CountryPhoneFormat("+1",10, 10));   // United States
        list.add(new CountryPhoneFormat("+598",8, 8));   // Uruguay
        list.add(new CountryPhoneFormat("+998",9, 9));   // Uzbekistan
        list.add(new CountryPhoneFormat("+678",7, 7));   // Vanuatu
        list.add(new CountryPhoneFormat("+379",8, 10));  // Vatican City
        list.add(new CountryPhoneFormat("+58",10, 10));  // Venezuela
        list.add(new CountryPhoneFormat("+84",9, 10));   // Vietnam
        list.add(new CountryPhoneFormat("+967",9, 9));   // Yemen
        list.add(new CountryPhoneFormat("+260",9, 9));   // Zambia
        list.add(new CountryPhoneFormat("+263",9, 9));   // Zimbabwe
        list.add(new CountryPhoneFormat("+93",9, 9));      // Afghanistan
        list.add(new CountryPhoneFormat("+355",9, 9));     // Albania
        list.add(new CountryPhoneFormat("+213",9, 9));     // Algeria
        list.add(new CountryPhoneFormat("+1684",10, 10));  // American Samoa
        list.add(new CountryPhoneFormat("+376",6, 6));     // Andorra
        list.add(new CountryPhoneFormat("+1264",7, 7));    // Anguilla
        list.add(new CountryPhoneFormat("+1268",10, 10));  // Antigua and Barbuda
        list.add(new CountryPhoneFormat("+994",9, 9));     // Azerbaijan
        list.add(new CountryPhoneFormat("+1242",10, 10));  // Bahamas
        list.add(new CountryPhoneFormat("+1246",10, 10));  // Barbados
        list.add(new CountryPhoneFormat("+357",8, 8));     // Cyprus
        list.add(new CountryPhoneFormat("+501",7, 7));     // Belize
        list.add(new CountryPhoneFormat("+229",8, 8));     // Benin
        list.add(new CountryPhoneFormat("+1441",7, 7));    // Bermuda
        list.add(new CountryPhoneFormat("+673",7, 7));     // Brunei
        list.add(new CountryPhoneFormat("+1345",10, 10));  // Cayman Islands
        list.add(new CountryPhoneFormat("+269",7, 7));     // Comoros
        list.add(new CountryPhoneFormat("+682",5, 5));     // Cook Islands
        list.add(new CountryPhoneFormat("+53",8, 8));      // Cuba
        list.add(new CountryPhoneFormat("+1767",10, 10));  // Dominica
        list.add(new CountryPhoneFormat("+1809",10, 10));  // Dominican Republic
        list.add(new CountryPhoneFormat("+500",5, 5));     // Falkland Islands
        list.add(new CountryPhoneFormat("+298",6, 6));     // Faroe Islands
        list.add(new CountryPhoneFormat("+594",9, 9));     // French Guiana
        list.add(new CountryPhoneFormat("+689",6, 6));     // French Polynesia
        list.add(new CountryPhoneFormat("+350",8, 8));     // Gibraltar
        list.add(new CountryPhoneFormat("+299",6, 6));     // Greenland
        list.add(new CountryPhoneFormat("+1473",10, 10));  // Grenada
        list.add(new CountryPhoneFormat("+590",9, 9));     // Guadeloupe
        list.add(new CountryPhoneFormat("+1671",10, 10));  // Guam
        list.add(new CountryPhoneFormat("+1876",10, 10));  // Jamaica
        list.add(new CountryPhoneFormat("+850",10, 10));   // North Korea
        list.add(new CountryPhoneFormat("+569",9, 9));     // Chile
        list.add(new CountryPhoneFormat("+691",7, 7));     // Micronesia
        list.add(new CountryPhoneFormat("+1664",10, 10));  // Montserrat
        list.add(new CountryPhoneFormat("+599",7, 7));     // Netherlands Antilles
        list.add(new CountryPhoneFormat("+683",4, 4));     // Niue
        list.add(new CountryPhoneFormat("+672",6, 6));     // Norfolk Island
        list.add(new CountryPhoneFormat("+1670",10, 10));  // Northern Mariana Islands
        list.add(new CountryPhoneFormat("+1787",10, 10));  // Puerto Rico
        list.add(new CountryPhoneFormat("+70",9, 9));      // Russia, Chechnya
        list.add(new CountryPhoneFormat("+290",4, 4));     // Saint Helena
        list.add(new CountryPhoneFormat("+1869",10, 10));  // Saint Kitts and Nevis
        list.add(new CountryPhoneFormat("+1758",10, 10));  // Saint Lucia
        list.add(new CountryPhoneFormat("+508",6, 6));     // Saint Pierre and Miquelon
        list.add(new CountryPhoneFormat("+1784",10, 10));  // Saint Vincent and the Grenadines
        list.add(new CountryPhoneFormat("+684",10, 10));   // American Samoa
        list.add(new CountryPhoneFormat("+690",4, 4));     // Tokelau
        list.add(new CountryPhoneFormat("+1868",10, 10));  // Trinidad and Tobago
        list.add(new CountryPhoneFormat("+7370",10, 10));  // Russia, Kaliningrad
        list.add(new CountryPhoneFormat("+1649",10, 10));  // Turks and Caicos Islands
        list.add(new CountryPhoneFormat("+1284",10, 10));  // British Virgin Islands
        list.add(new CountryPhoneFormat("+1340",10, 10));  // U.S. Virgin Islands
        list.add(new CountryPhoneFormat("+681",6, 6));


        for (CountryPhoneFormat format : list) {
            if (format.getCountryCode().equals("+"+countryCode)) {
                int length = phoneNumber.length();
                if (length >= format.getMinLength() && length <= format.getMaxLength()) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
//        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
//        try {
//            if (phoneNumber.trim().isEmpty()){
//                return  false;
//            }else {
//            Phonenumber.PhoneNumber numberProto = phoneNumberUtil.parse(phoneNumber, countryCode);
//                return phoneNumberUtil.isValidNumber(numberProto);
//            }
//        } catch (NumberParseException e) {
//            e.printStackTrace();
//            return false;
//        }
    }

    public static boolean validateTime(String startTime, String endTime, String count) {
        if (count.equals("")){
           return true;
        }else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Calendar current = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            end.add(Calendar.DAY_OF_YEAR, Integer.parseInt(count));

            try {
                Date startTimeDate = sdf.parse(startTime);
                Date endTimeDate = sdf.parse(endTime);

                if (startTimeDate.after(current.getTime()) && startTimeDate.before(end.getTime()) &&
                        endTimeDate.after(current.getTime()) && endTimeDate.before(end.getTime())) {
                    return true; // Times are within the valid range
                } else {
                    return false; // Times are not within the valid range
                }
            } catch (ParseException e) {
                e.printStackTrace();
                return false; // Invalid date format
            }
        }
    }
    public static void logSentryEvent(String apiFlag, Request request, Response<?> response, Throwable throwable) {
        SentryEvent event = new SentryEvent();

        // Determine if it's a success or error
        boolean isSuccess = response != null && response.isSuccessful();

        // Set event level based on success or error
        event.setLevel(isSuccess ? SentryLevel.INFO : SentryLevel.ERROR);

        // Set the event message
        String messageText = isSuccess ? "API Success: " + apiFlag : "API Error: " + apiFlag;
        Message message = new Message();
        message.setMessage(messageText);
        event.setMessage(message);

        // Log request details
        event.setTag("API URL",  request.method() + "  " +request.url().toString());
        event.setExtra("Request Headers", request.headers().toString());
        event.setExtra("Request Parameters", getRequestParameters(request));

        if (response != null) {
            // Log response details
            event.setTag("Status Code", String.valueOf(response.code()));
            event.setExtra("Response Headers", response.headers().toString());
            event.setExtra("Response Body", response.body() != null ? new Gson().toJson(response.body()) : "No response body");
        }

        if (throwable != null) {
            // Log the throwable in case of an error
            event.setThrowable(throwable);
        }

        // Attach any additional context like user info, environment, etc.
        User user = new User();
        user.setId("user-id"); // Replace with actual user ID if available
        event.setUser(user);

        event.setTag("Environment", BuildConfig.DEBUG ? "Development" : "Production");

        // Send the event to Sentry
        Sentry.captureEvent(event);
    }

    private static String getRequestParameters(Request request) {
        Map<String, String> params = new LinkedHashMap<>();

        // Extract query parameters from the URL
        HttpUrl url = request.url();
        for (int i = 0; i < url.querySize(); i++) {
            params.put(url.queryParameterName(i), url.queryParameterValue(i));
        }

        // Extract form data from the body if available
        if (request.body() instanceof FormBody) {
            FormBody formBody = (FormBody) request.body();
            for (int i = 0; i < formBody.size(); i++) {
                params.put(formBody.name(i), formBody.value(i));
            }
        } else {
            // For non-form bodies, capture the raw body as a string
//            params.put("Raw Body", getRequestBodyAsString(request));
        }

        return new Gson().toJson(params); // Convert the parameters map to a JSON string
    }


    public static void logDoorOpenEvent(String triggerSource, boolean isSuccess, String userId, String deviceDetails) {
        // Create a new Sentry event
        SentryEvent event = new SentryEvent();

        // Set event level based on success or failure
//        event.setLevel(isSuccess ? SentryLevel.INFO : SentryLevel.ERROR);

        // Build the event message based on the trigger source and status
        String messageText;
        switch (triggerSource) {
            case "GreenKey":
                messageText = (isSuccess ? "Success: Door open from Green key" : "Failed: Door open from Green key");
                break;
            case "DeviceList":
                messageText = (isSuccess ? "Success: Door open from device list" : "Failed: Door open from device list");
                break;
            case "VideoCall":
                messageText = (isSuccess ? "Success: Door open from video call" : "Failed: Door open from video call");
                break;
            case "VideoDeviceList":
                messageText = (isSuccess ? "Success: Door open from video device list" : "Failed: Door open from video device list");
                break;
            default:
                messageText = (isSuccess ? "Success: Door open" : "Failed: Door open");
                break;
        }

        Message message = new Message();
        message.setMessage(messageText);
        event.setMessage(message);

        // Add additional tags and extra data
        event.setTag("User ID", userId);
        event.setTag("Trigger Source", triggerSource);
        event.setExtra("Device Details", deviceDetails);

        // Set environment context
        event.setTag("Environment", BuildConfig.DEBUG ? "Development" : "Production");

        // Send the event to Sentry
        Sentry.captureEvent(event);
    }

    public static void logVideoCallEvent(String callType, String deviceSN, String deviceName) {
        SentryEvent event = new SentryEvent();

        // Determine the message for the event
        String messageText = callType.equals("INCOMING") ? "Incoming Video Call" : "Outgoing Video Call";

        // Set the event level (INFO for calls)
        event.setLevel(SentryLevel.INFO);

        // Set the event message
        Message message = new Message();
        message.setMessage(messageText);
        event.setMessage(message);

        // Attach additional context
        event.setTag("User ID", LOGIN_DETAIL.getAppuserID());
//        if (device != null) {
        event.setExtra("Device SN",deviceSN );
        event.setExtra("Device Name", deviceName);
//        }

        // Log environment (Production or Development)
        event.setTag("Environment", BuildConfig.DEBUG ? "Development" : "Production");

        // Send the event to Sentry
        Sentry.captureEvent(event);
    }

    public static String getPathFromUri(Context context, Uri uri) {
        String path = null;

        // Check if the document Uri is from MediaStore
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // External Storage Provider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            } else if (isDownloadsDocument(uri)) {
                // Downloads Provider
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);

            } else if (isMediaDocument(uri)) {
                // Media Provider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

    public static String formatFeedbackDate(String inputDate) {
        try {
            // Define the input and output date formats
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd MMM yy", Locale.getDefault());
            SimpleDateFormat outputTimeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

            // Parse the input date string to a Date object
            Date date = inputFormat.parse(inputDate);

            // Format the date and time to the desired output format
            String formattedDate = outputDateFormat.format(date);
            String formattedTime = outputTimeFormat.format(date);

            // Combine the formatted date and time, separated by a newline
            return formattedDate+"," + "\n" + formattedTime;
        } catch (ParseException e) {
            e.printStackTrace();
            return inputDate; // Return the input string if parsing fails
        }
    }
    public static  InputFilter filterEmoji = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                int type = Character.getType(source.charAt(i));
                if (type == Character.SURROGATE || type == Character.NON_SPACING_MARK || type == Character.OTHER_SYMBOL) {
                    // Block surrogate pairs (emoji) and non-spacing marks (composite characters)
                    return "";
                }
            }
            return null; // Accept valid input
        }
    };

    // Method to encode a chat string (convert to non-lossy ASCII)
    public static String encodeChatString(String input) {
        StringBuilder encodedString = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);

            // Check if the character is an emoji or a non-ASCII character
            if (ch > 127) {
                // Convert the character to its Unicode escape sequence
                encodedString.append(String.format("\\u%04x", (int) ch));
            } else {
                // Append normal ASCII characters directly
                encodedString.append(ch);
            }
        }

        return encodedString.toString();
    }

    // Method to decode the string from non-lossy ASCII
    public static String decodeChatString(String input) {
        try {
            StringBuilder decodedString = new StringBuilder();

            String[] unicodeArray = input.split("\\\\u");
            decodedString.append(unicodeArray[0]); // Append normal text before the first unicode

            // Loop through the Unicode parts and convert them to characters
            for (int i = 1; i < unicodeArray.length; i++) {
                String unicode = unicodeArray[i];

                // Check if the string has at least 4 characters for the Unicode sequence
                if (unicode.length() >= 4) {
                    // Get the first 4 characters as the Unicode code point
                    String unicodeChar = unicode.substring(0, 4);
                    // Convert the Unicode hex value to an integer and then to a char
                    int codePoint = Integer.parseInt(unicodeChar, 16);
                    decodedString.append(Character.toChars(codePoint));

                    // Append any remaining characters after the Unicode sequence
                    decodedString.append(unicode.substring(4));
                } else {
                    // If there are less than 4 characters, just append the string
                    decodedString.append(unicode);
                }
            }

            return decodedString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return input; // Return the original string if there's an exception
        }
    }
    public static String calculateTimeDifference(String progressDate, String feedbackDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            // Parse the dates
            Date progress = dateFormat.parse(progressDate);
            Date feedback = dateFormat.parse(feedbackDate);

            // Get the difference in milliseconds
            long differenceInMillis = Math.abs(progress.getTime() - feedback.getTime());

            // Convert milliseconds to hours and minutes
            long differenceInHours = TimeUnit.MILLISECONDS.toHours(differenceInMillis);
            long remainingMinutes = TimeUnit.MILLISECONDS.toMinutes(differenceInMillis) % 60;

            // Convert milliseconds to days
            long differenceInDays = TimeUnit.MILLISECONDS.toDays(differenceInMillis);

            // Return the formatted time difference
            if (differenceInDays == 0) { // If difference is less than a day
                // If minutes >= 31, round the hour difference up
                if (remainingMinutes >= 31) {
                    differenceInHours += 1;
                }

                return differenceInHours + " hours";
            } else if (differenceInDays == 1) { // Between 24 and 48 hours
                return "2 days";
            } else if (differenceInDays == 2) { // Between 48 and 72 hours
                return "3 days";
            } else {
                return differenceInDays + " days"; // For more than 72 hours
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return "Invalid date";
        }
    }
}
