package seribsp;

import java.io.Serializable;

public class Author implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -226545683519974884L;
	String name;
	String surname;
	
	
	public Author(String surname, String name) {
		this.name = name;
		this.surname = surname;
	}

	public String toString() {
		return name+";"+surname;
	}
}
