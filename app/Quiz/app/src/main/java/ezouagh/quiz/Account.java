package ezouagh.quiz;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.sql.Date;

public class Account extends AppCompatActivity implements View.OnClickListener{

////////////////////////Variables////////////////////////////////////////////////
    //Dialogs
    private Dialog FullName_dialog,Email_dialog,Birthday_dialog,Pass_dialog,RequirPass_dialog;
    //Clickables
    Button cancelBN,doneBTN,FullName_dialogok,Email_dialogok,Birthday_dialogok,
            Pass_dialogok, RequirPass_dialogok,
            FullName_dialogcancel,Email_dialogcancel,Birthday_dialogcancel,Pass_dialogcancel,
            RequirPass_dialogcancel;
    LinearLayout changeFullName,changeBirthDay,changeEmail,changePass;
    //Text boxes
    private EditText FullName_dialogTXT,Email_dialogTXT,Pass_dialogTXT,RequirPass_dialogTXT;
    private DatePicker Birthday_dialogTXT;
    //Text views
    private TextView FullnameTXT,EmailTXT,BirthdayTXT,PassTXT,EmailRequired2,EmailFormat2,
    FullNameRequired2,NewpassRequired,PassRequired,error;
    /////////
    boolean datachanged=false;
    private StoreUserLocaly storeUser;
    AlertDialog.Builder confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        LanguageManager.Load_Language(getApplicationContext());
        setContentView(R.layout.activity_account);
        try{
           AdView mAdView1 = (AdView) findViewById(R.id.adView1);
           AdView mAdView2 = (AdView) findViewById(R.id.adView2);
           AdRequest adRequest = new AdRequest.Builder()
                   .build();
           mAdView1.loadAd(adRequest);
           mAdView2.loadAd(adRequest);
           assert getSupportActionBar()!=null;
           getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
           getSupportActionBar().setDisplayHomeAsUpEnabled(true);
           getSupportActionBar().setTitle(R.string.Acount);
           //Prepare clickalbles
           cancelBN= (Button) findViewById(R.id.cancelBN);
           cancelBN.setOnClickListener(this);
           doneBTN= (Button) findViewById(R.id.doneBTN);
           doneBTN.setOnClickListener(this);
           changeFullName= (LinearLayout) findViewById(R.id.changeFullName);
           changeFullName.setOnClickListener(this);
           changeBirthDay= (LinearLayout) findViewById(R.id.changeBirthDay);
           changeBirthDay.setOnClickListener(this);
           changeEmail= (LinearLayout) findViewById(R.id.changeEmail);
           changeEmail.setOnClickListener(this);
           changePass= (LinearLayout) findViewById(R.id.changePass);
           changePass.setOnClickListener(this);

           confirm= new AlertDialog.Builder(this);
           confirm.setTitle(getString(R.string.Confirm_Back_TITLE));
           confirm.setIcon(android.R.drawable.ic_dialog_alert);
           confirm.setMessage(R.string.Confirm_Account_Back);
           confirm.setPositiveButton(getString(R.string.Yes), new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                   onBackPressed();
               }
           });
           confirm.setNegativeButton(getString(R.string.No), null);
           //////////
           ///Prepare local Text Views
           FullnameTXT= (TextView) findViewById(R.id.FullnameTXT);
           EmailTXT= (TextView) findViewById(R.id.EmailTXT);
           BirthdayTXT= (TextView) findViewById(R.id.BirthdayTXT);
           PassTXT= (TextView) findViewById(R.id.PassTXT);
           AffectDataToLABELS();
           PrepareFullName_dialog();
           PrepareEmail_dialog();
           PreparePass_dialog();
           PrepareBirthday_dialog();
           PrepareRequirPass_dialog();

       }catch(Exception e)
       {
           Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
       }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try
        {
            if(keyCode==KeyEvent.KEYCODE_BACK)
            {
                if(datachanged) {
                    confirm.show();
                    return false;
                }
            }
        }catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_invers, R.anim.slide_out_invers);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        try {
            int id = item.getItemId();
            switch (id)
            {
                case android.R.id.home:
                    if(datachanged)
                        confirm.show();
                    else {
                        finish();
                    }
                    overridePendingTransition(R.anim.slide_in_invers, R.anim.slide_out_invers);
                    break;
                case R.id.action_settings:
                    Setting();
                    break;
                case R.id.action_Logout:
                    logout();
                    break;
                case R.id.action_close:
                    if(datachanged)
                        confirm.show();
                    else {
                        finish();
                    }
                    overridePendingTransition(R.anim.slide_in_invers, R.anim.slide_out_invers);
                    break;
                default:return  true;
            }
        }catch(Exception e)
        {
            Toast.makeText(getApplicationContext(),getString(R.string.ereur)+e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v)
    {
        try
        {
            switch(v.getId())
            {
                case R.id.changeFullName :
                    ChangeFullName();
                    break;
                case R.id.changeEmail :
                    ChangeEmail();
                    break;
                case R.id.changeBirthDay :
                    ChangeBirthDay();
                    break;
                case R.id.changePass :
                    ChangePass();
                    break;
                case R.id.doneBTN :
                    RequirPass();
                break;
                case R.id.cancelBN :
                    Cancel();
                break;
            }
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void ChangeFullName()
    {
      try {

          FullName_dialog.show();
          FullName_dialogTXT.setText(FullnameTXT.getText());
          FullName_dialogTXT.selectAll();
      }catch(Exception e)
      {
          Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
      }
    }
    private void ChangeEmail()
    {
        try {
            Email_dialog.show();
            Email_dialogTXT.setText(EmailTXT.getText());
            Email_dialogTXT.selectAll();
        }catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void ChangeBirthDay()
    {
        try {
            Birthday_dialog.show();
           if(BirthdayTXT.getText()!=getString(R.string.Unavailable)) {
               Date bd=Date.valueOf(BirthdayTXT.getText().toString());

               int y=Integer.parseInt(bd.toString().split("-")[0]),
                       m = bd.getMonth(),
                       d=Integer.parseInt(bd.toString().split("-")[2]);
               Birthday_dialogTXT.updateDate(y, m, d);
           }
        }catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void ChangePass()
    {
        try {
            Pass_dialog.show();
        }catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void RequirPass()
    {
        try {
            RequirPass_dialog.show();
        }catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void Cancel()
    {
        try {
             finish();
            overridePendingTransition(R.anim.slide_in_invers, R.anim.slide_out_invers);
        }catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void PrepareFullName_dialog()
    {
        try
        {
            FullName_dialog=new Dialog(this);
            FullName_dialog.setContentView(R.layout.changename_dialog);
            FullName_dialog.setCanceledOnTouchOutside(false);
            FullName_dialog.setTitle(R.string.ChangeFullName);
            FullName_dialogok= (Button) FullName_dialog.findViewById(R.id.ok);
            FullName_dialogcancel=(Button) FullName_dialog.findViewById(R.id.cancel);
            FullName_dialogTXT= (EditText) FullName_dialog.findViewById(R.id.FullNameEditText);
            FullNameRequired2= (TextView) FullName_dialog.findViewById(R.id.FullNameRequired2);
            FullName_dialogok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        FullNameRequired2.setVisibility(View.GONE);
                        if (FullName_dialogTXT.getText().toString().length() != 0) {
                            FullnameTXT.setText(FullName_dialogTXT.getText().toString());
                            FullName_dialog.cancel();
                            datachanged=true;
                        } else {
                            FullNameRequired2.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
            FullName_dialogcancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        FullName_dialog.cancel();FullNameRequired2.setVisibility(View.GONE);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void PrepareEmail_dialog()
    {
        try
        {
            Email_dialog=new Dialog(this);
            Email_dialog.setContentView(R.layout.changeemail_dialog);
            Email_dialog.setCanceledOnTouchOutside(false);
            Email_dialog.setTitle(R.string.ChangeEmail);
            Email_dialogok= (Button) Email_dialog.findViewById(R.id.ok);
            Email_dialogcancel=(Button) Email_dialog.findViewById(R.id.cancel);
            Email_dialogTXT= (EditText) Email_dialog.findViewById(R.id.EmailEditText);
            EmailRequired2= (TextView) Email_dialog.findViewById(R.id.EmailRequired2);
            EmailFormat2= (TextView) Email_dialog.findViewById(R.id.EmailFormat2);
            Email_dialogok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        EmailRequired2.setVisibility(View.GONE);
                        EmailFormat2.setVisibility(View.GONE);
                        if(Email_dialogTXT.getText().toString().length()!=0 )
                        {
                            if(Patterns.EMAIL_ADDRESS.matcher(Email_dialogTXT.getText().toString()).matches())
                             {
                                 EmailTXT.setText(Email_dialogTXT.getText().toString());
                                 Email_dialog.cancel();
                                 datachanged=true;
                             }
                            else
                            {
                                EmailFormat2.setVisibility(View.VISIBLE);
                            }
                        }
                        else
                        {
                            EmailRequired2.setVisibility(View.VISIBLE);
                        }
                    }catch(Exception e)
                    {
                        Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
            Email_dialogcancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Email_dialog.cancel(); EmailRequired2.setVisibility(View.GONE);
                        EmailFormat2.setVisibility(View.GONE);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void PrepareBirthday_dialog()
    {
        try
        {
            Birthday_dialog=new Dialog(this);
            Birthday_dialog.setContentView(R.layout.changebirthday_dialog);
            Birthday_dialog.setCanceledOnTouchOutside(false);
            Birthday_dialog.setTitle(R.string.ChangeBirthday);
            Birthday_dialogok= (Button) Birthday_dialog.findViewById(R.id.ok);
            Birthday_dialogcancel=(Button) Birthday_dialog.findViewById(R.id.cancel);
            Birthday_dialogTXT= (DatePicker) Birthday_dialog.findViewById(R.id.ChangeBirthDay);
            Birthday_dialogok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                            BirthdayTXT.setText(Birthday_dialogTXT.getYear()
                                    + "-" + (Birthday_dialogTXT.getMonth() + 1) + "-" + Birthday_dialogTXT
                                    .getDayOfMonth());
                        Birthday_dialog.cancel();
                        datachanged=true;
                    }catch(Exception e)
                    {
                        Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
            Birthday_dialogcancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Birthday_dialog.cancel();
                    }catch(Exception e)
                    {
                        Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void PreparePass_dialog()
    {
        try
        {
            Pass_dialog=new Dialog(this);
            Pass_dialog.setContentView(R.layout.changepass_dialog);
            Pass_dialog.setCanceledOnTouchOutside(false);
            Pass_dialog.setTitle(R.string.ChangePass);
            Pass_dialogok= (Button) Pass_dialog.findViewById(R.id.ok);
            Pass_dialogcancel=(Button) Pass_dialog.findViewById(R.id.cancel);
            Pass_dialogTXT= (EditText) Pass_dialog.findViewById(R.id.NewPassEditText);
            NewpassRequired= (TextView) Pass_dialog.findViewById(R.id.NewpassRequired);
            Pass_dialogok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        NewpassRequired.setVisibility(View.GONE);
                        if(Pass_dialogTXT.getText().toString().length()>=6)
                        {
                            PassTXT.setText(Pass_dialogTXT.getText().toString());
                            Pass_dialog.cancel();
                            Pass_dialogTXT.setText("");
                            datachanged=true;
                        }
                        else
                        {
                            NewpassRequired.setVisibility(View.VISIBLE);
                        }
                    }catch(Exception e)
                    {
                        Toast.makeText(getApplicationContext(), getString(R.string.ereur) +
                                e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
            Pass_dialogcancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Pass_dialog.cancel();
                        Pass_dialogTXT.setText("");NewpassRequired.setVisibility(View.GONE);
                    }catch(Exception e)
                    {
                        Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }
    private void PrepareRequirPass_dialog()
    {
        try
        {
            RequirPass_dialog=new Dialog(this);
            RequirPass_dialog.setContentView(R.layout.requirepass_dialog);
            RequirPass_dialog.setCanceledOnTouchOutside(false);
            RequirPass_dialog.setTitle(R.string.PlaesePass);
            RequirPass_dialogok= (Button) RequirPass_dialog.findViewById(R.id.ok);
            RequirPass_dialogcancel=(Button) RequirPass_dialog.findViewById(R.id.cancel);
            RequirPass_dialogTXT= (EditText) RequirPass_dialog.findViewById(R.id.ReqPassEditText);
            PassRequired= (TextView) RequirPass_dialog.findViewById(R.id.PassRequired);
            error = (TextView)RequirPass_dialog.findViewById(R.id.PassIncorrect);
            RequirPass_dialogok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        PassRequired.setVisibility(View.GONE);
                        if(network.isNetworkAvailable(getApplicationContext()))
                        {
                          String  requirepass=RequirPass_dialogTXT.getText().toString();
                        if(requirepass.length()>=6)
                        {
                            ReAuthenticate(requirepass);
                        }
                        else
                        {
                            PassRequired.setVisibility(View.VISIBLE);
                        }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),getString(R.string.connexionLost),Toast.LENGTH_LONG).show();
                        }
                    }catch(Exception e)
                    {
                        Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
            RequirPass_dialogcancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        RequirPass_dialog.cancel();
                        RequirPass_dialogTXT.setText("");PassRequired.setVisibility(View.GONE);
                        error.setVisibility(View.GONE);
                    }catch(Exception e)
                    {
                        Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void AffectDataToLABELS()
    {
        storeUser =new StoreUserLocaly(getApplicationContext());
        Users _user=storeUser.GetLogedInUserData();
        if(_user.getFullName().toString().length()!=0)
        {
            FullnameTXT.setText(_user.getFullName());
        }
        else
        {
            FullnameTXT.setText(getString(R.string.Unavailable));
        }

        if(_user.getEmail().toString().length()!=0)
        {
            EmailTXT.setText(_user.getEmail());
        }
        else
        {
            EmailTXT.setText(getString(R.string.Unavailable));
        }

        if (_user.getBirthDay().compareTo(Date.valueOf("1111-11-11"))!=0)
        {
            BirthdayTXT.setText(_user.getBirthDay().toString());
        }
        else
        {
            BirthdayTXT.setText(getString(R.string.Unavailable));
        }
    }

    private void ReAuthenticate(String pass)
    {
        try
        {
            ServerRequests serverRequests=new ServerRequests(this);
            String em= storeUser.GetLogedInUserData().getEmail();
            String pass2=PassTXT.getText().toString();
            if(PassTXT.getText().toString()==getString(R.string.Pass))
            {
                pass2=pass;
            }
            String email=EmailTXT.getText().toString();
            Date BD;
            if(BirthdayTXT.getText().toString()==getString(R.string.Unavailable))
            {
                BD=Date.valueOf("1111-11-11");
            }
            else
            {
                BD=Date.valueOf(BirthdayTXT.getText().toString());
            }
            String Fn=FullnameTXT.getText().toString();
            final  Users updateUser=new Users(Fn,BD,email,pass2);
            serverRequests.UpdateUserDataInBackground(updateUser,em,pass, new GetUserCallback() {

                @Override
                public void Done(Users ReturnedUser) {
                    try {
                        if (ReturnedUser == null) {
                            error.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(), error.getText() + getString(R.string.sorry), Toast.LENGTH_LONG).show();
                        } else {
                            error.setVisibility(View.GONE);
                            storeUser.storeUserData(updateUser);
                            Toast.makeText(getApplicationContext(), getString(R.string.Updated), Toast.LENGTH_LONG).show();
                            RequirPass_dialog.cancel();
                            RequirPass_dialogTXT.setText("");
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), getString(R.string.ereur)+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void Setting()
    {
        try
        {
            Intent i= new Intent(Account.this, SettingsActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), getString(R.string.ereur)+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void logout()
    {
        try
        {
            StoreUserLocaly user =new StoreUserLocaly(getApplicationContext());
            user.SetUserLoggedIn(false);
            user.ClearUserData();
            Intent i = new Intent(Account.this, Login.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_invers, R.anim.slide_out_invers);
            finish();
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
