import InputValidation.Util;

import java.util.*;

public class UI {
    Scanner scanner = new Scanner(System.in);
    String m_title;
    List<String> m_menuItems = new ArrayList<>();
    private static final int numOfMenus = 8;

    private EnigmaMachineApplication m_machineApplication = new EnigmaMachineApplication();

    public UI(){
        m_menuItems.add("1. Load Machine details from XML file.");
        m_menuItems.add("2. Show Machine details.");
        m_menuItems.add("3. Define initial machine settings manually.");
        m_menuItems.add("4. Define initial machine settings automatically.");
        m_menuItems.add("5. Encrypt input.");
        m_menuItems.add("6. Reset machine to initial settings.");
        m_menuItems.add("7. Display history and statistics.");
        m_menuItems.add("8. Exit");
        System.out.println("started");
    }
    public void DisplayAllItems()
    {
        for (String item:m_menuItems) {
            System.out.println(item);
        }
    }

    public void MenuLoop()
    {
        String input;
        int selection;
        while(true) {
            DisplayAllItems();
            input = scanner.nextLine();

            try {
                selection = Integer.parseInt(input);
            }
            catch (NumberFormatException e){
                System.out.println("Selection is not a number, Please try again.");
                continue;
            }
            if(selection < 1 || selection > numOfMenus){
                System.out.println("Selection is not in range. please enter a number between 1 to " + numOfMenus + ".");
                continue;
            }
            if (selection == numOfMenus) {
                System.out.println("Bye Bye!");
                return;
            }
            try {
                HandleSelection(selection);
            }
            catch (Exception e){
                System.out.println(e.getMessage());
                continue;
            }
        }
    }
    public void HandleSelection(int selection)
    {
        switch (selection)
        {
            //load from XML
            case 1:
            {
                boolean valid = false;
                do{
                    System.out.println("Please Enter absolute XML path:");
                    String xmlPath = scanner.nextLine();
                    try {
                        m_machineApplication.LoadXMLFile(xmlPath);
                        valid = true;
                    }
                    catch(Exception e){
                        System.out.println(e.getMessage());
                    }
                }while(!valid);
            }
            //no break because 1 goes automatically to 2
            //display machine details
            case 2:
            {
                try {
                    if (m_machineApplication.commandIsPossible(2)) {
                      System.out.println(
                          "Number of Rotors being used: "
                              + m_machineApplication.getNumOfRotorsBeingUsed()
                              + ".");
                      System.out.println(
                          "Number of Available Rotors: "
                              + m_machineApplication.getNumOfAvailableRotors()
                              + ".");
                      Map<Integer, Integer> rotorsAndLocations =
                          m_machineApplication.getLocationOfNotchInEachRotor();
                      for (Map.Entry<Integer, Integer> entry : rotorsAndLocations.entrySet()) {
                        System.out.println(
                            "Rotor number: " + entry.getKey() + " and Notch location: " + entry.getValue());
                      }
                      System.out.println(
                          "Number of Reflectors: "
                              + m_machineApplication.getNumOfAvailableReflectors()
                              + ".");
                      System.out.println(
                          "Number of Processed messages: "
                              + m_machineApplication.getNumOfProcessedMessages()
                              + ".");
                      if (m_machineApplication.isSecretLoaded()) {
                        DisplayCurrentSecret();
                      } else {
                        System.out.println("No secret loaded yet.\n");
                      }
                    }else{
                        System.out.println("XML was not loaded, please load XML file and try again.");
                    }
                }
                catch(Exception e){
                    System.out.println(e.getMessage());
                }
                break;
            }
            //set initial secret
            case 3:
            {
                List<Integer> rotorsList = new ArrayList<>();
                String initialLocationString = "";
                String reflector = "";
                boolean inputOK = false;

                if(m_machineApplication.commandIsPossible(3)) {
                    //getRotors
                    while (!inputOK) {
                        rotorsList = getRotorsFromUser();
                        try {
                            m_machineApplication.ValidateReceivedRotors(rotorsList);
                            inputOK = true;
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                            System.out.println(" Please try again.");
                        }
                    }

                    //getRotors initial locations
                    inputOK = false;
                    while (!inputOK) {
                        initialLocationString = getInitialLocationsFromUser();
                        try {
                            m_machineApplication.ValidateInitialLocations(initialLocationString);
                            inputOK = true;
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                            System.out.println(" Please try again.");
                        }
                    }

                    //get reflector
                    inputOK = false;
                    while (!inputOK) {
                        reflector = getReflectorFromUser();
                        try {
                            m_machineApplication.ValidateReflector(reflector);
                            inputOK = true;
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                            System.out.println(" Please try again.");
                        }
                    }
                    try{
                        m_machineApplication.CreateManualSecret(rotorsList,initialLocationString,reflector);
                        System.out.println("Secret loaded successfully, displaying secret:");
                        DisplayCurrentSecret();
                    }catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }else{
                    System.out.println("XML was not loaded, please load XML file and try again.");
                }
                break;
            }
            //set initial secret automatically
            case 4:
            {

                try {
                    if (m_machineApplication.commandIsPossible(4)) {
                      m_machineApplication.setInitialSecretAutomatically();
                      System.out.println("Secret loaded successfully, displaying secret:");
                      DisplayCurrentSecret();
                    }else{
                        System.out.println("XML was not loaded, please load XML file and try again.");
                    }
                } catch(Exception e){
                    System.out.println(e.getMessage());
                }
                break;
            }
            //process string
            case 5:
            {
                boolean validInput = false;
                while(!validInput)
                {
                    try{
                        if(m_machineApplication.commandIsPossible(5)) {
                            System.out.println("Please Enter string to process:");
                            String unprocessedString = scanner.next();
                            String processedString = m_machineApplication.processString(unprocessedString);
                            System.out.println("Encrypted string is: " + processedString +".");
                            validInput = true;
                        }
                        else{
                            System.out.println("Command is not possible because the secret hasn't been loaded yet. Please load a secret and try again");
                            return;
                        }
                    }
                    catch(Exception e){
                        System.out.println(e.getMessage());
                    }
                }

                break;
            }
            //reset machine
            case 6:
            {
                try{
                    m_machineApplication.resetMachine();
                    System.out.println("Secret was reset successfully.");
                }
                catch(Exception e){
                    System.out.println(e.getMessage());
                }
                break;
            }
            case 7:
            {
                double[] avg = new double[1];
                Map<String, Set<String>> history = m_machineApplication.getHistory(avg);
                printHistory(history, avg);
                break;
            }
        }
    }

