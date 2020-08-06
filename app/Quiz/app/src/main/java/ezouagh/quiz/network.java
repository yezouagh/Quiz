package ezouagh.quiz;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Younes on 04-Jul-15.
 */
public class network {

    public static boolean isConnectedWifi(Context context){
       try
       {
        ConnectivityManager connectivity = (ConnectivityManager) context
            .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivity.getActiveNetworkInfo();

        if (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI){
            return true;
        }
       }
       catch(Exception e)
       {
           e.printStackTrace();
       }
        return false;
    }

    public static boolean isConnectedMobile(Context context){
     try
     {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivity.getActiveNetworkInfo();
        if(info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE){
            return true;
        }
     }
     catch(Exception e)
     {
         e.printStackTrace();
     }
        return false;
    }

    public static boolean isNetworkAvailable(Context context) {
     try
     {
         ConnectivityManager cm = (ConnectivityManager)
                 context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true ;
        }
     }
     catch(Exception e)
     {
         e.printStackTrace();
     }
        return false;
    }

}
