package Producer;

import DataTypes.GeneratedMachineDataTypes.Decipher;
import DataTypes.GeneratedMachineDataTypes.Machine;
import DataTypes.SecretWithMissionSize;
import InputValidation.Util;
import calc.DifficultyCalc;
import calc.SecretCalc;
import pukteam.enigma.component.machine.api.EnigmaMachine;
import pukteam.enigma.component.machine.api.Secret;
import sun.management.Agent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

public class Manager implements Runnable {
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


    public Manager(Decipher decipher, Machine xmlMachine){
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
        }
    }

    private void difficultyEasy() {
        int numOfCombinations = DifficultyCalc.easy(m_xmlMachine.getRotorsCount(), m_xmlMachine.getABC());
        m_secret = SecretCalc.resetRotorsPositions(m_secret, m_xmlMachine.getRotorsCount());
        SecretWithMissionSize missionToInsert = new SecretWithMissionSize();
        int sizeOfCurrMission;
        int numOfMissions = 0;

    // TODO: start all agents
        for (int i = 0; i < m_numOfAgentsSelection; i++) {
            Thread agent = new Thread(new Consumer.Agent(m_missionsQueue, m_responeQueue, m_unprocessedString, m_xmlMachine, m_decipher));
            agent.start();
        }
        //put all missions in queue
        while(numOfCombinations > 0){
            if(numOfCombinations < m_missionSizeSelection){
                sizeOfCurrMission = numOfCombinations;
            }
            else{
                sizeOfCurrMission = m_missionSizeSelection;
            }
            missionToInsert.setMissionSize(sizeOfCurrMission);
            missionToInsert.setSecret(m_secret);
            try{
                m_missionsQueue.put(missionToInsert);
            }
            catch (Exception e){

            }
            m_secret = SecretCalc.addPositions(count, m_secret, sizeOfCurrMission, m_xmlMachine.getRotorsCount(), m_xmlMachine.getABC());
            numOfCombinations -= m_missionSizeSelection;
        }

        //take response from queue
        while(numOfMissions >= count[0]){
            try{
                m_candidateStrings.add(m_responeQueue.take());
                if(numOfMissions == count[0])
                    break;
            }
            catch(Exception e){}
        }
    }

    public void set(String i_unprocessedString, Secret i_secret, Integer i_difficultySelection, Integer i_missionSizeSelection, Integer i_numOfAgentsSelection) {
        m_unprocessedString = i_unprocessedString;
        m_secret = i_secret;
        m_difficultySelection = i_difficultySelection;
        m_missionSizeSelection = i_missionSizeSelection;
        m_numOfAgentsSelection = i_numOfAgentsSelection;
    }

}
