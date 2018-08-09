package Consumer;

import DataTypes.CandidateStringWithEncryptionInfo;
import DataTypes.GeneratedMachineDataTypes.SerializeableMachine.CandidtaeStringWithEncInfo;
import DataTypes.GeneratedMachineDataTypes.SerializeableMachine.Enigma;
import DataTypes.SecretWithMissionSize;
import DataTypes.Util.CandidateWithEncInfoConverter;
import DataTypes.Util.SecretWithMissionSizeConverter;
import DataTypes.Util.SerializableToXMLEnigmaConverter;
import machine.EnigmaMachineApplication;
import machine.EnigmaMachineWrapper;
import pukteam.enigma.component.machine.builder.EnigmaMachineBuilder;
import pukteam.enigma.factory.EnigmaComponentFactory;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AgentAdapter {
    private static final String LOGOUT = "LOGOUT";
    private static final String READY = "READY";
    private static final String DONE = "DONE";
    private static final String END_OF_SESSION = "END_OF_SESSION";

    private String host;
    private int port;
    private BlockingQueue<CandidateStringWithEncryptionInfo> responseQueue;
    private Agent agent;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean logOut = false;

    public AgentAdapter(String host, int port) {
        this.host = host;
        this.port = port;
        responseQueue = new LinkedBlockingQueue<>();
    }

    public void run() {
        try{
            socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            out.flush();

            Enigma serializeableEnigma = (Enigma)in.readObject();
            DataTypes.GeneratedMachineDataTypes.Enigma xmlEnigma = SerializableToXMLEnigmaConverter.CreateEnigmaFromSerializeable(serializeableEnigma);

            //out.writeBytes(READY);

            while (!logOut) {
                // The queue arrive full from the DM
                List<DataTypes.GeneratedMachineDataTypes.SerializeableMachine.SecretWithMissionSize> missionQueue =
                        (List<DataTypes.GeneratedMachineDataTypes.SerializeableMachine.SecretWithMissionSize>) in.readObject();
                String encryptedString = (String)in.readObject(); // Or readObject
                EnigmaMachineWrapper machineWrapper = creatMachineWrapper(xmlEnigma);
                BlockingQueue<SecretWithMissionSize> queueToAgent =  fromListToBlockingQueue(missionQueue, machineWrapper);
                agent =
                        new Agent(machineWrapper,
                                missionQueue.size(),
                                queueToAgent,
                                responseQueue,
                                encryptedString,
                                xmlEnigma.getMachine(),
                                xmlEnigma.getDecipher());
                Thread thread = new Thread(agent);
                thread.setName("agent");
                thread.setPriority(Thread.MAX_PRIORITY);
                thread.start();
                sendResponsesToSocket();
            }

            //in.close();
            //socket.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private EnigmaMachineWrapper creatMachineWrapper(DataTypes.GeneratedMachineDataTypes.Enigma xmlEnigma) {
        EnigmaMachineBuilder machineBuilder = EnigmaComponentFactory.INSTANCE.buildMachine(xmlEnigma.getMachine().getRotorsCount(),xmlEnigma.getMachine().getABC());
        EnigmaMachineApplication.DefineRotors(machineBuilder,xmlEnigma.getMachine());
        EnigmaMachineApplication.DefineReflectors(machineBuilder,xmlEnigma.getMachine());
        return new EnigmaMachineWrapper(machineBuilder.create());
    }

    private void sendResponsesToSocket() {
        try{
            while(true){
                if(!responseQueue.isEmpty()){
                    CandidateStringWithEncryptionInfo candidate = responseQueue.poll();
                    if (candidate != null){
                        if(candidate.getString().equals(DONE)){
                            //the agent finish to process all the queue
                            CandidtaeStringWithEncInfo doneCandidate = new CandidtaeStringWithEncInfo();
                            doneCandidate.setM_string(candidate.getString());
                            out.writeObject(doneCandidate);
                            break;
                        }
                        else{
                            candidate.setId(agent.getId());
                            candidate.setLeftMissions(agent.getNumOfLeftMissions());
                            candidate.setCandidates(agent.getOldCandidates());
                            out.writeObject(CandidateWithEncInfoConverter.AviadToSerielizable(candidate, agent.getSecretBuilder()));
                        }
                    }
                }
                if(in.available() > 0){
                    String msgFromDm = in.readUTF();
                    //some alie win the round
                    if(msgFromDm.equals(END_OF_SESSION)){
                        break;
                    }
                    //alie log out
                    else if(msgFromDm.equals(LOGOUT)){
                        logOut = true;
                        break;
                    }
                }
            }
        } catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    private BlockingQueue<SecretWithMissionSize> fromListToBlockingQueue(List<DataTypes.GeneratedMachineDataTypes.SerializeableMachine.SecretWithMissionSize> missionQueue, EnigmaMachineWrapper machineWrapper) {
        BlockingQueue<SecretWithMissionSize> res = new LinkedBlockingQueue<>();
        for(DataTypes.GeneratedMachineDataTypes.SerializeableMachine.SecretWithMissionSize swms: missionQueue){
            res.add(SecretWithMissionSizeConverter.SerializableToAviad(swms,machineWrapper.createSecret()));
        }

        return res;
    }
}
