package Producer;

import DataTypes.GeneratedMachineDataTypes.Decipher;
import DataTypes.GeneratedMachineDataTypes.Machine;
import InputValidation.Util;
import calc.DificultyCalc;
import calc.SecretCalc;
import pukteam.enigma.component.machine.api.EnigmaMachine;
import pukteam.enigma.component.machine.api.Secret;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

public class Manager implements Runnable {
    private Decipher m_decipher;
    private Machine m_xmlMachine;
    private List<Thread> m_agentList;
    private BlockingQueue<Secret> m_missionsQueue;
    private BlockingQueue<String> m_responeQueue;
    private List<String> m_candidateStrings = new ArrayList<>();

    private String m_unprocessedString;
    private Secret m_secret;
    Integer m_difficultySelection;
    Integer m_missionSizeSelection;
    Integer m_numOfAgentsSelection;


    public Manager(Decipher decipher, Machine xmlMachine){
        m_decipher = new Decipher();
        m_decipher.setDictionary(Util.removeDoubleWordsAndExcludeChars(decipher.getDictionary()));
        m_decipher.setAgents(decipher.getAgents());
        m_xmlMachine = xmlMachine;
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
                int numOfCombinations = DificultyCalc.easy(m_xmlMachine.getRotorsCount(), m_xmlMachine.getABC());
                m_secret = SecretCalc.resetRotorsPositions(m_xmlMachine.getRotorsCount());

                //TODO: start all agents

                while(numOfCombinations > 0){
                    try{
                        m_missionsQueue.put(m_secret);
                    }
                    catch (Exception e){}
                    m_secret = SecretCalc.addPositions(m_missionSizeSelection, m_xmlMachine.getRotorsCount(), m_xmlMachine.getABC());
                    numOfCombinations -= m_missionSizeSelection;
                }

                break;
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
