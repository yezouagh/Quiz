package ezouagh.quiz;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    private StoreUserLocaly storeUser;
    private ThemesAdapter listAdapter;
    private ExpandableListView expListView;
    private List<String> listDataHeader = new ArrayList<>();
    private HashMap<String, List<NameValuePair>> listDataChild = new HashMap<String,List<NameValuePair>>();
    private ScrollView scrollView;
    private ListView listView;
    private ArrayAdapter<String> selectedThemesArrayAdapter;
    private ArrayList<String> selectedThemes;
    private int Available=0;
    final String Number_of_questions = "Number_of_questions";
    TextView AvailableTXT;
    Button Start;
    EditText numberOfQuestionsEditText;
    List<NameValuePair> Themes = null;

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        expListView.collapseGroup(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            storeUser= new StoreUserLocaly(this);
            if(storeUser.IsLoggedIn()) {

                assert  getSupportActionBar()!=null;
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setLogo(R.drawable.notification);
                getSupportActionBar().setDisplayUseLogoEnabled(true);
                LanguageManager.Load_Language(getApplicationContext());
                progressDialog=new ProgressDialog(MainActivity.this);
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setMessage(getString(R.string.Please));
                progressDialog.setTitle(R.string.processing);
                setContentView(R.layout.activity_main);
                AdView mAdView1 = (AdView) findViewById(R.id.adView1);
                AdView mAdView2 = (AdView) findViewById(R.id.adView2);
                AdRequest adRequest = new AdRequest.Builder()
                        .build();
                mAdView1.loadAd(adRequest);
                mAdView2.loadAd(adRequest);
                //Instantiating variables
                expListView = (ExpandableListView) findViewById(R.id.Themes);
                Start = (Button) findViewById(R.id.Go);
                prepareListData();
                AvailableTXT = (TextView) findViewById(R.id.numberOfQuestionsAvailableEditText);
                AvailableTXT.setText(getString(R.string.available) + "  " + Available);
                listView = (ListView) findViewById(R.id.listView);
                scrollView = (ScrollView) findViewById(R.id.scrollView);
                numberOfQuestionsEditText = (EditText) findViewById(R.id.numberOfQuestionsEditText);
                selectedThemes = new ArrayList<String>();
                selectedThemes.add(getString(R.string.None));
                // setting list adapter for listView
                selectedThemesArrayAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout
                        .costum_list_item,
                        R.id.itemlbl, selectedThemes);
                listView.setAdapter(selectedThemesArrayAdapter);
                // preparing list data
                expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                    @Override
                    public void onGroupExpand(int groupPosition) {
                        //invalidate form ScrollBar
                        try {
                            LinearLayout.LayoutParams par = (LinearLayout.LayoutParams) expListView.getLayoutParams();
                            int c = listAdapter.getChildrenCount(groupPosition) + 1;
                            int h = expListView.getHeight();
                            par.height = (c * h);
                            expListView.setLayoutParams(par);
                            expListView.refreshDrawableState();
                            scrollView.refreshDrawableState();
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

                expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

                    @Override
                    public void onGroupCollapse(int groupPosition) {
                        //invalidate form ScrollBar for expListView
                        try {
                            LinearLayout.LayoutParams par = (LinearLayout.LayoutParams) expListView.getLayoutParams();
                            par.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                            expListView.setLayoutParams(par);
                            expListView.refreshDrawableState();
                            //invalidate form ScrollBar for listView
                            scrollView.refreshDrawableState();
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

                expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v,
                                                int groupPosition, int childPosition, long id) {

                        try {

                            CheckBox checkBox = (CheckBox) v.findViewById(R.id.CheckBoxListItem);
                            checkBox.setChecked(!checkBox.isChecked());
                            TextView txt = (TextView) v.findViewById(R.id.lblListItem);
                            int nbr = Integer.valueOf(txt.getTag().toString());
                            if (checkBox.isChecked()) {
                                selectedThemes.remove(getString(R.string
                                        .None));
                                selectedThemes.add(listDataChild.get(
                                        listDataHeader.get(groupPosition)).get(
                                        childPosition).getName());
                                Available += nbr;
                            } else {
                                selectedThemes.remove(listDataChild.get(
                                        listDataHeader.get(groupPosition)).get(
                                        childPosition).getName());
                                if (selectedThemes.size() == 0) {
                                    selectedThemes.add(getString(R.string.None));
                                }
                                Available -= nbr;
                            }
                            selectedThemesArrayAdapter.notifyDataSetChanged();
                            LinearLayout.LayoutParams parms = (LinearLayout.LayoutParams) listView.getLayoutParams();
                            int count = selectedThemes.size();
                            int heigh = listView.getChildAt(groupPosition).getHeight();
                            parms.height = (count * heigh);
                            listView.setLayoutParams(parms);
                            scrollView.refreshDrawableState();
                            AvailableTXT.setText(getString(R.string.available) + "  " + Available);
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        return false;
                    }
                });

                Start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (network.isNetworkAvailable(getApplicationContext()))
                        {
                            if (selectedThemes.get(0) != getString(R.string.None))
                            {
                                if (numberOfQuestionsEditText.getText().length() > 0)
                                {
                                    int count = Integer.parseInt(numberOfQuestionsEditText.getText()
                                            .toString());
                                    if (count > 0 && count <= Available)
                                    {
                                        String ThemesDesQ = " ThemeName='" + selectedThemes.get(0) + "'";
                                        if (selectedThemes.size() > 1)
                                        {
                                            for (int i = 1; i < selectedThemes.size(); i++)
                                            {
                                                ThemesDesQ += " OR ThemeName='" + selectedThemes.get(i) + "'";
                                            }
                                        }
                                        Intent i = new Intent(getApplicationContext(), quiz_challenge.class);
                                        SharedPreferences sp = PreferenceManager
                                                .getDefaultSharedPreferences(getApplicationContext());
                                        sp.edit().putInt(Number_of_questions, count).commit();
                                        i.putExtra("Themes", ThemesDesQ);
                                        startActivity(i);
                                        overridePendingTransition(R.anim.slide_in_invers, R.anim
                                                .slide_out_invers);
                                        finish();
                                    } else
                                        Toast.makeText(getApplicationContext(), getString(R.string.CountLimit), Toast.LENGTH_LONG).show();
                                } else
                                    Toast.makeText(getApplicationContext(), getString(R.string.CountRequired), Toast.LENGTH_LONG).show();
                            }
                            else
                                Toast.makeText(getApplicationContext(), getString(R.string.ChoozThemes), Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), getString(R.string.connexionLost), Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
                else
                {
                    try
                    {
                        Intent i = new Intent(getApplicationContext(), Login.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_invers, R.anim.slide_out_invers);
                       this.finish();
                    }
                    catch(Exception e)
                    {
                        Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_invers, R.anim.slide_out_invers);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       try
       {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
       }
       catch(Exception e)
       {
           Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
       }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

      try {
        int id = item.getItemId();
        switch (id)
        {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.slide_in_invers, R.anim.slide_out_invers);
                break;
            case R.id.action_setting:
               Setting();
                break;
            case R.id.action_Logout:
                logout();
                break;
            case R.id.action_close:
                finish();
                overridePendingTransition(R.anim.slide_in_invers, R.anim.slide_out_invers);
                break;
            default:return  true;
        }
      }catch(Exception e)
      {
          Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
      }
        return super.onOptionsItemSelected(item);
    }

    private void Setting() {
       try
       {
        Intent i= new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(i);
           overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
       }
       catch(Exception e)
       {
           Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
       }
    }

    private void logout() {
        try
        {
            storeUser.SetUserLoggedIn(false);
            storeUser.ClearUserData();
            Intent i= new Intent(MainActivity.this, Login.class);
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

    private void prepareListData() {
        try
        {
            listDataHeader.add(getString(R.string.Themes));
            if (network.isNetworkAvailable(getApplicationContext())) {
                progressDialog.show();
                new FetchThemesAsyncTask().execute();
            }
            else {
                AlertDialog.Builder Alert = new AlertDialog.Builder(MainActivity.this);
                Alert.setMessage(getString(R.string.connexionLost));
                Alert.setCancelable(false);
                Alert.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                Alert.show();
            }
        }catch(Exception e)
        {
            Toast.makeText(MainActivity.this, getString(R.string.ereur) + "  " + e.getStackTrace()[0]
                    + e
                    .getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public  class FetchThemesAsyncTask extends AsyncTask<Void,Void,Void> {
        public FetchThemesAsyncTask(){
        }

        @Override
        protected void onPostExecute( Void v) {
            try
            {
                if(Themes.size()==0)
                  {
                      AlertDialog.Builder Alert = new AlertDialog.Builder(MainActivity.this);
                      Alert.setMessage(getString(R.string.server_error));
                      Alert.setCancelable(false);
                      Alert.setPositiveButton(getString(R.string.ok), new DialogInterface
                         .OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int which) {
                             finish();
                          }
                      });
                      Alert.show();
                  }
                listDataChild.put(listDataHeader.get(0), Themes);
                listAdapter = new ThemesAdapter(MainActivity.this, listDataHeader, listDataChild);
                // setting list adapter for expListView
                expListView.setAdapter(listAdapter);
                progressDialog.dismiss();

                super.onPostExecute(v);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... args) {
            int success;
            String url_Connection = "http://challengequiz.ml/ConnectToQuizChallenge/" ;
            String TAG_NBR_Q_T ="NBR_Q_T" ;
            String TAG_THEMES ="Themes" ;
            String TAG_Lang ="Lang" ;
            String TAG_SUCCESS = "success";
            String TAG_THEMENAME ="ThemeName" ;
            try {
                Themes=new ArrayList<>();
                List<NameValuePair> params = new ArrayList<>();
                String language=LanguageManager.get_language(getApplicationContext());
                params.add(new BasicNameValuePair(TAG_Lang,language));
                JSONObject json = JSONParser.makeHttpRequest(
                        url_Connection + "FetchThemes.php", params);
                if(json!=null)
                {
                    success = json.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        JSONArray ThemesObj = json.getJSONArray(TAG_THEMES);
                        for (int i = 0; i < ThemesObj.length(); i++) {
                            JSONObject c =ThemesObj.getJSONObject(i);
                            String Themename = c.getString(TAG_THEMENAME);
                            String NBR=c.getString(TAG_NBR_Q_T);
                            Themes.add(new BasicNameValuePair(Themename,NBR));
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
