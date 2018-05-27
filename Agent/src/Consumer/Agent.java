package Consumer;

import java.util.concurrent.BlockingQueue;

import DataTypes.GeneratedMachineDataTypes.Decipher;
import DataTypes.GeneratedMachineDataTypes.Dictionary;
import DataTypes.GeneratedMachineDataTypes.Machine;
import DataTypes.SecretWithCount;
import DataTypes.SecretWithMissionSize;
import DataTypes.CandidateStringWithEncryptionInfo;
import calc.SecretCalc;
import machine.EnigmaMachineApplication;
import machine.EnigmaMachineWrapper;
import pukteam.enigma.component.machine.api.Secret;
import pukteam.enigma.component.machine.builder.EnigmaMachineBuilder;
import pukteam.enigma.factory.EnigmaComponentFactory;

public class Agent implements Runnable{
    private BlockingQueue<SecretWithMissionSize> m_missionsQueue;
    private BlockingQueue<CandidateStringWithEncryptionInfo> m_decipheredQueue;
    private String m_stringToProcess;
    private EnigmaMachineWrapper m_machineWrapper;
    private Dictionary m_dictionry;
    private Integer[] m_count;
    /*FOR STATUS UPDATES*/
    private Secret m_currentSecret;
    private int m_jobsLeft;
    /*FOR STATUS UPDATES*/

    public Agent(Integer[] count, BlockingQueue<SecretWithMissionSize> missionsQueue,
                 BlockingQueue<CandidateStringWithEncryptionInfo> decipheredQueue,
                 String stringToProcess,
                 Machine xmlMachine,
                 Decipher decipher){

        //xmlMachine.getABC().charAt(xmlMachine.getABC().length());//TODO:: check if length or length-1
        m_missionsQueue = missionsQueue;
        m_decipheredQueue = decipheredQueue;
        m_stringToProcess = stringToProcess;
        m_dictionry = decipher.getDictionary();
        m_count = count;

        EnigmaMachineBuilder machineBuilder = EnigmaComponentFactory.INSTANCE.buildMachine(xmlMachine.getRotorsCount(),xmlMachine.getABC());
        EnigmaMachineApplication.DefineRotors(machineBuilder,xmlMachine);
        EnigmaMachineApplication.DefineReflectors(machineBuilder,xmlMachine);
        m_machineWrapper = new EnigmaMachineWrapper(machineBuilder.create());
        m_machineWrapper.setXMLMachine(xmlMachine);
    }

    @Override
    public void run(){
        while(true){
            if(!Thread.currentThread().isInterrupted()){//TODO:: verify that interrupted works
                try {
                    RunMission(m_missionsQueue.take());
                } catch (InterruptedException e) {
                    return;
                }
            } else if(Thread.currentThread().isInterrupted() && m_decipheredQueue.isEmpty()){
                return;
            }
        }
    }

    private void RunMission(SecretWithMissionSize mission){
        Secret localSecret = mission.getSecret();

        for(int i = 1 ; i <= mission.getMissionSize() ; i++){
            /*FOR STATUS UPDATES*/
            m_currentSecret = localSecret;
            m_jobsLeft = mission.getMissionSize()-i;
            /*FOR STATUS UPDATES*/

            PerformSingleString(localSecret);
            localSecret = SecretCalc.addPositions(
                    localSecret,
                    1,
                    m_machineWrapper.getXMLMachine().getRotorsCount(),
                    m_machineWrapper.getXMLMachine().getABC());
        }
    }

    private void PerformSingleString(Secret secret){
        String[] processedWords;
        boolean decypered = true;
        m_machineWrapper.initFromSecret(secret);
        String processedString = m_machineWrapper.process(m_stringToProcess);
        processedWords = processedString.split(" ");
        for(String s : processedWords){
            if(!m_dictionry.getWords().contains(" "+s+" ")){
                decypered = false;
                break;
            }
        }
        if(decypered) {
            m_decipheredQueue.add(new CandidateStringWithEncryptionInfo(processedString,Thread.currentThread().getId(),secret));
        }
        synchronized (m_decipheredQueue){
            m_count[0]++;
        }
    }

    public SecretWithCount getThreadsJob(){
        SecretWithCount res = new SecretWithCount(m_currentSecret,m_jobsLeft);
        return res;
    }
}
