package ezouagh.quiz;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by Younes on 09-Jul-15.
 */
public class LanguageManager {

    private static String file_name="Details";
    static String TAG_DEFAULT="en";
    private static String TAG_Language="Language";
    private static SharedPreferences UserLocalDataBase;

    public static void Load_Language(Context c)
    {
      try {
          String lan = get_language(c);
          Configuration config = new Configuration();
          Locale local = new Locale(lan);
          Locale.setDefault(local);
          config.locale = local;
          c.getResources().updateConfiguration(config, c.getResources()
                  .getDisplayMetrics());
      }
      catch(Exception e)
      {
         e.printStackTrace();
      }
    }

    public static void Store_and_Set_Language(String Language,Context c)
    {
        try
        {
        UserLocalDataBase=c.getSharedPreferences(file_name, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor=UserLocalDataBase.edit();
        editor.putString(TAG_Language, Language);
        editor.commit();
        Load_Language(c);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public  static String get_language(Context c)
    {
        try
        {
        UserLocalDataBase=c.getSharedPreferences(file_name, Activity.MODE_PRIVATE);
        return UserLocalDataBase.getString(TAG_Language,TAG_DEFAULT );
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return TAG_DEFAULT;
    }
}
