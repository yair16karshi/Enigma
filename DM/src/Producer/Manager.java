package Producer;

import DataTypes.GeneratedMachineDataTypes.Decipher;
import DataTypes.GeneratedMachineDataTypes.Machine;
import DataTypes.GeneratedMachineDataTypes.Reflector;
import DataTypes.SecretWithMissionSize;
import InputValidation.Util;
import calc.DifficultyCalc;
import calc.SecretCalc;
import machine.EnigmaMachineWrapper;
import pukteam.enigma.component.machine.api.EnigmaMachine;
import pukteam.enigma.component.machine.api.Secret;
import pukteam.enigma.component.machine.builder.EnigmaMachineBuilder;
import pukteam.enigma.component.machine.secret.SecretBuilder;
import pukteam.enigma.factory.EnigmaComponentFactory;
import sun.management.Agent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

import static machine.EnigmaMachineApplication.DefineReflectors;
import static machine.EnigmaMachineApplication.DefineRotors;

public class Manager implements Runnable {
    private EnigmaMachine m_machine;
    private Decipher m_decipher;
    private Machine m_xmlMachine;
    private List<Thread> m_agentList;
    private BlockingQueue<SecretWithMissionSize> m_missionsQueue;
    private BlockingQueue<String> m_responeQueue;
    private List<String> m_candidateStrings = new ArrayList<>();
    private Integer[] count = new Integer[1];

    private String m_unprocessedString;
    private Secret m_secret;
    private Integer m_difficultySelection;
    private Integer m_missionSizeSelection;
    private Integer m_numOfAgentsSelection;


    public Manager(EnigmaMachine machine, Decipher decipher, Machine xmlMachine){
        m_machine = machine;
        m_decipher = new Decipher();
        m_decipher.setDictionary(Util.removeDoubleWordsAndExcludeChars(decipher.getDictionary()));
        m_decipher.setAgents(decipher.getAgents());
        m_xmlMachine = xmlMachine;
        count[0] = 0;
    }

    public Decipher getDecipher() {
        return m_decipher;
    }

    public void setDecipher(Decipher m_decipher) {
        this.m_decipher = m_decipher;
    }

    public void run() {
        final int QUEUE_SIZE = 20;

        m_missionsQueue = new ArrayBlockingQueue<>(QUEUE_SIZE);

        switch (m_difficultySelection){
            case 1:
                difficultyEasy();
                break;
            case 2:
                //TODO
                difficultyMedium();
                break;
            case 3:
                break;
            case 4:
                break;
        }
    }

    private void difficultyMedium() {
        int[] numOfMissions = new int[1];
        numOfMissions[0] = 0;
        List<Integer> rotorsOrder = m_secret.getSelectedRotorsInOrder();

        startAllAgents();

        int numOfCombinations = DifficultyCalc.easy(m_xmlMachine.getRotorsCount(), m_xmlMachine.getABC());
        for (Reflector refl : m_xmlMachine.getReflectors().getReflector()) {
            SecretBuilder secretBuilder = m_machine.createSecret();
            secretBuilder.selectReflector(Util.romanToInt(refl.getId()));
            for(int i=0; i<rotorsOrder.size(); i++){
                secretBuilder.selectRotor(rotorsOrder.get(i), 0);
            }
            m_secret = secretBuilder.create();
            insertMissionsToQueue(numOfCombinations, numOfMissions);
        }

        takeResponsesFromAgents(numOfMissions);
    }

    private void difficultyEasy() {
        int numOfCombinations = DifficultyCalc.easy(m_xmlMachine.getRotorsCount(), m_xmlMachine.getABC());
        m_secret = SecretCalc.resetRotorsPositions(m_secret, m_xmlMachine.getRotorsCount());
        SecretWithMissionSize missionToInsert = new SecretWithMissionSize();
        int[] numOfMissions = new int[1];
        numOfMissions[0] = 0;

        //start agents
        startAllAgents();

        //put all missions in queue
        insertMissionsToQueue(numOfCombinations, numOfMissions);

        //take response from queue
        takeResponsesFromAgents(numOfMissions);
    }

    private void takeResponsesFromAgents(int[] numOfMissions) {
        while(numOfMissions[0] >= count[0]){
            try{
                m_candidateStrings.add(m_responeQueue.take());
                if(numOfMissions[0] == count[0])
                    break;
            }
            catch(Exception e){}
        }
    }

    private void startAllAgents() {
        for (int i = 0; i < m_numOfAgentsSelection; i++) {
            Thread agent = new Thread(new Consumer.Agent(m_missionsQueue, m_responeQueue, m_unprocessedString, m_xmlMachine, m_decipher));
            agent.start();
        }
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
            missionToInsert.setSecret(m_secret);
            try{
                m_missionsQueue.put(missionToInsert);
                numOfMissions[0]++;
            }
            catch (Exception e){

            }
            m_secret = SecretCalc.addPositions(count, m_secret, sizeOfCurrMission, m_xmlMachine.getRotorsCount(), m_xmlMachine.getABC());
            numOfCombinations -= m_missionSizeSelection;
        }
    }

    public void set(String i_unprocessedString, Secret i_secret, Integer i_difficultySelection, Integer i_missionSizeSelection, Integer i_numOfAgentsSelection) {
        m_unprocessedString = i_unprocessedString;
        m_secret = i_secret;
        m_difficultySelection = i_difficultySelection;
        m_missionSizeSelection = i_missionSizeSelection;
        m_numOfAgentsSelection = i_numOfAgentsSelection;
    }

    private void createMachineFromXML(){
        m_xmlMachine.setABC(m_xmlMachine.getABC().toUpperCase());
        EnigmaMachineBuilder machineBuilder = EnigmaComponentFactory.INSTANCE.buildMachine(m_xmlMachine.getRotorsCount(),m_xmlMachine.getABC());
        DefineRotors(machineBuilder,m_xmlMachine);
        DefineReflectors(machineBuilder,m_xmlMachine);
        m_machine = machineBuilder.create();
    }
}
