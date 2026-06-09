import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

abstract class Vehicle {
    private String licensePlate; 
    private String model; 
    private double currentMileage; 
    private double lastServiceMileage; 

    public Vehicle(String licensePlate, String model, double currentMileage, double lastServiceMileage) {
        this.licensePlate = licensePlate;
        this.model = model;
        this.currentMileage = currentMileage;
        this.lastServiceMileage = lastServiceMileage;
    }

    // Check if the distance driven since last service exceeds the interval
    public boolean needsService() {
        return (this.currentMileage - this.lastServiceMileage) >= getServiceInterval();
    }

    // Reset the service tracker to current mileage
    public void markServiced() {
        this.lastServiceMileage = this.currentMileage;
    }

    public abstract double getServiceInterval();

    public String getLicensePlate() { 
        return licensePlate; 
    }
    
    public double getCurrentMileage() { 
        return currentMileage; 
    }
    
    public double getLastServiceMileage() { 
        return lastServiceMileage; 
    }
    
    public void setCurrentMileage(double currentMileage) { 
        this.currentMileage = currentMileage; 
    }

    @Override
    public String toString() { 
        String status = needsService() ? "SERVICE DUE" : "OK";
        
        // Convert to string and handle formatting manually
        String cmStr = String.valueOf(currentMileage);
        String lsmStr = String.valueOf(lastServiceMileage);
        
        if (cmStr.contains(",")) cmStr = cmStr.replace(',', '.');
        if (lsmStr.contains(",")) lsmStr = lsmStr.replace(',', '.');
        
        return licensePlate + " " + model + " - " + cmStr + " km (last service: " + lsmStr + ") - " + status;
    }
}

class Sedan extends Vehicle {
    private static final double SEDAN_INTERVAL = 10000.0;
    public Sedan(String lp, String m, double cm, double lsm) { 
        super(lp, m, cm, lsm); 
    }
    
    @Override 
    public double getServiceInterval() { 
        return SEDAN_INTERVAL; 
    } 
    
    @Override 
    public String toString() { 
        return "[Sedan] " + super.toString(); 
    } 
}

class SUV extends Vehicle {
    private static final double SUV_INTERVAL = 8000.0;

    public SUV(String lp, String m, double cm, double lsm) { 
        super(lp, m, cm, lsm); 
    }
    
    @Override 
    public double getServiceInterval() { 
        return SUV_INTERVAL; 
    } 
    
    @Override 
    public String toString() { 
        return "[SUV] " + super.toString(); 
    } 
}

class Truck extends Vehicle {
    private static final double TRUCK_INTERVAL = 5000.0;

    public Truck(String lp, String m, double cm, double lsm) { 
        super(lp, m, cm, lsm); 
    }
    
    @Override 
    public double getServiceInterval() { 
        return TRUCK_INTERVAL; 
    } 
    
    @Override 
    public String toString() { 
        return "[Truck] " + super.toString(); 
    } 
}

class Fleet {
    private String fleetName; 
    private List<Vehicle> vehicles; 

    public Fleet(String fleetName) {
        this.fleetName = fleetName;
        this.vehicles = new ArrayList<>();
    }

    public void registerVehicle(Vehicle v) { 
        vehicles.add(v);
        System.out.println("Vehicle registered!");
    }

    public void listAllVehicles() {
        System.out.println("\n--- " + fleetName + " Vehicles ---"); 
        if (vehicles.size() == 0) {
            System.out.println("No vehicles in fleet.");
            return;
        }
        for (int i = 0; i < vehicles.size(); i++) {
            System.out.println((i + 1) + ") " + vehicles.get(i));
        }
    }

    public void listVehiclesNeedingService() { 
        System.out.println("\n--- Vehicles Needing Service ---");
        boolean anyVehicle = false;
        int index = 1;
        for (Vehicle v : vehicles) {
            if (v.needsService()) {
                System.out.println(index + ") " + v);
                index++;
                anyVehicle = true;
            }
        }
        if (!anyVehicle) {
            System.out.println("All vehicles are within their service interval.");
        }
    }

