class Customer {
	String customerName;
	String customerNumber;
	int customerRating;
	String cutomerGmail;
	int inAppCash;
	int totalRides;
}

class Driver {
	String driverName;
	String driverNumber;
	int driverRating;
	Vehicle vehicle;
	boolean onRide;
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
		this.pickUp = pickupLocation;
		this.dest = destination;
	}

	public void logRideHistory() {
		// store ride details in db
	}
}

interface RideState {
	void requestRide(Ride ride);
	void assignRide(Ride ride);
	void verifyOTPRide(Ride ride);
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

	NearestDriverStrategy(List<Driver> allDrivers, String pickupLocation) {
		this.allDrivers = allDrivers;
		this.pickupLocation = pickupLocation;
	}

	@Overrride
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

	public Ride serachRide() {
		Ride ride = new Ride(customer)
		ride.currentState = new SearchingRide();
		ride.rideCost = fareCalculator.calculateFare();
		ride.driver = driverMatchingAlgo.findDriver();
		ride.currentState.assignRide(ride);
		return ride;
	}

	public void beginTrip(Ride ride) {
		ride.curretState.verifyOTP
	}

	public void completeTrip() {
		ride.currentState.completeRide(ride);
		ride.logRideHistory();
	}

	public void cancelTripCustomer() {
		ride.currentState.cancelRide(ride);
		Penalty penalty = new Penalty();
		penalty.applyPenalty();
	}

	public void cancelTripDriver() {
		ride.currentState.cancelRide(ride);
	}
}

class Peality {
	String penalityMeasure;

	public int calculatePenalityAmt() {

	}

	publc void applyPenality() {

	}
}
