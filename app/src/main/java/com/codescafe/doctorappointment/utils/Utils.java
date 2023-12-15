package com.codescafe.doctorappointment.utils;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static java.lang.Integer.parseInt;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.RelativeSizeSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.codescafe.doctorappointment.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Utils {
    public  static Context context;
    //public static BankDetailModel bankDetailModel;
    //public static CompanyApiModel companyApiModel;
    //public static UserModel userModel;
    public  static String MyObject="MyObject";
    public static String Online="Online";
    public static String Offline="Offline";
    public static String Role="";
    public static String CH="";
    public static String Withdrawn="Withdrawn";
    public static String Money_Added="Money Added";
    public static String Money_Transfer="Transfered";
    public static String BASE_URL="https://easysoftpay.com/api/";
    public static String IMAGE_PATH_ORDERS = "https://fonecash.us/assets/product/";
    public static String IMAGE_PATH_SLIDER = "https://fonecash.us/assets/images/slider/";
    public static String IMAGE_PATH_PROFILE = "https://digitalearnpay.in/API/mlmapi/user_profile/";
    public static ProgressDialog progressDialog;
    public  static String Server_Key="Key=AAAAj8jf8-s:APA91bGHmlr4xAePSDxFGtWkS94CXuMJosYl00_7AI_J6jG2RgEgtY8hDHb72WfYZWQk0Y95882-6LZTNbsZ2lZiKY3jvwVnDfaePMeq50Ek8wvRf4BkbMJrfBTQTx1A-0e10TmoJdoV";
    public static String[] monthNames = new String[] {"January", "Febraury", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    public static ArrayList<String> Month_List=new ArrayList<>();
    public static ArrayList<String> Submit_Address = null;
    public static ArrayList<String> Get_Month_List(Activity context){
        Month_List.add("January");
        Month_List.add("February");
        Month_List.add("March");
        Month_List.add("April");
        Month_List.add("May");
        Month_List.add("June");
        Month_List.add("July");
        Month_List.add("August");
        Month_List.add("September");
        Month_List.add("October");
        Month_List.add("November");
        Month_List.add("December");
        return Month_List;
    }
    public Utils(Context context) {
        this.context=context;
    }
    public static void setToast(Activity context, String text){
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
    }
    public static boolean isDark(Context context){
        boolean is=context.getSharedPreferences("dark", MODE_PRIVATE)
                .getBoolean("isdark",false);
        return is;
    }
    public static void setDark(Context context,boolean is){
        context.getSharedPreferences("dark", MODE_PRIVATE).edit()
                .putBoolean("isdark",is).commit();

    }
    public static void parseVolleyError(VolleyError error,Context context) {
        String json;
        NetworkResponse response = error.networkResponse;
        if (response != null && response.data != null) {
            try {
                JSONObject errorObj = new JSONObject(new String(response.data));
                if (response.statusCode == 400 || response.statusCode == 405 || response.statusCode == 500) {
                    try {
                        //((Activity) context, errorObj.optString("message"));
                        Log.e("eeee_log", errorObj.optString("message"));
                    } catch (Exception e) {
                        // setToast((Activity) context, context.getString(R.string.something_went_wrong));
                        Log.e("eeee_log", e.toString());
                    }
                } else if (response.statusCode == 401) {
                    try {
                        if (!errorObj.optString("message").equalsIgnoreCase("invalid_token")) {
                            // setToast((Activity) context, errorObj.optString("message"));
                            Log.e("eeee_log", errorObj.optString("message"));
                        }
                    } catch (Exception e) {
                        // setToast((Activity) context, context.getString(R.string.something_went_wrong));
                        Log.e("eeee_log", e.toString());
                    }
                } else if (response.statusCode == 422) {
                    json = trimMessage(new String(response.data));
                    if (json != null && !json.equals("")) {
                        //setToast((Activity) context, json.toString());
                        Log.e("eeee_log", json.toString());
                    } else {
                        //setToast((Activity) context, context.getString(R.string.please_try_again));
                        Log.e("eeee_log", context.getString(R.string.please_try_again));
                    }
                } else {
                    Log.e("eeee_log", context.getString(R.string.please_try_again));
                }
            } catch (Exception e) {
                //setToast((Activity) context, context.getString(R.string.something_went_wrong));
                Log.e("eeee_log", e.toString());
            }
        } else {
            if (error instanceof NoConnectionError) {
                setToast((Activity) context, context.getString(R.string.oops_connect_your_internet));
            } else if (error instanceof NetworkError) {
                setToast((Activity) context, context.getString(R.string.oops_connect_your_internet));
            } else if (error instanceof TimeoutError) {
                //setToast((Activity) context, context.getString(R.string.something_went_wrong));
                Log.e("eeee_log", "timeout");
            }
        }
    }

    public static String trimMessage(String json) {
        StringBuilder trimmedString = new StringBuilder();

        try {
            JSONObject jsonObject = new JSONObject(json);
            Iterator<String> iter = jsonObject.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    JSONArray value = jsonObject.getJSONArray(key);
                    for (int i = 0, size = value.length(); i < size; i++) {
                        Log.e("Errors in Form", "" + value.getString(i));
                        trimmedString.append(value.getString(i));
                        if (i < size - 1) {
                            trimmedString.append('\n');
                        }
                    }
                } catch (JSONException e) {

                    trimmedString.append(jsonObject.optString(key));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        Log.e("Trimmed", "" + trimmedString);

        return trimmedString.toString();
    }
    @SuppressLint("ClickableViewAccessibility")
    public static void setHideShowPassword(final EditText edtPassword) {
        edtPassword.setTag("show");
        edtPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (edtPassword.getRight() - edtPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (edtPassword.getTag().equals("show")) {
                            edtPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pass, 0, R.drawable.ic_hide, 0);
                            edtPassword.setTransformationMethod(null);
                            edtPassword.setTag("hide");
                        } else {
                            edtPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pass, 0, R.drawable.ic_show, 0);
                            edtPassword.setTransformationMethod(new PasswordTransformationMethod());
                            edtPassword.setTag("show");
                        }
                        return true;
                    }
                }
                return false;
            }
        });
    }
    public static boolean checkPermission(Context activity){

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ||ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ||ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            //  Toast.makeText(this, "false", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            //  Toast.makeText(this, "true", Toast.LENGTH_SHORT).show();
            return true;
        }
    }
    public static void getpermission(Activity activity){

        String fineLocation = Manifest.permission.ACCESS_FINE_LOCATION;
        String coarseLocation = Manifest.permission.ACCESS_COARSE_LOCATION;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.requestPermissions(new String[]{fineLocation, coarseLocation}, 200);
        }
    }
    public static void increaseFontSizeForPath(Spannable spannable, String path, float increaseTime) {
        int startIndexOfPath = spannable.toString().indexOf(path);
        spannable.setSpan(new RelativeSizeSpan(increaseTime), startIndexOfPath,
                startIndexOfPath + path.length(), 0);
    }
    public static void setFontSizeForPath(Spannable spannable, String path, int fontSizeInPixel) {
        int startIndexOfPath = spannable.toString().indexOf(path);
        spannable.setSpan(new AbsoluteSizeSpan(fontSizeInPixel), startIndexOfPath,
                startIndexOfPath + path.length(), 0);
    }
    public static String getTextSize(String text,int size) {
        return "<font size="+size+">"+text+" </font>";
       // return "<span style=\"size:"+size+"\" >"+text+"</span>";
    }
    public static void StoreString(Context activity,String key,String value) {
        SharedPreferences mStudentPref=activity.getSharedPreferences("Userpref",MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mStudentPref.edit();
        prefsEditor.putString(key, value);
        prefsEditor.apply();
    }
    public static String GetString(Context activity,String key) {
        return activity.getSharedPreferences("Userpref",MODE_PRIVATE).getString(key,"");
    }
    public static void StoreInPref(Activity activity) {
        SharedPreferences mStudentPref=activity.getSharedPreferences("Userpref",MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mStudentPref.edit();
        prefsEditor.putString("MyObject", "true");
        prefsEditor.apply();
    }
    public static boolean GetStoreInPref(Context activity) {
        String json=activity.getSharedPreferences("Userpref",MODE_PRIVATE).getString("MyObject","false");
        return !json.equals("false");
    }
    public static void ClearStoreInPref(Context activity) {
        activity.getSharedPreferences("Userpref",MODE_PRIVATE).edit().clear().commit();
    }
    public static void showKeyboard(Context activity){
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }
    public static void closeKeyboard(Context activity){
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }
    public static String printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
        return ""+elapsedDays;
    }
    public static boolean isStringInt(String s)
    {
        try
        {
            parseInt(s);
            return true;
        } catch (NumberFormatException ex)
        {
            return false;
        }
    }
    public static boolean isStringBigInteger(String s)
    {

        try
        {
            BigInteger bigInteger=new BigInteger(s);
            return true;
        } catch (NumberFormatException ex)
        {
            return false;
        }
    }
    public static void hideSpinnerDropDown(Spinner spinner) {
        try {
            Method method = Spinner.class.getDeclaredMethod("onDetachedFromWindow");
            method.setAccessible(true);
            method.invoke(spinner);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String cmp2(String i){
        String am="AM"; int hour=parseInt(i.substring(0,2));
        if(parseInt(i.substring(0,2))>11) { am="PM"; hour-=12;}
        if(hour==0) { hour=12;}
        return hour+i.substring(2,5)+" "+am;
    }
    public static void cmp2(String start,String end,Activity activity){
        @SuppressLint("SimpleDateFormat") String time = new SimpleDateFormat("HHmm").format(new Date());
        int dbstarttime=parseInt(start.substring(0,2)+start.substring(3,5));
        int dbendtime=parseInt(end.substring(0,2)+end.substring(3,5));
        int curtime = parseInt(time);

        if(dbstarttime > dbendtime) { if(curtime >= dbstarttime || curtime < dbendtime){  } }
        else if (curtime >= dbstarttime && curtime < dbendtime) {  }
        else { activity.finish(); }
    }
    public static String cmp1(String start,String end,String time){
        int dbstarttime=parseInt(start.substring(0,2)+start.substring(3,5));
        int dbendtime=parseInt(end.substring(0,2)+end.substring(3,5));
        int curtime = parseInt(time);

        if(dbstarttime > dbendtime) { if(curtime >= dbstarttime || curtime < dbendtime){ return "PLAY GAME"; } }
        else if (curtime >= dbstarttime && curtime < dbendtime) { return "PLAY GAME"; }
        return "TIME OVER";
    }
    public static void Copy_Content(Context context, String content) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label",content);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, "Copied Successfully", Toast.LENGTH_SHORT).show();
    }
    public static void Open_WhatsApp(Context activity,String text,String message){
        String url = "https://wa.me/"+text+"?text="+message+",";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        activity.startActivity(i);
    }
    public static int getScreenWidth(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        return metrics.widthPixels;
    }
    public static boolean isValidEmail(String email) {

        if (TextUtils.isEmpty(email)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }
    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public static void copytoClipBoard(Context ctx, String data) {
        ClipboardManager clipboard = (ClipboardManager) ctx.getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", data);
        clipboard.setPrimaryClip(clip);
    }
    public static void toastLong(Context ctx, String data) {
        Toast.makeText(ctx, data, Toast.LENGTH_LONG).show();
    }
    public static void toastShort(Context ctx, String data) {
        Toast.makeText(ctx, data, Toast.LENGTH_SHORT).show();
    }
    public static void parse(Context ctx, String data) {
        Uri uri = Uri.parse(data);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ctx.startActivity(intent);
    }
    public static void shareApplication(Context ctx) {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, ctx.getString(R.string.app_name));
            String sAux= "https://play.google.com/store/apps/details?id=" + ctx.getPackageName();
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            ctx.startActivity(Intent.createChooser(i, ctx.getString(R.string.app_name)));

        } catch (Exception e) {
            //e.toString();
        }
    }
    public static void gotoMarket(Context ctx) {
        Uri uri = Uri.parse("market://details?id=" + ctx.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            ctx.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            ctx.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + ctx.getPackageName())));
        }
    }
    public static void setLanguage(Context context, String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }
    public static void warningDialog(Context ctx, String title, String message,
                                     Boolean Cancellable, Boolean ShowCancelButton, String cancelBtnText, String confirmBtnText,
                                     SweetAlertDialog.OnSweetClickListener ButtonClicklistener) {

        SweetAlertDialog dialog = new SweetAlertDialog(ctx, SweetAlertDialog.WARNING_TYPE);

        dialog.setTitleText("\n" + title);
        if (message != null) {
            dialog.setContentText(message + "\n");
        }
        dialog.setConfirmText(confirmBtnText);
        dialog.setCancelable(Cancellable);

        if (ShowCancelButton) {

            dialog.setCancelButton(cancelBtnText, new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismissWithAnimation();
                }
            });

        }

        if (ButtonClicklistener != null) {

            dialog.setConfirmClickListener(ButtonClicklistener);

        } else {

            dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismissWithAnimation();
                }
            });
        }

        dialog.show();
    }
    public static void succesDialog(Context ctx, String title, String message, Boolean Cancellable,
                                    Boolean ShowCancelButton, String cancelBtnText, String confirmBtnText,
                                    SweetAlertDialog.OnSweetClickListener ButtonClicklistener) {

        SweetAlertDialog dialog = new SweetAlertDialog(ctx, SweetAlertDialog.SUCCESS_TYPE);

        dialog.setTitleText("\n" + title);
        if (message != null) {
            dialog.setContentText(message + "\n");
        }
        dialog.setConfirmText(confirmBtnText);
        dialog.setCancelable(Cancellable);

        if (ShowCancelButton) {

            dialog.setCancelButton(cancelBtnText, new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismissWithAnimation();
                }
            });

        }

        if (ButtonClicklistener != null) {

            dialog.setConfirmClickListener(ButtonClicklistener);

        } else {

            dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismissWithAnimation();
                }
            });
        }

        dialog.show();

    }
    public static void errorDialog(Context ctx, String title, String message, Boolean Cancellable, Boolean ShowCancelButton, String cancelBtnText, String confirmBtnText, SweetAlertDialog.OnSweetClickListener ButtonClicklistener) {

        SweetAlertDialog dialog = new SweetAlertDialog(ctx, SweetAlertDialog.ERROR_TYPE);

        dialog.setTitleText("\n" + title);
        if (message != null) {
            dialog.setContentText(message + "\n");
        }
        dialog.setConfirmText(confirmBtnText);
        dialog.setCancelable(Cancellable);

        if (ShowCancelButton) {

            dialog.setCancelButton(cancelBtnText, new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismissWithAnimation();
                }
            });

        }

        if (ButtonClicklistener != null) {

            dialog.setConfirmClickListener(ButtonClicklistener);

        } else {

            dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismissWithAnimation();
                }
            });
        }

        dialog.show();

    }
    public static void normalDialog(Context ctx, String title, String message, Boolean Cancellable, Boolean ShowCancelButton, String cancelBtnText, String confirmBtnText, SweetAlertDialog.OnSweetClickListener ButtonClicklistener) {

        SweetAlertDialog dialog = new SweetAlertDialog(ctx);

        dialog.setTitleText("\n" + title);
        if (message != null) {
            dialog.setContentText(message + "\n");
        }
        dialog.setConfirmText(confirmBtnText);
        dialog.setCancelable(Cancellable);

        if (ShowCancelButton) {

            dialog.setCancelButton(cancelBtnText, new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismissWithAnimation();
                }
            });

        }

        if (ButtonClicklistener != null) {

            dialog.setConfirmClickListener(ButtonClicklistener);

        } else {

            dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismissWithAnimation();
                }
            });
        }

        dialog.show();

    }
    public static void processDialog(Context ctx, String title, String message, Boolean Cancellable) {

        SweetAlertDialog dialog = new SweetAlertDialog(ctx, SweetAlertDialog.PROGRESS_TYPE);

        dialog.getProgressHelper().setBarColor(ctx.getResources().getColor(R.color.colorPrimary));
        dialog.setTitleText(title);
        if (message != null) {
            dialog.setContentText(message + "\n");
        }
        dialog.setCancelable(Cancellable);

        dialog.show();

    }
    public static void editTextDialog(Context ctx, EditText editText, String message, Boolean Cancellable, Boolean ShowCancelButton, String cancelBtnText, String confirmBtnText, SweetAlertDialog.OnSweetClickListener ButtonClicklistener) {

        SweetAlertDialog dialog = new SweetAlertDialog(ctx, SweetAlertDialog.NORMAL_TYPE);

        dialog.setTitleText("\n" + message + "\n");
        dialog.setCustomView(editText);
        dialog.setConfirmText(confirmBtnText);
        dialog.setCancelable(Cancellable);

        if (ShowCancelButton) {

            dialog.setCancelButton(cancelBtnText, new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismissWithAnimation();
                }
            });

        }

        if (ButtonClicklistener != null) {

            dialog.setConfirmClickListener(ButtonClicklistener);

        } else {

            dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismissWithAnimation();
                }
            });
        }

        dialog.show();

    }
    public static void customDialog(Context ctx, View customView, String message, Boolean Cancellable, Boolean ShowCancelButton, String cancelBtnText, String confirmBtnText, SweetAlertDialog.OnSweetClickListener ButtonClicklistener) {

        SweetAlertDialog dialog = new SweetAlertDialog(ctx, SweetAlertDialog.NORMAL_TYPE);

        dialog.setTitleText("\n" + message + "\n");
        dialog.setCustomView(customView);
        dialog.setConfirmText(confirmBtnText);
        dialog.setCancelable(Cancellable);

        if (ShowCancelButton) {

            dialog.setCancelButton(cancelBtnText, new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismissWithAnimation();
                }
            });

        }

        if (ButtonClicklistener != null) {

            dialog.setConfirmClickListener(ButtonClicklistener);

        } else {

            dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismissWithAnimation();
                }
            });
        }

        dialog.show();

    }

    private static void createNewFile(String path) {
        int lastSep = path.lastIndexOf(File.separator);
        if (lastSep > 0) {
            String dirPath = path.substring(0, lastSep);
            makeDir(dirPath);
        }

        File file = new File(path);

        try {
            if (!file.exists()) file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFile(String path) {
        createNewFile(path);

        StringBuilder sb = new StringBuilder();
        FileReader fr = null;
        try {
            fr = new FileReader(new File(path));

            char[] buff = new char[1024];
            int length = 0;

            while ((length = fr.read(buff)) > 0) {
                sb.append(new String(buff, 0, length));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fr != null) {
                try {
                    fr.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }

    public static void writeFile(String path, String str) {
        createNewFile(path);
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(new File(path), false);
            fileWriter.write(str);
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileWriter != null)
                    fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void copyFile(String sourcePath, String destPath) {
        if (!isExistFile(sourcePath)) return;
        createNewFile(destPath);

        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {
            fis = new FileInputStream(sourcePath);
            fos = new FileOutputStream(destPath, false);

            byte[] buff = new byte[1024];
            int length = 0;

            while ((length = fis.read(buff)) > 0) {
                fos.write(buff, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void moveFile(String sourcePath, String destPath) {
        copyFile(sourcePath, destPath);
        deleteFile(sourcePath);
    }

    public static void deleteFile(String path) {
        File file = new File(path);

        if (!file.exists()) return;

        if (file.isFile()) {
            file.delete();
            return;
        }

        File[] fileArr = file.listFiles();

        if (fileArr != null) {
            for (File subFile : fileArr) {
                if (subFile.isDirectory()) {
                    deleteFile(subFile.getAbsolutePath());
                }

                if (subFile.isFile()) {
                    subFile.delete();
                }
            }
        }

        file.delete();
    }

    public static boolean isExistFile(String path) {
        File file = new File(path);
        return file.exists();
    }

    public static void makeDir(String path) {
        if (!isExistFile(path)) {
            File file = new File(path);
            file.mkdirs();
        }
    }

    public static void listDir(String path, ArrayList<String> list) {
        File dir = new File(path);
        if (!dir.exists() || dir.isFile()) return;

        File[] listFiles = dir.listFiles();
        if (listFiles == null || listFiles.length <= 0) return;

        if (list == null) return;
        list.clear();
        for (File file : listFiles) {
            list.add(file.getAbsolutePath());
        }
    }

    public static boolean isDirectory(String path) {
        if (!isExistFile(path)) return false;
        return new File(path).isDirectory();
    }

    public static boolean isFile(String path) {
        if (!isExistFile(path)) return false;
        return new File(path).isFile();
    }

    public static long getFileLength(String path) {
        if (!isExistFile(path)) return 0;
        return new File(path).length();
    }

    public static String getExternalStorageDir() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public static String getPackageDataDir(Context context) {
        return context.getExternalFilesDir(null).getAbsolutePath();
    }

    public static String getPublicDir(String type) {
        return Environment.getExternalStoragePublicDirectory(type).getAbsolutePath();
    }

    public static String convertUriToFilePath(final Context context, final Uri uri) {
        String path = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    path = Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);

                if (!TextUtils.isEmpty(id)) {
                    if (id.startsWith("raw:")) {
                        return id.replaceFirst("raw:", "");
                    }
                }

                final Uri contentUri = ContentUris
                        .withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                path = getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
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

                path = getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if (ContentResolver.SCHEME_CONTENT.equalsIgnoreCase(uri.getScheme())) {
            path = getDataColumn(context, uri, null, null);
        } else if (ContentResolver.SCHEME_FILE.equalsIgnoreCase(uri.getScheme())) {
            path = uri.getPath();
        }

        if (path != null) {
            try {
                return URLDecoder.decode(path, "UTF-8");
            } catch(Exception e) {
                return null;
            }
        }
        return null;
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        final String column = MediaStore.Images.Media.DATA;
        final String[] projection = {
                column
        };

        try (Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } catch (Exception e) {

        }
        return null;
    }


    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static void saveBitmap(Bitmap bitmap, String destPath) {
        Utils.createNewFile(destPath);
        try (FileOutputStream out = new FileOutputStream(new File(destPath))) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getScaledBitmap(String path, int max) {
        Bitmap src = BitmapFactory.decodeFile(path);

        int width = src.getWidth();
        int height = src.getHeight();
        float rate = 0.0f;

        if (width > height) {
            rate = max / (float) width;
            height = (int) (height * rate);
            width = max;
        } else {
            rate = max / (float) height;
            width = (int) (width * rate);
            height = max;
        }

        return Bitmap.createScaledBitmap(src, width, height, true);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int width = options.outWidth;
        final int height = options.outHeight;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampleBitmapFromPath(String path, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    public static void resizeBitmapFileRetainRatio(String fromPath, String destPath, int max) {
        if (!isExistFile(fromPath)) return;
        Bitmap bitmap = getScaledBitmap(fromPath, max);
        saveBitmap(bitmap, destPath);
    }

    public static void resizeBitmapFileToSquare(String fromPath, String destPath, int max) {
        if (!isExistFile(fromPath)) return;
        Bitmap src = BitmapFactory.decodeFile(fromPath);
        Bitmap bitmap = Bitmap.createScaledBitmap(src, max, max, true);
        saveBitmap(bitmap, destPath);
    }

    public static void resizeBitmapFileToCircle(String fromPath, String destPath) {
        if (!isExistFile(fromPath)) return;
        Bitmap src = BitmapFactory.decodeFile(fromPath);
        Bitmap bitmap = Bitmap.createBitmap(src.getWidth(),
                src.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, src.getWidth(), src.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(src.getWidth() / 2, src.getHeight() / 2,
                src.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(src, rect, rect, paint);

        saveBitmap(bitmap, destPath);
    }

    public static void resizeBitmapFileWithRoundedBorder(String fromPath, String destPath, int pixels) {
        if (!isExistFile(fromPath)) return;
        Bitmap src = BitmapFactory.decodeFile(fromPath);
        Bitmap bitmap = Bitmap.createBitmap(src.getWidth(), src
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, src.getWidth(), src.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(src, rect, rect, paint);

        saveBitmap(bitmap, destPath);
    }

    public static void cropBitmapFileFromCenter(String fromPath, String destPath, int w, int h) {
        if (!isExistFile(fromPath)) return;
        Bitmap src = BitmapFactory.decodeFile(fromPath);

        int width = src.getWidth();
        int height = src.getHeight();

        if (width < w && height < h) return;

        int x = 0;
        int y = 0;

        if (width > w) x = (width - w) / 2;

        if (height > h) y = (height - h) / 2;

        int cw = w;
        int ch = h;

        if (w > width) cw = width;

        if (h > height) ch = height;

        Bitmap bitmap = Bitmap.createBitmap(src, x, y, cw, ch);
        saveBitmap(bitmap, destPath);
    }

    public static void rotateBitmapFile(String fromPath, String destPath, float angle) {
        if (!isExistFile(fromPath)) return;
        Bitmap src = BitmapFactory.decodeFile(fromPath);
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap bitmap = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        saveBitmap(bitmap, destPath);
    }

    public static void scaleBitmapFile(String fromPath, String destPath, float x, float y) {
        if (!isExistFile(fromPath)) return;
        Bitmap src = BitmapFactory.decodeFile(fromPath);
        Matrix matrix = new Matrix();
        matrix.postScale(x, y);

        int w = src.getWidth();
        int h = src.getHeight();

        Bitmap bitmap = Bitmap.createBitmap(src, 0, 0, w, h, matrix, true);
        saveBitmap(bitmap, destPath);
    }

    public static void skewBitmapFile(String fromPath, String destPath, float x, float y) {
        if (!isExistFile(fromPath)) return;
        Bitmap src = BitmapFactory.decodeFile(fromPath);
        Matrix matrix = new Matrix();
        matrix.postSkew(x, y);

        int w = src.getWidth();
        int h = src.getHeight();

        Bitmap bitmap = Bitmap.createBitmap(src, 0, 0, w, h, matrix, true);
        saveBitmap(bitmap, destPath);
    }

    public static void setBitmapFileColorFilter(String fromPath, String destPath, int color) {
        if (!isExistFile(fromPath)) return;
        Bitmap src = BitmapFactory.decodeFile(fromPath);
        Bitmap bitmap = Bitmap.createBitmap(src, 0, 0,
                src.getWidth() - 1, src.getHeight() - 1);
        Paint p = new Paint();
        ColorFilter filter = new LightingColorFilter(color, 1);
        p.setColorFilter(filter);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(bitmap, 0, 0, p);
        saveBitmap(bitmap, destPath);
    }

    public static void setBitmapFileBrightness(String fromPath, String destPath, float brightness) {
        if (!isExistFile(fromPath)) return;
        Bitmap src = BitmapFactory.decodeFile(fromPath);
        ColorMatrix cm = new ColorMatrix(new float[]
                {
                        1, 0, 0, 0, brightness,
                        0, 1, 0, 0, brightness,
                        0, 0, 1, 0, brightness,
                        0, 0, 0, 1, 0
                });

        Bitmap bitmap = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(src, 0, 0, paint);
        saveBitmap(bitmap, destPath);
    }

    public static void setBitmapFileContrast(String fromPath, String destPath, float contrast) {
        if (!isExistFile(fromPath)) return;
        Bitmap src = BitmapFactory.decodeFile(fromPath);
        ColorMatrix cm = new ColorMatrix(new float[]
                {
                        contrast, 0, 0, 0, 0,
                        0, contrast, 0, 0, 0,
                        0, 0, contrast, 0, 0,
                        0, 0, 0, 1, 0
                });

        Bitmap bitmap = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(src, 0, 0, paint);

        saveBitmap(bitmap, destPath);
    }

    public static int getJpegRotate(String filePath) {
        int rotate = 0;
        try {
            ExifInterface exif = new ExifInterface(filePath);
            int iOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);

            switch (iOrientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
            }
        } catch (IOException e) {
            return 0;
        }

        return rotate;
    }

    public static File createNewPictureFile(Context context) {
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String fileName = date.format(new Date()) + ".jpg";
        return new File(context.getExternalFilesDir(Environment.DIRECTORY_DCIM).getAbsolutePath() + File.separator + fileName);
    }

}
