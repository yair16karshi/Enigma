package DataTypes.Util;

import DataTypes.GeneratedMachineDataTypes.Decipher;
import DataTypes.GeneratedMachineDataTypes.Machine;
import Producer.Manager;
import pukteam.enigma.component.machine.api.Secret;

import java.lang.management.ManagementFactory;
import java.util.Random;
import java.util.Set;

/**
 * Created by yairk on Aug, 2018
 */
public class Ally {
    private String name;
    private Manager dm;
    private int port;
    private boolean isReady;
    private Round currentRound;

    private Ally(String name, Decipher decipher, Machine machine){
        this.name = name;
        isReady = false;
        dm = new Manager(null, decipher, machine);
        port = dm.getPort();
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public void setDM(String i_unprocessedString, Secret i_secret, Integer i_difficultySelection, Integer i_missionSizeSelection, Integer i_numOfAgentsSelection){
        dm.set(i_unprocessedString, i_secret, i_difficultySelection, i_missionSizeSelection, i_numOfAgentsSelection);
    }

}
