
package DataTypes.GeneratedMachineDataTypes.SerializeableMachine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Rotors implements Serializable {

    protected List<Rotor> rotor;

    public List<Rotor> getRotor() {
        if (rotor == null) {
            rotor = new ArrayList<Rotor>();
        }
        return this.rotor;
    }

}
