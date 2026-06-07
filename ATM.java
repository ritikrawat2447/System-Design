class Card {
	int cardNumber;
	String bankName;
	String expiryDate;
	int csv;
	Card( int cardNumber, String bankName, String expiryDate, int csv ) {
		this.cardNumber = cardNumber;
		this.bankName = bankName;
		this.expiryDate = expiryDate;
		this.csv = csv;
	}
}

interface ATMState {
	public void insertCard(ATM atm);
	public void enterPIN(ATM atm);
	public void selectService(ATM atm);
	public void enterAmount(ATM atm);
	public void cancel(ATM atm);
}


class idleState implements ATMState {
	public void insertCard(ATM atm) {
		System.out.println("Insert Card");
	}
	public void enterPIN(ATM atm) {
		System.out.println("Insert Card");
	}
	public void selectService(ATM atm) {
		System.out.println("Insert Card");
	}
	public void enterAmount(ATM atm) {
		System.out.println("Insert Card");
	}
	public void cancel(ATM atm) {
		System.out.println("Insert Card");
	}
}

class cardInsertState implements ATMState {
	public void insertCard(ATM atm) {
		System.out.println("Card already inserted");
	}
	public void enterPIN(ATM atm) {
		// valid - move to PIN validation
		System.out.println("Please enter PIN");
		atm.setState(new PinValidationState());
	}
	public void selectService(ATM atm) {
		System.out.println("Enter PIN first");
	}
	public void enterAmount(ATM atm) {
		System.out.println("Enter PIN first");
	}
	public void cancel(ATM atm) {
		System.out.println("Wait till Pin Validation");
	}
}

class pinValidationState implements ATMState {
	public void insertCard(ATM atm) {
		System.out.println("Card already Inserted");
	}
	public void enterPIN(ATM atm) {
		BankService bs = atm.getBankService();
		int pin = atm.getEnteredPIN();
		int cardNumber = atm.getCard().getCardNumber();

		if (bs.verifyPin(cardNumber, pin)) {
			System.out.println("PIN Validated");
			atm.setState(new AuthenticatedState());
		} else {
			System.out.println("Wrong PIN");
			atm.setState(new OutOfServiceState());
		}
	}
	public void selectService(ATM atm) {
		System.out.println("Wait till Pin Validation");
	}
	public void enterAmount(ATM atm) {
		System.out.println("Wait till Pin Validation");
	}
	public void cancel(ATM atm) {
		System.out.println("Wait till Pin Validation");
	}
}

class transactionState implements ATMState {
	public void insertCard(ATM atm) {
		System.out.println("Transaction is going on");
	}
	public void enterPIN(ATM atm) {
		System.out.println("Transaction is going on");
	}
	public void selectService(ATM atm) {
		System.out.println("Transaction is going on");
	}
	public void enterAmount(ATM atm) {
		System.out.println("Transaction is going on");
	}
	public void cancel(ATM atm) {
		System.out.println("Transaction is going on");
	}
}

class cashDispenseState implements ATMState {
	public void insertCard(ATM atm) {
		System.out.println("Cash is Dispensing");
	}
	public void enterPIN(ATM atm) {
		System.out.println("Cash is Dispensing");
	}
	public void selectService(ATM atm) {
		System.out.println("Cash is Dispensing");
	}
	public void enterAmount(ATM atm) {
		System.out.println("Cash is Dispensing");
	}
	public void cancel(ATM atm) {
		System.out.println("Cash is Dispensing");
	}
}

class outOfServiceState implements ATMState {
	public void insertCard(ATM atm) {
		System.out.println("ATM is out of Service");
	}
	public void enterPIN(ATM atm) {
		System.out.println("ATM is out of Service");
	}
	public void selectService(ATM atm) {
		System.out.println("ATM is out of Service");
	}
	public void enterAmount(ATM atm) {
		System.out.println("ATM is out of Service");
	}
	public void cancel(ATM atm) {
		System.out.println("ATM is out of Service");
	}
}

class ATM {
	ATMState currentState;
	Card card;
	BankService bankService;
	CashDispenser cashDispenser;
	int pin;

	ATM(BankService bankService, CashDispenser cashDispenser) {
		this.bankService = bankService;
		this.cashDispenser = cashDispenser;
		this.currentState = new IdleState();
	}
	public void insertCard(Card card) {
		this.card = card;
		currentState.insertCard(this);
	}
	public void enterPIN(int pin) {
		this.pin = pin;
		currentState.enterPIN(this);
	}
	public void selectService(String type) {
		currentState.selectService(this);
	}
	public void enterAmount(int amount) {
		currentState.enterAmount(this);
	}
	public void cancel() {
		currentState.cancel(this);
	}

