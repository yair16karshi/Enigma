package DataTypes;

import java.util.List;

public class EncryptionStatus {
    private String m_duration;
    private float m_percentage;
    private List<CandidateStringWithEncryptionInfo> m_candidateStringsList;
    private List<SecretWithCount> m_currentThreadsJobs;

    public EncryptionStatus(String duration, List<CandidateStringWithEncryptionInfo> candidateList,float percentage,List<SecretWithCount> currentThreadsJobs){
        m_duration = duration;
        m_candidateStringsList = candidateList;
        m_percentage = percentage;
        this.m_currentThreadsJobs = currentThreadsJobs;
    }

    public String getDuration() {
        return m_duration;
    }

    public List<CandidateStringWithEncryptionInfo> getCandidateStringsList() {
        return m_candidateStringsList;
    }

    public float getPercentage() {
        return m_percentage;
    }

    public List<SecretWithCount> getCurrentThreadsJobs() {
        return m_currentThreadsJobs;
    }
}