    public void updateMileage(String plate, double newMileage) { 
        for (Vehicle v : vehicles) {
            if (v.getLicensePlate().equalsIgnoreCase(plate)) {
                if (newMileage < v.getCurrentMileage()) {
                    System.out.println("New mileage cannot be less than current mileage.");
                    return;
                }
                v.setCurrentMileage(newMileage);
                System.out.println("Mileage updated.");
                return;
            }
        }
        System.out.println("Vehicle not found.");
    }

    public void markVehicleServiced(String plate) { 
        for (Vehicle v : vehicles) {
            if (v.getLicensePlate().equalsIgnoreCase(plate)) {
                v.markServiced();
                String lsmStr = String.valueOf(v.getLastServiceMileage());
                if (lsmStr.contains(",")) lsmStr = lsmStr.replace(',', '.');
                
                System.out.println("Marked as serviced: " + plate + " (last service updated to " + lsmStr + ")");
                return;
            }
        }
        System.out.println("Vehicle not found.");
    }
}

public class MaintenanceScheduler {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in); 
        System.out.print("Enter fleet name: "); 
        Fleet myFleet = new Fleet(scan.nextLine());

        while (true) { 
            System.out.println("\n--- Fleet Maintenance Menu ---"); 
            System.out.println("1. Register a vehicle");
            System.out.println("2. List all vehicles");
            System.out.println("3. List vehicles needing service");
            System.out.println("4. Update vehicle mileage");
            System.out.println("5. Mark a vehicle as serviced");
            System.out.println("6. Exit");
            System.out.print("Choose an option (1-6): ");

            String option = scan.nextLine();

            try {
                if (option.equals("1")) { 
                    System.out.print("Enter vehicle type (sedan/suv/truck): ");
                    String type = scan.nextLine().toLowerCase().trim();
                    
                    if (!type.equals("sedan") && !type.equals("suv") && !type.equals("truck")) {
                        System.out.println("Unknown vehicle type. Skipping..."); 
                    } else {
                        System.out.print("Enter license plate: ");
                        String lp = scan.nextLine();
                        System.out.print("Enter model: ");
                        String m = scan.nextLine();
                        System.out.print("Enter current mileage: ");
                        double cm = Double.parseDouble(scan.nextLine());
                        System.out.print("Enter last service mileage: ");
                        double lsm = Double.parseDouble(scan.nextLine());

                        if (type.equals("sedan")) myFleet.registerVehicle(new Sedan(lp, m, cm, lsm));
                        else if (type.equals("suv")) myFleet.registerVehicle(new SUV(lp, m, cm, lsm));
                        else if (type.equals("truck")) myFleet.registerVehicle(new Truck(lp, m, cm, lsm));
                    }
                } 
                else if (option.equals("2")) { 
                    myFleet.listAllVehicles(); 
                } 
                else if (option.equals("3")) { 
                    myFleet.listVehiclesNeedingService(); 
                } 
                else if (option.equals("4")) { 
                    System.out.print("Enter license plate: ");
                    String plate = scan.nextLine();
                    System.out.print("Enter new mileage: ");
                    double newMil = Double.parseDouble(scan.nextLine());
                    myFleet.updateMileage(plate, newMil);
                } 
                else if (option.equals("5")) { 
                    System.out.print("Enter license plate: ");
                    String plate = scan.nextLine();
                    myFleet.markVehicleServiced(plate);
                } 
                else if (option.equals("6")) { 
                    System.out.println("Exiting. Goodbye!");
                    break;
                    } 
            } catch (NumberFormatException nfe) {
                System.out.println("Invalid input encountered.");
            } catch (Exception e) { 
                System.out.println("Invalid input encountered.");
            }
        }
        scan.close();
    }
}
