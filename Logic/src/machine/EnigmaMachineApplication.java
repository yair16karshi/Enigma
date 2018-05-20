package machine;

import DataTypes.GeneratedMachineDataTypes.Decipher;
import DataTypes.GeneratedMachineDataTypes.Machine;
import DataTypes.GeneratedMachineDataTypes.Reflector;
import DataTypes.GeneratedMachineDataTypes.Rotor;
import InputValidation.Util;
import InputValidation.XMLParser;
import Producer.Manager;
import pukteam.enigma.component.machine.builder.EnigmaMachineBuilder;
import pukteam.enigma.factory.EnigmaComponentFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.*;

public class EnigmaMachineApplication {
    private EnigmaMachineWrapper m_machineWrapper;
    private Producer.Manager dm;
    private XMLParser m_xmlParser = new XMLParser();

    public boolean LoadXMLFile(String path)throws Exception{
        try {
            m_xmlParser.XMLIsValid(path);
        }
        catch(Exception e){
            throw e;
        }
        setMachineFromObject();
        m_machineWrapper.setIsXMLLoaded(true);
        return true;
    }

    private void setMachineFromObject() {
        m_xmlParser.machine.getMachine().setABC(m_xmlParser.machine.getMachine().getABC().toUpperCase());
        Machine xmlMachine = m_xmlParser.machine.getMachine();
        Decipher deciper = new Decipher();
        deciper.setAgents(m_xmlParser.machine.getDecipher().getAgents());
        deciper.setDictionary(m_xmlParser.machine.getDecipher().getDictionary());
        dm = new Manager(m_machineWrapper.getMachine(), deciper, xmlMachine);
        EnigmaMachineBuilder machineBuilder = EnigmaComponentFactory.INSTANCE.buildMachine(xmlMachine.getRotorsCount(),xmlMachine.getABC());
        DefineRotors(machineBuilder,xmlMachine);
        DefineReflectors(machineBuilder,xmlMachine);
        m_machineWrapper = new EnigmaMachineWrapper(machineBuilder.create());

        //set some extra useful parameters
        m_machineWrapper.setXMLMachine(xmlMachine);

    }

    public static void DefineReflectors(EnigmaMachineBuilder machineBuilder, Machine xmlMachine) {
        for(int i=0 ; i < xmlMachine.getReflectors().getReflector().size() ; i++){
            DataTypes.GeneratedMachineDataTypes.Reflector xmlReflector = xmlMachine.getReflectors().getReflector().get(i);

            byte[] input = new byte[xmlReflector.getReflect().size()];
            byte[] output = new byte[xmlReflector.getReflect().size()];
            for(int j = 0 ; j<xmlReflector.getReflect().size() ; j++){
                input[j] =  (byte)xmlReflector.getReflect().get(j).getInput();//test if the casting works
                output[j] =  (byte)xmlReflector.getReflect().get(j).getOutput();//test if the casting works

            }
            machineBuilder.defineReflector(Util.romanToInt(xmlReflector.getId()),input,output);
        }
    }

    public static void DefineRotors(EnigmaMachineBuilder machineBuilder, Machine xmlMachine) {
        for(int i=0 ; i<xmlMachine.getRotors().getRotor().size(); i++){
            DataTypes.GeneratedMachineDataTypes.Rotor xmlRotor = xmlMachine.getRotors().getRotor().get(i);

            //create string source
            String source = "";
            for(int j=0; j< xmlRotor.getMapping().size() ; j++)
            {
                source = source.concat(xmlRotor.getMapping().get(j).getFrom());
            }
            String target = "";
            for(int j=0; j< xmlRotor.getMapping().size() ; j++)
            {
                target = target.concat(xmlRotor.getMapping().get(j).getTo());
            }
            machineBuilder.defineRotor(xmlRotor.getId(),source,target,xmlRotor.getNotch());
        }
    }

    //GETTERS
    //2.1
    public int getNumOfRotorsBeingUsed(){
        return m_machineWrapper.getXMLMachine().getRotorsCount();
    }

    public int getNumOfAvailableRotors(){
        return m_machineWrapper.getXMLMachine().getRotors().getRotor().size();
    }
    //2.2
    public Map<Integer, Integer> getNotchPosInEveryRotor(){
        Map<Integer, Integer> res = new HashMap<>();
        for(DataTypes.GeneratedMachineDataTypes.Rotor rotor: m_machineWrapper.getXMLMachine().getRotors().getRotor()){
            res.put(rotor.getId(), rotor.getNotch());
        }

        return res;
    }
    //2.3
    public int getNumOfReflectore(){
        return m_machineWrapper.getXMLMachine().getReflectors().getReflector().size();
    }
    //2.4
    public int getNumOfProccesedMessages(){
        return m_machineWrapper.getNumOfPreccesedMessages();
    }
    //2.5
    public boolean secretIsAvailable(){
        return m_machineWrapper.isSecretHasBeenSet();
    }
    //return the rotors id according to their position
    public List<Integer> getSelectedRotorsInOrder(){
        return m_machineWrapper.getSecret().getSelectedRotorsInOrder();
    }
    public List<Integer> getSelectedRotorsPos(){
        return m_machineWrapper.getSecret().getSelectedRotorsPositions();
    }
    public int getSelectedReflector(){
        return m_machineWrapper.getSecret().getSelectedReflector();
    }

    //4
    public void setInitialSecretAutomatically() {
        m_machineWrapper.setInitialSecretAutomatically();
    }


    public Map<Integer,Integer> getLocationOfNotchInEachRotor() {
        return m_machineWrapper.getLocationOfNotchInEachRotor();
    }

