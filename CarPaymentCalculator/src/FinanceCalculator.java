/*
 * ********************************************************************************************************************
 * 
 * Name: 			Riley Martinez
 * Course Number:	SEIS602 - 01
 * Description: Back-end processing of the Finance Calculator program. Uses the finance loan calculator formula, and
 * 				overloaded "calculate" methods to calculate the needed value based on the user's input:
 * 
 * 				PV = (PMT/i) [1 - (1 / (1 + i)^n], where PV is the loan amount, PMT is the monthly payment, i is the
 * 				monthly interest rate in decimal form, and n is the number of months (number of payments). 	
 * 
 **********************************************************************************************************************
 **/

import java.text.DecimalFormat;

public class FinanceCalculator {
	private double loanAmount; 
	private float interestRate; 
	private int numberOfPayments; 
	private double paymentAmount; 
	
	// Variables used in handling penny loss for 0% apr scenarios
	private float remainder;
	private double payment;
	private int tempRemainder;
	private int hold;
	private double paymentDecimal;
	private double remainderDecimal;
	
	/**
	* Creates format objects to be used in the toString method for returning loanAmount, interestRate, 
	* numberOfPayments, and paymentAmount
	*/
	DecimalFormat formatLoanAmount = new DecimalFormat("$###,###0.00");
	DecimalFormat formatInterestRate = new DecimalFormat("0.0000%");
	DecimalFormat formatNumberOfPayments = new DecimalFormat("###");
	DecimalFormat formatPaymentAmount = new DecimalFormat("$###,###.00");
	
	public FinanceCalculator() {} // default constructor
	
