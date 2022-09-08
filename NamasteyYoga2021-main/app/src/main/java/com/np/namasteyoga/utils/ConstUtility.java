package com.np.namasteyoga.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;


import com.np.namasteyoga.BuildConfig;
import com.np.namasteyoga.R;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import static com.np.namasteyoga.modules.SharedPreference.DataPrefs;



public class ConstUtility {

    static {
        System.loadLibrary("native-lib");
    }

    public static native String getAPIKey();

    private static final String characterEncoding = "UTF-8";
    private static final String cipherTransformation = "AES/ECB/PKCS7PADDING";
    private static final String cipherTransformation_ = "AES/ECB/PKCS7Padding";
    private static final String aesEncryptionAlgorithem = "AES-128-ECB";


    public static String encryptLC(String strToEncrypt) {
        try {
            byte[] key = getAPIKey().getBytes();
            SecretKeySpec secretKey = new SecretKeySpec(key, aesEncryptionAlgorithem);
            Cipher cipher = Cipher.getInstance(cipherTransformation_);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            String encryptedSignature = Base64.encodeToString(cipher.doFinal(strToEncrypt.getBytes()), Base64.DEFAULT);
            encryptedSignature = encryptedSignature.replace("\\u003d", "=");
            encryptedSignature = encryptedSignature.replace("\n", "");
            encryptedSignature = encryptedSignature.replace("/", "__");

            encryptedSignature = encryptedSignature.trim();

            return encryptedSignature;
        } catch (Exception e) {
            return null;
        }


    }

    public static String decrypt(String encryptedText) {
        String decryptedText = "";
        try {
            if (encryptedText == null) {
                return null;
            } else if (encryptedText.equals("")) {
                return "";
            } else {
                Cipher cipher = Cipher.getInstance(cipherTransformation);


                byte[] key = getAPIKey().getBytes(characterEncoding);

                SecretKeySpec secretKey = new SecretKeySpec(key, aesEncryptionAlgorithem);
                IvParameterSpec ivparameterspec = new IvParameterSpec(key);
                cipher.init(Cipher.DECRYPT_MODE, secretKey);
                byte[] cipherText = Base64.decode(encryptedText, Base64.DEFAULT);
                decryptedText = new String(cipher.doFinal(cipherText), "UTF-8");
            }
        } catch (Exception E) {
            if (BuildConfig.DEBUG)
            System.err.println("decrypt Exception : " + E.getMessage());
            return "";
        }
        return decryptedText;
    }

    public static Map<String, String> getMD5EncryptedString(String encTarget) {
        MessageDigest mdEnc = null;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Exception while encrypting to md5");
            if (BuildConfig.DEBUG)
            e.printStackTrace();
        } // Encryption algorithm
        mdEnc.update(encTarget.getBytes(), 0, encTarget.length());
        String md5 = new BigInteger(1, mdEnc.digest()).toString(16);
        while (md5.length() < 32) {
            md5 = "0" + md5;
        }
        HashMap<String, String> headers = new HashMap<String, String>();

        headers.put("checksum", md5);