    public int getNumOfAvailableReflectors() {
        return m_machineWrapper.getXMLMachine().getReflectors().getReflector().size();
    }

    public int getNumOfProcessedMessages() {
        return m_machineWrapper.getNumOfPreccesedMessages();
    }

    public boolean isSecretLoaded() {
        return m_machineWrapper.isSecretHasBeenSet();
    }


    public String processString(String unprocessedString) throws Exception {
        for (char ch: unprocessedString.toUpperCase().toCharArray()){
            if(!m_machineWrapper.isABCContainsChar(ch)){
                throw new Exception("The ABC the the system is not contains: "+ ch);
            }
        }

        return m_machineWrapper.process(unprocessedString);
    }

    public void resetMachine() {
        m_machineWrapper.initFromSecret(m_machineWrapper.getSecret());
    }

    public boolean hasRotor(int i) {
        return i < m_machineWrapper.getSecret().getSelectedRotorsInOrder().size() ? true : false;
    }

    public int getRotorNum(int i) {
        return m_machineWrapper.getSecret().getSelectedRotorsInOrder().get(i);
    }

    public char getInitialLocationOfRotor(int i) {
        int abcIndex = m_machineWrapper.getSecret().getSelectedRotorsPositions().get(i);
        return m_machineWrapper.getXMLMachine().getABC().charAt(abcIndex-1);
    }

    public int getReflectorLetter() {
        return m_machineWrapper.getSecret().getSelectedReflector();
    }
    public Map<String, Set<String>> getHistory(double[] avg){
        return m_machineWrapper.getHistory(avg);
    }

    public void ValidateReceivedRotors(List<Integer> rotorsList) throws Exception{
        String exception = "";
        List<Rotor> xmlRotorsList = m_machineWrapper.getXMLMachine().getRotors().getRotor();
        for(int i=0 ; i<rotorsList.size() ; i++) {
            boolean exist = false;
            for(Rotor rotor : xmlRotorsList){
                if(rotor.getId() == rotorsList.get(i)){
                    exist=true;
                }
            }
            if(!exist){
                //does not exist
                exception+=rotorsList.get(i);
                exception += " ";
                exception+=",";
            }
        }
        if(!exception.equals("")){
            exception = exception.substring(0, exception.length() - 1);
            exception += "Does not exist in the machine";
            exception = "Rotors id's: " + exception;
            throw new Exception(exception);
        }
    }

    public void ValidateInitialLocations(String initialLocationString) throws Exception {
        char[] locations =  initialLocationString.toUpperCase().toCharArray();
        int rotorsSize = m_machineWrapper.getXMLMachine().getRotorsCount();
        String exception = "";
        if(rotorsSize != locations.length){
            throw new Exception("Error: "+ initialLocationString.length() +
                    " letters were loaded, but "+  rotorsSize+
                    " should have been loaded according to the number of rotors.");
        }
        for(int i=0 ; i < locations.length ; i++){
            if(!m_machineWrapper.getXMLMachine().getABC().contains(String.valueOf(locations[i]))) {
                //does not exist
                exception += locations[i];
                exception += " ";
                exception += ",";
            }
        }
        if(!exception.equals("")){
            exception = exception.substring(0, exception.length() - 1);
            exception += "Does not exist in the machine ABC";
            throw new Exception(exception);
        }
    }

    public void ValidateReflector(String reflector) throws Exception{
        boolean exist = false;
        List<Reflector> xmlReflectorsList = m_machineWrapper.getXMLMachine().getReflectors().getReflector();
        for(Reflector xmlReflector : xmlReflectorsList){
            if(xmlReflector.getId().equals(reflector)){
                exist=true;
            }
        }
        if(!exist){
            //does not exist
            throw new Exception(reflector + " is not a valid reflector in the machine.");
        }
    }

    public boolean commandIsPossible(int i) {
        switch (i){
            case 2:{
                if(m_machineWrapper != null){
                    if(m_machineWrapper.getIsXMLLoaded()){
                        return true;
                    }
                }
                return false;
            }
            case 3:{
                if(m_machineWrapper != null){
                    if(m_machineWrapper.getIsXMLLoaded()){
                        return true;
                    }
                }
                return false;
            }
            case 4:{
                if(m_machineWrapper != null){
                    if(m_machineWrapper.getIsXMLLoaded()){
                        return true;
                    }
                }
                return false;
            }
            case 5:{
                return m_machineWrapper.isSecretHasBeenSet();
            }
            case 8:{
                //TODO:: implement
                /* DEBBUGING*/
                return true;
            }
        }
        return false;
    }

    public void CreateManualSecret(List<Integer> rotorsList, String initialLocationString, String reflector) {
        m_machineWrapper.SetInitialSecretManually(rotorsList,initialLocationString,reflector);
    }

    public boolean IsLegalStringOfDictionaryWords(String unprocessedString) throws Exception {
        String[] words = unprocessedString.split(" ");
        for(String word: words){
            if(!(dm.getDecipher().getDictionary().getWords().contains(word))){
                throw new Exception("The word: "+word+" is not in the dictionary");
            }
        }
        return true;
    }

    public void startBruteForce(String unprocessedString, Integer difficultySelection, Integer missionSizeSelection, Integer numOfAgentsSelection) {
        dm.set(unprocessedString, m_machineWrapper.getSecret(), difficultySelection, missionSizeSelection, numOfAgentsSelection);
        Thread thread = new Thread(dm);
        thread.start();
    }

    public Integer getMaxAllowedAgents() {
        return dm.getDecipher().getAgents();
    }
}
