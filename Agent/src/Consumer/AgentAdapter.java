package Consumer;

import DataTypes.CandidateStringWithEncryptionInfo;
import DataTypes.GeneratedMachineDataTypes.SerializeableMachine.Enigma;
import DataTypes.SecretWithMissionSize;
import DataTypes.Util.SerializableToXMLEnigmaConverter;

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
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());

            Enigma serializeableEnigma = (Enigma)in.readObject();
            DataTypes.GeneratedMachineDataTypes.Enigma xmlEnigma = SerializableToXMLEnigmaConverter.CreateEnigmaFromSerializeable(serializeableEnigma);

            out.writeBytes(READY);

            while (!logOut) {
                // The queue arrive full from the DM
                List<SecretWithMissionSize> missionQueue = (List<SecretWithMissionSize>) in.readObject();
                String encryptedString = in.readUTF(); // Or readObject

                agent =
                        new Agent(
                                missionQueue.size(),
                                fromListToBlockingQueue(missionQueue),
                                responseQueue,
                                encryptedString,
                                xmlEnigma.getMachine(),
                                xmlEnigma.getDecipher());
                Thread thread = new Thread(agent);
                thread.start();
                sendResponsesToSocket();
                if(thread.isAlive()){
                    thread.stop();
                }
            }

            //in.close();
            //socket.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }











    private void sendResponsesToSocket() {
        try{
            while(true){
                if(!responseQueue.isEmpty()){
                    CandidateStringWithEncryptionInfo candidate = responseQueue.poll();
                    if (candidate != null){
                        if(candidate.getString().equals(DONE)){
                            //the agent finish to process all the queue
                            break;
                        }
                        else{
                            candidate.setId(agent.getId());
                            candidate.setLeftMissions(agent.getNumOfLeftMissions());
                            candidate.setCandidates(agent.getOldCandidates());
                            out.writeObject(candidate);
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

        }
    }

    private BlockingQueue<SecretWithMissionSize> fromListToBlockingQueue(List<SecretWithMissionSize> missionQueue) {
        BlockingQueue<SecretWithMissionSize> res = new LinkedBlockingQueue<>();
        for(SecretWithMissionSize swms: missionQueue){
            res.add(swms);
        }

        return res;
    }
}
