package ezouagh.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.sql.Date;

public class Register extends AppCompatActivity implements View.OnClickListener{
    private EditText Fullname;
    private EditText email;
    private EditText pass;
    private TextView FullNameRequired;
    private TextView EmailRequired;
    private TextView PassRequired;
    private TextView EmailFormat;
    private DatePicker  BirthDay;
    StoreUserLocaly storeUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       try
       {
           storeUser= new StoreUserLocaly(this);
           if(!storeUser.IsLoggedIn()) {
               assert getSupportActionBar()!=null;
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setLogo(R.drawable.icon);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
               LanguageManager.Load_Language(getApplicationContext());
            setContentView(R.layout.activity_register);
               AdView mAdView1 = (AdView) findViewById(R.id.adView1);
               AdView mAdView2 = (AdView) findViewById(R.id.adView2);
               AdRequest adRequest = new AdRequest.Builder()
                       .build();
               mAdView1.loadAd(adRequest);
               mAdView2.loadAd(adRequest);
            final TextView loginBtn = (TextView) findViewById(R.id.BTNLogin);
            loginBtn.setOnClickListener(this);
            final Button RegisterBtn = (Button) findViewById(R.id.BTNRegister);
            RegisterBtn.setOnClickListener(this);
            Fullname = (EditText) findViewById(R.id.FullNameEditText);
            email = (EditText) findViewById(R.id.EditTextEmail);
            pass = (EditText) findViewById(R.id.EditTextPass);
            FullNameRequired = (TextView) findViewById(R.id.FullNameRequired);
            EmailRequired = (TextView) findViewById(R.id.RequiredEmail);
            PassRequired = (TextView) findViewById(R.id.RequiredPass);
            EmailFormat = (TextView) findViewById(R.id.FormatEmail);
            BirthDay = (DatePicker) findViewById(R.id.BirthDay);
        }
        else
        {
            Intent i = new Intent(getApplicationContext(), Starting.class);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            startActivity(i);
            this.finish();
        }
       }
       catch(Exception e)
       {
           Toast.makeText(getApplicationContext(), getString(R.string.ereur)+e.getMessage(), Toast.LENGTH_LONG).show();
       }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_invers, R.anim.slide_out_invers);
    }

    private void RegisterClick() {
     try {

         if(network.isNetworkAvailable(getApplicationContext()))
         {
             PassRequired.setVisibility(View.GONE);
             EmailRequired.setVisibility(View.GONE);
             EmailFormat.setVisibility(View.GONE);
             FullNameRequired.setVisibility(View.GONE);

             if (email.getText().toString().length() < 6 || pass.getText().toString().length() ==
                     0 || Fullname.getText().toString().length() == 0) {
                 if (email.getText().toString().length() == 0) {
                     EmailRequired.setVisibility(View.VISIBLE);
                 }
            if (pass.getText().toString().length() <6) {
                PassRequired.setVisibility(View.VISIBLE);
            }
            if (Fullname.getText().toString().length() == 0)
            {
                FullNameRequired.setVisibility(View.VISIBLE);
            }
        }
        else {
           if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches())
            {
              EmailFormat.setVisibility(View.VISIBLE);
            }
            else
            {
            Users Registered = new Users();
            Registered.setFullName(Fullname.getText().toString());
            Registered.setBirthDay(Date.valueOf(BirthDay.getYear() + "-" + (BirthDay.getMonth()+1) +
                    "-" + BirthDay.getDayOfMonth()));
            Registered.setEmail(email.getText().toString());
            Registered.setPass(pass.getText().toString());
            ServerRequests serverRequests = new ServerRequests(this);
            serverRequests.StoreUserDataInBackground(Registered, new GetUserCallback() {
                @Override
                public void Done(Users ReturnedUser) {
                  try
                  {
                    if(ReturnedUser==null)
                    {
                        Toast.makeText(getApplicationContext(),getString(R.string.RegistrationFailed), Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),getString(R.string.congrats), Toast.LENGTH_LONG).show();
                        loginClick();
                    }
                  }
                  catch(Exception e)
                  {
                      Toast.makeText(getApplicationContext(), getString(R.string.ereur)+e.getMessage(), Toast.LENGTH_LONG).show();
                  }
                }
            });
            }
        }
         } else
        {
            Toast.makeText(getApplicationContext(), getString(R.string.connexionLost), Toast.LENGTH_LONG).show();
        }
    }
     catch(Exception e)
    {
        Toast.makeText(getApplicationContext(), getString(R.string.ereur)+e.getMessage(), Toast.LENGTH_LONG).show();
    }
    }

    private void loginClick() {
        try
        {
            Intent i= new Intent(Register.this, Login.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            finish();
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        catch(Exception e)
            {
                Toast.makeText(getApplicationContext(), getString(R.string.ereur)+e.getMessage(), Toast.LENGTH_LONG).show();
            }
    }

    private void Setting() {
        try
        {
        Intent i= new Intent(Register.this, SettingsActivity.class);
        startActivity(i);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), getString(R.string.ereur)+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
      try
      {
        switch (view.getId())
        {
            case R.id.BTNLogin:
                loginClick();
                break;
            case R.id.BTNRegister:
                RegisterClick();
                break;
        }
      }
      catch(Exception e)
      {
          Toast.makeText(getApplicationContext(), getString(R.string.ereur)+e.getMessage(), Toast.LENGTH_LONG).show();
      }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      try
      {// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
      }
      catch(Exception e)
      {
          Toast.makeText(getApplicationContext(), getString(R.string.ereur)+e.getMessage(), Toast.LENGTH_LONG).show();
      }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try
        {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.action_setting:
                Setting();
                break;
            case R.id.action_Login:
                loginClick();
                break;
            case R.id.action_close:
                finish();
                overridePendingTransition(R.anim.slide_in_invers, R.anim.slide_out_invers);
                break;
            default:return  true;
        }
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), getString(R.string.ereur)+e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
