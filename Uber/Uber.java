class Customer {
	String customerName;
	String customerNumber;
	int customerRating;
	String cutomerGmail;
	int inAppCash;
	int totalRides;

	Customer( String customerName, Stirng customerNumber, int customerRating,
	          String customerGmail, int inAppCash, int totalRides ) {
		this.customerName = customerName;
		this.customerNumber = customerNumber;
		this.customerRating = customerRating;
		this.customerGmail = customerGmail;
		this.inAppCash = inAppCash;
		this.totalRides = totalRides;
	}
}

class Driver {
	String driverName;
	String driverNumber;
	int driverRating;
	Vehicle vehicle;
	boolean onRide;

	Driver(String driverName, String driverNumber,
	       int driverRating, Vehicle vehicle, boolean onRide) {
		this.driverName = driverName;
		this.driverNumber = driverNumber;
		this.driverRating = driverRating;
		this.vehicle = vehicle;
		this.onRide = onRide;
	}
}

abstract class Vehicle {
	String vehicleType;
	String vehicleNumber;
	int vehicleChalans;
	String vehicleColor;
	String vehicleModel; // instead of brand, we can have the model instead easy to identify
	// in future vehicle can have more functions, like vehicle state(moving, stoped,..) can be used for // maps and for more security reason for passenger

	Vehicle(String vehicleType, String vehicleNumber,
	        int vehicleChallans, String vehicleColor, String vehicleModel) {
		this.vehicleType = vehicleType;
		this.vehicleNumber = vehicleNumber;
		this.vehicleChallans = vehicleChallans;
		this.vehicleColor = vehicleColor;
		this.vehicleModel = vehicleModel;
	}
}

class Car extends Vehicle {
	Car(String vehicleNumber, String vehicleColor, String vehicleModel) {
		super("car", vehicleNumber, 0, vehicleColor, vehicleModel);
	}
}

class Bike extends Vehicle {
	Bike(String vehicleNumber, String vehicleColor, String vehicleModel) {
		super("bike", vehicleNumber, 0, vehicleColor, vehicleModel);
	}
}

class Auto extends Vehicle {
	Auto(String vehicleNumber, String vehicleColor, String vehicleModel) {
		super("auto", vehicleNumber, 0, vehicleColor, vehicleModel);
	}
}

class Ride {
	int rideId;
	String pickupLocation;
	String destination;
	int rideCost;
	RideState currentState;
	Customer customer;
	Driver driver;
	int otp;
	Payment payment;

	Ride( Customer customer, String pickUp, String dest  ) {
		this.customer = customer;
		this.pickupLocation = pickUp;
		this.destination = dest;
		generateOTP();
	}

	void generateOTP() {
		// uniq code generated for each ride
		otp = 123;
	}

	public void logRideHistory() {
		// store ride details in db
	}
}

interface RideState {
	void requestRide(Ride ride);
	void assignRide(Ride ride);
	void verifyOTPRide(Ride ride, int otp);
	void cancelRide(Ride ride);
	void completeRide(Ride ride);
}

class SearchingState implements RideState {

	@Override
	public void requestRide(Ride ride) {
		System.out.println("Ride already requested.");
	}

	@Override
	public void assignRide(Ride ride) {
		System.out.println("Driver assigned is " + ride.driver.driverName);
		ride.currentState = new DriverAssignedState();
	}

	@Override
	public void verifyOTPRide(Ride ride, int otp) {
		System.out.println("Cannot verify OTP before driver assignment.");
	}

	@Override
	public void cancelRide(Ride ride) {
		System.out.println("Ride cancelled");
		ride.currentState = new CancelledState();
	}

	@Override
	public void completeRide(Ride ride) {
		System.out.println("Ride has not started yet.");
	}
}

class DriverAssignedState implements RideState {

	@Override
	public void requestRide(Ride ride) {
		System.out.println("Ride already requested.");
	}

	@Override
	public void assignRide(Ride ride) {
		System.out.println("Driver already assigned.");
	}

	@Override
	public void verifyOTPRide(Ride ride, int Otp) {
		if ( Otp == ride.otp ) {
			System.out.println("OTP Verified. Let's Begin the Ride!!");
			ride.currentState = new OngoingState();
		} else {
			System.out.println("Invalid Otp");
		}
	}

	@Override
	public void cancelRide(Ride ride) {
		System.out.println("Ride cancelled");
		ride.currentState = new CancelledState();
	}

	@Override
	public void completeRide(Ride ride) {
		System.out.println("Ride has not started yet.");
	}
}

class ArrivedState implements RideState {
	@Override
	public void requestRide(Ride ride) {
		System.out.println("Ride already requested.");
	}

	@Override
	public void assignRide(Ride ride) {
		System.out.println("Driver already assigned.");
	}

	@Override
	public void verifyOTPRide(Ride ride, int Otp) {
		System.out.println("OTP already verified");
	}

	@Override
	public void cancelRide(Ride ride) {
		System.out.println("Can't cancel the Ride, u are now close to the destination");
	}

