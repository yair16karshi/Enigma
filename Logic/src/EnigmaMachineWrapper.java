import DataTypes.GeneratedMachineDataTypes.Machine;
import DataTypes.GeneratedMachineDataTypes.Rotor;
import DataTypes.Util.ProcessStringAndTime;
import InputValidation.Util;
import pukteam.enigma.component.machine.api.EnigmaMachine;
import pukteam.enigma.component.machine.api.Secret;
import pukteam.enigma.component.machine.secret.SecretBuilder;

import java.util.*;
import java.util.function.Consumer;

public class EnigmaMachineWrapper  {
    private EnigmaMachine m_machine;
    private Machine m_xmlMachine;
    private Map<Secret, Set<ProcessStringAndTime>> m_history = new HashMap<>();
    private int m_numOfPreccesedMessages;
    private boolean m_secretHasBeenSet = false;
    private boolean m_isXMLLoaded = false;

    public EnigmaMachineWrapper(EnigmaMachine machine){
        m_machine = machine;
        m_numOfPreccesedMessages = 0;
        m_history.clear();
    }
    public void setSecretHasBeenSet(){
        m_secretHasBeenSet = true;
    }

    public SecretBuilder createSecret() {
        return m_machine.createSecret();
    }

    public Secret getSecret() {
        return m_machine.getSecret();
    }

    public void initFromSecret(Secret secret) {
        m_machine.initFromSecret(secret);
    }

    public void resetToInitialPosition() {
        m_machine.resetToInitialPosition();
    }


    public void setInitialPosition(String s) {
        m_machine.setInitialPosition(s);
    }

    public String process(String s) {
        ProcessStringAndTime stringAndTime = new ProcessStringAndTime();
        String processedString;
        m_numOfPreccesedMessages++;

        long startTime = System.nanoTime();
        processedString = m_machine.process(s);
        long stopTime = System.nanoTime();

        stringAndTime.setUnprocessedString(s);
        stringAndTime.setProcessedString(processedString);
        stringAndTime.setTime(stopTime - startTime);
        m_history.get(m_machine.getSecret()).add(stringAndTime);

        return processedString;
    }

    public char process(char c) {
        m_numOfPreccesedMessages++;
        return m_machine.process(c);
    }


    public void setDebug(boolean b) {
        m_machine.setDebug(b);
    }


    public void consumeState(Consumer<String> consumer) {
        m_machine.consumeState(consumer);
    }

    public int getNumOfPreccesedMessages() {
        return m_numOfPreccesedMessages;
    }

    public void setNumOfPreccesedMessages(int m_numOfPreccesedMessages) {
        this.m_numOfPreccesedMessages = m_numOfPreccesedMessages;
    }

    public boolean isSecretHasBeenSet() {
        return m_secretHasBeenSet;
    }


    public Machine getXMLMachine() {
        return m_xmlMachine;
    }

    public void setXMLMachine(Machine m_xmlMachine) {
        this.m_xmlMachine = m_xmlMachine;
    }

    public void setInitialSecretAutomatically() {
        SecretBuilder secretBuilder = m_machine.createSecret();
        List<Integer> shuffledList = new ArrayList<>();
        Map<Integer, Integer> rndRotors = getRandomRotors(m_xmlMachine.getRotors().getRotor().size(), m_xmlMachine.getRotorsCount(), m_xmlMachine.getABC().length());
        shuffledList.addAll(rndRotors.keySet());
        Collections.shuffle(shuffledList);
        for (int rotorId : shuffledList) {
            secretBuilder.selectRotor(rotorId, rndRotors.get(rotorId));
        }

        int rndRefl = getRandomInRange(1, m_xmlMachine.getReflectors().getReflector().size());
        secretBuilder.selectReflector(rndRefl);

        Secret secret = secretBuilder.create();
        m_machine.initFromSecret(secret);
        this.setSecretHasBeenSet();

        if(m_history.get(secret) == null)
        {
            m_history.put(secret, new HashSet<ProcessStringAndTime>());
        }
    }

