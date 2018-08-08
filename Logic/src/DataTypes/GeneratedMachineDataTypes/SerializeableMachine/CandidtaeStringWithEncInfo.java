package DataTypes.GeneratedMachineDataTypes.SerializeableMachine;

import pukteam.enigma.component.machine.api.Secret;

import java.io.Serializable;

/**
 * Created by yairk on Aug, 2018
 */
public class CandidtaeStringWithEncInfo implements Serializable {
    private long id;
    private String m_string;
    private long m_threadId;
    private DataTypes.GeneratedMachineDataTypes.SerializeableMachine.Secret m_secret;
    private int m_leftMissions;
    private int m_candidates;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getM_string() {
        return m_string;
    }

    public void setM_string(String m_string) {
        this.m_string = m_string;
    }

    public long getM_threadId() {
        return m_threadId;
    }

    public void setM_threadId(long m_threadId) {
        this.m_threadId = m_threadId;
    }

    public DataTypes.GeneratedMachineDataTypes.SerializeableMachine.Secret getM_secret() {
        return m_secret;
    }

    public void setM_secret(DataTypes.GeneratedMachineDataTypes.SerializeableMachine.Secret m_secret) {
        this.m_secret = m_secret;
    }

    public int getM_leftMissions() {
        return m_leftMissions;
    }

    public void setM_leftMissions(int m_leftMissions) {
        this.m_leftMissions = m_leftMissions;
    }

    public int getM_candidates() {
        return m_candidates;
    }

    public void setM_candidates(int m_candidates) {
        this.m_candidates = m_candidates;
    }
}
