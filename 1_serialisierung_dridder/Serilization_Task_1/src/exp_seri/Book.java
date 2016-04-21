/**
 * Class, for saving the book.
 * 
 * @author Dominik Ridder
 */
package exp_seri;

import java.io.Serializable;

public class Book implements Serializable {
	
	/**
	 * Auto Generated.
	 */
	private static final long serialVersionUID = -4992800505401636887L;
	private String isbn;
	private String title;
	private Author author;
	
	
	public Book(String isbn, String title, String surname, String name) {
	    this(isbn, title, new Author(surname, name));
	}

	public Book(String isbn, String title, Author author) {
		this.isbn = isbn;
		this.title = title;
		this.author = author;
	}

	public String getISBN() {
		return isbn;
	}

	public Object getTitle() {
		return title;
	}

	public Object getAuthor() {
		return author;
	}
}
