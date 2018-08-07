package Producer;

import pukteam.enigma.component.machine.api.Secret;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by yairk on Aug, 2018
 */
public class AgentResults {
    private long id;
    private int m_leftMissions;
    private int m_candidates;

    public AgentResults(){
    }


    public int getLeftMissions() {
        return m_leftMissions;
    }

    public void setLeftMissions(int m_leftMissions) {
        this.m_leftMissions = m_leftMissions;
    }

    public int getCandidates() {
        return m_candidates;
    }

    public void setCandidates(int m_candidates) {
        this.m_candidates = m_candidates;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
