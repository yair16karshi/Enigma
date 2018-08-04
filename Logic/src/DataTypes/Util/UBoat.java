package DataTypes.Util;

import DataTypes.GeneratedMachineDataTypes.Enigma;
import machine.EnigmaMachineApplication;
import machine.EnigmaMachineWrapper;
import pukteam.enigma.component.machine.api.Secret;
import pukteam.enigma.component.machine.builder.EnigmaMachineBuilder;
import pukteam.enigma.factory.EnigmaComponentFactory;

public class UBoat {

    EnigmaMachineWrapper    m_machine;
    String                  m_userName;

    public String getUserName() {
        return m_userName;
    }

    public void setUserName(String userName) {
        this.m_userName = userName;
    }

    public UBoat(){
    }

    public BattlefieldWrapper CreateBattlefield(String wordToEncrypt) {
        BattlefieldWrapper battlefield = new BattlefieldWrapper();
        //todo:: implement

        return battlefield;
    }

    public void Logout() {

    }

    public String EncryptWord(String word){//maybe ,Secret secretSettings needed as parameter
        //TODO
        return word;
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
}
