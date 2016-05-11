package client;

import java.net.UnknownHostException;
import java.util.Scanner;

import de.root1.simon.Lookup;
import de.root1.simon.Simon;
import de.root1.simon.annotation.SimonRemote;
import de.root1.simon.exceptions.EstablishConnectionFailed;
import de.root1.simon.exceptions.LookupFailedException;
import enums.State;
import server.ServerInterface;

@SimonRemote
public class ClientImpl implements ClientCallbackInterface {

	public static void main(String[] args) {
		new ClientImpl();
	}

	public ClientImpl() {
		Lookup nameLookup;
		Scanner sc;
		
		try {
			nameLookup = Simon.createNameLookup("localhost");
//			nameLookup = Simon.createNameLookup("134.94.12.43");
			ServerInterface server = (ServerInterface) nameLookup.lookup("server");
			server.login(this);
			sc = new Scanner(System.in);
			System.out.println("Enter a Number from 1-9 to set a Turn.\n");
			System.out.println(server.getStringBoard());
			
			while(server.getState() == State.NOT_DONE) {
				int row, column, num;
				do{
					System.out.println("Enter a Turn");
					num = Integer.parseInt(sc.nextLine().trim());
					row = (num-1)/3;
					column = (num-1)%3;

				}while(server.place(row, column) == State.FIELD_NOT_EMPTY);
				System.out.println(server.getStringBoard());
			}
			
			System.out.println("Winner:\n"+server.getState());
			nameLookup.release(server);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (LookupFailedException e) {
			e.printStackTrace();
		} catch (EstablishConnectionFailed e) {
			e.printStackTrace();
		}

	}

	public void callback(int[] move) {

	}

}