	public void setState(ATMState state) { this.currentState = state; }
	public void setCard(Card card) { this.card = card; }
	public void ejectCard() {
		this.card = null;
		System.out.println("Card ejected");
	}
	public Card getCard() { return card; }
	public BankService getBankService() { return bankService; }
	public CashDispenser getCashDispenser() { return cashDispenser; }
	public int getEnteredPIN() { return pin; }
}


interface  BankService {
	public boolean verifyPin(int cardNumber, int pin);
	public boolean verifyCardValid(int cardNumber, String expiryDate);
	public boolean isAmtValid(int cardNumber, int amt);
	public void processTransaction(int cardNumber, int amt);
	public void updateBankAccount(int cardNumber, int amt);
}

class HDFCBankService implements BankService {
	public boolean verifyPin(int cardNumber, int pin) {
		return pin == 1234;
	}
	public boolean verifyCardValid(int cardNumber, String expiryDate) {
		return true;
	}
	public boolean isAmtValid(int cardNumber, int amt) {
		return amt <= 50000;
	}
	public void processTransaction(int cardNumber, int amt) {
		System.out.println("HDFC processing: " + amt);
	}
	public void updateBankAccount(int cardNumber, int amt) {
		System.out.println("HDFC account updated");
	}
}

class Receipt {
	int totalBalance;
	Receipt(int totalBalance) {
		this.totalBalance = totalBalance;
	}
	public void printStatement() {
		System.out.println("Current balance after transaction is " + totalBalance);
	}
}

abstract class Transaction {
	int amount;
	BankService bankService;
	Receipt receipt;
	CashDispenser cashDispenser;

	Transaction(int amount, BankService bs,
	            Receipt r, CashDispenser cd) {
		this.amount = amount;
		this.bankService = bs;
		this.receipt = r;
		this.cashDispenser = cd;
	}

	public void printReceipt() {
		receipt.printStatement();
	}

	public abstract void enterAmount();
	public abstract void verify();
	public abstract void process();
}

class WithdrawlTransaction extends Transaction {
	WithdrawalTransaction(int amount, BankService bs, Receipt reciept, CashDispenser cd) {
		super(amount, bs, reciept, cd); // use parent constructor
	}

	public void enterAmount() {
		System.out.println("Enter withdrawal amount");
	}
	public void verify() {
		bankService.verifyAmt(amount);
		cd.veriftAmt(amount);
	}
	public void process() {
		System.out.println("Dispensing Ruppes " + amount );
		cashDispenser.dispense();
		cashDispenser.updateInventory();
		bs.updateBankAccount();
		reciept.printStatement();
	}
}

class DepositTransaction extends Transaction {
	DepositTransaction(int amount, BankService bs, Receipt reciept, CashDispenser cd) {
		super(amount, bs, reciept, cd); // use parent constructor
	}

	public void enterAmount() {
		System.out.println("Enter deposit amount");
	}
	public void verify() {
		System.out.println("Validating deposit notes");
	}
	public void process() {
		System.out.println("Credit Ruppes " + amount );
		bs.updateBankAccount();
		reciept.printStatement();
	}
}

class CashDispensser {
	int total2000;
	int total500;
	int total200;
	int total100;
	CashDispenser( int total2000, int total500, int total200, int total100) {
		this.total2000 = total2000;
		this.total500 = total500;
		this.total200 = total200;
		this.total100 = total100;
	}

	public boolean verifyAmount( int amount ) {
		int total = (total2000 * 2000) + (total500 * 500) + (total200 * 200) + (total100 * 100);
		return total >= amount;
	}
	public void calculateDenomination() {
		System.out.println("Calculating denomination for: " + amount);
	}
	public void dispense() {
		System.out.println("Cash is Despensing");
	}
	public void updateInventory() {
		System.out.println("Inventory updated");
	}
}

class Main {
	public static void main( String args[] ) {
		BankService bankService = new HDFCBankService();
		CashDispenser cd = new CashDispenser(100, 200, 200, 500);
		ATM atm = new ATM(bankService, cd);
		Card card = new Card(123456781234, "HDFC", "10/31", 234);

		atm.insertCard(card);
		atm.enterPIN(1234);
		atm.selectService("W");
		atm.enterAmount(2500);

	}
}