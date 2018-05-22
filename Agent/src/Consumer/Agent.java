package Consumer;

import java.util.concurrent.BlockingQueue;

import DataTypes.GeneratedMachineDataTypes.Decipher;
import DataTypes.GeneratedMachineDataTypes.Dictionary;
import DataTypes.GeneratedMachineDataTypes.Machine;
import DataTypes.SecretWithMissionSize;
import calc.SecretCalc;
import machine.EnigmaMachineApplication;
import machine.EnigmaMachineWrapper;
import pukteam.enigma.component.machine.api.Secret;
import pukteam.enigma.component.machine.builder.EnigmaMachineBuilder;
import pukteam.enigma.factory.EnigmaComponentFactory;

public class Agent implements Runnable{
    private BlockingQueue<SecretWithMissionSize> m_missionsQueue;
    private BlockingQueue<String> m_decipheredQueue;
    private String m_stringToProcess;
    private EnigmaMachineWrapper m_machineWrapper;
    private Dictionary m_dictionry;

    public Agent(BlockingQueue<SecretWithMissionSize> missionsQueue,
                 BlockingQueue<String> decipheredQueue,
                 String stringToProcess,
                 Machine xmlMachine,
                 Decipher decipher){

        //xmlMachine.getABC().charAt(xmlMachine.getABC().length());//TODO:: check if length or length-1
        m_missionsQueue = missionsQueue;
        m_decipheredQueue = decipheredQueue;
        m_stringToProcess = stringToProcess;
        m_dictionry = decipher.getDictionary();

        EnigmaMachineBuilder machineBuilder = EnigmaComponentFactory.INSTANCE.buildMachine(xmlMachine.getRotorsCount(),xmlMachine.getABC());
        EnigmaMachineApplication.DefineRotors(machineBuilder,xmlMachine);
        EnigmaMachineApplication.DefineReflectors(machineBuilder,xmlMachine);
        m_machineWrapper = new EnigmaMachineWrapper(machineBuilder.create());
    }

    @Override
    public void run() {
        while(true){
            if(!Thread.interrupted()){//TODO:: verify that interrupted works
                try {
                    RunMission(m_missionsQueue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void RunMission(SecretWithMissionSize mission){
        Secret localSecret = mission.getSecret();

        for(int i = 0 ; i < mission.getMissionSize() ; i++){
            PerformSingleString(localSecret);
            localSecret = SecretCalc.addPositions(new Integer[]{},
                    localSecret,
                    1,
                    m_machineWrapper.getXMLMachine().getRotorsCount(),
                    m_machineWrapper.getXMLMachine().getABC());
        }
    }
    private void PerformSingleString(Secret secret){
        String[] processedWords;
        m_machineWrapper.initFromSecret(secret);
        String processedString = m_machineWrapper.process(m_stringToProcess);
        processedWords = processedString.split(" ");
        for(String s : processedWords){
            if(!m_dictionry.getWords().contains(s)){
                return;
            }
        }
        m_decipheredQueue.add(processedString);
    }
}
