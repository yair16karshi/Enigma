package DataTypes.GeneratedMachineDataTypes.SerializeableMachine;

import java.io.Serializable;

public class Dictionary implements Serializable {

    protected String words;
    protected String excludeChars;

    public String getWords() {
        return words;
    }

    public void setWords(String value) {
        this.words = value;
    }

    public String getExcludeChars() {
        return excludeChars;
    }

    public void setExcludeChars(String value) {
        this.excludeChars = value;
    }

}
