
package DataTypes.GeneratedMachineDataTypes.SerializeableMachine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Reflectors implements Serializable {
    protected List<Reflector> reflector;

    public List<Reflector> getReflector() {
        if (reflector == null) {
            reflector = new ArrayList<Reflector>();
        }
        return this.reflector;
    }

}
