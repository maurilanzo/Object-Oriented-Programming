package university;

public class Student {
    private String first;
    private String last;
    private int studentID;
    private float average=-1;
    private int examNum;
    private float points;


    public Student(String first, String last, int studentID){
        this.first=first;
        this.last=last;
        this.studentID=studentID;
    }
    public int getID(){
        return studentID;
    }
    
    public String getFirst(){
        return this.first;
    }
    public String getLast(){
        return this.last;
    }

    public void setAvg(float average){
        this.average=average;
        
    }
    
    public float getAvg(){
        return average;
    }

    public void setExamnum(int num){
        this.examNum=num;
    }

    public int getExamnum(){
        return examNum;
    }

    public void setPoints(float points){
        this.points=points;
    }

    public float getPoints(){
        return points;
    }




}
