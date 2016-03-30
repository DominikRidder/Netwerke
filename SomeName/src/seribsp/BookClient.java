package seribsp;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.plaf.synth.SynthSeparatorUI;

public class BookClient {

	// TODO: declare book container
	static Scanner scanner = new Scanner(System.in);
	static File bookFile;

	static ArrayList<Book> books = new ArrayList<Book>();

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
			scanner.nextLine(); // read \n
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
	}

	/**
	 * Loading the Books given in a File. The Filename is requested with a
	 * Dialog via stdout and stdin.
	 * 
	 * @param server
	 *            The File, that is used and returned, containing the Books.
	 */
	public static void loadBooks(File server) {
		books.clear();
		System.out.print("Server name: ");
		server = new File(scanner.nextLine());

		if (!server.exists()) {
			System.out.println("Server existiert nicht!");
			return;
		}

		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(server))) {
			while (true) {
				Book nextBook = (Book) ois.readObject();
				books.add(nextBook);
			}

		} catch (EOFException e) {
			// exit point
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Writes out a the current loaded Books as a list of csv values.
	 * 
	 * Therefore is calling the toString method of Book class.
	 */
	public static void showBooks() {
		for (Book b : books) {
			System.out.println(b);
		}
	}

	/**
	 * Adds a method to the loaded books. If a Book with the same ISBN Number is
	 * already loaded, the Book is .
	 */
	public static void addBook() {
		System.out.print("ISBN Nummer: ");
		String isbn = scanner.nextLine();
		System.out.print("Vorname: ");
		String surname = scanner.nextLine();
		System.out.print("Nachname: ");
		String name = scanner.nextLine();

		boolean incollection = false;

		for (Book b : books) {
			if (b.getISBN().equals(isbn)) {
				incollection = true;
				break;
			}
		}

		if (incollection) {
			System.out.println("Hinzufuegen fehlgeschlagen.");
			System.out.println("Die ISBN Nr wurde bereits verwendet.");
		} else {
			books.add(new Book(isbn, surname, name));
		}
	}

	public static void deleteBook() {
		System.out.println("Welches Buch soll geloescht werden?");
		System.out.print("ISBN Nr: ");
		String isbn = scanner.nextLine();

		boolean exist = false;

		for (Book b : books) {
			if (b.getISBN().equals(isbn)) {
				books.remove(b);
				exist = true;
			}
		}

		if (!exist) {
			System.out.println("Buch wurde nicht gefunden.");
		}
	}

	public static void saveBooks(File server) {
		// Get Server
		if (server == null) {
			System.out.println("Server name: ");
			server = new File(scanner.nextLine());
		}

		// Remove old stuff
		if (server.exists()) {
			server.delete();
		}

		// Create File
		try {
			server.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}

		// Write Data
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(server))) {

			for (Book nextBook : books) {
				oos.writeObject(nextBook);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}