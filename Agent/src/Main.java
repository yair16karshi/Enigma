import Consumer.Agent;
import Consumer.AgentAdapter;
import DataTypes.GeneratedMachineDataTypes.Decipher;
import DataTypes.GeneratedMachineDataTypes.Machine;

import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class Main{
    public static void main(String[] args){
        AgentAdapter agentAdapter;
        agentAdapter = initAgentAdapter(args);
        agentAdapter.run();
    }

    private static AgentAdapter initAgentAdapter(String[] args) {
//        String[] input = args[0].split(":");
//        String host = input[0];
//        int port = Integer.parseInt(input[1]);
//        return new AgentAdapter(host, port);

        return new AgentAdapter("localhost", 58012);
    }
}