	@Override
	public void completeRide(Ride ride) {
		System.out.println("Reached the destination waiting for the payment!!");
		ride.currentState = new CompletedState();
	}
}

class OngoingState implements RideState {

	@Override
	public void requestRide(Ride ride) {
		System.out.println("Ride already requested.");
	}

	@Override
	public void assignRide(Ride ride) {
		System.out.println("Driver already assigned.");
	}

	@Override
	public void verifyOTPRide(Ride ride, int Otp) {
		System.out.println("OTP already verified");
	}

	@Override
	public void cancelRide(Ride ride) {
		System.out.println("Ride cancelled");
		ride.currentState = new CancelledState();
	}

	@Override
	public void completeRide(Ride ride) {
		System.out.println("Ride is ongoing rightNow");
	}
}

class CompletedState implements RideState {
	@Override
	public void requestRide(Ride ride) {
		System.out.println("Ride is getting cancelled");
	}

	@Override
	public void assignRide(Ride ride) {
		System.out.println("Ride is getting cancelled");
	}

	@Override
	public void verifyOTPRide(Ride ride, int Otp) {
		System.out.println("Ride is getting cancelled");
	}

	@Override
	public void cancelRide(Ride ride) {
		System.out.println("Ride Completed already, can't cancel");
	}

	@Override
	public void completeRide(Ride ride) {
		System.out.println("Ride Completed!!");
	}
}

class CancelledState implements RideState {
	@Override
	public void requestRide(Ride ride) {
		System.out.println("Ride already cancelled");
	}

	@Override
	public void assignRide(Ride ride) {
		System.out.println("Ride already cancelled");
	}

	@Override
	public void verifyOTPRide(Ride ride, int Otp) {
		System.out.println("Ride already cancelled");
	}

	@Override
	public void cancelRide(Ride ride) {
		System.out.println("Ride Cancelled Succesfully!!");
		ride.currentState = new SearchingState();
	}

	@Override
	public void completeRide(Ride ride) {
		System.out.println("Ride already cancelled");
	}
}

interface DriverMatchingStrategy {
	Driver findDriver();
}

class NearestDriver implements DriverMatchingStrategy {

	List<Driver> allDrivers;
	String pickupLocation;

	NearestDriver(List<Driver> allDrivers, String pickupLocation) {
		this.allDrivers = allDrivers;
		this.pickupLocation = pickupLocation;
	}

	@Override
	public Driver findDriver() {
		System.out.println("Finding nearest driver");
		return allDrivers.get(0);
	}
}

class FareCalculation {
	String pickupLocation;
	String destination;
	String vehicleType;

	public int calculateFare() {

	}
}

class RideService {

	DriverMatchingStrategy driverMatchingAlgo;
	FareCalculation fareCalculator;

	RideService(DriverMatchingStrategy strategy, FareCalculation fareCalc) {
		this.driverMatchingAlgo = strategy;
		this.fareCalculator = fareCalc;
	}

	public Ride serachRide(Customer customer, String pickup, String dest) {
		Ride ride = new Ride(customer)
		ride.currentState = new SearchingRide();
		ride.rideCost = fareCalculator.calculateFare();
		ride.driver = driverMatchingAlgo.findDriver();
		ride.currentState.assignRide(ride);
		return ride;
	}

	public void beginTrip(Ride ride, int otp ) {
		ride.curretState.verifyOTP(ride, otp);
	}

	public void completeTrip(Ride ride) {
		ride.currentState.completeRide(ride);
		ride.logRideHistory();
	}

	public void cancelTripCustomer(Ride ride) {
		ride.currentState.cancelRide(ride);
		Penalty penalty = new Penalty();
		penalty.applyPenalty();
	}

	public void cancelTripDriver(Ride ride) {
		ride.currentState.cancelRide(ride);
	}
}

class Peality {
	String penalityMeasure;

	public int calculatePenalityAmt() {
		int amt = 100;
		System.out.println("Penalty amount is " + amt );
		return amt;
	}

	publc void applyPenality() {
		customer.inAppCash -= amt;
		System.out.println("Penalty of " + amt + " applied");
	}
}

class Main {
	public static void main(String[] args) {

		List<Driver> drivers = new ArrayList<>();
		Driver driver1 = new Driver("Rahul", "9876543210", 4,
		                            new Car("DL7SUB4641", "White", "BMW M5"), false);
		drivers.add(driver1);

		DriverMatchingStrategy strategy = new NearestDriverStrategy(drivers, "CP");
		FareCalculation fareCalc = new FareCalculation("CP", "Noida", "car");
		RideService service = new RideService(strategy, fareCalc);

		Customer customer = new Customer("Arjun", "9999999999", 5,
		                                 "arjun@gmail.com", 500, 10);

		Ride ride = service.searchRide(customer, "CP", "Noida");
		service.beginTrip(ride, 123); // otp will be provided by customer to verify is the correct otp for the ride
		service.completeTrip(ride);
	}
}
