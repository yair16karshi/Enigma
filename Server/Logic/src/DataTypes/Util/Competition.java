package DataTypes.Util;

import DataTypes.GeneratedMachineDataTypes.Battlefield;
import DataTypes.GeneratedMachineDataTypes.Machine;
import machine.EnigmaMachineWrapper;

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
    private Battlefield battlefield;
    private List<Round> rounds;
    private Round currentRound;


    public Competition(UBoat uBoat) {
        this.uBoat = uBoat;
    }

    public void addAlly(Ally ally) {
        synchronized (alies){
            alies.add(ally);
        }
    }

    public void addWinner(String winnerName) {
        currentRound.addWinner(winnerName);
    }


}
