package ezouagh.quiz;

import java.util.Date;

/**
 * Created by Younes on 01-Jul-15.
 */
public class Users {

    private String FullName,Email,Pass;

    private Date BirthDay;

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPass() {
        return Pass;
    }

    public void setPass(String pass) {
        Pass = pass;
    }

    public Date getBirthDay() {
        return BirthDay;
    }

    public void setBirthDay(Date birthDay) {
        BirthDay = birthDay;
    }

    public  Users(){}

    public  Users( String FullName ,Date BirthDay,String Email,String Pass)
    {
        this.FullName=FullName;
        this.BirthDay=BirthDay;
        this.Email=Email;
        this.Pass=Pass;
    }
}
