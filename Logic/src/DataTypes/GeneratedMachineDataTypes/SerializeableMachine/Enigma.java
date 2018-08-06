
package DataTypes.GeneratedMachineDataTypes.SerializeableMachine;

import DataTypes.GeneratedMachineDataTypes.Battlefield;

import java.io.Serializable;

public class Enigma implements Serializable {

    private Machine machine;
    private Decipher decipher;

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine value) {
        this.machine = value;
    }

    public Decipher getDecipher() {
        return decipher;
    }


    public void setDecipher(Decipher value) {
        this.decipher = value;
    }

}
