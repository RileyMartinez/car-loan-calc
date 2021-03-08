/*
 * ********************************************************************************************************************
 * 
 * Name: 			Riley Martinez
 * Course Number:	SEIS602 - 01
 * Description: 	Front-end console UI class that integrates with the FinanceCalculator class via a (has-a)
 * 					composition relationship. Contains the main method which serves as the starting point for the
 * 					Finance Calculator program.
 * 				
 **********************************************************************************************************************
 **/

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class CalculatorMenu {
	private boolean exit;
	private double principal = 0;
	private float apr = 0;
	private int numberOfPayments = 0;
	private double monthlyAmount = 0;
	
	// Creates a FinanceCalculator object to invoke calculate methods
	private FinanceCalculator calculator = new FinanceCalculator(); 
	
	// Creates format objects for the displayed principal, apr, numberOfPayments, and monthlyAmount values.
	DecimalFormat formatPrincipal = new DecimalFormat("$###,###0.00");
	DecimalFormat formatAPR = new DecimalFormat("0.0000%");
	DecimalFormat formatNumberOfPayments = new DecimalFormat("###");
	DecimalFormat formatMonthlyAmount = new DecimalFormat("$###,###.00");
	
	// Stores any results calculated during a session.
	ArrayList<String> savedResults = new ArrayList<String>();
	
	//-----------------------------------------------------------------------------------------------------------------
	// Main method that runs the calculator menu program.
	//-----------------------------------------------------------------------------------------------------------------
	public static void main(String[] args) {
		
		CalculatorMenu menu = new CalculatorMenu();
		menu.runMenu();
		
	}
	
	public void runMenu() {
		while (!exit) {
			printMainMenu();
			int choice = getMenuInput();
			mainMenuSelection(choice);
		}
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	// Method that prints the Main Menu UI.
	//-----------------------------------------------------------------------------------------------------------------
	private void printMainMenu() {
		System.out.println("\n+---------------------------------------------------------------------+\n"
				 + "| Finance Calculator 1.0                                              |\n"
				 + "+---------------------------------------------------------------------+\n\n"
				 + " Please select one of the following options:\n\n"
				 + " 1\tCalculate Monthly Amount\n"
				 + " 2\tCalculate APR\n"
				 + " 3\tCalculate Number of Payments\n"
				 + " 4\tCalculate Principle Amount\n"
				 + " 5\tPrint All Saved Results\n"
				 + " 0\tTo Exit the Application\n\n");                                              
				
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	// Evaluates the Main Menu user selection.
	//-----------------------------------------------------------------------------------------------------------------
	private void mainMenuSelection(int choice) {
		Scanner in = new Scanner(System.in);
		switch(choice) {
		case 0:
			exit = true;
			System.out.println("\n Thank you for using Finance Calculator 1.0.\n"
					 + " Application Terminated.");
			break;
		case 1:
			System.out.println("\n Calculating Monthly Amount . . .");
			this.apr = getAPRInput();
			this.numberOfPayments = getNumberOfPaymentsInput();
			this.principal = getPrincipalInput();
			this.monthlyAmount = calculator.calculate(principal, apr, numberOfPayments);
			if ((this.principal % this.numberOfPayments) != 0) {
				System.out.println("\n Calculated Monthly Amount: " + formatMonthlyAmount.format(this.monthlyAmount)
						+ " for the first payment, and " + formatMonthlyAmount.format(calculator.regularPayment())
						+ " thereafter.");
			}
			else {
			System.out.println("\n Calculated Monthly Amount: " + formatMonthlyAmount.format(this.monthlyAmount));
			}
			if (getSavedResultsInput().equalsIgnoreCase("y")) {
				savedResults.add("APR: " + formatAPR.format(this.apr/100)
				+ "\tNumber of Months: " + formatNumberOfPayments.format(this.numberOfPayments)
				+ "\tPrincipal: " + formatPrincipal.format(this.principal)
				+ "\tCalculated Monthly Amount: " + formatMonthlyAmount.format(this.monthlyAmount));
			}
			break;
		case 2:
			System.out.println("\n Calculating APR . . .");
			this.monthlyAmount = getMonthlyAmountInput();
			this.numberOfPayments = getNumberOfPaymentsInput();
			this.principal = getPrincipalInput();
			this.apr = calculator.calculate(this.principal, this.numberOfPayments, this.monthlyAmount);
			System.out.println("\n Calculated APR: " + formatAPR.format(this.apr*12));
			if (getSavedResultsInput().equalsIgnoreCase("y")) {
				savedResults.add("Monthly Amount: " + formatMonthlyAmount.format(this.monthlyAmount)
				+ "\tNumber of Months: " + formatNumberOfPayments.format(this.numberOfPayments)
				+ "\tPrincipal: " + formatPrincipal.format(this.principal)
				+ "\tCalculated APR: " + formatAPR.format(this.apr*12));
			}
			break;
		case 3:
			System.out.println("\n Calculating Number of Payments . . .");
			this.monthlyAmount = getMonthlyAmountInput();
			this.apr = getAPRInput();
			this.principal = getPrincipalInput();
			this.numberOfPayments = calculator.calculate(this.principal, this.apr, this.monthlyAmount);
			System.out.println("\n Calculated Number of Payments: " + formatNumberOfPayments.format(this.numberOfPayments));
			if (getSavedResultsInput().equalsIgnoreCase("y")) {
				savedResults.add("APR: " + formatAPR.format(this.apr/100)
				+ "\tMonthly Amount: " + formatMonthlyAmount.format(this.monthlyAmount)
				+ "\tPrincipal: " + formatPrincipal.format(this.principal)
				+ "\tCalculated Number of Payments: " + formatNumberOfPayments.format(this.numberOfPayments));
			}
			break;
		case 4:
			System.out.println("\n Calculating Principal . . .");
			this.monthlyAmount = getMonthlyAmountInput();
			this.apr = getAPRInput();
			this.numberOfPayments = getNumberOfPaymentsInput();
			this.principal = calculator.calculate(this.apr, this.numberOfPayments, this.monthlyAmount);
			System.out.println("\n Calculated Principal: " + formatPrincipal.format(this.principal));
			if (getSavedResultsInput().equalsIgnoreCase("y")) {
				savedResults.add("APR: " + formatAPR.format(this.apr/100)
				+ "\tNumber of Months: " + formatNumberOfPayments.format(this.numberOfPayments)
				+ "\tMonthly Amount: " + formatMonthlyAmount.format(this.monthlyAmount)
				+ "\tCalculated Principal: " + formatPrincipal.format(this.principal));
			}
			break;
		case 5:
			System.out.println();
			printAllSavedResults();
			System.out.println("Press Enter to return to the Main Menu");
			in.nextLine();
			break;
		default:
			break;
		}
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	// Prints all calculated results the user saved back to the user.
	//-----------------------------------------------------------------------------------------------------------------
	private void printAllSavedResults() {
		for (int i = 0; i < savedResults.size(); i++) {
			System.out.println("Result " + (i+1) + ":\n" + savedResults.get(i) + "\n");
		}
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	// Prompts the user to make a selection and handles their input for the Main Menu. 
	//-----------------------------------------------------------------------------------------------------------------
	private int getMenuInput() {
		Scanner in = new Scanner(System.in);
		int choice = -1;
		while (choice < 0 || choice > 5) {
			try {
				System.out.print(" \tSelection: ");
				choice = Integer.parseInt(in.nextLine());
				if (choice < 0 || choice > 5) {
					System.out.println("\n Invalid selection. Please enter a value between 0 and 5.\n");
				}
			}
			catch(NumberFormatException e) {
				System.out.println("\n Invalid selection. Please enter a value between 0 and 5.\n");
			}
		}
		return choice;
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	// Prompts the user to enter the Number of Months, and handles the input.
	//-----------------------------------------------------------------------------------------------------------------
	private int getNumberOfPaymentsInput() {
		Scanner in = new Scanner(System.in);
		int input = -1;
		while (input <= 0) {
			try {
				System.out.print("\n Please enter the Number of Months: ");
				input = Integer.parseInt(in.nextLine());
				if (input < 0) {
					System.out.println("\n Invalid entry. Please enter a positive value.");
				}
			}
			catch(NumberFormatException e) {
				System.out.println("\n Invalid entry. Please enter a valid Number of Months.\n"
						+ " Omit any special characters, such as \",\".\n\n"
						+ " For example, \"48\" is a valid input for 48 months.");
			}
		}
		return input;
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	// Prompts the user to enter the Monthly Amount, and handles the input.
	//-----------------------------------------------------------------------------------------------------------------
	private double getMonthlyAmountInput() {
		Scanner in = new Scanner(System.in);
		double input = -1;
		while (input < 0) {
			try {
				System.out.print("\n Please enter the Monthly Amount: ");
				input = Double.parseDouble(in.nextLine());
				if (input < 0) {
					System.out.println("\n Invalid entry. Please enter a positive value.");
				}
			}
			catch(NumberFormatException e) {
				System.out.println("\n Invalid entry. Please enter a valid Monthly Amount.\n"
						+ " Omit any special characters, such as \"$\" or \",\".\n\n"
						+ " For example, \"1741.11\" is a valid input for $1,741.11.");
			}
		}
		return input;
	}

	//-----------------------------------------------------------------------------------------------------------------
	// Prompts the user to enter the Principal Amount, and handles the input.
	//-----------------------------------------------------------------------------------------------------------------
	private double getPrincipalInput() {
		Scanner in = new Scanner(System.in);
		double input = -1;
		while (input < 0) {
			try {
				System.out.print("\n Please enter the Principal Amount: ");
				input = Double.parseDouble(in.nextLine());
				if (input < 0) {
					System.out.println("\n Invalid entry. Please enter a positive value.");
				}
			}
			catch(NumberFormatException e) {
				System.out.println("\n Invalid entry. Please enter a valid Principal Amount.\n"
						+ " Omit any special characters, such as \",\" or \"$\".\n\n"
						+ " For example, \"50000.00\" is a valid input for $50,000.00.");
			}
		}
		return input;
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	// Prompts the user to enter the APR, and handles the input.
	//-----------------------------------------------------------------------------------------------------------------
	private float getAPRInput() {
		Scanner in = new Scanner(System.in);
		float input = -1;
		while (input < 0) {
			try {
				System.out.print("\n Please enter the APR (#.####): ");
				input = Float.parseFloat(in.nextLine());
				if (input < 0) {
					System.out.println("\n Invalid entry. Please enter a positive value.");
				}
			}
			catch(NumberFormatException e) {
				System.out.println("\n Invalid entry. Please enter a valid APR.\n"
						+ " Omit any special characters, such as \"%\".\n\n"
						+ " For example, \"4.5\" is a valid input for 4.5%.");
			}
		}
		return input;
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	// Prompts if they would like to save their calculated results, and handles the input.
	//-----------------------------------------------------------------------------------------------------------------
	private String getSavedResultsInput() {
		Scanner in = new Scanner(System.in);
		String input = "";
		while (!input.equalsIgnoreCase("y") && !input.equalsIgnoreCase("n")) {
		System.out.print("\n Would you like to save these results (Y/N)? ");
		input = in.nextLine();
			if (!input.equalsIgnoreCase("y") && !input.equalsIgnoreCase("n")) {
			System.out.println("\n Invalid entry. Please enter \"Y\" to save the results or \"N\" to discard them.");
			}
		}
		return input;
	}
}
