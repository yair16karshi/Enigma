
package DataTypes.GeneratedMachineDataTypes.SerializeableMachine;

import java.io.Serializable;

public class Decipher implements Serializable {

    protected Dictionary dictionary;
    protected int agents;

    public Dictionary getDictionary() {
        return dictionary;
    }

    public void setDictionary(Dictionary value) {
        this.dictionary = value;
    }

    public int getAgents() {
        return agents;
    }

    public void setAgents(int value) {
        this.agents = value;
    }

}
