package Producer;

import Consumer.Agent;
import DataTypes.*;
import DataTypes.GeneratedMachineDataTypes.Decipher;
import DataTypes.GeneratedMachineDataTypes.Machine;
import DataTypes.GeneratedMachineDataTypes.Reflector;
import DataTypes.GeneratedMachineDataTypes.Rotor;
import InputValidation.Util;
import calc.DifficultyCalc;
import calc.SecretCalc;
import machine.EnigmaMachineApplication;
import machine.EnigmaMachineWrapper;
import pukteam.enigma.component.machine.api.EnigmaMachine;
import pukteam.enigma.component.machine.api.Secret;
import pukteam.enigma.component.machine.builder.EnigmaMachineBuilder;
import pukteam.enigma.component.machine.secret.SecretBuilder;
import pukteam.enigma.factory.EnigmaComponentFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static machine.EnigmaMachineApplication.DefineReflectors;
import static machine.EnigmaMachineApplication.DefineRotors;

public class Manager implements Runnable {
    private EnigmaMachineWrapper m_machineWrapper;
    private Decipher m_decipher;
    private Machine m_xmlMachine;
   //private List<Thread> m_agentList;
    private BlockingQueue<SecretWithMissionSize> m_missionsQueue;
    private BlockingQueue<CandidateStringWithEncryptionInfo> m_responeQueue;
    private ArrayList<Agent> m_agentListInstances;
    private boolean m_isFinished;
    private String m_totalTime;

    public List<CandidateStringWithEncryptionInfo> getCandidateList() {
        return m_candidateStrings;
    }

    private List<CandidateStringWithEncryptionInfo> m_candidateStrings = new ArrayList<>();
    private Integer[] count = new Integer[1];
    private boolean m_isSuspend = false;

    private String m_processedString;
    private Secret m_secret;
    private Integer m_difficultySelection;
    private Integer m_missionSizeSelection;
    private Integer m_numOfAgentsSelection;
    private Thread m_missionsThread;
    private int m_currNumOfCombinations;

    Instant m_agentsStartedTime;

    public Manager(EnigmaMachine machine, Decipher decipher, Machine xmlMachine){
        EnigmaMachineBuilder machineBuilder = EnigmaComponentFactory.INSTANCE.buildMachine(xmlMachine.getRotorsCount(),xmlMachine.getABC());
        EnigmaMachineApplication.DefineRotors(machineBuilder,xmlMachine);
        EnigmaMachineApplication.DefineReflectors(machineBuilder,xmlMachine);
        m_machineWrapper = new EnigmaMachineWrapper(machineBuilder.create());
        m_decipher = new Decipher();
        m_decipher.setDictionary(Util.removeDoubleWordsAndExcludeChars(decipher.getDictionary()));
        m_decipher.setAgents(decipher.getAgents());
        m_xmlMachine = xmlMachine;
        m_isFinished = false;
        count[0] = 0;
    }

    public Decipher getDecipher() {
        return m_decipher;
    }

    public void setDecipher(Decipher m_decipher) {
        this.m_decipher = m_decipher;
    }