	//-----------------------------------------------------------------------------------------------------------------
	// Calculates loan amount (PV)
	//-----------------------------------------------------------------------------------------------------------------
	public double calculate (float interestRate, int numberOfPayments, double paymentAmount) {
		this.interestRate = (interestRate / 100) / 12;
		this.numberOfPayments = numberOfPayments;
		this.paymentAmount = paymentAmount;
		
		if (this.interestRate == 0.0) {
			this.loanAmount = this.numberOfPayments * this.paymentAmount;
		}
		else {
			this.loanAmount = (this.paymentAmount/this.interestRate) * 
					(1 - (1 / Math.pow((1 + this.interestRate), this.numberOfPayments)));
		}
		return this.loanAmount;	
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	// Calculates interest rate
	// Requires use of Newton Raphson Method to solve for interest rate
	// Initial guess = 2(n * PMT - PV) / n * PV
	//-----------------------------------------------------------------------------------------------------------------
	public float calculate (double loanAmount, int numberOfPayments, double paymentAmount) {
		this.loanAmount = loanAmount;
		this.numberOfPayments = numberOfPayments;
		this.paymentAmount = paymentAmount;
		
		double initialGuessForX = (2 * (this.numberOfPayments * this.paymentAmount - 
				this.loanAmount) / (this.numberOfPayments * this.loanAmount));
		
		this.interestRate = (float) findRoot(initialGuessForX);
		
		return this.interestRate;
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	// Calculates number of months/payments (n)
	//-----------------------------------------------------------------------------------------------------------------
	public int calculate (double loanAmount, float interestRate, double paymentAmount) {
		this.loanAmount = loanAmount;
		this.interestRate = (interestRate / 100) / 12;
		this.paymentAmount = paymentAmount;
		
		if (this.interestRate == 0.0) {
			this.numberOfPayments = (int) (this.loanAmount / this.paymentAmount);
		}
		else
			this.numberOfPayments = (int) (Math.log((this.paymentAmount / 
					this.interestRate) / ((this.paymentAmount/this.interestRate) - 
							this.loanAmount)) / Math.log(1.0 + this.interestRate));
		
		return this.numberOfPayments;
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	// Calculates monthly payment amount (PMT)
	//-----------------------------------------------------------------------------------------------------------------
	public double calculate (double loanAmount, float interestRate, int numberOfPayments) {
		double finalPayment;
		this.loanAmount = loanAmount;
		this.interestRate = (interestRate / 100) / 12;
		this.numberOfPayments = numberOfPayments;
		
		if (this.interestRate == 0.0) {
			if ((this.loanAmount % this.numberOfPayments) == 0) {
				this.paymentAmount = this.loanAmount / this.numberOfPayments;
				return this.paymentAmount;
			}
			else {
				this.remainder = (float) (this.loanAmount % this.numberOfPayments);
				this.payment = ((this.loanAmount - remainder) / this.numberOfPayments);
				this.tempRemainder = (int) ((remainder * 100) % this.numberOfPayments);
				this.hold = (int) ((remainder * 100) / this.numberOfPayments);
				this.paymentDecimal = (double) hold / 100;
				this.remainderDecimal = (double) tempRemainder / 100;
				
				payment = payment + paymentDecimal;
				finalPayment = payment + remainderDecimal;
				return finalPayment;
			}
		}
		else {
			this.paymentAmount = (this.loanAmount * this.interestRate * 
					Math.pow((1 + this.interestRate), this.numberOfPayments)) / 
					(Math.pow((1 + this.interestRate), this.numberOfPayments) - 1);
				
			return this.paymentAmount;
		}
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	// Returns the regular payment amount for scenarios when pennies are lost due to rounding.
	//-----------------------------------------------------------------------------------------------------------------
	public double regularPayment() {
		return this.payment;
	}
	
	// Represents a very small number near zero to be used with findRoot method
	static final double EPSILON = 0.00001; 
	
	//-----------------------------------------------------------------------------------------------------------------
	// f(x) 
	//-----------------------------------------------------------------------------------------------------------------
	private double function(double x) { 
		return ((this.loanAmount * x * Math.pow(1 + x, this.numberOfPayments))
				/(Math.pow(1 + x, this.numberOfPayments) - 1)) - this.paymentAmount; 
	} 
	
	//-----------------------------------------------------------------------------------------------------------------
	// f'(x) 
	//-----------------------------------------------------------------------------------------------------------------
	private double derivativeFunction(double x) { 
		return (this.loanAmount * Math.pow(x + 1, this.numberOfPayments - 1) 
				* (x * Math.pow(x + 1, this.numberOfPayments) + Math.pow(x + 1, this.numberOfPayments) 
				- (this.numberOfPayments * x) - x - 1)) / ((Math.pow(x + 1, this.numberOfPayments) - 1) 
						* (Math.pow(x + 1,  this.numberOfPayments) - 1)); 
	} 
	
	//-----------------------------------------------------------------------------------------------------------------
	// Finds the root of f(x)
	//-----------------------------------------------------------------------------------------------------------------
	private double findRoot(double x) { 
		double a = function(x) / derivativeFunction(x); 
		while (Math.abs(a) >= EPSILON) 
		{ 
			a = function(x) / derivativeFunction(x); 
	
			// x(i+1) = x(i) - f(x) / f'(x) 
			x = x - a; 
		} 
	
		return Math.round(x * 100000.0)/100000.0; 
	} 
	
	//-----------------------------------------------------------------------------------------------------------------
	// Accessors and mutators for finance calculator variables
	//-----------------------------------------------------------------------------------------------------------------
	public double getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(double loanAmount) {
		this.loanAmount = loanAmount;
	}

	public float getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(float interestRate) {
		this.interestRate = interestRate;
	}

	public int getNumberOfPayments() {
		return numberOfPayments;
	}

	public void setNumberOfPayments(int numberOfPayments) {
		this.numberOfPayments = numberOfPayments;
	}

	public double getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	// toString formats all finance calculator variables and prints them.
	//-----------------------------------------------------------------------------------------------------------------
	public String toString() {
		return "Loan Amount (PV): " + formatLoanAmount.format(loanAmount) + "\nNumber of Payments (n): " 
				+ formatNumberOfPayments.format(numberOfPayments) + "\nAnnual Interest Rate (i * 12): " 
				+ formatInterestRate.format(interestRate * 12) 
				+ "\nPayment Amount (PMT): " + formatPaymentAmount.format(paymentAmount); 
	}
	
} 
	
