package com.company;
import java.util.*;
    public class CarServiceStation {
    /*
    *
    * This program conditionally displays a user prompt in the console if no command line arguments are passed.
    * Expected command line arguments : {CAR_TYPE} SPACE {SERVICE_CODE} SPACE {SERVICE_CODE}...
    * First argument must be the car type, followed by all the service codes separated by space.
    * If command line args are not provided then a user prompt is displayed.
    * Further instructions are given in user prompt itself.
    *
    * */

        public static class ServiceCharges {
            private final HashMap<String, List<Integer>> costs;
            public ServiceCharges(){
                this.costs = new HashMap<>();
                this.costs.put("HATCHBACK",Arrays.asList(2000,5000,2000,1000,3000));
                this.costs.put("SEDAN",Arrays.asList(4000,8000,4000,1500,6000));
                this.costs.put("SUV",Arrays.asList(5000,10000,6000,2500,8000));
            }
            public HashMap<String, List<Integer>> getCharges(){
                return this.costs;
            }
        }


        public static final ArrayList<String> carTypes = new ArrayList<>(Arrays.asList("HATCHBACK","SEDAN","SUV"));
        public static final String[] servicePrompt = new String[]{"Basic Servicing","Engine Fixing","Clutch Fixing","Brake Fixing","Gear Fixing"};
        public static final ArrayList<String> serviceCodes = new ArrayList<>(Arrays.asList("BS01","EF01","CF01","BF01","GF01"));
        private static class CarServiceRequest{

            private final String carType;
            private final ArrayList<Integer> services;

            public CarServiceRequest(String carType, ArrayList<Integer> services){
                this.carType = carType;
                this.services = services;
            }

            public  void getReceipt(HashMap<String,List<Integer>> charges){
                    int amount = 0;
                    int cost;
                    if(services.size()==0){
                        System.out.println( "\n----------------------------------------\033[0;31m \nNo services were selected!. Please Try Again.\033[0m\n----------------------------------------");
                        return;
                    }
                    System.out.println("\n\n---------------------------");
                    System.out.println("Servicing charges for your "+carType);
                    System.out.println("---------------------------\n");
                    boolean[] done = new boolean[]{false,false,false,false,false};
                    for(int i: services){
                        if(done[i]){
                            continue;
                        }
                        cost = charges.get(this.carType).get(i);
                        amount += cost;
                        System.out.println("Charges for "+servicePrompt[i]+" - \u20B9 "+cost);
                        done[i]=true;
                    }

                    if(amount>10000){
                        System.out.println("\nComplementary Cleaning (for billing above \u20B910000) - FREE OF COST");
                    }

                    System.out.println("\n---------------------------\nTotal Bill - \u20B9 "+amount+"/-\n---------------------------");

            }
        }
        public static void main(String[] args) {

            //for coloured output
            String green = "\033[0;32m";
            String red = "\033[0;31m";
            String reset = "\033[0m";
            String message;

            if(args.length!=0){
                String carType = args[0].toUpperCase();
                int indexOfCarType = carTypes.indexOf(carType.toUpperCase());
                if(indexOfCarType==-1){
                    System.out.println(red+"Received Input for Car Type: "+args[0]+reset);
                    System.out.println(red+"\nInvalid Car Type. \nExiting!!"+reset);
                }else{
                    ArrayList<Integer> requiredServices = new ArrayList<>();
                    int serviceIndex;
                    for(int i=1 ; i<args.length ; i++){
                        serviceIndex = serviceCodes.indexOf(args[i].toUpperCase());
                        if(serviceIndex==-1){
                            System.out.println(red+"\nService required is not available. \nPlease check the code or try again later."+reset);
                            System.out.println("For service code: "+args[i]);
                        }else{
                            requiredServices.add(serviceIndex);
                        }
                    }
                    CarServiceRequest request = new CarServiceRequest(carType,requiredServices);
                    request.getReceipt(new ServiceCharges().getCharges());
                }
                return;
            }

            int carType=-1;
            String temp;
            Scanner sc = new Scanner(System.in);
            int tries = 3;

            // Taking car type input
            while(carType!=1 && carType!=2 && carType!=3){
                System.out.println("\n\nPlease choose a car type by appropriate serial number:\n1. Hatchback\n2. Sedan\n3. SUV");

                    temp = sc.nextLine();
            try{
                carType = Integer.parseInt(temp);
            }catch (Exception e){
                    System.out.println(red+"\nInvalid Car Type. Please try again."+reset);
                    tries--;
                if(tries<=0){
                    System.out.println(red + "Exiting!! "+reset);
                    return;
                }
                    continue;
                }
                if(carType>3 || carType<1){
                    message = red+"\nInvalid Car Type. Please try again. "+reset;
                    System.out.println(message);
                    tries--;
                    if(tries<=0){
                        System.out.println(red + "\nExiting!! "+reset);
                        return;
                    }

                }
            }
            System.out.println("\n\nYou have chosen "+carTypes.get(carType-1)+ " car type.");


            //Fetching charges
            HashMap<String, List<Integer>> charges = new ServiceCharges().getCharges();

            boolean submit=false;
            boolean[] services = new boolean[]{false,false,false,false,false};
            int input;
            boolean canSubmit=false;
            String color;
            int count=0;

            //taking required services input
            while(!submit){
                System.out.println("\n---------------------------\nSelect the services required, by appropriate serial number to toggle the selection:");

                //showing services as per selection
                for(int i=0 ; i<5 ; i++){
                    color = services[i] ?green:red;
                    message = color+ (i+1)+". "+servicePrompt[i] +reset;
                    System.out.println(message);
                }

                //showing submit button conditionally
                if(canSubmit){
                    System.out.println("0. Submit");
                }

                System.out.println("---------------------------\n"+green+"Green"+reset+" - Selected");
                System.out.println(red+"Red"+reset+" - Not Selected");

                temp = sc.nextLine();
                try{
                    input = Integer.parseInt(temp);
                }catch (Exception e){
                    System.out.println(red+"\nInvalid Input"+reset);
                    continue;
                }


                if(input==0){
                    if(canSubmit) {
                        submit=true;
                        continue;
                    }else {
                        System.out.println(red+"Cannot submit without selecting any service. Please try again."+reset);
                    }
                }
                if(input>5 || input<1){
                    System.out.println(red+"\nInvalid Input"+reset);
                }else{

                    services[input-1] = !services[input-1];
                    if(services[input-1]){  count++;    }
                    else{   count--;   }
                    canSubmit = count>0;
                }
            }

            //refactoring required services to their integer position in charges
            ArrayList<Integer> requiredServices = new ArrayList<>();
            for(int i=0 ; i<5 ; i++){
                if(services[i]){
                    requiredServices.add(i);
                }
            }

            String type = carTypes.get(carType-1);

            //creating car service request and getting receipt
            CarServiceRequest request = new CarServiceRequest(type,requiredServices);
            request.getReceipt(charges);
        }
    }