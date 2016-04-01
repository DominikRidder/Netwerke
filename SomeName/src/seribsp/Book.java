package seribsp;

import java.io.Serializable;

public class Book implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4992800505401636887L;
	String isbn_Nr;
	String title;
	Author author;
	
	
	public Book(String isbn, String title, String surname, String name) {
	    this(isbn, title, new Author(surname, name));
	}

	public Book(String isbn, String title, Author author) {
		isbn_Nr = isbn;
		this.title = title;
		this.author = author;
	}

	public String getISBN() {
		return isbn_Nr;
	}
	
	public String toString() {
		return isbn_Nr+";"+title+";"+author;
	}

	public Object getTitle() {
		return title;
	}

	public Object getAuthor() {
		return author;
	}
}
