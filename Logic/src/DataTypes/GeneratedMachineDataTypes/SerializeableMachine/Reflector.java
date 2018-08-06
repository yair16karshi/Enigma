
package DataTypes.GeneratedMachineDataTypes.SerializeableMachine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Reflector implements Serializable {

    protected List<Reflect> reflect;
    protected String id;


    public List<Reflect> getReflect() {
        if (reflect == null) {
            reflect = new ArrayList<Reflect>();
        }
        return this.reflect;
    }

    public String getId() {
        return id;
    }

    public void setId(String value) {
        this.id = value;
    }

}
