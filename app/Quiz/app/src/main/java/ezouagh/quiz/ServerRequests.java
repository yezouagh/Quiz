package ezouagh.quiz;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
/**
 * Created by Younes on 02-Jul-15.
 */
public class ServerRequests {

    ProgressDialog progressDialog;
    private static String url_Connection = "http://challengequiz.ml/ConnectToQuizChallenge/";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_User = "userinfo";
    private static final String TAG_Email = "email";
    private static final String TAG_FullNAME = "fullname";
    private static final String TAG_BD = "birthday";
    private static final String TAG_pass = "pass";
    Context ctxt;

    public ServerRequests(Context context) {
        try
       {
           ctxt=context;
           progressDialog=new ProgressDialog(context);
           progressDialog.setCancelable(false);
           progressDialog.setCanceledOnTouchOutside(false);
           progressDialog.setMessage(ctxt.getString(R.string.Please));
           progressDialog.setTitle(R.string.processing);
       }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void StoreUserDataInBackground(Users user,GetUserCallback userCallback) {
        try
        {
            progressDialog.show();
            new StoreUserDataAsyncTask(user, userCallback).execute();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void FetchUserDataInBackground(Users user,GetUserCallback userCallback) {
       try
       {
           progressDialog.show();
           new FetchUserDataAsyncTask(user, userCallback).execute();
       }catch(Exception e)
       {
           e.printStackTrace();
       }
    }

    public void UpdateUserDataInBackground(Users user,String email,String pass,GetUserCallback
            userCallback) {
        try
        {
            progressDialog.show();
            new UpdateUserDataAsyncTask(user,email,pass, userCallback).execute();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public  class StoreUserDataAsyncTask extends AsyncTask<Void,Void,Users> {

        Users user ;
        GetUserCallback userCallback;

        public StoreUserDataAsyncTask(Users user,GetUserCallback userCallback) {
          try
          {
              this.user=user;
              this.userCallback=userCallback;
          }
          catch(Exception e)
          {
              e.printStackTrace();
          }
        }

        @Override
        protected void onPostExecute(Users aVoid) {
          try{
              progressDialog.dismiss();
              userCallback.Done(aVoid);
              super.onPostExecute(aVoid);
          }catch(Exception e)
          {
              e.printStackTrace();
          }
        }

        @Override
        protected Users doInBackground(Void... args) {
            Users IsSeccuss=new Users();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(TAG_FullNAME,this.user.getFullName()));
            params.add(new BasicNameValuePair(TAG_Email,this.user.getEmail()));
            params.add(new BasicNameValuePair(TAG_BD,this.user.getBirthDay().toString()));
            params.add(new BasicNameValuePair(TAG_pass, this.user.getPass()));
            try {
                JSONObject json = JSONParser.makeHttpRequest(url_Connection+"Register.php", params);
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    IsSeccuss=new Users();
                } else {
                    IsSeccuss=null;
                     }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return IsSeccuss;
        }
    }

    public  class FetchUserDataAsyncTask extends AsyncTask<Void,Void,Users> {

        Users user;
        GetUserCallback userCallback;

        public FetchUserDataAsyncTask(Users user, GetUserCallback userCallback) {
          try{
              this.user = user;
              this.userCallback = userCallback;  }
          catch(Exception e)
          {
             e.printStackTrace();
          }
        }

        @Override
        protected void onPostExecute(Users ReturnedUser) {
           try
           {
               progressDialog.dismiss();
               userCallback.Done(ReturnedUser);
               super.onPostExecute(ReturnedUser);
           }
           catch(Exception e)
           {
               e.printStackTrace();
           }
        }

        @Override
        protected Users doInBackground(Void... args) {

            Users Returnuser=new Users();

                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair(TAG_Email,user.getEmail()));
                        params.add(new BasicNameValuePair(TAG_pass,user.getPass()));

                        JSONObject json = JSONParser.makeHttpRequest(
                                url_Connection+"FetchUserData.php",params);

                        // check your log for json response
                        Log.d("Single User Details", json.toString());

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received user details
                            JSONArray UserObj = json.getJSONArray(TAG_User); // JSON Array
                            // get first user object from JSON Array
                            JSONObject userO = UserObj.getJSONObject(0);

                            String FullName=userO.getString(TAG_FullNAME);
                            String birthday=userO.getString(TAG_BD);
                            String email=userO.getString(TAG_Email);
                            String pass=userO.getString(TAG_pass);
                            Returnuser=new Users(FullName, java.sql.Date.valueOf(birthday),email,pass);

                        } else {
                            Returnuser=null;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            return Returnuser;
        }
    }

    public  class UpdateUserDataAsyncTask extends AsyncTask<Void,Void,Users> {

        Users user;
        GetUserCallback userCallback;
        String email,pass;

        public UpdateUserDataAsyncTask(Users user,String email,String pass, GetUserCallback userCallback) {
            try{
                this.user = user; this.email = email; this.pass = pass;
                this.userCallback = userCallback;  }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(Users ReturnedUser) {
            try
            {
                progressDialog.dismiss();
                userCallback.Done(ReturnedUser);
                super.onPostExecute(ReturnedUser);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        @Override
        protected Users doInBackground(Void... args) {

            Users Returnuser=new Users();

            // Check for success tag
            int success;
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair(TAG_Email,email));
                params.add(new BasicNameValuePair(TAG_pass,pass));
                params.add(new BasicNameValuePair("UP"+TAG_Email,user.getEmail()));
                params.add(new BasicNameValuePair("UP"+TAG_pass,user.getPass()));
                params.add(new BasicNameValuePair("UP"+TAG_BD,user.getBirthDay().toString()));
                params.add(new BasicNameValuePair("UP" + TAG_FullNAME, user.getFullName()));
                JSONObject json = JSONParser.makeHttpRequest(
                        url_Connection+"UpdateUserData.php", params);

                // json success tag
                if(json!=null)
                {
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // successfully received user details
                    JSONArray UserObj = json.getJSONArray(TAG_User); // JSON Array
                    // get first user object from JSON Array
                    JSONObject userO = UserObj.getJSONObject(0);

                    String FullName=userO.getString(TAG_FullNAME);
                    String birthday=userO.getString(TAG_BD);
                    String email=userO.getString(TAG_Email);
                    String pass=userO.getString(TAG_pass);
                    Returnuser=new Users(FullName, java.sql.Date.valueOf(birthday),email,pass);

                } else {
                    Returnuser=null;
                }
                }else {
                    Returnuser=null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return Returnuser;
        }
    }

}
