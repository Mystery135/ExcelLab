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
		Constants.CELL_SPACE = Utils.getInt(scanner);
		scanner.nextLine();

		System.out.print("Spreadsheet height: ");
		height = Utils.getInt(scanner);
		scanner.nextLine();

		System.out.print("Spreadsheet width: ");
		width = Utils.getInt(scanner);
		scanner.nextLine();

		Spreadsheet spreadsheet = new Spreadsheet(height, width);
		System.out.println(spreadsheet.getGridText());
		String input;

		//////////////////////////////////////////////////User guide
		System.out.println();
		System.out.println("Commands available:");
		System.out.println("- history start/display/clear/stop");
		System.out.println("- <cell> = <value> (ex. a1 = 1 or a1 = \"apple\" or ex. a1 = (a2 + a3) or a1 = 100%)");
		System.out.println("- import");
		System.out.println("- save");
		System.out.println();
		System.out.println();
		//////////////////////////////////////////////////

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
