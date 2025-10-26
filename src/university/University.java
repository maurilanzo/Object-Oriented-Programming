package university;
import java.util.logging.Logger;


/**
 * This class represents a university education system.
 * 
 * It manages students and courses.
 *
 */
public class University {
//attributes
	private String name;
	private String first;
	private String last;
	
	

// R1
	/**
	 * Constructor
	 * @param name name of the university
	 */
	public University(String name){
		// Example of logging
		// logger.info("Creating extended university object");
		//TODO: to be implemented
		this.name=name;
	}
	
	/**
	 * Getter for the name of the university
	 * 
	 * @return name of university
	 */
	public String getName(){
		//TODO: to be implemented
		return this.name;
	}
	
	/**
	 * Defines the rector for the university
	 * 
	 * @param first first name of the rector
	 * @param last	last name of the rector
	 */
	public void setRector(String first, String last){
		//TODO: to be implemented
		this.first=first;
		this.last=last;

	}
	
	/**
	 * Retrieves the rector of the university with the format "First Last"
	 * 
	 * @return name of the rector
	 */
	public String getRector(){
		//TODO: to be implemented
		String toReturn= String.format("%s %s",first,last);
		return toReturn;
	}
	
// R2
	/**
	 * Enrol a student in the university
	 * The university assigns ID numbers 
	 * progressively from number 10000.
	 * 
	 * @param first first name of the student
	 * @param last last name of the student
	 * 
	 * @return unique ID of the newly enrolled student
	 */
	Student [] students= new Student[1000]; 
	private int numstudent = -1;
	public int enroll(String first, String last){
		//TODO: to be implemented
		numstudent++;
		int id=numstudent+10000;
		students[numstudent]=new Student(first,last,id);
		logger.info("New student enrolled : " + id + " " + first + " " + last);
		return id;
	}
	
	/**
	 * Retrieves the information for a given student.
	 * The university assigns IDs progressively starting from 10000
	 * 
	 * @param id the ID of the student
	 * 
	 * @return information about the student
	 */
	public String student(int id){
		//TODO: to be implemented
		for (Student n:students){
			if ((n != null) && (n.getID()==id)){
				return String.format("%d %s %s",n.getID(),n.getFirst(),n.getLast());
			}
		}
		return "No student found";		
	}
	
// R3
	/**
	 * Activates a new course with the given teacher
	 * Course codes are assigned progressively starting from 10.
	 * 
	 * @param title title of the course
	 * @param teacher name of the teacher
	 * 
	 * @return the unique code assigned to the course
	 */
	private int numcourse=-1;
	Course [] courses= new Course [50];
	public int activate(String title, String teacher){
		//TODO: to be implemented
		numcourse++;
		courses[numcourse]=new Course(title, teacher, numcourse+10);
		logger.info("New course activated : " + numcourse + " " + title + " " + teacher);
		return numcourse+10;
	}
	
	/**
	 * Retrieve the information for a given course.
	 * 
	 * The course information is formatted as a string containing 
	 * code, title, and teacher separated by commas, 
	 * e.g., {@code "10,Object Oriented Programming,James Gosling"}.
	 * 
	 * @param code unique code of the course
	 * 
	 * @return information about the course
	 */
	
	public String course(int code){
		
		//TODO: to be implemented
		for (Course n:courses){
			if (n.getCoursecode()==code){
				return String.format("%d,%s,%s",n.getCoursecode(),n.getTitle(),n.getTeacher());
			}
		}
		return "No Course found";
	}

// R4
	/**
	 * Register a student to attend a course
	 * @param studentID id of the student
	 * @param courseCode id of the course
	 */
	
	
	public void register(int studentID, int courseCode){
		//TODO: to be implemented
		for(Course n: courses){
			if (n != null && n.getCoursecode()==courseCode){
				n.registerStudent(studentID);
				logger.info("Student " + studentID + " signed up for course " + courseCode);
			}
		}
	}
	
	/**
	 * Retrieve a list of attendees.
	 * 
	 * The students appear one per row (rows end with `'\n'`) 
	 * and each row is formatted as describe in in method {@link #student}
	 * 
	 * @param courseCode unique id of the course
	 * @return list of attendees separated by "\n"
	 */
	private int [] studlist;
	public String listAttendees(int courseCode){
		String list= "";
		//TODO: to be implemented
		for(Course n: courses){  //get the list of studentid
			if (n != null && n.getCoursecode()==courseCode){
				studlist = n.getList();		
			}
		}

		for (int i:studlist){ //i = studentID
			for (Student stud:students){
				if (stud != null && stud.getID()==i){
					list = list + String.format("%d %s %s\n",i,stud.getFirst(),stud.getLast());
				}
			}
		}
		
		return list;
	}

