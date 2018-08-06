
package DataTypes.GeneratedMachineDataTypes.SerializeableMachine;

import java.io.Serializable;

public class Machine implements Serializable {

    protected String abc;
    protected Rotors rotors;
    protected Reflectors reflectors;
    protected int rotorsCount;

    public String getABC() {
        return abc;
    }

    public void setABC(String value) {
        this.abc = value;
    }

    public Rotors getRotors() {
        return rotors;
    }

    public void setRotors(Rotors value) {
        this.rotors = value;
    }


    public Reflectors getReflectors() {
        return reflectors;
    }

    public void setReflectors(Reflectors value) {
        this.reflectors = value;
    }


    public int getRotorsCount() {
        return rotorsCount;
    }

    public void setRotorsCount(int value) {
        this.rotorsCount = value;
    }

}
