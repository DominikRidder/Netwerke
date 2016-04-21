/**
 * The BookClient provides a book server dialog, that is started with the main method.
 * 
 * @author Dominik Ridder
 */
package exp_seri;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class BookClient {

	static ArrayList<Book> books = new ArrayList<Book>();
	static Scanner scanner = new Scanner(System.in);
	static File bookFile;

	public static void main(String[] args) {
		int input = -1;

		while (input != 0) {
			System.out.println("Choose");
			System.out.println(" (0) Quit program");
			System.out.println(" (1) Load books from file");
			System.out.println(" (2) Show Books");
			System.out.println(" (3) Add Book");
			System.out.println(" (4) Delete Book");
			System.out.println(" (5) Save books in file");
			input = scanner.nextInt();
			scanner.nextLine(); // Read "\n"
			switch (input) {
			case 1:
				loadBooks(bookFile);
				break;
			case 2:
				showBooks();
				break;
			case 3:
				addBook();
				break;
			case 4:
				deleteBook();
				break;
			case 5:
				saveBooks(bookFile);
				break;
			}
		}

		System.out.println("--- Quit Programm ---");
	}

	/**
	 * Loading the books given in a file. The filename is requested via standard
	 * input.
	 * 
	 * @param server
	 *            The file, that contains the books.
	 */
	@SuppressWarnings("unchecked")
	public static void loadBooks(File server) {
		System.out.println("--- Load books from file ---");
		BookClient.printKnownServers();

		// ### Get Server ###
		System.out.print("\nServer name: ");
		server = new File(scanner.nextLine());

		// ### Check extension ###
		if (!server.exists()) {
			File forgottExtension = new File(server.getName() + ".ser");
			if (!forgottExtension.exists()) {
				System.out.println("Server do not exist!");
				return;
			} else {
				server = forgottExtension;
			}
		}

		// ### Load books ###
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
				server))) {
			books = (ArrayList<Book>) ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Prints a Table of the current loaded Books.
	 * 
	 */
	public static void showBooks() {
		String format = "%32s%32s%32s\n"; // 3 Column format
		System.out.format(format + "\n", "", "--- Show Books ---", "");

		// ### Table ###
		System.out.format(format, "ISBN", "Title", "Author");
		for (Book b : books) {
			System.out.format(format, b.getISBN(), b.getTitle(), b.getAuthor());
		}
	}

	/**
	 * Add a book to the loaded books. If a Book with the same ISBN is already
	 * loaded, the Book is not added.
	 */
	public static void addBook() {
		System.out.println("--- Add Book ---");

		// ### Parameters ###
		System.out.print("ISBN Nummer: ");
		String isbn = scanner.nextLine();
		System.out.print("Title: ");
		String title = scanner.nextLine();
		System.out.print("Surname: ");
		String surname = scanner.nextLine();
		System.out.print("Name: ");
		String name = scanner.nextLine();

		// ### Book exist? ###
		boolean incollection = false;
		for (Book b : books) {
			if (b.getISBN().equals(isbn)) {
				incollection = true;
				break;
			}
		}

		if (incollection) {
			// ### Duplicate Error ###
			System.out.print("ADD ERROR: ");
			System.out.println("The ISBN is already in use.");
		} else {
			// ### Add book ###
			books.add(new Book(isbn, title, surname, name));
		}
	}

	/**
	 * Delete the Book, with the requested ISBN (given by standard input).
	 */
	public static void deleteBook() {
		System.out.println("--- Delete Book ---");

		// ### Request ISBN ###
		System.out.print("ISBN: ");
		String isbn = scanner.nextLine();

		// ### Find book to ISBN ###
		Book todelete = null;
		for (Book b : books) {
			if (b.getISBN().equals(isbn)) {
				todelete = b;
				break;
			}
		}

		// ### Delete book ###
		if (todelete == null) {
			System.out.println("Book not found.");
		} else {
			books.remove(todelete);
		}
	}

	/**
	 * Saving the Books to a given File. If no File is given, than a Server name
	 * is requested.
	 * 
	 * @param server
	 *            The File, to save to.
	 */
	public static void saveBooks(File server) {
		System.out.println("--- Save Books ---");

		// ### Get server ###
		if (server == null) {
			System.out.println("Server name: ");
			String servername = scanner.nextLine();

			if (!servername.endsWith(".ser")) {
				servername = servername + ".ser";
			}

			server = new File(servername);
		}

		// ### Delete old file ###
		if (server.exists()) {
			server.delete();
		}

		// ### Create new file ###
		try {
			server.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}

		// ### Save books ###
		try (ObjectOutputStream oos = new ObjectOutputStream(
				new FileOutputStream(server))) {
			oos.writeObject(books);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// *******************************
	// *----------PRIVAT-------------*
	// *******************************

	private static void printKnownServers() {
		System.out.println("Known Servers:");

		// ### Parameters ###
		String name = null;
		boolean foundone = false;
		File root = new File("./");

		// ### find servers + print ###
		for (File possServer : root.listFiles()) {
			name = possServer.getName();

			if (name.endsWith(".ser")) {
				System.out.println(name.substring(0, name.length() - 4));
				foundone = true;
			}
		}

		// ### No Server found ###
		if (!foundone) {
			System.out.println("(None)\n");
		}
	}

}