package ezouagh.quiz;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.sql.Date;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout lang;
    private final String TAG_EN="en";
    private final String TAG_FR="fr";
    private final String TAG_AR="ar";
    private Dialog Language_dialog;
    private RadioButton en,fr,ar;
    private TextView langTXT,FullnameTXT,EmailTXT,BirthdayTXT;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LanguageManager.Load_Language(getApplicationContext());
        setContentView(R.layout.settings);
        try {
            AdView mAdView2 = (AdView) findViewById(R.id.adView2);
            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            mAdView2.loadAd(adRequest);
            assert getSupportActionBar()!=null;
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.action_settings);
        lang= (LinearLayout) findViewById(R.id.Language);
        langTXT= (TextView) findViewById(R.id.LanguageTXT);
        FullnameTXT= (TextView) findViewById(R.id.FullnameTXT);
        EmailTXT= (TextView) findViewById(R.id.EmailTXT);
        BirthdayTXT= (TextView) findViewById(R.id.BirthdayTXT);
        setLang();
        lang.setOnClickListener(this);
        Language_dialog=new Dialog(this);
        Language_dialog.setContentView(R.layout.language_dialog);
        Language_dialog.setCanceledOnTouchOutside(true);
        Language_dialog.setTitle(R.string.change_language);
        en= (RadioButton) Language_dialog.findViewById(R.id.English_radioButton);
        fr= (RadioButton) Language_dialog.findViewById(R.id.French_radioButton);
        ar= (RadioButton) Language_dialog.findViewById(R.id.Arabic_radioButton);
        radioGroup= (RadioGroup) Language_dialog.findViewById(R.id.Radio_group);
        Checkradio();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                try
                {
                    String lan = TAG_EN;
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.English_radioButton:
                        lan = TAG_EN;
                        break;
                    case R.id.French_radioButton:
                        lan = TAG_FR;
                        break;
                    case R.id.Arabic_radioButton:
                        lan = TAG_AR;
                        break;
                    default:
                        lan = TAG_EN;
                }
                LanguageManager.Store_and_Set_Language(lan, getApplicationContext());
                Language_dialog.cancel();
            }
            catch(Exception e)
            {
                Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
       });
        Language_dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
             try   {
                 startActivity(getIntent());
                 finish();
             }catch(Exception e)
             {
                 Toast.makeText(getApplicationContext(), getString(R.string.ereur)+e.getMessage(), Toast.LENGTH_LONG).show();
             }
            }
        });
        Language_dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
              try{
                switch (LanguageManager.get_language(getApplicationContext())) {
                    case  TAG_EN:
                        RadioBTN_for_en_fr();
                        radioGroup.check(R.id.English_radioButton);
                        break;
                    case  TAG_AR:
                        radioGroup.check(R.id.Arabic_radioButton);
                        Drawable d=getResources().getDrawable(android.R.drawable.btn_radio);
                        ar.setCompoundDrawables(null, null, d.getCurrent(), null);
                        en.setCompoundDrawables(null, null, d.getCurrent(), null);
                        fr.setCompoundDrawables(null, null, d.getCurrent(), null);
                        break;
                    case  TAG_FR:
                        RadioBTN_for_en_fr();
                        radioGroup.check(R.id.French_radioButton);
                        break;
                    default:
                        RadioBTN_for_en_fr();
                        radioGroup.check(R.id.English_radioButton);
                        break;
                }
            }catch(Exception e)
            {
                Toast.makeText(getApplicationContext(), getString(R.string.ereur)+e.getMessage(), Toast.LENGTH_LONG).show();
            }
            }
        });
       }catch(Exception e)
       {
           Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
       }
    }

    @Override
    public void onClick(View v) {
      try
      {
          Language_click();
      }
      catch(Exception e)
      {
          Toast.makeText(getApplicationContext(), getString(R.string.ereur)+e.getMessage(), Toast.LENGTH_LONG).show();
      }
    }

    public void Language_click() {Language_dialog.show();}

    void setLang()
    {
        try
        {
       switch (LanguageManager.get_language(getApplicationContext())) {
           case  TAG_EN:
               langTXT.setText(R.string.English);
               break;
           case  TAG_AR:
               langTXT.setText(R.string.Arabic);
               break;
           case  TAG_FR:
               langTXT.setText(R.string.French);
               break;
           default:
               langTXT.setText(R.string.English);
               break;
       }
          StoreUserLocaly user =new StoreUserLocaly(getApplicationContext());
          Users _user=user.GetLogedInUserData();
            if(_user.getFullName()!="")
            {
                FullnameTXT.setText(_user.getFullName());
            }
            else
            {
                FullnameTXT.setText(getString(R.string.Unavailable));
            }

            if(_user.getEmail()!="")
              {
                  EmailTXT.setText(_user.getEmail());
              }
              else
              {
                  EmailTXT.setText(getString(R.string.Unavailable));
              }

            if(_user.getBirthDay().compareTo(Date.valueOf("1111-11-11"))!=0)
               {
                   BirthdayTXT.setText(_user.getBirthDay().toString());
               }
           else
              {
                  BirthdayTXT.setText(getString(R.string.Unavailable));
              }
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), getString(R.string.ereur)+e.getMessage(), Toast.LENGTH_LONG).show();
        }
   }

    void RadioBTN_for_en_fr()
    {
        try
        {
        en.setCompoundDrawables(getResources().getDrawable(android.R.drawable.btn_radio),null, null, null);
        ar.setCompoundDrawables(getResources().getDrawable(android.R.drawable.btn_radio),null, null, null);
        fr.setCompoundDrawables(getResources().getDrawable(android.R.drawable.btn_radio),null, null, null);
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), getString(R.string.ereur)+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    void Checkradio(){
       try
       {
        switch (LanguageManager.get_language(getApplicationContext())) {
            case  TAG_EN:
                radioGroup.check(R.id.English_radioButton);
                break;
            case  TAG_AR:
                radioGroup.check(R.id.Arabic_radioButton);
                break;
            case  TAG_FR:
                radioGroup.check(R.id.French_radioButton);
                break;
            default:
                radioGroup.check(R.id.English_radioButton);
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
        finish();
        overridePendingTransition(R.anim.slide_in_invers, R.anim.slide_out_invers);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        try {
            int id = item.getItemId();
            switch (id)
            {
                case android.R.id.home:
                    finish();
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
}
