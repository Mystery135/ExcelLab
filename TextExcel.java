package textExcel;
// Update this file with your own code.

import textExcel.helpers.Utils;

import java.util.Scanner;
public class TextExcel
{
	public static void main(String[] args)  {
		//Sets up user input & spreadsheet




		int height, width;
		Scanner scanner = new Scanner(System.in);
		System.out.print("Cell space: ");
		Constants.CELL_SPACE = Utils.getPositiveNonZeroInt(scanner);
		scanner.nextLine();

		System.out.print("Spreadsheet height: ");
		height = Utils.getPositiveNonZeroInt(scanner);
		scanner.nextLine();

		System.out.print("Spreadsheet width: ");
		width = Utils.getPositiveNonZeroInt(scanner);
		scanner.nextLine();

		Spreadsheet spreadsheet = new Spreadsheet(height, width);
		System.out.println(spreadsheet.getGridText());
		String input;

//Main loop which gets and processes user input. Loops until user types quit.
		do {
			input = scanner.nextLine();
			if (!input.equalsIgnoreCase("quit")){System.out.println(spreadsheet.processCommand(input));
			}else{
				System.out.println("Stopping...");
			}
		}while (!input.equalsIgnoreCase("quit"));
	}
}