	/**
	 * Retrieves the study plan for a student.
	 * for(int n: numList){
            System.out.print(n+" ");
        }
	 * 
	 * 
	 * The study plan is reported as a string having
	 * one course per line (i.e. separated by '\n').
	 * The courses are formatted as describe in method {@link #course}
	 * 
	 * @param studentID id of the student
	 * 
	 * @return the list of courses the student is registered for
	 */
	public String studyPlan(int studentID){
		String listcourse = "";
		//TODO: to be implemented
		for (Course n:courses){
			if (n != null) {
				studlist=n.getList();
			for (int id:studlist){
				if (id==studentID){
					listcourse = listcourse + String.format("%d,%s,%s\n",n.getCoursecode(),n.getTitle(),n.getTeacher());
				}
			}}
		}
		
		return listcourse;
	}
	




// R5
	/**
	 * records the grade (integer 0-30) for an exam can 
	 * 
	 * @param studentId the ID of the student
	 * @param courseID	course code 
	 * @param grade		grade ( 0-30)
	 */
	int exnum=-1;
	Exam [] exams = new Exam [500];
	public void exam(int studentId, int courseID, int grade) {
		exnum++;
		exams[exnum]=new Exam(courseID, studentId, grade);
		for (Student stud:students){
			if (stud.getID()==studentId){
				logger.info("Student " + studentId + " took an exam in course " + courseID + " with grade " + grade);
				float avg= stud.getAvg();
				int num=stud.getExamnum();
				if (avg==-1){
					stud.setAvg(grade);
					stud.setExamnum(1);
				}
				else {
					stud.setExamnum(num+1);
					stud.setAvg((avg*num+grade)/(num+1));
				}
				break;
			}
		}
	}

	/**
	 * Computes the average grade for a student and formats it as a string
	 * using the following format 
	 * 
	 * {@code "Student STUDENT_ID : AVG_GRADE"}. 
	 * 
	 * If the student has no exam recorded the method
	 * returns {@code "Student STUDENT_ID hasn't taken any exams"}.
	 * 
	 * @param studentId the ID of the student
	 * @return the average grade formatted as a string.
	 */

	public String studentAvg(int studentId) {
		double cont=0.0;
		double sum=0.0;
		for (Exam exam:exams){
			if (exam != null && exam.getID()==studentId){
				cont++;
				sum=sum+exam.getGrade();
			}
		}
		if(sum==0){
			return String.format("Student %d hasn't taken any exams",studentId);
		}
		else{
			return "Student " + studentId + " : " + sum/cont;	
		}
	}
	
	/**
	 * Computes the average grades of all students that took the exam for a given course.
	 * 
	 * The format is the following: 
	 * {@code "The average for the course COURSE_TITLE is: COURSE_AVG"}.
	 * 
	 * If no student took the exam for that course it returns {@code "No student has taken the exam in COURSE_TITLE"}.
	 * 
	 * @param courseId	course code 
	 * @return the course average formatted as a string
	 */
	public String courseAvg(int courseId) {
		//I need course title
		String title="";
		for(Course course:courses){
			if (course != null && courseId==course.getCoursecode()){
				title=course.getTitle();
			}
		}

		float cont=0;
		float sum=0;
		for (Exam exam:exams){
			if (exam != null && exam.getCoursecode()==courseId){
				cont++;
				sum=sum+exam.getGrade();
				
			}
		}
		if(sum==0){
			return String.format("No student has taken the exam in %s",title);
		}
		else{
			return "The average for the course " + title + " is : " + sum/cont;
		}

	}
	

// R6
	/**
	 * Retrieve information for the best students to award a price.
	 * 
	 * The students' score is evaluated as the average grade of the exams they've taken. 
	 * To take into account the number of exams taken and not only the grades, 
	 * a special bonus is assigned on top of the average grade: 
	 * the number of taken exams divided by the number of courses the student is enrolled to, multiplied by 10.
	 * The bonus is added to the exam average to compute the student score.
	 * 
	 * The method returns a string with the information about the three students with the highest score. 
	 * The students appear one per row (rows are terminated by a new-line character {@code '\n'}) 
	 * and each one of them is formatted as: {@code "STUDENT_FIRSTNAME STUDENT_LASTNAME : SCORE"}.
	 * 
	 * @return info on the best three students.
	 */

	public int studyNumber(int studentID){
		int num=0;
		for (Course n:courses){
			if (n != null) {
				studlist=n.getList();
			for (int id:studlist){
				if (id==studentID){
					num++;
				}
			}}
		}
		return num;
	}
		
	
	public String topThreeStudents() {
		for (Student stud: students){
			if (stud != null){
				stud.setPoints(stud.getAvg()+((stud.getExamnum()/studyNumber(stud.getID()))*10));
			}
		}
		float [] maximum = new float[3];
		String [] name = new String[3];
		for (int i=0; i<3; i++){
			maximum[i]=-1;
			name[i]="";
		}
		for (Student stud : students) {
			if (stud != null) {
				if (stud.getPoints() > maximum[0]) {
					maximum[0] = stud.getPoints();
					name[0] = stud.getFirst() + " " + stud.getLast() + " : " + stud.getPoints();
				}
			}
		}
		
		for (Student stud : students) {
			if (stud != null) {
				if (stud.getPoints() > maximum[1] && stud.getPoints() < maximum[0]) {
					maximum[1] = stud.getPoints();
					name[1] = stud.getFirst() + " " + stud.getLast() + " : " + stud.getPoints();
				}
			}
		}
		
		for (Student stud : students) {
			if (stud != null) {
				if (stud.getPoints() > maximum[2] && stud.getPoints() < maximum[1]) {
					maximum[2] = stud.getPoints();
					name[2] = stud.getFirst() + " " + stud.getLast() + " : " + stud.getPoints();
				}
			}
		}
		
		return name[0] + "\n" + name[1] + "\n" + name[2];
	}
		
	

	

// R7
    /**
     * This field points to the logger for the class that can be used
     * throughout the methods to log the activities.
     */
    public static final Logger logger = Logger.getLogger("University");

}
