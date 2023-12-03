package textExcel;
// Update this file with your own code.

import javax.script.ScriptException;
import java.util.Scanner;
public class TextExcel
{
public static int CELL_SPACE = 20;
	public static void main(String[] args)  {
		Scanner scanner = new Scanner(System.in);;
		Spreadsheet spreadsheet = new Spreadsheet();
		System.out.println(spreadsheet.getGridText());
		String input;
		do {
			input = scanner.nextLine();
			if (!input.equalsIgnoreCase("quit")){System.out.println(spreadsheet.processCommand(input));
			}else{
				System.out.println("Stopping...");
			}
		}while (!input.equalsIgnoreCase("quit"));



		Grid sheet = new Spreadsheet(); // Keep this as the first statement in main
		// TODO finish implementing main by adding your own code here
	}
}
