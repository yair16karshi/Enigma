package DataTypes;

import java.util.List;

public class EncryptionStatus {
    private String m_duration;
    private Integer m_percentage;
    private List<CandidateStringWithEncryptionInfo> m_candidateStringsList;

    public EncryptionStatus(String duration, List<CandidateStringWithEncryptionInfo> candidateList,int percentage){
        m_duration = duration;
        m_candidateStringsList = candidateList;
        m_percentage = percentage;
    }

    public String getDuration() {
        return m_duration;
    }

    public List<CandidateStringWithEncryptionInfo> getCandidateStringsList() {
        return m_candidateStringsList;
    }

    public Integer getPercentage() {
        return m_percentage;
    }
}
