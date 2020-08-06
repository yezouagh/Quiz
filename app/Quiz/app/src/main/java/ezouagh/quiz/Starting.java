package ezouagh.quiz;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;

public class Starting extends AppCompatActivity implements View.OnClickListener{

    StoreUserLocaly storeUser;
    Button StartBTN,AcountBTN,SettingsBTN,LogoutBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      try
      {
          assert getSupportActionBar()!=null;
          getSupportActionBar().setDisplayUseLogoEnabled(true);
          LanguageManager.Load_Language(getApplicationContext());
          setContentView(R.layout.activity_starting);
          AdView mAdView1 = (AdView) findViewById(R.id.adView1);
          AdView mAdView2 = (AdView) findViewById(R.id.adView2);
          AdRequest adRequest = new AdRequest.Builder()
                  .build();
          mAdView1.loadAd(adRequest);
          mAdView2.loadAd(adRequest);
          StartBTN= (Button) findViewById(R.id.StartBTN );
          AcountBTN= (Button) findViewById(R.id.AcountBTN );
          SettingsBTN= (Button) findViewById(R.id.SettingsBTN );
          LogoutBTN= (Button) findViewById(R.id.LogOutBTN );
          StartBTN.setOnClickListener(this);
          AcountBTN.setOnClickListener(this);
          SettingsBTN.setOnClickListener(this);
          LogoutBTN.setOnClickListener(this);
          storeUser=new StoreUserLocaly(getApplicationContext());
      }
      catch(Exception e)
      {
          Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
      }
    }

    private void Start()
    {
        try
        {
            if(network.isNetworkAvailable(getApplicationContext()))
            {
                final Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
            else
            {
                Toast.makeText(getApplicationContext(), getString(R.string.connexionLost), Toast.LENGTH_LONG).show();
            }
        }
        catch(Exception e)
        {
            Toast.makeText(Starting.this, getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void Account()
    {
        try
        {
            Intent i= new Intent(getApplicationContext(), Account.class);/////////
            startActivity(i);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }
        catch(Exception e)
        {
            Toast.makeText(Starting.this, getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void Settings()
    {
        try
        {
            Intent i= new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }
        catch(Exception e)
        {
            Toast.makeText(Starting.this, getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void Logout()
    {
        try
        {
            storeUser.SetUserLoggedIn(false);
            storeUser.ClearUserData();
            Intent i= new Intent(Starting.this, Login.class);
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

    @Override
    public void onClick(View v) {
       try
       {
        switch(v.getId())
        {
            case R.id.StartBTN :
                Start();
                break;
            case R.id.AcountBTN :
                Account();
            break;
            case R.id.SettingsBTN :
                Settings();
            break;
            case R.id.LogOutBTN :
                Logout();
                break;
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

}