    public void SetInitialSecretManually(List<Integer> rotorsList, String initialLocationString, String reflector) {
        SecretBuilder secretBuilder = m_machine.createSecret();
        for (int i=0 ; i<rotorsList.size() ; i++) {
            secretBuilder.selectRotor(rotorsList.get(i), initialLocationString.charAt(i));
        }
        secretBuilder.selectReflector(Util.romanToInt(reflector));

        Secret secret = secretBuilder.create();
        m_machine.initFromSecret(secret);
        this.setSecretHasBeenSet();
        if(m_history.get(secret) == null)
        {
            m_history.put(secret, new HashSet<ProcessStringAndTime>());
        }
    }

    private int getRandomInRange(int min, int max) {
        Random rand = new Random();
        return rand.nextInt(max)+min;
    }

    private Map<Integer,Integer> getRandomRotors(int range, int rotorsCount, int abcSize) {
        Map<Integer, Integer> res = new HashMap<>();
        int added = 0;
        int rndRotorId;
        while(added < rotorsCount){
            rndRotorId = getRandomInRange(1, range);
            if(res.get(rndRotorId) == null){
                res.put(rndRotorId, getRandomInRange(1, abcSize));
                added++;
            }
        }

        return res;
    }

    public Map<Integer,Integer> getLocationOfNotchInEachRotor() {
        Map<Integer, Integer> res = new HashMap<>();
        for(Rotor rotor: m_xmlMachine.getRotors().getRotor()){
            res.put(rotor.getId(), rotor.getNotch());
        }

        return res;
    }

    public boolean isABCContainsChar(char ch) {
        return m_xmlMachine.getABC().contains(String.valueOf(ch));
    }

    public boolean getIsXMLLoaded() {
        return m_isXMLLoaded;
    }

    public void setIsXMLLoaded(boolean isXMLLoaded) {
        this.m_isXMLLoaded = isXMLLoaded;
    }

    public Map<String,Set<String>> getHistory(double[] avg) {
        Map<String, Set<String>> res = new HashMap<>();
        avg[0]=0;
        double[] avgPerSecret = new double[1];
        int avgCount=0;

        for(Secret secret: m_history.keySet()){
            Set<String> processes = processesToStrings(m_history.get(secret), avgPerSecret);
            if (avgPerSecret[0] != 0) {
                avgCount++;
            }
            avg[0]+= avgPerSecret[0];
            res.put(secretToString(secret), processes);
        }
        if (avgCount != 0) {
            avg[0] /= avgCount;
        }

        return res;
    }

    private String secretToString(Secret secret){
        String res ="<";
        for(int i=0; i< secret.getSelectedRotorsInOrder().size() ; i++){
            res+= secret.getSelectedRotorsInOrder().get(i);
            res+=',';
        }
        res = res.substring(0, res.length() - 1);
        res += "><";

        for(int i=0 ; i<secret.getSelectedRotorsPositions().size(); i++){
            res += m_xmlMachine.getABC().charAt(secret.getSelectedRotorsPositions().get(i)-1);
        }
        res+= "><";
        res += Util.fromIntToRoman(secret.getSelectedReflector());
        res+=">";
        return res;
    }

    private Set<String> processesToStrings(Set<ProcessStringAndTime> input, double[] avg){
        Set<String> res = new HashSet<>();
        avg[0] = 0;
        int avgCount = 0;

        for(ProcessStringAndTime processStringAndTime: input){
            res.add("#.<" + processStringAndTime.getUnprocessedString() + "> --> <"+processStringAndTime.getProcessedString()+"> ("+processStringAndTime.getTime()+")");
            avgCount++;
            avg[0]+= processStringAndTime.getTime();
        }
        if (avgCount != 0) {
            avg[0] /= avgCount;
        }

        return res;
    }
}
