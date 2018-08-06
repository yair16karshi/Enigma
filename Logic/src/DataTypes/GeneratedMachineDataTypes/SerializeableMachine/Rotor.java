
package DataTypes.GeneratedMachineDataTypes.SerializeableMachine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Rotor implements Serializable {

    protected List<Mapping> mapping;
    protected int notch;
    protected int id;

    public List<Mapping> getMapping() {
        if (mapping == null) {
            mapping = new ArrayList<Mapping>();
        }
        return this.mapping;
    }

    public int getNotch() {
        return notch;
    }

    public void setNotch(int value) {
        this.notch = value;
    }

    public int getId() {
        return id;
    }


    public void setId(int value) {
        this.id = value;
    }

}
