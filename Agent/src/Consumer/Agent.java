package Consumer;

import java.util.concurrent.BlockingQueue;

import DataTypes.GeneratedMachineDataTypes.Decipher;
import DataTypes.GeneratedMachineDataTypes.Machine;
import DataTypes.SecretWithMissionSize;
import pukteam.enigma.component.machine.api.Secret;
import pukteam.enigma.component.machine.builder.EnigmaMachineBuilder;
import pukteam.enigma.factory.EnigmaComponentFactory;

public class Agent implements Runnable{
    private BlockingQueue<SecretWithMissionSize> m_missionsQueue;
    private BlockingQueue<String> m_decipheredQueue;
    private String m_stringToProcess;
    private EnigmaMachineWrapper m_machineWrapper;

    public Agent(BlockingQueue<SecretWithMissionSize> missionsQueue,
                 BlockingQueue<String> decipheredQueue,
                 String stringToProcess,
                 Machine xmlMachine,
                 Decipher decipher){
        xmlMachine.getABC().charAt(xmlMachine.getABC().length());//TODO:: check if length or length-1
        m_missionsQueue = missionsQueue;
        m_decipheredQueue = decipheredQueue;
        m_stringToProcess = stringToProcess;

        EnigmaMachineBuilder machineBuilder = EnigmaComponentFactory.INSTANCE.buildMachine(xmlMachine.getRotorsCount(),xmlMachine.getABC());
        EnigmaMachineApplication.DefineRotors(machineBuilder,xmlMachine);
        EnigmaMachineApplication.DefineReflectors(machineBuilder,xmlMachine);
        m_machineWrapper = new EnigmaMachineWrapper(machineBuilder.create());
    }

    @Override
    public void run() {

    }
    private void PerformSingleMission(){

    }
}
