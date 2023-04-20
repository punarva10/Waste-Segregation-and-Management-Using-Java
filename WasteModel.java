import java.util.Scanner;

//Strategy - Location Choice
abstract class DumpLocation
{
    RecyclableLocation recyclableLocation;

    public DumpLocation() {
        this.recyclableLocation = null;
    }
  
    public DumpLocation(RecyclableLocation recyclableLocation)
    {
        this.recyclableLocation = recyclableLocation;
    }

    public void Jayanagar()
    {
        System.out.println("Location: Jayanagar");
    }

    public void Recyclable()
    {
        if(recyclableLocation == null) {
            System.out.println("You do not have access to this location");
        }
        else {
            recyclableLocation.recycle();
        }
    }
    
    public abstract void display();
}
  
interface RecyclableLocation
{
    public void recycle();
}

class JP_Nagar implements RecyclableLocation
{
    public void recycle()
    {
        System.out.println("Location: JP Nagar");
    }
}

class Vijayanagar implements RecyclableLocation
{
    public void recycle()
    {
        System.out.println("Location: glass recycling landfill");
    }
}

class K_cross implements RecyclableLocation
{
    public void recycle()
    {
        System.out.println("Waste dumped at wet waste recycling landfill");
    }
}

class Driver extends DumpLocation
{
    public Driver(RecyclableLocation recyclableLocation)
    {
        super(recyclableLocation);
    }
    public void display()
    {
        System.out.println("Driver1 dumped waste");
    }
}


//Chain of responsibility - Waste Segregation
abstract class WasteHandler {
    public Scanner scanner = new Scanner(System.in);
    protected WasteHandler nextHandler;
    public int[] weight = {0,0,0,0};

    public void setNextHandler(WasteHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public abstract void segregateWaste(Waste waste, String specific_waste);
}

class HazardousHandler extends WasteHandler {
    public WasteHandler successor;

    public void setSuccessor(WasteHandler successor) {
        this.successor = successor;
    }

    public void segregateWaste(Waste waste, String specific_waste) {
        if (specific_waste.equalsIgnoreCase("hazard")) {
            System.out.println("Weight of waste before emptying hazardous waste: " + waste.getWeight());
            System.out.println("Enter the weight of the hazardous waste: ");
            weight[0] = scanner.nextInt();
            waste.setWeight(waste.getWeight() - weight[0]);
            System.out.println("Hazardous waste segregated.");
            System.out.println("Weight of waste after emptying hazardous waste: " + waste.getWeight());
        } else {
            successor.segregateWaste(waste, specific_waste);
        }
    }
}

class PlasticHandler extends WasteHandler {

    public void segregateWaste(Waste waste, String specific_waste) {
        if (specific_waste.equalsIgnoreCase("plastic")) {
            System.out.println("Weight of waste before emptying plastic waste: " + waste.getWeight());
            System.out.println("Enter the weight of the plastic waste: ");
            weight[1] = scanner.nextInt();
            waste.setWeight(waste.getWeight() - weight[1]);
            System.out.println("Plastic waste segregated.");
            System.out.println("Weight of waste after emptying plastic waste: " + waste.getWeight());
        } else {
            System.out.println("Wrong input");
        }
    }
}

class WetwasteHandler extends WasteHandler {
    public WasteHandler successor;
    public void setSuccessor(WasteHandler successor) {
        this.successor = successor;
    }

    public void segregateWaste(Waste waste, String specific_waste) {
        if (specific_waste.equalsIgnoreCase("wet waste")) {
            System.out.println("Weight of waste before emptying wet waste: " + waste.getWeight());
            System.out.println("Enter the weight of the wet waste: ");
            weight[2] = scanner.nextInt();
            waste.setWeight(waste.getWeight() - weight[2]);
            System.out.println("Wet waste segregated.");
            System.out.println("Weight of waste after emptying wet waste: " + waste.getWeight());
        } else {
            successor.segregateWaste(waste, specific_waste);
        }
    }
}

class GlassHandler extends WasteHandler {
    public WasteHandler successor;

