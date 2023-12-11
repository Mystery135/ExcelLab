package textExcel;

import textExcel.helpers.Utils;

import java.io.File;
import java.io.PrintWriter;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static textExcel.helpers.Utils.formatText;

public class Spreadsheet implements Grid {
    int cellWidth;
    int cellHeight;
    History history;
    int historyDepth = 0;
    boolean addedHistory = false;
    private Cell[][] cells;

	//Creates a 2d array of cells and fills it with emptycells.
    public Spreadsheet(int height, int width) {
        cellWidth = width;
        cellHeight = height;
        cells = new Cell[cellHeight][cellWidth];
        System.out.println(cellHeight + "," + cells.length);
        System.out.println(cellWidth + "," + cells[1].length);
        for (int i = 0; i < cellHeight; i++) {
            for (int j = 0; j < cellWidth; j++) {
                cells[i][j] = new EmptyCell();
            }
        }
    }

    @Override
    public String processCommand(String command) {
        if (!command.contains("history") || command.contains("\"")) {
            if (history != null) {//If there's a history array, add the command to history
                history.add(command);
                addedHistory = true;
            }
        }

        //Try/catch so that if code errors, the program won't stop
        try {

			//Clears spreadsheet by replacing everything with emptycells
            if (command.equalsIgnoreCase("clear")) {
                for (int i = 0; i < cellWidth; i++) {
                    for (int j = 0; j < cellHeight; j++) {
                        cells[i][j] = new EmptyCell();
                    }
                }
                return getGridText();
            }

			//If user command specified a location, only clear that location
			else if (command.contains("clear") && !command.contains("\"") && !command.contains("history")) {
                command = formatText(command);
                String position = formatText(formatText(command.replace("CLEAR", "")));
                SpreadsheetLocation location = new SpreadsheetLocation(position);
                cells[location.getRow()][location.getCol()] = new EmptyCell();
                return getGridText();
            }

			//Processes history start/display/clear/stop
			else if (!command.contains("\"") && formatText(command.split(" ")[0]).equalsIgnoreCase("history")) {

				//Gets the second and third words of the command
                List<String> args;
                args = List.of(command.split(" "));

				//Makes sure the command has a positive nonzero int and creates a new history with a length of that number.
                if (formatText(args.get(1)).equalsIgnoreCase("start")) {
                    try {
                        if (Integer.parseInt(formatText(args.get(2))) > 0) {
                            historyDepth = Integer.parseInt(formatText(args.get(2)));
                            history = new History(historyDepth);
                        } else {
                            System.out.println("Please input a nonzero positive integer for the length of the history.");
                            historyDepth = Utils.getInt(new Scanner(System.in));
                            history = new History(historyDepth);
                        }
                    } catch (Exception e) {
                        System.out.println("Please input a nonzero positive integer for the length of the history.");
                        historyDepth = Utils.getInt(new Scanner(System.in));
                        history = new History(historyDepth);
                    }
                    return "History started of length " + historyDepth + "! Type history display to display history.";

				//Displays the history if it exists
                } else if (formatText(args.get(1)).equalsIgnoreCase("display")) {
                    if (history == null) {
                        return "No history started yet! Type \"history start n\" to start history.";
                    }
                    return history.toString();

				//Clears n elements of the history or the whole history if no number specified
                } else if (formatText(args.get(1)).equalsIgnoreCase("clear")) {
                    if (history == null) {
                        return "No history started yet! Type \"history start n\" to start history.";
                    }
                    if (args.size() < 3) {
                        history.clear();
                        return "All history cleared!";
                    }
                    int clearLength;
                    try {
                        if (Integer.parseInt(formatText(args.get(2))) < 0) {
                            throw new InvalidParameterException("Clear length cannot be negative");
                        }
                        clearLength = Math.min(Integer.parseInt(formatText(args.get(2))), history.size());
                    } catch (Exception e) {
                        System.out.print("Please input a nonzero positive integer for how much history to clear: ");
                        clearLength = Utils.getInt(new Scanner(System.in));
                        if (clearLength <= history.size()) {
                            clearLength = Integer.parseInt(formatText(args.get(2)));
                        } else {
                            clearLength = history.size();
                        }
                    }
                    for (int i = 0; i < clearLength; i++) {
                        history.remove(0);
                    }
                    return clearLength + " history entr" + (clearLength == 1 ? "y" : "ies") + " cleared! Type history display to display history.";

				//Removes current history object
                } else if (formatText(args.get(1)).equalsIgnoreCase("stop")) {
                    history = null;
                    return "History stopped.";
                }

			//Writes all data from the spreadsheet to a .csv file with a name chosen by the user
            } else if (formatText(command).equalsIgnoreCase("save")) {
                Scanner scanner = new Scanner(System.in);
				System.out.println("What do you want to name your file? (If you name your file the same as an existing .csv file, you will overwrite it.)");
                System.out.println("ex. myData");
                System.out.println("You can also specify a path (ex. C:\\Users\\user\\Downloads\\data)");
                String fileName = scanner.nextLine();
                File file = new File(fileName + ".csv");
                if (!file.exists()) {
                    file.createNewFile();
                }
                PrintWriter printWriter = new PrintWriter(file);
                StringBuilder lineBuilder = new StringBuilder();
                for (int i = 0; i < getRows(); i++) {
                    for (int j = 0; j < getCols(); j++) {
                        lineBuilder.append(cells[i][j].fullCellText()).append(",");
                    }
                    printWriter.println(lineBuilder);
                    lineBuilder = new StringBuilder();
                }

                printWriter.close();
                return "Saved! Path: " + file.getAbsolutePath();

			//First asks the user if they want to save the current spreadsheet, then reads a file and imports the data
            } else if (formatText(command).equalsIgnoreCase("import")) {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Do you want to save this current file first? (Y/N)");
                String save = Utils.getInput(scanner, List.of("Y", "N"), "Type \"Y\" for yes and \"N\" for no!");
                if (save.equalsIgnoreCase("Y")) {
                    System.out.println(processCommand("save"));
                    System.out.println();
                }
                System.out.println("What is the name of the file you want to import? (ex. data.csv.)");
                System.out.println("If the file is not in this project's folder, you may need to specify the file path (ex. C:\\Users\\user\\Downloads\\data)");

                String filePath = scanner.nextLine();

                try {
                    File file = new File(filePath);
                    if (!file.exists()) {//Return early if no file exists with the name specified
                        return "No file with that name exists! Maybe you forgot the file extension? (.csv)";
                    }
                    Scanner fileReader = new Scanner(file);
                    StringBuilder data = new StringBuilder();
                    while (fileReader.hasNextLine()) {
                        data.append(fileReader.nextLine()).append("\n");
                    }
                    ArrayList<ArrayList<Cell>> cellArrayList = new ArrayList<>();

					//Converts data to a string, the splits the string into an array for every newline character
                    ArrayList<String> lines = new ArrayList<>(List.of(data.toString().split("\\r?\\n")));

                    for (int i = 0; i < lines.size(); i++) {
                        cellArrayList.add(new ArrayList<>());

                        for (int j = 0; j < lines.get(i).split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1).length; j++) {//Split for every comma, but excluding commas between quotation marks. -1 ensures that emptycells are looped through.
                            List<String> cells = List.of(lines.get(i).split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1));

                            if (Utils.isDouble(cells.get(j))) {
                                cellArrayList.get(i).add(new ValueCell(cells.get(j)));
                                continue;//continue if cell is successfully assigned

                            }

                            if (cells.get(j).contains("%")) {
                                if (Utils.isDouble(cells.get(j), "%")) {
                                    cellArrayList.get(i).add(j, new PercentCell(cells.get(j)));
                                    continue;//continue if cell is successfully assigned
                                }
                            }
                            if (cells.get(j).contains("\"")) {
                                cellArrayList.get(i).add(j, new TextCell(cells.get(j).substring(1, cells.get(j).length() - 1)));
                                continue;
                            }
                            String cellValue = formatText(cells.get(j));
                            if (cellValue.matches(".*[+\\-*/^].*") || Pattern.compile("[A-Za-z]+\\d+").matcher(cellValue).find()) {
                                //Formulas are not working yet
                                HashSet<String> listOfCellsInFormula = new HashSet<>();
                                Matcher matcher = Pattern.compile("[A-Za-z]+\\d+").matcher(cellValue);// Use a Matcher to find all cell references in the cell value
                                while (matcher.find()) {
                                    listOfCellsInFormula.add(matcher.group());
                                }
                                cellArrayList.get(i).add(j, new FormulaCell(cells.get(j), listOfCellsInFormula, this.cells));
                                continue;
                            }

                            cellArrayList.get(i).add(j, new EmptyCell());//adds an emptycell if nothing matches
                        }
                    }

                    fileReader.close();

					//Finds max cell row if jagged array
                    int max = 0;
                    for (ArrayList<Cell> arrayList : cellArrayList) {
                        if (arrayList.size() > max) {
                            max = arrayList.size() - 1;
                        }
                    }

                    this.cells = new Cell[cellArrayList.size()][max];

                    //Imports all collected data
                    for (int i = 0; i < cellArrayList.size(); i++) {
                        for (int j = 0; j < max; j++) {
                            while (cellArrayList.get(i).size() < max) {
                                cellArrayList.get(i).add(new EmptyCell());
                            }
                            this.cells[i][j] = cellArrayList.get(i).get(j);
                        }
                    }
                    return getGridText();
                } catch (Exception e) {
                    //If something goes wrong, return invalid file
                    return "Invalid file.";
                }
            } else if (command.contains("=")) {//If command contains =, process cell assignation
                return processCellAssignation(command);

            } else if (command.toCharArray().length > 1) {//Process cell inspection if command contains a cell
                if (Character.isDigit(command.toCharArray()[command.toCharArray().length - 1]) && Character.isLetter(command.toCharArray()[0])) {
                    return processCellInspection(formatText(command));
                }
            }
            if (history != null) {//Prevents typos from being put into the history array
                history.remove(history.size() - 1);
                addedHistory = false;
            }
            return Constants.INVALID_COMMAND;
        } catch (Exception e) {
            if (addedHistory && history != null) {//Prevents typos from being put into the history array
                history.remove(history.size() - 1);
                addedHistory = false;
            }
            return Constants.INVALID_COMMAND;
        }


    }

    private String processCellInspection(String command) {//returns value of cell specified
        SpreadsheetLocation location = new SpreadsheetLocation(command);
        return getCell(location).fullCellText();
    }

    //Assigns cell values
    private String processCellAssignation(String command) {
        String coordinates;
        String value;
        if ((command.contains("(") || command.contains(")")) && !command.contains("\"")) {//Parenthesis not implemented yet, so this removes them
            command = command.replace("(", "").replace(")", "");
        }

        //Splits command into coordinate and value
        String[] commandParts = command.split("=");
        if (commandParts.length < 2) {
            throw new IllegalArgumentException();
        }
        coordinates = commandParts[0];
        value = commandParts[1];
        coordinates = formatText(coordinates);

        //Creates a textcell if command contains a "
        if (command.contains("\"")) {
            String strValue = value.split("\"")[1];
            SpreadsheetLocation location = new SpreadsheetLocation(coordinates);
            cells[location.getRow()][location.getCol()] = new TextCell(strValue);
        }
        //Creates a textcell if command contains a %
        else if (command.contains("%") && Utils.isDouble(value, "%")) {
            value = formatText(value);
            SpreadsheetLocation location = new SpreadsheetLocation(coordinates);
            cells[location.getRow()][location.getCol()] = new PercentCell(value);
        }
        //Creates a textcell if command contains a numerical value
        else if (formatText(value).matches("[+-]?\\d+(\\.\\d+)?")) {
            value = formatText(value);
            SpreadsheetLocation location = new SpreadsheetLocation(coordinates);
            cells[location.getRow()][location.getCol()] = new ValueCell(value);
        }

        //Evaluates command to see if its a function
        else {
            boolean isFunction = value.matches(".*[+\\-*/^].*");//If value has +, -, *, /, or ^
            HashSet<String> listOfCellsInFormula = new HashSet<>();
            value = formatText(value);
            Matcher matcher = Pattern.compile("[A-Z]+\\d+").matcher(value);//Creates a matcher to see if value has a cell coordinate (a capital letter + a number)


            while (matcher.find()) {
                listOfCellsInFormula.add(matcher.group());
                isFunction = true; //if there's at least one cell coordinate, then it's a function
            }

            if (!isFunction) {//If the command has neither +, -, *, /, ^ or a cell coordinate, it is not a function.
                if (history != null) {
                    history.remove(history.size() - 1);
                }
                return Constants.INVALID_COMMAND;
            } else {//If command is a function, process function
                processFunction(value, coordinates, listOfCellsInFormula);
            }
        }
        return getGridText();
    }


    //Processes a formula by replacing cells with their double values
    private void processFunction(String value, String coordinates, HashSet<String> listOfCellsInFormula) {
        SpreadsheetLocation location = new SpreadsheetLocation(coordinates);
        if (value.contains(coordinates)) {
            if (cells[location.getRow()][location.getCol()] instanceof RealCell) {
                value = value.replace(coordinates, String.valueOf(((RealCell) cells[location.getRow()][location.getCol()]).getDoubleValue()));
            } else {
                value = value.replace(coordinates, cells[location.getRow()][location.getCol()].fullCellText());
            }
            listOfCellsInFormula.remove(coordinates);
        }
        FormulaCell formulaCell = new FormulaCell(value, listOfCellsInFormula, cells);
        cells[location.getRow()][location.getCol()] = formulaCell;

    }


    @Override
    public int getRows() {
        return cells.length;
    }

    @Override
    public int getCols() {
        return cells[0].length;
    }

    @Override
    public Cell getCell(Location loc) {
        return cells[loc.getRow()][loc.getCol()];
    }


    //returns grid text using stringbuilder
    @Override
    public String getGridText() {
        StringBuilder gridText = new StringBuilder();
        int numSpaces = String.valueOf(getRows()).length() + 1;
        gridText.append(" ".repeat(numSpaces)).append("|");
        for (int i = 0; i < getCols(); i++) {
            gridText.append((char) (i + 65));
            String repeatedSpaces = " ".repeat(Constants.CELL_SPACE - 1);
            gridText.append(repeatedSpaces).append("|");
        }
        gridText.append("\n");
        for (int i = 0; i < getRows(); i++) {
            int currentNumSpaces = numSpaces - String.valueOf((i + 1)).length();
            String repeatedSpaces = " ".repeat(currentNumSpaces);
            gridText.append(i + 1).append(repeatedSpaces).append("|");
            for (int j = 0; j < getCols(); j++) {
                gridText.append(cells[i][j].abbreviatedCellText()).append("|");
            }
            gridText.append("\n");
        }
        return gridText.toString();
    }
}
