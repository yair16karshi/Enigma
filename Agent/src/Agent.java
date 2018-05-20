import java.util.concurrent.BlockingQueue;

import DataTypes.GeneratedMachineDataTypes.Machine;
import pukteam.enigma.component.machine.api.Secret;
import pukteam.enigma.component.machine.builder.EnigmaMachineBuilder;
import pukteam.enigma.factory.EnigmaComponentFactory;

public class Agent implements Runnable{
    private int m_missionSize;
    private BlockingQueue<Secret> m_missionsQueue;
    private BlockingQueue<String> m_decipheredQueue;
    private String m_stringToProcess;
    private EnigmaMachineWrapper m_machineWrapper;

    public Agent(BlockingQueue<Secret> missionsQueue,
                 BlockingQueue<String> decipheredQueue,
                 int missionSize,
                 String stringToProcess,
                 Machine xmlMachine){
        xmlMachine.getABC().charAt(xmlMachine.getABC().length());//TODO:: check if length or length-1
        m_missionsQueue = missionsQueue;
        m_decipheredQueue = decipheredQueue;
        m_missionSize = missionSize;
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
