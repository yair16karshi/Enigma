package DataTypes;

import calc.SecretCalc;
import pukteam.enigma.component.machine.api.Secret;

import java.util.LinkedList;
import java.util.List;

public class CandidateStringWithEncryptionInfo {
    private long id;
    private String m_string;
    private long m_threadId;
    private Secret m_secret;
    private int m_leftMissions;
    private int m_candidates;

    public CandidateStringWithEncryptionInfo(String string, long threadID, Secret secret){
        m_string = string;
        m_threadId = threadID;
        m_secret = secret;
    }

    @Override
    public String toString() {
        return "candidate: " + m_string +
                " discovered by thread: " + m_threadId +
                " discovered in secret: " + m_secret.toString()+'\n';
    }

    public long getThreadId() {
        return m_threadId;
    }

    public String getString() {
        return m_string;
    }

    public Secret getSecret() {
        return m_secret;
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
