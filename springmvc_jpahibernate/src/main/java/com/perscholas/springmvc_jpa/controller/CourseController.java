package com.perscholas.springmvc_jpa.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.perscholas.springmvc_jpa.model.Course;
import com.perscholas.springmvc_jpa.model.Student;

@RequestMapping("/courses")
@Controller
public class CourseController {
	@Autowired
	@Qualifier("hibernateSession")
	private Session session;
	
	@GetMapping(value = {"/","/allCourses"})
	public String getAllCourses(Model model) {
		Query<Course> q = session.createQuery("from Course", Course.class);
		List<Course> allCourses = q.list();
		model.addAttribute("allCourses", allCourses);
		return "Courses";
	}
	@GetMapping("/courseForm")
	public String displayCourseForm(Model model) {
		model.addAttribute("course", new Course());
		return "CourseForm";
	}
	@PostMapping("/createCourse")
	public String createStudent(@Valid @ModelAttribute("course") 
		Course course, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "CourseForm";
		}
		Integer courseId;
		session.beginTransaction();
		courseId = (Integer)session.save(course);
		session.getTransaction().commit();
		System.out.println("Course ID: " + courseId);
		return "redirect:/courses/allCourses";
	}
	@GetMapping("/courseById/{id}")
	public String getCourseById(@PathVariable Integer id, Model model) {
		Course course = session.find(Course.class, id);
		if (course == null) {
			model.addAttribute("errorMessage", "Course not found.");
		} else {
			model.addAttribute("course", course);
		}
		return "CoursePage";
	}
	@GetMapping("/deleteCourse/{id}")
	public String deleteCourse(@PathVariable Integer id) {
		Course c = session.get(Course.class, id);
		session.beginTransaction();
		Query<?> q = session.createNativeQuery("delete from students_courses where "
				+ "studentCourses_course_id = ?");
		q.setParameter(1, c.getCourseId());
		q.executeUpdate();
		session.remove(c);
		session.getTransaction().commit();		
		return "redirect:/courses/allCourses";
	}
	@GetMapping("/updateCourseForm/{id}")
	public String updateForm(@PathVariable Integer id, Model model) {
		Course course = session.find(Course.class, id);
		if (course != null) {
			model.addAttribute("course", course);
			return "UpdateCourseForm";
		}
		model.addAttribute("errorMessage", "Sorry! There was an error in your "
				+ "inquiry. Please contact the administrator.");
		return "forward:/courses/allCourses";
	}
	@PostMapping("/updateCourse")
	public String updateCourse(@Valid @ModelAttribute("course") 
		Course course, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "UpdateCourseForm";
		}
		Course updateCourse = session.find(Course.class, course.getCourseId());
		updateCourse.setCode(course.getCode());
		updateCourse.setName(course.getName());
		updateCourse.setMaxSize(course.getMaxSize());
		session.beginTransaction();
		session.merge(updateCourse);
		session.getTransaction().commit();
		return "redirect:/courses/courseById/" + course.getCourseId();
	}
	@GetMapping("/courseEnrollmentForm")
	public String courseEnrollmentForm(Model model) {
		Query<Student> studentSql = session.createQuery("from Student", Student.class);
		List<Student> allStudents = studentSql.list();
		Query<Course> courseSql = session.createQuery("from Course", Course.class);
		List<Course> allCourses = courseSql.list();
		model.addAttribute("allStudents", allStudents);
		model.addAttribute("allCourses", allCourses);
		model.addAttribute("enrollmentViolations", model.asMap().get("enrollmentViolations"));
		model.addAttribute("successEnrollmentMessage", model.asMap()
				.get("successEnrollmentMessage"));
		return "CourseEnrollmentForm";
	}
	@PostMapping("/enrollInCourse")
	public String enrollInCourse(@RequestParam Integer studentId, 
			@RequestParam Integer courseId, Model model, RedirectAttributes 
			redirectAtt) {
		List<String> enrollmentViolations = new ArrayList<>();
		// Check to see if student is already enrolled in course
		Student student = session.find(Student.class, studentId);
		List<Course> enrollmentChecklist = student.getStudentCourses();
		if (!enrollmentChecklist.isEmpty()) {
			for (Course c : enrollmentChecklist) {
				if (c.getCourseId() == courseId) {
					enrollmentViolations.add(student.getName() + " is already "
							+ "enrolled in " + c.getName() + ".");
				}
			}
		}
		// Check if maximum size is exceeded
		Course registeredCourse = session.find(Course.class, courseId);
		if (registeredCourse.getStudentRoster().size() >= registeredCourse.getMaxSize()) {
			enrollmentViolations.add("Cannot enroll student. Course is full.");
		}
		if (!enrollmentViolations.isEmpty()) {
			redirectAtt.addFlashAttribute("enrollmentViolations", enrollmentViolations);
			return "redirect:/courses/courseEnrollment";
		}
		// Proceed with enrollment if there are no errors
		student.addCourse(registeredCourse);
		session.beginTransaction();
		/* Need to clear session memory otherwise the session will attempt to 
		 * run extra queries that are not necessary. */
		session.clear();
		Query<?> q = session.createNativeQuery("insert into students_courses "
				+ "(studentRoster_student_id, studentCourses_course_id) values (?,?)");
		q.setParameter(1, student.getStudentId());
		q.setParameter(2, registeredCourse.getCourseId());
		q.executeUpdate();
		session.getTransaction().commit();
		redirectAtt.addFlashAttribute("successEnrollmentMessage", 
				student.getName() + " has been successfuly enrolled in " 
				+ registeredCourse.getName() + ".");
		return "redirect:/courses/courseEnrollmentForm";
	}
	@GetMapping("/removeStudent")
	public String removeStudentFromCourse(@RequestParam Integer courseId, 
			@RequestParam Integer studentId) {
		Student student = session.load(Student.class, studentId);
		Course course = session.load(Course.class, courseId);
		student.removeCourse(course);
		session.beginTransaction();
		/* Need to clear session memory otherwise the session will attempt to 
		 * run extra queries that are not necessary. */
		session.clear();
		Query<?> q = session.createNativeQuery("delete from students_courses where "
                        + "studentRoster_student_id = ? and studentCourses_course_id = ?");
		q.setParameter(1, student.getStudentId());
		q.setParameter(2, course.getCourseId());
		q.executeUpdate();
		session.getTransaction().commit();
		return "redirect:courseById/" + courseId;
	}
	@InitBinder
	public void initializeBinder(WebDataBinder binder) {
		binder.setAllowedFields("courseId", "code", "name", "maxSize");
	}
}