package Consumer;

import DataTypes.CandidateStringWithEncryptionInfo;
import DataTypes.GeneratedMachineDataTypes.Decipher;
import DataTypes.GeneratedMachineDataTypes.Machine;
import DataTypes.SecretWithMissionSize;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AgentAdapter {
    private static String READY = "READY";
    private static String DONE = "DONE";
    private static String END_OF_SESSION = "END_OF_SESSION";
    private String host;
    private int port;
    private BlockingQueue<CandidateStringWithEncryptionInfo> responseQueue;
    private Agent agent;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

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

            Machine xmlMachine = (Machine) in.readObject();
            Decipher decipher = (Decipher) in.readObject();
            out.writeBytes(READY);

            while (true) {
                // The queue arrive full from the DM
                List<SecretWithMissionSize> missionQueue = (List<SecretWithMissionSize>) in.readObject();
                String encryptedString = (String) in.readUTF(); // Or readObject

                agent =
                        new Agent(
                                missionQueue.size(),
                                fromListToBlockingQueue(missionQueue),
                                responseQueue,
                                encryptedString,
                                xmlMachine,
                                decipher);
                Thread thread = new Thread(agent);
                thread.start();
                sendResponsesToSocket();
            }

            in.close();
            socket.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void sendResponsesToSocket() {
        try{
            while(true){
                if(!responseQueue.isEmpty()){
                    CandidateStringWithEncryptionInfo candidate = responseQueue.poll();
                    if(candidate.getString().equals(DONE) || in.readUTF().equals(END_OF_SESSION)){
                        //the agent finish to process all the queue
                        break;
                    }
                    if (candidate != null) {
                        out.writeObject(candidate);
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
