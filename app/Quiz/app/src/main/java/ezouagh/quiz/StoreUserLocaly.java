package ezouagh.quiz;

import android.content.Context;
import android.content.SharedPreferences;

import java.sql.Date;

/**
 * Created by Younes on 01-Jul-15.
 */
public class StoreUserLocaly {

    public  static final String file_name="Details";

    private SharedPreferences UserLocalDataBase;

    public StoreUserLocaly(Context context) {
       try {
           UserLocalDataBase=context.getSharedPreferences(file_name,0);
       }
       catch(Exception e)
       {
         e.printStackTrace();
       }
    }

    public void storeUserData(Users user) {
        try {
        SharedPreferences.Editor editor=UserLocalDataBase.edit();
        editor.putString("FullName",user.getFullName());
        editor.putString("Email", user.getEmail());
        //editor.putString("Pass",user.getPass());
        editor.putString("BirthDay", user.getBirthDay().toString());
        editor.commit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public Users GetLogedInUserData() {
        Users user =new Users();
        try
        {
        String fullname=  UserLocalDataBase.getString("FullName", "");
        String email=  UserLocalDataBase.getString("Email", "");
        String BirthDay=  UserLocalDataBase.getString("BirthDay","1111-11-11");
        //String Pass=  UserLocalDataBase.getString("Pass", "");

        user.setFullName(fullname);
        user.setEmail(email);
        user.setBirthDay(Date.valueOf(BirthDay));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return user;
    }

    public  void SetUserLoggedIn(boolean loggedIn) {
       try
       {
        SharedPreferences.Editor editor=UserLocalDataBase.edit();
        editor.putBoolean("LoggedIn", loggedIn);
        editor.commit();
       }
       catch(Exception e)
       {
           e.printStackTrace();
       }
    }

    public boolean IsLoggedIn() {
      try
      {
        if(UserLocalDataBase.getBoolean("LoggedIn",false)==true)
        {
            return  true;
        }
        else {
            return  false;
        }
      }
      catch(Exception e)
      {
          e.printStackTrace();
      }
        return false;
    }

    public  void ClearUserData() {
       try
       {
        SharedPreferences.Editor editor=UserLocalDataBase.edit();
        editor.clear();
        editor.commit();
       }
       catch(Exception e)
       {
           e.printStackTrace();
       }
    }
}
