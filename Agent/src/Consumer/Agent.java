package Consumer;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

import DataTypes.GeneratedMachineDataTypes.Decipher;
import DataTypes.GeneratedMachineDataTypes.Dictionary;
import DataTypes.GeneratedMachineDataTypes.Machine;
import DataTypes.SecretWithCount;
import DataTypes.SecretWithMissionSize;
import DataTypes.CandidateStringWithEncryptionInfo;
import InputValidation.Util;
import calc.SecretCalc;
import machine.EnigmaMachineApplication;
import machine.EnigmaMachineWrapper;
import pukteam.enigma.component.machine.api.Secret;
import pukteam.enigma.component.machine.builder.EnigmaMachineBuilder;
import pukteam.enigma.component.machine.secret.SecretBuilder;
import pukteam.enigma.factory.EnigmaComponentFactory;

public class Agent extends Thread{
    private BlockingQueue<SecretWithMissionSize> m_missionsQueue;
    private BlockingQueue<CandidateStringWithEncryptionInfo> m_decipheredQueue;
    private int m_OldCandidates = 0;
    private String m_stringToProcess;
    private EnigmaMachineWrapper m_machineWrapper;
    private Dictionary m_dictionry;
    private int m_count;
    /*FOR STATUS UPDATES*/
    private volatile Secret m_currentSecret;
    private volatile int m_jobsLeft;
    private int m_possibleDeciphered = 0;

    private long m_id;
    /*FOR STATUS UPDATES*/

    public Agent(int count, BlockingQueue<SecretWithMissionSize> missionsQueue,
                 BlockingQueue<CandidateStringWithEncryptionInfo> decipheredQueue,
                 String stringToProcess,
                 Machine xmlMachine,
                 Decipher decipher){

        //xmlMachine.getABC().charAt(xmlMachine.getABC().length());//TODO:: check if length or length-1
        Random rand = new Random();
        m_id = rand.nextInt(100) + 1;

        m_missionsQueue = missionsQueue;
        m_decipheredQueue = decipheredQueue;
        m_stringToProcess = stringToProcess;
        m_dictionry = decipher.getDictionary();
        m_count = count;
        m_jobsLeft = missionsQueue.size();

        EnigmaMachineBuilder machineBuilder = EnigmaComponentFactory.INSTANCE.buildMachine(xmlMachine.getRotorsCount(),xmlMachine.getABC());
        EnigmaMachineApplication.DefineRotors(machineBuilder,xmlMachine);
        EnigmaMachineApplication.DefineReflectors(machineBuilder,xmlMachine);
        m_machineWrapper = new EnigmaMachineWrapper(machineBuilder.create());
        m_machineWrapper.setXMLMachine(xmlMachine);
    }

    @Override
    public void run(){
        SecretWithMissionSize secretWithMissionSize = null;
        while(true){
            if(!Thread.currentThread().isInterrupted() ){//TODO:: verify that interrupted works
                if (!m_missionsQueue.isEmpty()){
                    secretWithMissionSize = m_missionsQueue.poll();
                    m_jobsLeft--;
                    if(secretWithMissionSize != null)
                        RunMission(secretWithMissionSize);
                }
                else{
                    m_decipheredQueue.add(new CandidateStringWithEncryptionInfo("DONE",0,null));
                }
            } else if(Thread.currentThread().isInterrupted() && m_decipheredQueue.isEmpty()){
                return;
            }
        }
    }

    private void RunMission(SecretWithMissionSize mission){
        Secret localSecret = mission.getSecret();

        for(int i = 1 ; i <= mission.getMissionSize() ; i++){
 //          System.out.println(m_count[0]);
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
        boolean decypered;
        m_machineWrapper.initFromSecret(secret);
        String processedString = m_machineWrapper.process(m_stringToProcess);
        processedString = Util.removeExcludeCharsFromString(m_dictionry.getExcludeChars(), processedString);
        decypered = Util.checkIfAllProcessedStringInDictionry(processedString, m_dictionry.getWords());
        if(decypered) {
            m_decipheredQueue.add(new CandidateStringWithEncryptionInfo(processedString,Thread.currentThread().getId(),secret));
            m_OldCandidates++;
            m_possibleDeciphered++;
        }
        synchronized (m_decipheredQueue){
            m_count--;
        }
    }

    public SecretWithCount getThreadsJob(){
        SecretWithCount res = new SecretWithCount(m_currentSecret,m_jobsLeft);
        return res;
    }

    @Override
    public String toString() {
        return Long.toString(this.getId());
    }

    public int getNumOfLeftMissions() {
        return m_jobsLeft;
    }

    public int numOfOptionalResults() {
        return m_possibleDeciphered;
    }

    public int getOldCandidates() {
        return m_OldCandidates;
    }

    public long getId() {
        return m_id;
    }

    public void setId(long m_id) {
        this.m_id = m_id;
    }

    public SecretBuilder getSecretBuilder() {
        return m_machineWrapper.createSecret();
    }
}
