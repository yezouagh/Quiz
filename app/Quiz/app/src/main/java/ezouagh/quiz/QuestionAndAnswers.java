package ezouagh.quiz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Younes on 01-Jul-15.
 */
public class QuestionAndAnswers {


    public String getQid() {
        return Qid;
    }

    public void setQid(String qid) {
        Qid = qid;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getCorrectAnswer() {
        return CorrectAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        CorrectAnswer = correctAnswer;
    }

    public String getWrongAnswer1() {
        return WrongAnswer1;
    }

    public void setWrongAnswer1(String wrongAnswer1) {
        WrongAnswer1 = wrongAnswer1;
    }

    public String getWrongAnswer2() {
        return WrongAnswer2;
    }

    public void setWrongAnswer2(String wrongAnswer2) {
        WrongAnswer2 = wrongAnswer2;
    }

    public String getWrongAnswer3() {
        return WrongAnswer3;
    }

    public void setWrongAnswer3(String wrongAnswer3) {
        WrongAnswer3 = wrongAnswer3;
    }


    public boolean isReview() {
        return Review;
    }

    public void setReview(boolean review) {
        Review = review;
    }

    public boolean isAnswered() {
        return Answered;
    }

    public void setAnswered(boolean answered) {
        Answered = answered;
    }

    private String Qid,Question,CorrectAnswer,WrongAnswer1,WrongAnswer2,WrongAnswer3;

    public int getDif() {
        return Dif;
    }

    public void setDif(int dif) {
        Dif = dif;
    }

    private int Dif;

    private boolean Review =false,Answered=false;

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    private int answerId =0,CorrectAnswerTxtId,WrongAnswer1TxtId,WrongAnswer2TxtId,WrongAnswer3TxtId;

    public int getAnswerId() {
        return answerId;
    }

    public int getWrongAnswer3TxtId() {
        return WrongAnswer3TxtId;
    }

    public int getWrongAnswer2TxtId() {
        return WrongAnswer2TxtId;
    }

    public int getCorrectAnswerTxtId() {
        return CorrectAnswerTxtId;
    }

    public int getWrongAnswer1TxtId() {
        return WrongAnswer1TxtId;
    }

    private  void random()
    {
        List<Integer> ids=new ArrayList<>();
        ids.add(R.id.AnswerATXT);
        ids.add(R.id.AnswerBTXT);
        ids.add(R.id.AnswerCTXT);
        ids.add(R.id.AnswerDTXT);
        int r=new Random().nextInt(4);
        CorrectAnswerTxtId=ids.get(r);
        ids.remove(r);
        r=new Random().nextInt(3);
        WrongAnswer1TxtId=ids.get(r);
        ids.remove(r);
        r=new Random().nextInt(2);
        WrongAnswer2TxtId=ids.get(r);
        ids.remove(r);
        WrongAnswer3TxtId=ids.get(0);
    }
    public QuestionAndAnswers(){

    }

    public QuestionAndAnswers(String qid, String question, String correctAnswer ,String
            wrongAnswer1, String wrongAnswer2, String wrongAnswer3,int dif)
    {
        random();
        this.Qid=qid;
        this.Question=question;
        this.CorrectAnswer=correctAnswer;
        this.WrongAnswer1=wrongAnswer1;
        this.WrongAnswer2=wrongAnswer2;
        this.WrongAnswer3=wrongAnswer3;
        this.Dif=dif;
    }
}
