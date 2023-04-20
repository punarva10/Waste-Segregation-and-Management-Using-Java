import java.util.Scanner;

public class WasteController {
    private Truck truck;
    private Supervisor supervisor;
    private Waste waste;

    public WasteController() {
        supervisor = new Supervisor();
    }

    public void startProcess() throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter truck's initial weight:");
        double initialWeight = scanner.nextDouble();
        truck = new Truck(initialWeight);

        System.out.println("Give location access to dump the waste: ");
        RecyclableLocation spcl_location = new JP_Nagar();
        Driver driver1 = new Driver(spcl_location);
        driver1.display();
        System.out.print("Location to dump the waste: ");
        driver1.Jayanagar();
        System.out.println();

        System.out.print("Enter truck's final weight:");
        double finalWeight = scanner.nextDouble();
        scanner.nextLine();
        supervisor.weighTruck(truck, finalWeight);
        System.out.println();

        double wasteWeight = supervisor.calculateWasteWeight(truck);
        waste = new Waste("glass", wasteWeight);
        System.out.println("Waste weight: " + waste.getWeight());

        System.out.println("Segregating the waste...");
        HazardousHandler hazard = new HazardousHandler();
        PlasticHandler plastic = new PlasticHandler();
        WetwasteHandler wetwaste = new WetwasteHandler();
        GlassHandler glass = new GlassHandler();

        hazard.setSuccessor(wetwaste);
        wetwaste.setSuccessor(glass);
        glass.setSuccessor(plastic);
        

        

        System.out.print("Enter types of waste present (space separated): ");
        String[] waste_present = scanner.nextLine().split(" ");

        for (int i=0;i<waste_present.length;i++){
            hazard.segregateWaste(waste, waste_present[i]);
        }
        

        PaymentInfoFactory payment = new PaymentInfoFactory();

        double total_payment = 0;
        for (int j=0;j<waste_present.length;j++){
            String w = waste_present[j];
            PaymentInfoModel paymentInfoModel = payment.createPaymentInfo(w);
            if(w.equals("hazard")) total_payment += paymentInfoModel.calculatePayment(waste, truck, hazard);
            else if(w.equals("plastic")) total_payment += paymentInfoModel.calculatePayment(waste, truck, plastic);
            else if(w.equals("wetwaste")) total_payment += paymentInfoModel.calculatePayment(waste, truck, wetwaste);
            else if(w.equals("glass")) total_payment += paymentInfoModel.calculatePayment(waste, truck, glass);
        }
        
        System.out.println("Final payment made to driver:" + total_payment);

        scanner.close();

    }
}