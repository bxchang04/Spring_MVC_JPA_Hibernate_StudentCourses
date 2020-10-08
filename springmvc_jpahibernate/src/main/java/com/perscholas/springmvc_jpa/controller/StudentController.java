package com.perscholas.springmvc_jpa.controller;

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
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.perscholas.springmvc_jpa.model.Course;
import com.perscholas.springmvc_jpa.model.Student;

@Controller
public class StudentController {
	@Autowired
	@Qualifier("hibernateSession")
	private Session session;
	
	@GetMapping(value = {"/","/allStudents"})
	public String getAllStudents(Model model) {
		Query<Student> q = session.createQuery("from Student", Student.class);
		List<Student> allStudents = q.list();
		model.addAttribute("allStudents", allStudents);
		return "Students";
	}
	@GetMapping("/studentForm")
	public String displayStudentForm(Model model) {
		model.addAttribute("student", new Student());
		return "StudentForm";
	}
	@PostMapping("/createStudent")
	public String createStudent(@Valid @ModelAttribute("student") Student student, 
			BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "StudentForm";
		}
		Integer studentId;
		session.beginTransaction();
		studentId = (Integer) session.save(student);
		session.getTransaction().commit();
		System.out.println("Student ID: " + studentId);
		return "redirect:allStudents";
	}
	@GetMapping("/studentById/{id}")
	public String getStudentById(@PathVariable Integer id, Model model) {
		Student student = session.find(Student.class, id);
		if (student == null) {
			model.addAttribute("errorMessage", "Student not found.");
		} else {
			model.addAttribute("student", student);
		}
		return "StudentPage";
	}
	@GetMapping("/deleteStudent/{id}")
	public String deleteStudent(@PathVariable Integer id) {
		Student s = session.get(Student.class, id);
		session.beginTransaction();
		session.delete(s);
		session.getTransaction().commit();
		return "redirect:/allStudents";
	}
	@GetMapping("/updateStudentForm/{id}")
	public String updateForm(@PathVariable Integer id, Model model) {
		Student student = session.find(Student.class, id);
		if (student != null) {
			model.addAttribute("student", student);
			return "UpdateStudentForm";
		}
		model.addAttribute("errorMessage", "Sorry! There was an error in your "
				+ "inquiry. Please contact the administrator.");
		return "forward:/allStudents";
	}
	@PostMapping("/updateStudent")
	public String updateStudent(@Valid @ModelAttribute("student") Student student, 
			BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "UpdateStudentForm";
		}
		Student studentUpdate = session.find(Student.class, student.getStudentId());
		studentUpdate.setName(student.getName());
		studentUpdate.setEmail(student.getEmail());
		session.beginTransaction();
		session.merge(studentUpdate);
		session.getTransaction().commit();
		return "redirect:/studentById/" + student.getStudentId();
	}
	@GetMapping("/removeCourse")
	public String removeCourseFromStudent(@RequestParam Integer studentId, 
			@RequestParam Integer courseId) {
		Student student = session.load(Student.class, studentId);
		Course course = session.load(Course.class, courseId);
		student.removeCourse(course);
		session.beginTransaction();
		/* Need to clear session memory otherwise the session will attempt to 
		 * run extra queries that are not necessary. */
		session.clear();
		Query<?> q = session.createNativeQuery("delete from students_courses where studentRoster_student_id = ? and studentCourses_course_id = ?");
		q.setParameter(1, student.getStudentId());
		q.setParameter(2, course.getCourseId());
		q.executeUpdate();
		session.getTransaction().commit();
		return "redirect:studentById/" + studentId;
	}
	/* The initializeBinder method includes the @InitBinder annotation which 
	 * identifies the method as initializing and configuring the WebDataBinder 
	 * instance. This allows us to set allowed fields as well as other parameters 
	 * to prevent the input of unauthorized request parameters. Since we only 
	 * need three properties to create or update a Student we will set the 
	 * WebDataBinder instance to accept only these three properties. */
	@InitBinder
	public void initializeBinder(WebDataBinder binder) {
		binder.setAllowedFields("studentId", "name", "email");
	}
}