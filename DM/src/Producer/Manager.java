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

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static machine.EnigmaMachineApplication.DefineReflectors;
import static machine.EnigmaMachineApplication.DefineRotors;

public class Manager implements Runnable {
    private static final String READY = "READY";

    private EnigmaMachineWrapper m_machineWrapper;
    private Decipher m_decipher;
    private Machine m_xmlMachine;
   //private List<Thread> m_agentList;
    private BlockingQueue<SecretWithMissionSize> m_missionsQueue;
    private BlockingQueue<CandidateStringWithEncryptionInfo> m_responeQueue;
    private ArrayList<Agent> m_agentListInstances;
    private boolean m_isFinished = false;

    public boolean isRoundFinished() {
        return m_roundFinished;
    }

    public void setIsRoundFinished(boolean m_roundFinished) {
        this.m_roundFinished = m_roundFinished;
    }

    private boolean m_roundFinished = false;
    private String m_totalTime;
    private boolean m_stayConnected = true;
    private List<Socket> m_agentSockets;
    private List<ObjectOutputStream> m_agentsOutputStreams;
    private List<ObjectInputStream> m_agentsInputStreams;

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
    private int m_numOfAgents = 0;
    private int m_port;
    private ServerSocket serverSocket;

    Instant m_agentsStartedTime;

    public Manager(){
        m_agentSockets = new LinkedList<>();
        try {
            serverSocket = new ServerSocket(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        m_port = serverSocket.getLocalPort();
    }

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
        //yair changes
        try{
            m_port = serverSocket.getLocalPort();
            Thread thread = new Thread(()->{
                Socket socket;
                while(m_stayConnected){
                    try{
                        socket = serverSocket.accept();
                        m_agentSockets.add(socket);
                        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                        m_agentsOutputStreams.add(outputStream);
                        m_agentsInputStreams.add(inputStream);
                        outputStream.writeObject(m_xmlMachine);
                        outputStream.writeObject(m_decipher);
                        outputStream.flush();
                        m_numOfAgents++;
                    }catch (Exception e){

                    }
                }
            });
            thread.start();
        }catch (Exception e){

        }

        allocateMissionsAndWaitForCandidates();
    }

    private void allocateMissionsAndWaitForCandidates() {
        m_missionsQueue = new LinkedBlockingQueue<>();
        try {
            switch (m_difficultySelection){
                case 1:
                    allocateMissionsToAgentsEasy();
                    break;
                case 2:

                    break;
                case 3:

                    break;
                case 4:

                    break;
            }

            receiveResponseFromAgents();

        } catch (Exception ex){

        }
    }

    private void receiveResponseFromAgents() throws IOException, ClassNotFoundException {
        while(!m_isFinished && !m_roundFinished){
            for(ObjectInputStream inputStream : m_agentsInputStreams){
                CandidateStringWithEncryptionInfo candidate = (CandidateStringWithEncryptionInfo)inputStream.readObject();
                if(!candidate.getString().equals("DONE")){
                    m_responeQueue.add(candidate);
                    m_candidateStrings.add(candidate);
                }
            }
        }
        if(m_roundFinished){
            m_roundFinished = false;
            sendMassageToAgents("END_OF_SESSION");
            allocateMissionsAndWaitForCandidates();
        }
        if(m_isFinished){
            sendMassageToAgents("LOGOUT");
        }
    }

    private void sendMassageToAgents(String msg) throws IOException {
        for(ObjectOutputStream objectOutputStream : m_agentsOutputStreams){
            objectOutputStream.writeUTF(msg);
        }
        if(msg.equals("LOGOUT")){
            for(Socket socket : m_agentSockets){
                socket.close();
            }
        }
    }

    private void allocateMissionsToAgentsEasy() throws InterruptedException, IOException {
        m_currNumOfCombinations = DifficultyCalc.easy(m_xmlMachine.getRotorsCount(), m_xmlMachine.getABC());
        m_secret = SecretCalc.resetRotorsPositions(m_secret, m_xmlMachine.getRotorsCount());
        int[] numOfMissions = new int[1];
        numOfMissions[0] = 0;

        //put missions in queues
        insertMissionsToQueue(m_currNumOfCombinations, numOfMissions);
        int numMissionsToEachAgent = m_missionsQueue.size() / m_numOfAgents;
        List<SecretWithMissionSize> agentMissions;
        while(!m_missionsQueue.isEmpty()){
            for (ObjectOutputStream outputStream : m_agentsOutputStreams) {
                agentMissions = new LinkedList<>();
                for (int i = 0; i < numMissionsToEachAgent; i++) {
                    agentMissions.add(m_missionsQueue.poll());
                    if (m_missionsQueue.isEmpty()) {
                        break;
                }
                outputStream.writeObject(agentMissions);
              }
            }
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

        startAllAgents();
        takeResponsesFromAgents(numOfMissions);
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

        startAllAgents();

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
            Agent agentInstance = new Consumer.Agent(count[0], m_missionsQueue, m_responeQueue, m_processedString, m_xmlMachine, m_decipher);
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

    public int getPort() {
        return m_port;
    }
}
