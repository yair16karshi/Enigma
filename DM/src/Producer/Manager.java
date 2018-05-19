package Producer;

import DataTypes.GeneratedMachineDataTypes.Decipher;
import InputValidation.Util;

public class Manager {
    private Decipher m_decipher;

    public Manager(Decipher decipher){
        m_decipher = new Decipher();
        m_decipher.setDictionary(Util.removeDoubleWordsAndExcludeChars(decipher.getDictionary()));
        m_decipher.setAgents(decipher.getAgents());
    }

    public Decipher getDecipher() {
        return m_decipher;
    }

    public void setDecipher(Decipher m_decipher) {
        this.m_decipher = m_decipher;
    }
}
