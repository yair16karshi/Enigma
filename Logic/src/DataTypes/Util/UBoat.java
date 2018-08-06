package DataTypes.Util;

import DataTypes.GeneratedMachineDataTypes.Decipher;
import DataTypes.GeneratedMachineDataTypes.Dictionary;
import DataTypes.GeneratedMachineDataTypes.Enigma;
import machine.EnigmaMachineApplication;
import machine.EnigmaMachineWrapper;
import pukteam.enigma.component.machine.api.Secret;
import pukteam.enigma.component.machine.builder.EnigmaMachineBuilder;
import pukteam.enigma.factory.EnigmaComponentFactory;

public class UBoat {
    EnigmaMachineWrapper    m_machine;
    Decipher                m_decipher;
    String                  m_userName;
    String                  m_msgBeforeEnc;
    String                  m_encryptedMsg;
    boolean                 m_isReady;

    public UBoat(){
    }

    public String getEncryptedMsg() {
        return m_encryptedMsg;
    }

    public void setEncryptedMsg(String m_encryptedMsg) {
        this.m_encryptedMsg = m_encryptedMsg;
    }

    public String getMsgBeforeEnc() {
        return m_msgBeforeEnc;
    }

    public void setMsgBeforeEnc(String m_msgBeforeEnc) {
        this.m_msgBeforeEnc = m_msgBeforeEnc;
    }

    public boolean isReady() {
        return m_isReady;
    }

    public void setReady(boolean m_isReady) {
        this.m_isReady = m_isReady;
    }

    public Decipher getDecipher() {
        return m_decipher;
    }

    public void setDecipher(Decipher i_decipher) {
        Decipher decipher = new Decipher();
        Dictionary dictionary = new Dictionary();
        dictionary.setWords(i_decipher.getDictionary().getWords().trim().toUpperCase());
        dictionary.setExcludeChars(i_decipher.getDictionary().getExcludeChars().trim().toUpperCase());
        decipher.setDictionary(dictionary);
        decipher.setAgents(i_decipher.getAgents());
        this.m_decipher = decipher;
    }

    public String getUserName() {
        return m_userName;
    }

    public void setUserName(String userName) {
        this.m_userName = userName;
    }

    public EnigmaMachineWrapper getMachineWrapper() {
        return m_machine;
    }

    public BattlefieldWrapper CreateBattlefield(String wordToEncrypt) {
        BattlefieldWrapper battlefield = new BattlefieldWrapper();
        //todo:: implement

        return battlefield;
    }

    public void Logout() {

    }

    public void EncryptWord(){//maybe ,Secret secretSettings needed as parameter
        //TODO
    }
    private EnigmaMachineWrapper CreateMachine(){
        //TODO
        return null;
    }

    public void createMachineWrapper(Enigma machine) {
        EnigmaMachineBuilder machineBuilder = EnigmaComponentFactory.INSTANCE.buildMachine(machine.getMachine().getRotorsCount(),machine.getMachine().getABC());
        EnigmaMachineApplication.DefineRotors(machineBuilder,machine.getMachine());
        EnigmaMachineApplication.DefineReflectors(machineBuilder,machine.getMachine());
        m_machine = new EnigmaMachineWrapper(machineBuilder.create());
        m_machine.setXMLMachine(machine.getMachine());
    }

    public boolean isCandidateCurrect(String candidate) {
        return candidate.equals(m_encryptedMsg) ? true : false;
    }
}
