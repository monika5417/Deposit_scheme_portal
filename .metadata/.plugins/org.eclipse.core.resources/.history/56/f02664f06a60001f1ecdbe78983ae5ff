package com.TestMapping.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

// 	@OneToOne mapping ko agar single direction rkhna h to sirf ek hi table me ise add krenege and by default hi y dusri table ki 
// forign ki lelega . ---------------- And agar hme bidirection 	@OneToOne mapping krne hi to dono hi table me is use krna hoga.

// 	@ManyToOne  mapping me ek particular thing k liye multiple data ho skta h tb ise use krte hai and @JoinColumn me other table ki foriegn ki dete hai

//@OneToMany(mappedBy = "department")
//private List<Student> student; mapping me  ek student k multiple department ho skte h and mappedBy use krna mandatory h jisme hum another table ka reference name dete hai

// Note point jb bhi hum list me data lete hai tab @OneToMany mapping ka use krenege

// @ManyToMany mapping me hme both table me COllection ka use krna hai  and mappedBy jo owner table nhii h usme use krna hai
// and owner table me JOinTable and joinCOlumn and inverseCOloumn lagana hota hai

@Entity
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long rollNo;
	private String name;
	private String standard;
	
	@OneToOne
	private Laptop laptop;
	
	
	
//	multiple student ka 1 department ho skta hai              and isika oppsite OneToMany hme student table me use krna hai
	@ManyToOne
	@JoinColumn(name="department_id")
	private Department department;
	
	@ManyToMany
	@JoinTable(name="student_mission",
	joinColumns = @JoinColumn(name="student_id"),
	inverseJoinColumns = @JoinColumn(name="mission_id"))
	private List<Mission> mission;
	
	
	
	
	public List<Mission> getMission() {
		return mission;
	}
	public void setMission(List<Mission> mission) {
		this.mission = mission;
	}
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	public Laptop getLaptop() {
		return laptop;
	}
	public void setLaptop(Laptop laptop) {
		this.laptop = laptop;
	}
	public Long getRollNo() {
		return rollNo;
	}
	public void setRollNo(Long rollNo) {
		this.rollNo = rollNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStandard() {
		return standard;
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}
	
	
}
