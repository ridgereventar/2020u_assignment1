package sample;

import java.text.DecimalFormat;

public class TestFile {

    private String filename;
    private double spamProbability;
    private String actualClass;

    public TestFile(String f, double sp, String ac) {
        this.filename = f;
        this.spamProbability = sp;
        this.actualClass = ac;
    }

    // get
    public String getFilename() { return this.filename; }
    public double getSpamProbability() { return this.spamProbability; }
    public String getSpamProbRounded() {
        DecimalFormat df = new DecimalFormat("0.00000");
        return df.format(this.spamProbability);
    }
    public String getActualClass() { return this.actualClass; }

    //set
    public void setFilename(String value) { this.filename = value; }
    public void setSpamProbability(double val) { this.spamProbability = val; }
    public void setActualClass(String value) { this.actualClass = value; }

    public boolean isHam() {
        if(this.actualClass.equals("ham")) {
            return true;
        } else {
            return false;
        }
    }
    public boolean isSpam() {
        if(this.actualClass.equals("spam")) {
            return true;
        } else {
            return false;
        }
    }
}