    private void printHistory(Map<String, Set<String>> history, double[] avg) {
        for(String secret: history.keySet()){
            System.out.println("The strings that have been processed for secret: "+secret+ " is:");
            for(String processedStr: history.get(secret)){
                System.out.println(processedStr);
            }
        }
        if (avg[0] != 0) {
            System.out.println("The avarage time to process each string is: " + avg[0]);
        } else {
            System.out.println("No history was recorded");
        }
    }

    private String getReflectorFromUser()
    {
        boolean validInput=false;
        String reflectorInput;
        String res = "";
        while(!validInput) {
            System.out.println("Please enter Reflector num (roman letters)");
            reflectorInput = scanner.next();
            if(Util.isRoman(reflectorInput)){
                if (Util.isValidRomanLetter(reflectorInput)) {
                    res = reflectorInput;
                    //res = Util.romanToInt(reflectorInput);
                    validInput = true;
                }
                else{
                    System.out.println(reflectorInput+" is not in range");
                }
            }
            else{
                System.out.println(reflectorInput + " is not a valid roman letter");
            }
        }

        return res;
    }
    private String getInitialLocationsFromUser() {
        System.out.println("Please choose rotor's initial location:");
        String rotorsInitialLocations = scanner.next();
        return rotorsInitialLocations;
    }

    private List<Integer> getRotorsFromUser() {
        List<Integer> rotorsList = new ArrayList<>();
        Integer rotorId = -1;
        for(int i=0 ; i < m_machineApplication.getNumOfRotorsBeingUsed() ; i++){
            System.out.println("Please choose rotor number");

            while(!scanner.hasNextInt()){
                scanner.next();
                System.out.println("input is not a number, please try again.");
            }
            rotorId = scanner.nextInt();
            if(rotorsList.contains(rotorId)){
                System.out.println("Rotor number " + rotorId + " is already set. Please try a different one.");
                i--;
            }else{
                rotorsList.add(rotorId);
            }
        }
        return rotorsList;
    }

    private void DisplayCurrentSecret() {
        int i=0;
        String output = "";
        output+='<';
        if(m_machineApplication.hasRotor(i)){
            //System.out.print(m_machineApplication.getRotorNum(i));
            output+=m_machineApplication.getRotorNum(i);
            i++;
        }
        while(m_machineApplication.hasRotor(i))//rotor index
        {
            //System.out.print("," + m_machineApplication.getRotorNum(i));
            output+=',';
            output+=m_machineApplication.getRotorNum(i);
            i++;
        }
        //System.out.print("><");
        output+="><";
        i=0;
        while(m_machineApplication.hasRotor(i)){
            //System.out.print(m_machineApplication.getInitialLocationOfRotor(i));
            output+=m_machineApplication.getInitialLocationOfRotor(i);
            i++;
        }
        //System.out.print("><");
        output+="><";
        //System.out.println(m_machineApplication.getReflectorLetter()+ ">");
        output+=Util.fromIntToRoman(m_machineApplication.getReflectorLetter());
        output+=">";
        System.out.println(output);
    }
}