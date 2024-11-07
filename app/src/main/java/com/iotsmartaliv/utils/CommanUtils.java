package com.iotsmartaliv.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.os.Build;
import androidx.appcompat.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iotsmartaliv.R;
import com.iotsmartaliv.constants.Constant;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * This class is used as utility class that is handle some common function.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 9/5/19 :May : 2019 on 14 : 34.
 */
public class CommanUtils {
    public static boolean isServiceRunning(Context mContext, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager)
                mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList
                = activityManager.getRunningServices(30);
        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showSucessDialog(Context context, String title, String msg) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.my_sucess_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        Button buttonOk = dialogView.findViewById(R.id.buttonOk);
        TextView titleTv = dialogView.findViewById(R.id.titleTv);
        TextView msgTv = dialogView.findViewById(R.id.msgTv);
        titleTv.setText(title);
        msgTv.setText(msg);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        buttonOk.setOnClickListener(v -> alertDialog.dismiss());
    }

    public static String convert12To24Time(String timeOn12formate) {
        SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
        Date date = null;
        try {
            date = parseFormat.parse(timeOn12formate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return displayFormat.format(date);
    }

    public static String convert24To12Time(String timeOn24formate) {
        SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
        Date date = null;
        try {
            date = displayFormat.parse(timeOn24formate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(parseFormat.format(date) + " = ");
        return parseFormat.format(date);
    }

    public static boolean accessWithinRange(String isaccessable, Date startTime, Date endTime, Date currentTime) {
        if (isaccessable.equals("1")) {
            if (currentTime.after(startTime) && currentTime.before(endTime)) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }


    public static String getDaysOnFormateString(String stringlist) {
        try {
            stringlist = stringlist.replace("{", "[").replace("}", "]").replace(":", ",");
            Type listOfTestObject = new TypeToken<List<String>>() {
            }.getType();
            List<String> list2 = new Gson().fromJson(stringlist, listOfTestObject);
            StringBuilder sb = new StringBuilder();
            for (String day : list2) {
                if (sb.length() > 0 && sb.charAt(sb.length()-1) != ',') {
                    sb.append(',');
                }
                switch (day) {
                    case "0":
                        if (!sb.toString().contains("Sunday"))
                            sb.append("Sunday");
                        break;
                    case "1":
                        if (!sb.toString().contains("Monday"))
                            sb.append("Monday");
                        break;
                    case "2":
                        if (!sb.toString().contains("Tuesday"))
                            sb.append("Tuesday");
                        break;
                    case "3":
                        if (!sb.toString().contains("Wednesday"))
                            sb.append("Wednesday");
                        break;
                    case "4":
                        if (!sb.toString().contains("Thursday"))
                            sb.append("Thursday");
                        break;
                    case "5":
                        if (!sb.toString().contains("Friday"))
                            sb.append("Friday");
                        break;
                    case "6":
                        if (!sb.toString().contains("Saturday"))
                            sb.append("Saturday");
                        break;
                }
            }
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }


    /**
     * This method is used for converting bitmap into base64 string.
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {
        // Define the target width and height
        int targetWidth = 480;
        int targetHeight = 640;

        // Resize the bitmap to the specified dimensions
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true);

        // Convert the resized bitmap to Base64
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

        return encoded;
    }

    public static List<Intent> POWERMANAGER_INTENTS = Arrays.asList(
            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity")),
            new Intent().setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")),
            new Intent().setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity")),
            new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.startupapp.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.oppo.safe", "com.oppo.safe.permission.startup.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager")),
            new Intent().setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")),
            new Intent().setComponent(new ComponentName("com.asus.mobilemanager", "com.asus.mobilemanager.entry.FunctionActivity")).setData(android.net.Uri.parse("mobilemanager://function/entry/AutoStart"))
    );

    public static void startPowerSaverIntent(Context context) {
        boolean skipMessage = SharePreference.getInstance(context).getBoolean(Constant.SKIP_PROTECTIONAPPCHECK);

        if (!skipMessage) {
            boolean foundCorrectIntent = false;
            for (Intent intent : POWERMANAGER_INTENTS) {
                if (isCallable(context, intent)) {
                    foundCorrectIntent = true;

                    new AlertDialog.Builder(context)
                            .setTitle(Build.MANUFACTURER + " Protected Apps")
                            .setMessage(String.format("%s requires to be 'White list' to function properly.\nDisable %s from list.%n", context.getString(R.string.app_name), context.getString(R.string.app_name)))
                            .setPositiveButton("Go to settings", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    context.startActivity(intent);
                                    SharePreference.getInstance(context).putBoolean(Constant.SKIP_PROTECTIONAPPCHECK, true);
                                    dialog.dismiss();
                                }
                            })
                            .show();
                    break;
                }
            }
        }
    }

    public static Date utcToLocalTimeZone(String time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = df.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        df.setTimeZone(TimeZone.getDefault());
        String formattedDate = df.format(date);

        /*Date result = new Date();
//        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat inputFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
//        outputFormat.setTimeZone(TimeZone.getDefault());
        inputFormat1.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            result = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
                    parse(outputFormat.format(inputFormat1.parse(time)));
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        return date;
    }

    public static Date sgtToUtc(String sgtTime) {
        // Define the formatter to parse the SGT date-time string
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

        // Set the formatter's time zone to SGT (Singapore Time)
        df.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));

        Date date = null;
        try {
            // Parse the input SGT time to a Date object
            date = df.parse(sgtTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Change the formatter's time zone to UTC
        df.setTimeZone(TimeZone.getTimeZone("UTC"));

        // Format the date object to a UTC string
        String formattedDate = df.format(date);

        // Parse the formatted UTC string back to a Date object
        Date utcDate = null;
        try {
            utcDate = df.parse(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return utcDate;
    }

    private static boolean isCallable(Context context, Intent intent) {
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }
    public static boolean isAppRunning(final Context context, final String packageName) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        if (procInfos != null) {
            for (final ActivityManager.RunningAppProcessInfo processInfo : procInfos) {
                if (processInfo.processName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }
}