    public void run() {
        final int QUEUE_SIZE = 15000;

        //m_missionsQueue = new ArrayBlockingQueue<>(QUEUE_SIZE);
        //m_responeQueue = new ArrayBlockingQueue<>(QUEUE_SIZE);
        m_missionsQueue = new LinkedBlockingQueue<>();
        m_responeQueue = new LinkedBlockingQueue<>();
        m_missionsThread =
            new Thread(
                    () -> {
                      switch (m_difficultySelection) {
                        case 1:
                          difficultyEasy();
                          break;
                        case 2:
                          difficultyMedium();
                          break;
                        case 3:
                          difficultyHard();
                          break;
                        case 4:
                          difficultyImpossible();
                          break;
                      }
                        waitToAllAgentsToFinish();
                    });
        m_missionsThread.start();
        try {
            m_missionsThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void difficultyImpossible(){
        List<Rotor> rotors = m_xmlMachine.getRotors().getRotor();
        int[] numOfMissions = new int[1];
        numOfMissions[0] = 0;
        int numOfCombinations = DifficultyCalc.easy(m_xmlMachine.getRotorsCount(), m_xmlMachine.getABC());

        List<Integer> rotorsIDs = new ArrayList<>();
        for(int i=0; i<rotors.size(); i++){
            rotorsIDs.add(i);
        }
        List<Set<Integer>> combinationsSet = new ArrayList<>();
        combinationsSet = DifficultyCalc.getSubsets(rotorsIDs, m_xmlMachine.getRotorsCount());

        for(Set<Integer> combination: combinationsSet){
            Set<Integer[]> rotorsCombinations = new HashSet<>();
            DifficultyCalc.getAllCombinationsOfList(combination, new Stack<Integer>(), combination.size(), rotorsCombinations);
            for(Integer[] combination2: rotorsCombinations){
                for (Reflector refl : m_xmlMachine.getReflectors().getReflector()) {
                    SecretBuilder secretBuilder = m_machineWrapper.getMachine().createSecret();
                    secretBuilder.selectReflector(Util.romanToInt(refl.getId()));
                    for (Integer rotorID : combination2) {
                        secretBuilder.selectRotor(rotorID, 1);
                    }
                    m_secret = secretBuilder.create();
                    insertMissionsToQueue(numOfCombinations, numOfMissions);
                }
            }
        }
    }

    private void difficultyHard() {
        int[] numOfMissions = new int[1];
        numOfMissions[0] = 0;
        Set<Integer> rotorsSet = new HashSet<>(m_secret.getSelectedRotorsInOrder());
        Set<Integer[]> rotorsCombinations = new HashSet<>();
        DifficultyCalc.getAllCombinationsOfList(rotorsSet, new Stack<Integer>(), rotorsSet.size(), rotorsCombinations);
        int numOfCombinations = DifficultyCalc.easy(m_xmlMachine.getRotorsCount(), m_xmlMachine.getABC());
        m_currNumOfCombinations = m_xmlMachine.getReflectors().getReflector().size()* numOfCombinations*(DifficultyCalc.factorial(m_xmlMachine.getRotorsCount()));

        startAllAgents();

        for(Integer[] combination: rotorsCombinations){
            for (Reflector refl : m_xmlMachine.getReflectors().getReflector()) {
                SecretBuilder secretBuilder = m_machineWrapper.getMachine().createSecret();
                secretBuilder.selectReflector(Util.romanToInt(refl.getId()));
                for (Integer rotorID : combination) {
                    secretBuilder.selectRotor(rotorID, 1);
                }
                m_secret = secretBuilder.create();
                insertMissionsToQueue(numOfCombinations, numOfMissions);
            }
        }

        takeResponsesFromAgents(numOfMissions);
    }

    private void difficultyMedium() {
        int[] numOfMissions = new int[1];
        numOfMissions[0] = 0;
        List<Integer> rotorsOrder = m_secret.getSelectedRotorsInOrder();

        startAllAgents();

        int numOfCombinations = DifficultyCalc.easy(m_xmlMachine.getRotorsCount(), m_xmlMachine.getABC());
        m_currNumOfCombinations = m_xmlMachine.getReflectors().getReflector().size()* numOfCombinations;
        for (Reflector refl : m_xmlMachine.getReflectors().getReflector()) {
            SecretBuilder secretBuilder = m_machineWrapper.getMachine().createSecret();
            secretBuilder.selectReflector(Util.romanToInt(refl.getId()));
            for(int i=0; i<rotorsOrder.size(); i++){
                secretBuilder.selectRotor(rotorsOrder.get(i), 1);
            }
            m_secret = secretBuilder.create();
            insertMissionsToQueue(numOfCombinations, numOfMissions);
        }

        takeResponsesFromAgents(numOfMissions);
    }

    private void difficultyEasy() {
        m_currNumOfCombinations = DifficultyCalc.easy(m_xmlMachine.getRotorsCount(), m_xmlMachine.getABC());
        m_secret = SecretCalc.resetRotorsPositions(m_secret, m_xmlMachine.getRotorsCount());
        int[] numOfMissions = new int[1];
        numOfMissions[0] = 0;

        //start agents
        //startAllAgents();

        //put all missions in queue
        insertMissionsToQueue(m_currNumOfCombinations, numOfMissions);
        startAllAgents();
        //take response from queue
        takeResponsesFromAgents(numOfMissions);

    }

    private void waitToAllAgentsToFinish() {
        for(Thread agent: m_agentListInstances){
            //TODO: interrupt to all the the agents, and check inside them if interrupt and the queue is empty then stop
            agent.interrupt();
            try{
                agent.join();
            } catch (Exception e){}
        }
        m_isFinished = true;
        System.out.println("The DM finished feel free to press something...");
    }

    private void takeResponsesFromAgents(int[] numOfMissions) {
        CandidateStringWithEncryptionInfo response = null;
        while(numOfMissions[0] >= count[0]){
//            try{
            if(numOfMissions[0] == count[0] && m_responeQueue.isEmpty())
                break;
            if(!m_responeQueue.isEmpty()){
//                 m_candidateStrings.add(m_responeQueue.take());
                response = m_responeQueue.poll();
                if (response != null) {
                    if ((!response.getString().equals(" ")))
                    m_candidateStrings.add(response);
                }
            }
//            catch(Exception e){}
        }
    }

    private void startAllAgents() {
        //m_agentList = new ArrayList<>(m_numOfAgentsSelection);
        m_agentListInstances = new ArrayList<>(m_numOfAgentsSelection);
        for (int i = 0; i < m_numOfAgentsSelection; i++) {
            Agent agentInstance = new Consumer.Agent(count, m_missionsQueue, m_responeQueue, m_processedString, m_xmlMachine, m_decipher);
            //Thread agentThread = new Thread(agentInstance);
            agentInstance.setName("Agent-"+i);
           // m_agentList.add(agentThread);
            m_agentListInstances.add(agentInstance);
        }
        for(Thread agent: m_agentListInstances){
            agent.start();
        }
        m_agentsStartedTime = null;
        m_agentsStartedTime = Instant.now();
    }

    private void insertMissionsToQueue(int numOfCombinations, int[] numOfMissions) {
        int sizeOfCurrMission;
        while(numOfCombinations > 0){
            if(numOfCombinations < m_missionSizeSelection){
                sizeOfCurrMission = numOfCombinations;
            }
            else{
                sizeOfCurrMission = m_missionSizeSelection;
            }
            SecretWithMissionSize missionToInsert = new SecretWithMissionSize();
            missionToInsert.setMissionSize(sizeOfCurrMission);
            Secret secretToInsert = createNewSecret(m_secret);
            missionToInsert.setSecret(secretToInsert);
            try{
                m_missionsQueue.put(missionToInsert);
                numOfMissions[0]+=sizeOfCurrMission;
            }
            catch (Exception e){

            }
            m_secret = SecretCalc.addPositions(m_secret, sizeOfCurrMission, m_xmlMachine.getRotorsCount(), m_xmlMachine.getABC());
            numOfCombinations -= m_missionSizeSelection;
        }
    }

    private Secret createNewSecret(Secret i_secret) {
        SecretBuilder secretBuilder = m_machineWrapper.getMachine().createSecret();
        for(int i = 0; i<i_secret.getSelectedRotorsInOrder().size(); i++){
            secretBuilder.selectRotor(i_secret.getSelectedRotorsInOrder().get(i), i_secret.getSelectedRotorsPositions().get(i));
        }
        secretBuilder.selectReflector(i_secret.getSelectedReflector());

        return secretBuilder.create();
    }

    public void set(String i_unprocessedString, Secret i_secret, Integer i_difficultySelection, Integer i_missionSizeSelection, Integer i_numOfAgentsSelection) {
        m_secret = createNewSecret(i_secret);
        m_machineWrapper.initFromSecret(m_secret);
        i_unprocessedString = Util.removeExcludeCharsFromString(m_decipher.getDictionary().getExcludeChars(), i_unprocessedString);
        m_processedString = m_machineWrapper.process(i_unprocessedString);
        m_difficultySelection = i_difficultySelection;
        m_missionSizeSelection = i_missionSizeSelection;
        m_numOfAgentsSelection = i_numOfAgentsSelection;
    }

//    private void createMachineFromXML(){
//        m_xmlMachine.setABC(m_xmlMachine.getABC().toUpperCase());
//        EnigmaMachineBuilder machineBuilder = EnigmaComponentFactory.INSTANCE.buildMachine(m_xmlMachine.getRotorsCount(),m_xmlMachine.getABC());
//        DefineRotors(machineBuilder,m_xmlMachine);
//        DefineReflectors(machineBuilder,m_xmlMachine);
//        m_machine = machineBuilder.create();
//    }

    public void stopDMandAgents() {
        for(Thread agent: m_agentListInstances){
            agent.interrupt();
        }
        System.out.println(getFinishStatus());
        m_missionsThread.stop();
        m_isFinished = true;
    }

    public void stopAndResumeDMandAgents() {
        for(Thread thread: m_agentListInstances){
            if(m_isSuspend){
                thread.resume();
            } else{
                thread.suspend();
            }
        }

        if(m_isSuspend){
            m_missionsThread.resume();
            m_isSuspend = false;
        } else{
            m_missionsThread.suspend();
            m_isSuspend = true;
        }
    }

    public Duration getEncryptionDurationUntilNow(){
        Instant now = Instant.now();
        return Duration.between(m_agentsStartedTime, now);
    }

    public EncryptionStatus getEncryptionStatus() {
        long seconds = getEncryptionDurationUntilNow().getSeconds();
        long absSeconds = Math.abs(seconds);
        String time = String.format(
                "%02d:%02d",
                (absSeconds % 3600) / 60,
                absSeconds % 60);

        //System.out.println(time);
        List<CandidateStringWithEncryptionInfo> candidateList = new ArrayList<>();
        for(int i=0 ; i < 10 && i < getCandidateList().size() ; i++){
            candidateList.add(getCandidateList().get(i));//TODO:: check if it works
        }

        float percentage = DifficultyCalc.getPrecentageWorkDone(m_currNumOfCombinations,count[0]);

        List<SecretWithCount> currentThreadsJobs = new ArrayList<>();
        for(Agent agent : m_agentListInstances)
        {
            currentThreadsJobs.add(agent.getThreadsJob());
        }

        EncryptionStatus status = new EncryptionStatus(time,candidateList,1
                ,currentThreadsJobs);
        return status;
    }

    public boolean isFinished() {
        return m_isFinished;
    }

    public FinishStatus getFinishStatus() {
        Instant now = Instant.now();

        return new FinishStatus(
                Duration.between(m_agentsStartedTime, now).getSeconds(),
                count[0],
                m_agentListInstances,
                m_candidateStrings);
    }
}
