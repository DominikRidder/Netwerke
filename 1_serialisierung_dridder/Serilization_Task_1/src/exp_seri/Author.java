/**
 * Class, for saving the author.
 * 
 * @author Dominik Ridder
 */
package exp_seri;

import java.io.Serializable;

public class Author implements Serializable {

	/**
	 * Auto Generated.
	 */
	private static final long serialVersionUID = -226545683519974884L;
	private String name;
	private String surname;
	
	
	public Author(String surname, String name) {
		this.name = name;
		this.surname = surname;
	}

	public String toString() {
		return name+", "+surname;
	}
}
