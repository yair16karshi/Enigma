package DataTypes.Util;

import DataTypes.GeneratedMachineDataTypes.Battlefield;
import DataTypes.GeneratedMachineDataTypes.Machine;
import machine.EnigmaMachineWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yairk on Aug, 2018
 */
public class Competition {
    private UBoat uBoat;
    private List<Ally> alies;
    private boolean isActive;
    private boolean competitionFinish;
    private EnigmaMachineWrapper machine;
    private BattlefieldWrapper battlefield;
    private String compName;

    private List<String> candidatesList = new ArrayList<>();
    private String m_encryptedWord;

    public UBoat getuBoat() {
        return uBoat;
    }

    public void setuBoat(UBoat uBoat) {
        this.uBoat = uBoat;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }
    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<Ally> getAlies() {
        return alies;
    }

    public Competition(UBoat uBoat) {
        this.uBoat = uBoat;
    }

    public void addAlly(Ally ally) {
        synchronized (alies){
            alies.add(ally);
        }
    }


    public List<String> getCandidatesList() {
        return candidatesList;
    }

    public void setCandidatesList(List<String> candidatesList) {
        this.candidatesList = candidatesList;
    }

    public BattlefieldWrapper getBattlefield() {
        return battlefield;
    }

    public void setBattlefield(BattlefieldWrapper battlefield) {
        this.battlefield = battlefield;
    }

    public void SetEncryptedWord(String encryptedWord) {
        m_encryptedWord = encryptedWord;
    }
}
