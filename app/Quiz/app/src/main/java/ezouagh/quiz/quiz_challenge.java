package ezouagh.quiz;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class quiz_challenge extends AppCompatActivity
{
    ProgressDialog progressDialog;
    int count,secondsLeft;
    final String TAG_THEMES ="Themes" ;
    final String TAG_Score ="Score" ;
    final String TAG_SUCCESS = "success";
    final String TAG_Email = "email";
    final String url_Connection = "http://challengequiz.ml/ConnectToQuizChallenge/";
    final String TAG_Lang ="Lang" ;
    final String Tag_Dif ="Dif" ;
    final String TAG_Count ="Count" ;
    final String TAG_QuestionAndAnswers ="QuestionAndAnswers" ;
    final String TAG_Question ="Question" ;
    final String TAG_CorrectAnswer ="CorrectAnswer" ;
    final String TAG_WrongAnswer1 ="WrongAnswer1" ;
    final String TAG_WrongAnswer2 ="WrongAnswer2" ;
    final String TAG_WrongAnswer3 ="WrongAnswer3" ;
    final String TAG_Qid ="Qid";
    private static final String Number_of_questions = "Number_of_questions";
    MenuItem timer;
    CharSequence mTitle;
    AlertDialog.Builder confirm;
    private  static boolean isFinished=false,destroyed=true;
    private  static List<QuestionAndAnswers> QuestionsAndAnswers;
    private  static CountDownTimer countDownTimer;
    private  static String lang;
    private  static NavigationDrawerFragment mNavigationDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       try {
        super.onCreate(savedInstanceState);
        ////Initiate Static var
           isFinished=false;
           destroyed=true;
           count=0;secondsLeft=0;
           QuestionsAndAnswers=new ArrayList<>();
           progressDialog=new ProgressDialog(quiz_challenge.this);
           progressDialog.setCancelable(false);
           progressDialog.setCanceledOnTouchOutside(false);
           progressDialog.setMessage(getString(R.string.Please));
           progressDialog.setTitle(R.string.processing);
           setContentView(R.layout.activity_quiz_challenge);
           progressDialog.show();
           SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
           count=sp.getInt(Number_of_questions, 0);
           secondsLeft=((count*180)/100)*8000;
           countDownTimer=new CountDownTimer(secondsLeft,1000) {
               @Override
               public void onTick(long millisUntilFinished) {
                try
                {
                Date timeLeft= new Date(millisUntilFinished);
                if(timer!=null)
                    timer.setTitle(timeLeft.getHours()+"h : "+timeLeft.getMinutes()
                            +"m : "+timeLeft.getSeconds()+"s ");

                secondsLeft=timeLeft.getSeconds();
                }catch(Exception e)
                {
                    Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFinish() {
                try
                {
                if(!isFinished) {
                    timer.setTitle(getString(R.string.time_up));
                    Toast.makeText(quiz_challenge.this,getString(R.string.time_up), Toast.LENGTH_SHORT).show();
                }
                progressDialog.show();
                countDownTimer.cancel();
                new UpdateUserScoreAsyncTask().execute();
                destroyed=false;
                }catch(Exception e)
                {
                    Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        };
        new FetchQuestionsAsyncTask().execute();

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout), new NavigationDrawerFragment
                        .NavigationDrawerCallbacks() {
                    @Override
                    public void onNavigationDrawerItemSelected(int position) {
                       try {
                           if (QuestionsAndAnswers.size() > 0) {
                               FragmentManager fragmentManager =
                                       getSupportFragmentManager();
                               fragmentManager.beginTransaction()
                                       .replace(R.id.container, PlaceholderFragment.newInstance
                                               (position + 1))
                                       .commit();
                               mTitle = getString(R.string.qs)+" "+(position + 1);
                               assert getSupportActionBar()!=null;
                               getSupportActionBar().setTitle(mTitle);
                           }
                       }
                       catch(Exception e)
                          {
                           Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e
                                 .getMessage(), Toast.LENGTH_LONG).show();
                          }
                }
                });
        confirm= new AlertDialog.Builder(this);
        confirm.setTitle(getString(R.string.Confirm_Back_TITLE));
        confirm.setIcon(android.R.drawable.ic_dialog_alert);
        confirm.setMessage(getString(R.string.Confirm_Quiz_Back));
        confirm.setPositiveButton(getString(R.string.Yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               try
               {
                onBackPressed();
               }
               catch(Exception e)
               {
                   Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
               }
            }
        });
        confirm.setNegativeButton(getString(R.string.No), null);
      }catch(Exception e)
      {
          Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG)
                 .show();
      }
    }
    @Override
    protected void onDestroy() {
       try {
           if(destroyed)
           {
           Toast.makeText(getApplicationContext(),getString(R.string.lostScore)+getString(R.string
                        .sorry),
                   Toast.LENGTH_LONG).show();
           }
           getIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
           overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
           countDownTimer.cancel();
           super.onDestroy();
           finish();
       }catch(Exception e)
       {
           Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
       }
    }

    @Override
    public void onBackPressed() {
        try
        {
        countDownTimer.cancel();
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_invers, R.anim.slide_out_invers);
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
            confirm.show();
            return false;
        }
    }catch(Exception e)
    {
        Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
    }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
           if (!mNavigationDrawerFragment.isDrawerOpen()) {
               getMenuInflater().inflate(R.menu.quiz_challenge, menu);
               timer= menu.getItem(0);
               restoreActionBar();
               return true;
           }
        }catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try
        {
          int id = item.getItemId();
          if (id == R.id.TimeLeft)
          {
              return false;
          }
        }catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onSectionAttached(int number) {
       try
       {
        mTitle = getString(R.string.qs)+" "+number;
       }catch(Exception e)
       {
           Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
       }
    }

    public void restoreActionBar() {
       try {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar!=null;
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
       }catch(Exception e)
       {
           Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
       }
    }

    int getNBIncompleted()
    {
        int nb=0;
        try {
             for (int i = 0; i <QuestionsAndAnswers.size() ; i++) {
                 if(!QuestionsAndAnswers.get(i).isAnswered())
                 {
                     nb++;
                 }
             }
        }catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), getString(R.string.ereur) + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return nb;
    }

    public static class PlaceholderFragment extends Fragment{

        private static final String ARG_SECTION_NUMBER = "section_number";
        static int section=0;
        TextView QuestionTXT,ReviewtextView;
        RadioButton AnswerATXT,AnswerBTXT,AnswerCTXT,
                AnswerDTXT;
        RadioGroup radioGroup;
        Button ButtonFinish;
        CheckBox ReviewcheckBox;
        ImageButton ButtonNext,ButtonPrev;
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            section=sectionNumber-1;
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_quiz_challenge, container, false);
            try
            {
            final QuestionAndAnswers questionAndAnswer=QuestionsAndAnswers.get(section);
            QuestionTXT= (TextView) rootView.findViewById(R.id.QuestionTXT);
            ReviewtextView= (TextView) rootView.findViewById(R.id.ReviewtextView);
            ButtonNext= (ImageButton) rootView.findViewById(R.id.ButtonNext);
            ButtonPrev= (ImageButton) rootView.findViewById(R.id.ButtonPrev);
            AnswerATXT= (RadioButton) rootView.findViewById(R.id.AnswerATXT);
            AnswerBTXT= (RadioButton) rootView.findViewById(R.id.AnswerBTXT);
            AnswerCTXT= (RadioButton) rootView.findViewById(R.id.AnswerCTXT);
            AnswerDTXT= (RadioButton) rootView.findViewById(R.id.AnswerDTXT);
            ButtonFinish= (Button) rootView.findViewById(R.id.ButtonFinish);
            ReviewcheckBox= (CheckBox) rootView.findViewById(R.id.ReviewcheckBox);
            radioGroup= (RadioGroup) rootView.findViewById(R.id.radioGroup);
            QuestionTXT.setText(questionAndAnswer.getQuestion());
            switch (questionAndAnswer.getCorrectAnswerTxtId())
            {
                case R.id.AnswerATXT:
                    AnswerATXT.setText(getString(R.string.A)+" "+ questionAndAnswer
                            .getCorrectAnswer());
                    break;
                case R.id.AnswerBTXT:
                    AnswerBTXT.setText(getString(R.string.B)+" "+questionAndAnswer
                            .getCorrectAnswer());
                    break;
                case R.id.AnswerCTXT:
                    AnswerCTXT.setText(getString(R.string.C)+" "+questionAndAnswer
                            .getCorrectAnswer());
                    break;
                case R.id.AnswerDTXT:
                    AnswerDTXT.setText(getString(R.string.D)+" "+questionAndAnswer
                            .getCorrectAnswer());
                    break;
                default:
                    AnswerBTXT.setText(getString(R.string.B)+" "+questionAndAnswer
                            .getCorrectAnswer());
                    break;
            }
            switch (questionAndAnswer.getWrongAnswer1TxtId())
            {
                case R.id.AnswerATXT:
                    AnswerATXT.setText(getString(R.string.A)+" "+ questionAndAnswer
                            .getWrongAnswer1());
                    break;
                case R.id.AnswerBTXT:
                    AnswerBTXT.setText(getString(R.string.B)+" "+questionAndAnswer
                            .getWrongAnswer1());
                    break;
                case R.id.AnswerCTXT:
                    AnswerCTXT.setText(getString(R.string.C)+" "+questionAndAnswer
                            .getWrongAnswer1());
                    break;
                case R.id.AnswerDTXT:
                    AnswerDTXT.setText(getString(R.string.D)+" "+questionAndAnswer
                            .getWrongAnswer1());
                    break;
                default:
                    AnswerDTXT.setText(getString(R.string.D)+" "+questionAndAnswer
                            .getWrongAnswer1());
                    break;
            }
            switch (questionAndAnswer.getWrongAnswer2TxtId())
            {
                case R.id.AnswerATXT:
                AnswerATXT.setText(getString(R.string.A)+" "+ questionAndAnswer
                        .getWrongAnswer2());
                break;
                case R.id.AnswerBTXT:
                    AnswerBTXT.setText(getString(R.string.B)+" "+questionAndAnswer
                            .getWrongAnswer2());
                    break;
                case R.id.AnswerCTXT:
                    AnswerCTXT.setText(getString(R.string.C)+" "+questionAndAnswer
                            .getWrongAnswer2());
                    break;
                case R.id.AnswerDTXT:
                    AnswerDTXT.setText(getString(R.string.D)+" "+questionAndAnswer
                            .getWrongAnswer2());
                    break;
                default:
                    AnswerCTXT.setText(getString(R.string.C)+" "+questionAndAnswer
                            .getWrongAnswer2());
                    break;
            }
            switch (questionAndAnswer.getWrongAnswer3TxtId())
            {
                case R.id.AnswerATXT:
                    AnswerATXT.setText(getString(R.string.A)+" "+ questionAndAnswer
                            .getWrongAnswer3());
                    break;
                case R.id.AnswerBTXT:
                    AnswerBTXT.setText(getString(R.string.B)+" "+questionAndAnswer
                            .getWrongAnswer3());
                    break;
                case R.id.AnswerCTXT:
                    AnswerCTXT.setText(getString(R.string.C)+" "+questionAndAnswer
                            .getWrongAnswer3());
                    break;
                case R.id.AnswerDTXT:
                    AnswerDTXT.setText(getString(R.string.D)+" "+questionAndAnswer
                            .getWrongAnswer3());
                    break;
                default:
                    AnswerATXT.setText(getString(R.string.A)+" "+ questionAndAnswer
                            .getWrongAnswer3());
                    break;
            }
            if(questionAndAnswer.isAnswered()) {
                radioGroup.check(questionAndAnswer.getAnswerId());
            }
            if(questionAndAnswer.isReview()) {
                ReviewcheckBox.setChecked(true);
            }
            ReviewcheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                   try
                   {
                    if(isChecked) {
                        mNavigationDrawerFragment.ChangeItemColor( R.drawable.review_round_yellow);
                        questionAndAnswer.setReview(true);
                    }
                    else
                    {
                        if(questionAndAnswer.isAnswered())
                        {
                            mNavigationDrawerFragment.ChangeItemColor(R.drawable.review_round_answered);
                        }
                        else
                        {
                            mNavigationDrawerFragment.ChangeItemColor(R.drawable.review_round);
                        }
                        questionAndAnswer.setReview(false);
                    }
                   }catch(Exception e)
                   {
                       Toast.makeText(getActivity(), getString(R.string.ereur) + e.getMessage(), Toast
                               .LENGTH_LONG).show();
                   }
                }
            });
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                  try
                  {
                    questionAndAnswer.setAnswered(true);
                    questionAndAnswer.setAnswerId(checkedId);
                    if(questionAndAnswer.isReview()) {
                        mNavigationDrawerFragment.ChangeItemColor(R.drawable.review_round_yellow);
                    }
                    else
                    {
                            mNavigationDrawerFragment.ChangeItemColor(R.drawable.review_round_answered);
                    }
                  }catch(Exception e)
                  {
                      Toast.makeText(getActivity(), getString(R.string.ereur) + e.getMessage(), Toast
                              .LENGTH_LONG).show();
                  }
                }
            });
            ReviewtextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   try {
                       ReviewcheckBox.setChecked(!ReviewcheckBox.isChecked());
                   }catch(Exception e)
                   {
                       Toast.makeText(getActivity(), getString(R.string.ereur) + e.getMessage(), Toast
                               .LENGTH_LONG).show();
                   }
                }
            });
            ButtonNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   try
                   {
                    if(lang!="ar")
                        ShowNext();
                    else
                        ShowPrev();}catch(Exception e)
                   {
                       Toast.makeText(getActivity(), getString(R.string.ereur) + e.getMessage(), Toast
                               .LENGTH_LONG).show();
                   }
                }
            });
            ButtonPrev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  try
                  {
                    if(lang != "ar")
                        ShowPrev();
                    else
                        ShowNext();
                  }catch(Exception e)
                  {
                      Toast.makeText(getActivity(), getString(R.string.ereur) + e.getMessage(), Toast
                              .LENGTH_LONG).show();
                  }
                }
            });

            ButtonFinish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        try
                        {isFinished=true;
                        countDownTimer.onFinish();
                        }catch(Exception e)
                        {
                            Toast.makeText(getActivity(), getString(R.string.ereur) + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                        }
                }
            });
          }catch(Exception e)
          {
              Toast.makeText(this.getActivity(), getString(R.string.ereur) + e.getMessage(), Toast
                      .LENGTH_LONG).show();
          }
            return rootView;
        }

        private void ShowPrev() {
          try
          {
            if(section>0) {
                int pos=section-1;
                mNavigationDrawerFragment.selectItem(pos);
            }
            else
                Toast.makeText(this.getActivity(),getString(R.string.FirstOne), Toast
                        .LENGTH_SHORT).show();
          }catch(Exception e)
          {
              Toast.makeText(this.getActivity(), getString(R.string.ereur) + e.getMessage(), Toast
                      .LENGTH_LONG).show();
          }
        }

        private void ShowNext() {
        try
        {
           if(section<(mNavigationDrawerFragment.getChildsCount()-1))
            {
                int pos=section+1;
                mNavigationDrawerFragment.selectItem(pos);
            }
            else
                Toast.makeText(this.getActivity(),getString(R.string.LastOne), Toast
                        .LENGTH_SHORT).show();
        }catch(Exception e)
        {
            Toast.makeText(this.getActivity(), getString(R.string.ereur) + e.getMessage(), Toast
                    .LENGTH_LONG).show();
        }
        }

        @Override
        public void onAttach(Activity activity) {
          try {
              super.onAttach(activity);
            ((quiz_challenge) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
          }catch(Exception e)
          {
              Toast.makeText(this.getActivity(), getString(R.string.ereur) + e.getMessage(), Toast
                      .LENGTH_LONG).show();
          }
        }
    }

    public  class FetchQuestionsAsyncTask extends AsyncTask<Void,Void,Void> {
        public FetchQuestionsAsyncTask() {
        }

        @Override
        protected void onPostExecute(Void arg) {
            try
            {
                progressDialog.dismiss();
                if(QuestionsAndAnswers.size()==0)
                {
                    destroyed=false;
                    Toast.makeText(getApplicationContext(), getString(R.string.server_error),
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
                mNavigationDrawerFragment.selectItem(0);
                countDownTimer.start();
                super.onPostExecute(arg);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... args) {

            int success;
            try {
                lang=LanguageManager.get_language(getApplicationContext());
                String themes=getIntent().getStringExtra("Themes");
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair(TAG_THEMES,themes));
                params.add(new BasicNameValuePair(TAG_Count,String.valueOf(count)));
                params.add(new BasicNameValuePair(TAG_Lang,lang));
                JSONObject json = JSONParser.makeHttpRequest(
                        url_Connection + "FetchQuestions.php", params);
                if(json!=null)
                {
                   success = json.getInt(TAG_SUCCESS);
                   if (success == 1) {
                       JSONArray ThemesObj = json.getJSONArray(TAG_QuestionAndAnswers);
                       for (int i = 0; i < ThemesObj.length(); i++) {
                           JSONObject c =ThemesObj.getJSONObject(i);
                           String Question = c.getString(TAG_Question);
                           String CorrectAnswer=c.getString(TAG_CorrectAnswer);
                           String WrongAnswer1=c.getString(TAG_WrongAnswer1);
                           String WrongAnswer2=c.getString(TAG_WrongAnswer2);
                           String WrongAnswer3=c.getString(TAG_WrongAnswer3);
                           String Qid=c.getString(TAG_Qid);
                           int Dif=c.getInt(Tag_Dif);
                           QuestionsAndAnswers.add(new QuestionAndAnswers(Qid, Question,
                                 CorrectAnswer,
                                   WrongAnswer1, WrongAnswer2, WrongAnswer3,Dif));
                       }
                   }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public  class UpdateUserScoreAsyncTask extends AsyncTask<Void,Void,Void> {

        Spanned Msg=Html.fromHtml("");

        public UpdateUserScoreAsyncTask() {

        }

        @Override
        protected void onPostExecute(Void v) {
            try
            {
                progressDialog.dismiss();
                super.onPostExecute(v);
                AlertDialog.Builder  congrats= new AlertDialog.Builder(quiz_challenge.this);
                congrats.setTitle(getString(R.string.Finish));
                congrats.setMessage(Msg);
                congrats.setCancelable(false);
                congrats.setPositiveButton(getString(R.string.ok), new DialogInterface
                        .OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        destroyed=false;
                        finish();
                    }
                }).show();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... args) {
            int success;
            try {
                int Score=0,NRA=0;
                  for (int i = 0; i <QuestionsAndAnswers.size() ; i++) {
                      QuestionAndAnswers q =  QuestionsAndAnswers.get(i);
                      int aID=q.getAnswerId();
                      int CaID=q.getCorrectAnswerTxtId();
                      if(aID==CaID && q.isAnswered())
                      {
                          Score+=q.getDif();
                      }
                      else
                      {
                          if(q.isAnswered()) {
                              NRA++;
                          }
                      }
                  }
                  if(NRA==0)
                  {
                      Score+=secondsLeft;
                  }
                StoreUserLocaly storeUser=new StoreUserLocaly(getApplicationContext());
                String Email= storeUser.GetLogedInUserData().getEmail();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair(TAG_Score,String.valueOf(Score)));
                params.add(new BasicNameValuePair(TAG_Email,Email));
                JSONObject json = JSONParser.makeHttpRequest(
                        url_Connection+"UpdateUserScore.php", params);
                if(json!=null)
                {
                    success = json.getInt(TAG_SUCCESS);
                    if (success == 1)
                    {
                        int unanswered=getNBIncompleted();
                        Msg= Html.fromHtml("*"+getString(R.string.scoreis)+"<font " +
                                "color='green'> " + Score + ". </font><br>*"+getString(R.string
                                .nbwrong)+ "<font color='red'>" +NRA +". </font> <br>*"+
                                getString(R.string.unanswered)+ "<font color='red'>  " +
                                unanswered+ "</font>");
                    }
                    else
                    {
                        Msg=Html.fromHtml("<font color='red'>" + getString(R.string.updating_score_error) +"</font>");
                    }
                }
                else
                {
                    Msg=Html.fromHtml("<font color='red'>" + getString(R.string.updating_score_error) + "</font>");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