        return headers;
    }



     //===============================================
    public static boolean isNetworkConnectivity(Context activity) {
        ConnectivityManager cm = (ConnectivityManager) activity
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }




    public static boolean isValidMail(String email) {
        return email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }




    public static String getDateInNumber(String s) {

//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//
//        Date date = null;
//        try {
//
//            date = sdf.parse(s);
//            sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
//            return sdf.format(date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return s;

        String dateToReturn = s;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date gmt = null;

//        SimpleDateFormat sdfOutPutToSend = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        SimpleDateFormat sdfOutPutToSend = new SimpleDateFormat("MMM dd yyyy hh:mm a");
//        sdfOutPutToSend.setTimeZone(TimeZone.getDefault());

        try {

            gmt = sdf.parse(s);
            dateToReturn = sdfOutPutToSend.format(gmt);

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String sub1 = dateToReturn.substring(dateToReturn.length() - 3).toUpperCase();

        return dateToReturn.substring(0, dateToReturn.length() - 3) + sub1;
    }






    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;







    public static void hideKeyBoard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null)
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }




    public static void showToast(final Activity activity, int stringId, final boolean isFinishActivity) {
        try {
            final LayoutInflater factory = LayoutInflater.from(activity);
            final View deleteDialogView = factory.inflate(
                    R.layout.toast_layout, null);
            final AlertDialog deleteDialog = new AlertDialog.Builder(activity).create();
            deleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            deleteDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            deleteDialog.setView(deleteDialogView);

            TextView textView = (TextView) deleteDialogView.findViewById(R.id.tvToast);
            textView.setText(activity.getResources().getString(stringId));
            deleteDialog.show();
            deleteDialog.setCancelable(false);
            final Timer timer2 = new Timer();
            timer2.schedule(new TimerTask() {
                public void run() {
                    deleteDialog.dismiss();
                    timer2.cancel();
                    if (isFinishActivity)
                        activity.finish();
                }
            }, 2000);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void showToast(final Activity activity, String stringId, final boolean isFinishActivity) {


        try {
            final LayoutInflater factory = LayoutInflater.from(activity);
            final View deleteDialogView = factory.inflate(
                    R.layout.toast_layout, null);
            final AlertDialog deleteDialog = new AlertDialog.Builder(activity).create();
            deleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            deleteDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            deleteDialog.setView(deleteDialogView);

            TextView textView = (TextView) deleteDialogView.findViewById(R.id.tvToast);
            textView.setText(stringId);
            deleteDialog.show();
            deleteDialog.setCancelable(false);
            final Timer timer2 = new Timer();
            timer2.schedule(new TimerTask() {
                public void run() {
                    deleteDialog.dismiss();
                    timer2.cancel();
                    if (isFinishActivity)
                        activity.finish();
                }
            }, 2000);

        } catch (Exception e) {
            if (C.DEBUG)
                e.printStackTrace();
        }


    }
















    // Ye header banaya hai
    public static Map<String, String> getHeaderPHP(String encTarget, Context context) {
//        SharedPreferences sharedPreferences;
        SharedPreferences sharedPreferences;
        sharedPreferences = context.getSharedPreferences(DataPrefs, Context.MODE_PRIVATE);
        HashMap<String, String> headers = new HashMap<String, String>();
//        headers.put("Authorization", "Bearer " + SharedPreference.getInstance(context).getString(C.TOKEN_VALUE));
        if(sharedPreferences.contains("user_detail")) {
            if (SharedPreferencesUtils.INSTANCE.getUserDetails(sharedPreferences)!=null) {
                String token = SharedPreferencesUtils.INSTANCE.getUserDetails(sharedPreferences).getToken();
                Logger.INSTANCE.Debug(token, Logger.TAG);
                headers.put("Authorization", "Bearer " + token);
            }
        }

        MessageDigest mdEnc = null;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            if (C.DEBUG)
                e.printStackTrace();
        } // Encryption algorithm
        mdEnc.update(encTarget.getBytes(), 0, encTarget.length());
        String md5 = new BigInteger(1, mdEnc.digest()).toString(16);
        while (md5.length() < 32) {
            md5 = "0" + md5;
        }
        headers.put("checksum", md5);

        Logger.INSTANCE.Debug(md5, Logger.TAG);

        return headers;
    }

    public static SharedPreferences getSharedPrefereences(Context context){
        return context.getSharedPreferences(DataPrefs, Context.MODE_PRIVATE);
    }

    // header for city search
    public static Map<String, String> getHeaderCity(String encTarget) {


        HashMap<String, String> headers = new HashMap<String, String>();

        MessageDigest mdEnc = null;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            if (C.DEBUG)
                e.printStackTrace();
        } // Encryption algorithm
        mdEnc.update(encTarget.getBytes(), 0, encTarget.length());
        String md5 = new BigInteger(1, mdEnc.digest()).toString(16);
        while (md5.length() < 32) {
            md5 = "0" + md5;
        }
        headers.put("checksum", md5);


        return headers;
    }







    public static Map<String, String> getHeaderPHP() {
        SharedPreferences sharedPreferences = null;
       // sharedPreferences = getSharedPreferences(DataPrefs, Context.MODE_PRIVATE);

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer " + SharedPreferencesUtils.INSTANCE.getUserDetails(sharedPreferences).getToken());

        return headers;
    }




    public static String getDateformate(Date time) {
        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);

        return sdf.format(time);
    }


    public static ProgressDialog getProgressDialog(Context context, String msg) {
        ProgressDialog progressDialog;

        progressDialog = new ProgressDialog(context, R.style.AppCompatAlertDialogStyle);
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(false);
        progressDialog.show();


        return progressDialog;
    }






    public void showDialog(boolean isShown, String msg, Context context) {
        try {

            if (isShown) {
                if (progressDialog == null || !progressDialog.isShowing()) {
                    progressDialog = new ProgressDialog(context, R.style.AppCompatAlertDialogStyle);
                    progressDialog.setMessage(msg);
                    progressDialog.setCancelable(true);
                    progressDialog.show();
                }
            }
        } catch (Exception e) {
            if (C.DEBUG)
                e.printStackTrace();
        }
    }

    public void hideDialog() {
        try {

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            if (C.DEBUG)
                e.printStackTrace();
        }
    }


    ProgressDialog progressDialog = null;





    public static boolean isGpsEnabled(Context context) {
        try {
            if (context == null)
                return false;

            LocationManager locationManager;
            locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
            //getting GPS status
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            return isGPSEnabled;

        } catch (Exception e) {
            if (C.DEBUG)
                e.printStackTrace();
            return false;
        }
    }

    public static boolean checkValidReturnDate(String startDate, String endDate) {
        try {
            Date start = new SimpleDateFormat(C.DATE_FORMAT_EVENT, Locale.ENGLISH)
                    .parse(startDate);

            Date end = new SimpleDateFormat(C.DATE_FORMAT_EVENT, Locale.ENGLISH)
                    .parse(endDate);
            if (start.after(end))
                return false;
            else
                return true;

        } catch (ParseException e) {
            if (C.DEBUG)
                e.printStackTrace();
        }
        return true;

    }
    public static boolean checkValidReturnDate(Date startDate, String endDate) {
        try {


            Date end = new SimpleDateFormat(C.DATE_FORMAT_EVENT, Locale.ENGLISH)
                    .parse(endDate);
            if (startDate.after(end))
                return false;
            else
                return true;

        } catch (ParseException e) {
            if (C.DEBUG)
                e.printStackTrace();
        }
        return true;

    }


    public static void showSettingsAlert(final Activity mContext) {
        try {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

            //Setting Dialog Title
            alertDialog.setTitle(R.string.settings);

            //Setting Dialog Message
            alertDialog.setMessage(R.string.on_gps);

            //On Pressing Setting button
            alertDialog.setPositiveButton(R.string.settings, (dialog, which) -> {
                dialog.cancel();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivityForResult(intent,1010);
            });

            alertDialog.show();
        } catch (Exception e) {
            if (C.DEBUG)
                e.printStackTrace();
        }
    }



    @SuppressLint("TrulyRandom")
    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }



    public static float duration = 0f;
}



