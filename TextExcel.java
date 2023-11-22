package textExcel;
// Update this file with your own code.

import java.util.Scanner;

public class TextExcel
{

	public static void main(String[] args)
	{
		Scanner scanner = new Scanner(System.in);
		String input;
		do {
			input = scanner.nextLine();

		}while (!input.equalsIgnoreCase("quit"));



		Grid sheet = new Spreadsheet(); // Keep this as the first statement in main
		// TODO finish implementing main by adding your own code here
	}
}
