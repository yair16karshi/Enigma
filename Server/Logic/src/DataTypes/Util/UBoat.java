package DataTypes.Util;

import machine.EnigmaMachineWrapper;
import pukteam.enigma.component.machine.api.Secret;

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

    private String EncryptWord(String word,Secret secretSettings){

    }
    private EnigmaMachineWrapper CreateMachine(){

    }
}
