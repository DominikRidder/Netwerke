package seribsp;

import java.io.Serializable;

public class Book implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4992800505401636887L;
	String isbn_Nr;
	Author author;
	
	
	public Book(String isbn, String surname, String name) {
		isbn_Nr = isbn;
		author = new Author(surname, name);
	}


	public String getISBN() {
		return isbn_Nr;
	}
	
	public String toString() {
		return isbn_Nr+";"+author;
	}
}
