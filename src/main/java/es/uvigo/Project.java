package es.uvigo;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Project {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private int id;

	private String name;

	@ManyToMany
	private Set<Employee> employees = new HashSet<>();

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Employee> getEmployees() {
		return Collections.unmodifiableSet(employees);
	}

	public void addEmployee(Employee e) {
		e.internalAddProject(this);
		this.employees.add(e);
	}

	void internalAddEmployee(Employee employee) {
		this.employees.add(employee);
	}

	public void removeEmployee(Employee e) {
		e.internalRemoveProject(this);
		this.employees.remove(e);
	}

	public void setEmployees(Collection<Employee> employees) {
		// remove my employees not in employees
		Set<Employee> myEmployeesCopy = new HashSet<>(this.employees);
		for (Employee employee : myEmployeesCopy) {
			if (!employees.contains(employee)) {
				this.removeEmployee(employee);
			}
		}

		// add new employees (since it is a set, no repetitions are possible)
		for (Employee employee : employees) {
			this.addEmployee(employee);
		}

	}

}
