package university;

public class Course {
    private String title;
    private String teacher;
    private int courseCode;
    private int [] registeredStudents = new int[100];
    private int cont;

    public Course(String title, String teacher, int courseCode){
        this.title=title;
        this.teacher=teacher;
        this.courseCode=courseCode;
    }

    public int getCoursecode(){
        return this.courseCode;
    }

    public String getTeacher(){
        return this.teacher;
        }

    public String getTitle(){
        return this.title;
    }    
    
    public void registerStudent(int studentID){
        registeredStudents[cont]=studentID;
        cont ++;
    }

    public int[] getList(){
       return registeredStudents;
    }
}
