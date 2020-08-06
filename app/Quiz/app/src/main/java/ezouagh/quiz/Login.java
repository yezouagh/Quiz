package ezouagh.quiz;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class Login extends AppCompatActivity implements View.OnClickListener
{
    private StoreUserLocaly storeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

          try
          {
              storeUser= new StoreUserLocaly(this);
              if(!storeUser.IsLoggedIn())
              {
                  assert getSupportActionBar()!=null;
                  getSupportActionBar().setDisplayShowHomeEnabled(true);
                  getSupportActionBar().setLogo(R.drawable.icon);
                  getSupportActionBar().setDisplayUseLogoEnabled(true);
                  LanguageManager.Load_Language(getApplicationContext());
                  setContentView(R.layout.activity_login);
                  AdView mAdView1 = (AdView) findViewById(R.id.adView1);
                  AdView mAdView2 = (AdView) findViewById(R.id.adView2);
                  AdRequest adRequest = new AdRequest.Builder()
                          .build();
                  mAdView1.loadAd(adRequest);
                  mAdView2.loadAd(adRequest);
                  final Button login=(Button)findViewById(R.id.LoginBTN);
                  login.setOnClickListener(this);
                  final TextView Register=(TextView)findViewById(R.id.RegisterBTN);
                  Register.setOnClickListener(this);
              }
              else
              {
                  Intent i = new Intent(getApplicationContext(), MainActivity.class);
                  overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                  startActivity(i);
                  this.finish();
                  i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
              }
          }
          catch(Exception e)
          {
              Toast.makeText(getApplicationContext(), getString(R.string.ereur)+e.getMessage(), Toast.LENGTH_LONG).show();
          }

    }

    private void loginClick()
    {
      try
      {
          if(network.isNetworkAvailable(getApplicationContext()))

          {
              final EditText email=(EditText)findViewById(R.id.EmailEditText);
              final EditText pass=(EditText)findViewById(R.id.PassEditText);
              final TextView EmailRequired=(TextView)findViewById(R.id.EmailRequired);
              final TextView PassRequired=(TextView)findViewById(R.id.PassRequired);
              final TextView EmailFormat=(TextView)findViewById(R.id.EmailFormat);
              PassRequired.setVisibility(View.GONE);
              EmailRequired.setVisibility(View.GONE);
              EmailFormat.setVisibility(View.GONE);
              if(email.getText().toString().length()==0|| pass.getText().toString().length()<6)
              {
                  if(email.getText().toString().length()==0)
                  {
                      EmailRequired.setVisibility(View.VISIBLE);
                  }

                  if(pass.getText().toString().length()<6)
                  {
                      PassRequired.setVisibility(View.VISIBLE);
                  }
        }
        else
        {
           if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches())
            {
               EmailFormat.setVisibility(View.VISIBLE);
            }
            else
            {
                    Users loggedInUser =new Users();
                    loggedInUser.setEmail(email.getText().toString());
                    loggedInUser.setPass(pass.getText().toString());
                    Authenticate(loggedInUser);
            }
        }
        }
        else
        {
            Toast.makeText(getApplicationContext(),getString(R.string.connexionLost),Toast.LENGTH_LONG).show();
        }
    }catch(Exception e)
    {
        Toast.makeText(getApplicationContext(), getString(R.string.ereur)+e.getMessage(), Toast.LENGTH_LONG).show();
    }
    }

    private void Authenticate(Users user) {
        try
        {
        ServerRequests serverRequests=new ServerRequests(this);
        serverRequests.FetchUserDataInBackground(user, new GetUserCallback() {
            final TextView error = (TextView) findViewById(R.id.Error);

            @Override
            public void Done(Users ReturnedUser) {

                try {
                    if (ReturnedUser == null) {
                        error.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), error.getText() + getString(R.string.sorry), Toast.LENGTH_LONG).show();
                    } else {
                        error.setVisibility(View.GONE);
                        storeUser.storeUserData(ReturnedUser);
                        storeUser.SetUserLoggedIn(true);
                        Toast.makeText(getApplicationContext(), getString(R.string.Loggedin), Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(), Starting.class);
                        overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_top);
                        startActivity(i);
                        finish();
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

    private void RegisterClick() {
        try
        {
            Intent i= new Intent(getApplicationContext(), Register.class);
            overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_top);
            startActivity(i);
            finish();
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), getString(R.string.ereur)+e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onClick(View v) {
      try
      {
        int id=v.getId();
             switch (id)
                {
                   case R.id.LoginBTN:
                        loginClick();
                        break;
                    case R.id.RegisterBTN:
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
       {
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
            case R.id.action_Register:
                RegisterClick();
                break;
            case R.id.action_close:
                overridePendingTransition(R.anim.slide_in_invers, R.anim.slide_out_invers);
                finish();
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

    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.slide_in_invers, R.anim.slide_out_invers);
        super.onBackPressed();
    }

    private void Setting() {
      try
      {
        Intent i= new Intent(Login.this, SettingsActivity.class);
          overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
          startActivity(i);
      }
      catch(Exception e)
      {
          Toast.makeText(getApplicationContext(), getString(R.string.ereur)+e.getMessage(), Toast.LENGTH_LONG).show();
      }
    }
}
