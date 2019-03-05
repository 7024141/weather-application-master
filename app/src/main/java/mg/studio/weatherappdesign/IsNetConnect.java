package mg.studio.weatherappdesign;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class IsNetConnect {
    public static boolean isNetworkAvalible(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager == null) return false;
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            //boolean connected = networkInfo.isConnected();
//            if (networkInfo!=null&&connected){
//                if (networkInfo.getState()== NetworkInfo.State.CONNECTED){
//                    return true;
//                }else{
//                    return false;
//                }
//            }
            if (networkInfo == null || !networkInfo.isAvailable()) {
                return false;
            }
            return true;
            //return false;
        }
        else
            return false;
    }

    public static boolean isOnline(){
        URL url;
        try {
            url = new URL("https://www.baidu.com");
            InputStream stream = url.openStream();
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
