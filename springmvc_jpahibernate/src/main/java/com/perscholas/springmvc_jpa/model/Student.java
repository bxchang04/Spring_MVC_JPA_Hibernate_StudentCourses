package com.perscholas.springmvc_jpa.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/* The @Entity annotation identifies this class as a JPA entity which registers 
 * it with a JPA entity manager (Hibernate Session in this case) as handling 
 * persistent data. The @Table annotation specifies the primary table to the 
 * entity and provides additional configuration capabilities. */
@Entity
@Table(name = "students")
public class Student {
	// Attributes
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "student_id")
	private Integer studentId;
	@Size(min = 2, max = 25, message = "Name must be between 2 and 25 characters long.")
	@NotBlank(message = "Name is required.")
	private String name;
	@Email(message = "Please provide a valid email")
	@NotBlank(message = "Email is required.")
	private String email;
	/*
	 * The @ManyToMany annotation identifies this attribute/property as containing
	 * data related to a many-to-many relationship with another class in the project
	 * which will also be annotated with @ManyToMany along with the argument
	 * mappedBy="studentCourses" (see Course class). Cascade type "PERSIST"
	 * indicates that the entity persist operation will be cascaded to child
	 * entities. Cascade type "MERGE" indicates that the merge operation will be
	 * cascaded to child entities. You can learn more about cascade types at:
	 * https://www.baeldung.com/hibernate-save-persist-update-merge-saveorupdate.
	 * The FetchType.EAGER parameter indicates that data for this property should be
	 * eagerly fetched (i.e., a query for one entity also loads related entities as
	 * part of the query. You can read more about this here:
	 * https://docs.jboss.org/hibernate/jpa/2.1/api/javax/persistence/FetchType.
	 * html.
	 */
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
	private List<Course> studentCourses;

	// Constructors
	public Student() { super(); }
	
	public Student(Integer studentId,
			@Size(min = 2, max = 25, message = "Name must be between 2 and 25 characters long.") @NotBlank(message = "Name is required.") String name,
			@Email(message = "Please provide a valid email") @NotBlank(message = "Email is required.") String email,
			List<Course> studentCourses) {
		super();
		this.studentId = studentId;
		this.name = name;
		this.email = email;
		this.studentCourses = studentCourses;
	}

	// Getters and Setters
	public Integer getStudentId() {
		return studentId;
	}

	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Course> getStudentCourses() {
		return studentCourses;
	}

	public void setStudentCourses(List<Course> studentCourses) {
		this.studentCourses = studentCourses;
	}

	public void removeCourse(Course course) {
		studentCourses.remove(course);
		course.getStudentRoster().remove(this);
	}

	// Other Methods
	/*
	 * These methods will be called when a course is added to or removed from a
	 * student or a student is added to or removed from a course.
	 */
	public void addCourse(Course course) {
		studentCourses.add(course);
		course.getStudentRoster().add(this);
	}
}