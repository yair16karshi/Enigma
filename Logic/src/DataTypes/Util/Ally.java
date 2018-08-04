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
    private int m_port;
    private boolean isReady;

    public Ally(String name){
        this.name = name;
        isReady = false;
        dm = new Manager();
        m_port = dm.getPort();
    }

    public int GetPort(){return m_port;}
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setDM(String i_unprocessedString, Secret i_secret, Integer i_difficultySelection, Integer i_missionSizeSelection, Integer i_numOfAgentsSelection){
        dm.set(i_unprocessedString, i_secret, i_difficultySelection, i_missionSizeSelection, i_numOfAgentsSelection);
    }

    public void startCompetition() {
        //TODO
        dm.run();
    }

    public int getNumOfAgents() {
        return dm.getNumOfAgents();
    }
}
