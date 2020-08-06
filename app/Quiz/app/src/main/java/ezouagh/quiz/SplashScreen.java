package ezouagh.quiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class SplashScreen extends Activity {

    private StoreUserLocaly storeUser;

    static int splashInterval = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       try
       {
        super.onCreate(savedInstanceState);
           requestWindowFeature(Window.FEATURE_NO_TITLE);
           getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
           overridePendingTransition(R.anim.slide_in, R.anim.abc_slide_out_top);
           LanguageManager.Load_Language(getApplicationContext());
           setContentView(R.layout.activity_splash_screen);
           storeUser= new StoreUserLocaly(this);
           new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
             try
             {
                Intent i;
               if(!storeUser.IsLoggedIn())
               {
                   i= new Intent(SplashScreen.this, Login.class);
               }
               else
               {
                    i = new Intent(getApplicationContext(), Starting.class);
               }
                 startActivity(i);
                 overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_top);
                 this.finish();
                 i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }catch(Exception e)
               {
                   Toast.makeText(getApplicationContext(), getString(R.string.ereur)+e.getMessage(), Toast.LENGTH_LONG).show();
               }
            }
            private void finish() {

            }
        }, splashInterval);
       }catch(Exception e)
       {
           Toast.makeText(getApplicationContext(), getString(R.string.ereur)+e.getMessage(), Toast.LENGTH_LONG).show();
       }
    }

}