    public void setSuccessor(WasteHandler successor) {
        this.successor = successor;
    }

    public void segregateWaste(Waste waste, String specific_waste) {
        if (specific_waste.equalsIgnoreCase("glass")) {
            System.out.println("Weight of waste before emptying glass waste: " + waste.getWeight());
            System.out.println("Enter the weight of the glass waste: ");
            weight[3] = scanner.nextInt();
            waste.setWeight(waste.getWeight() - weight[3]);
            System.out.println("Glass waste segregated.");
            System.out.println("Weight of waste before emptying glass waste: " + waste.getWeight());

        } else {
            successor.segregateWaste(waste, specific_waste);
        }
       

    }
}


//Factory - Payment
class PaymentInfoFactory {
    public PaymentInfoModel createPaymentInfo(String wasteType) throws Exception {
      switch (wasteType) {
        case "hazard":
          return new HazardousPaymentInfo();
        case "plastic":
          return new PlasticPaymentInfo();
        case "glass":
          return new GlassPaymentInfo();
        case "wetwaste":
          return new WetWastePaymentInfo();
        default:
          throw new Exception("Invalid waste type: " + wasteType);
      }
    }
}

abstract class PaymentInfoModel {
    public static int sum;
    abstract public double calculatePayment(Waste waste, Truck truck, WasteHandler handler);
}

class HazardousPaymentInfo extends PaymentInfoModel {
    
    public double calculatePayment(Waste waste, Truck truck, WasteHandler handler) {
      return handler.weight[0] * 0.3;
    }
}
  
class PlasticPaymentInfo extends PaymentInfoModel {
    public double calculatePayment(Waste waste, Truck truck, WasteHandler handler) {
        return handler.weight[1] * 0.6;
    }       
}

class GlassPaymentInfo extends PaymentInfoModel {
    public double calculatePayment(Waste waste, Truck truck, WasteHandler handler) {
        return handler.weight[2] * 0.5;
    }       
}

class WetWastePaymentInfo extends PaymentInfoModel {
    public double calculatePayment(Waste waste, Truck truck, WasteHandler handler) {
        return handler.weight[3] * 0.4;
    }       
}





























//Factory
abstract class PaymentProcessor {
    public abstract void processPayment(double amount);
}

class CreditCardProcessor extends PaymentProcessor {
    public void processPayment(double amount) {
        System.out.println("Processing credit card payment: $" + amount);
    }
}

class PayPalProcessor extends PaymentProcessor {
    public void processPayment(double amount) {
        System.out.println("Processing PayPal payment: $" + amount);
    }
}

class PaymentProcessorFactory {
    public PaymentProcessor createProcessor(String paymentMethod) {
        if (paymentMethod.equalsIgnoreCase("creditcard")) {
            return new CreditCardProcessor();
        } else if (paymentMethod.equalsIgnoreCase("paypal")) {
            return new PayPalProcessor();
        } else {
            throw new IllegalArgumentException("Invalid payment method: " + paymentMethod);
        }
    }
}


class Waste {
    private String type;
    private double weight = 0;

    public Waste(String type, double weight) {
        this.type = type;
        this.weight = weight;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}

class Truck {
    private double initialWeight;
    private double finalWeight;

    public Truck(double initialWeight) {
        this.initialWeight = initialWeight;
    }

    public double getInitialWeight() {
        return initialWeight;
    }

    public void setInitialWeight(double initialWeight) {
        this.initialWeight = initialWeight;
    }

    public double getFinalWeight() {
        return finalWeight;
    }

    public void setFinalWeight(double finalWeight) {
        this.finalWeight = finalWeight;
    }
}

class PaymentInfo {
    private String paymentMethod;

    public PaymentInfo(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}

class Supervisor {
    public void weighTruck(Truck truck, double weight) {
        truck.setFinalWeight(weight);
    }

    public double calculateWasteWeight(Truck truck) {
        return truck.getInitialWeight() - truck.getFinalWeight();
    }
}