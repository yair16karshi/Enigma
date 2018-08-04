package DataTypes;

import Consumer.Agent;

import java.util.List;

public class FinishStatus {
    private long m_totalTimeInSeconds;
    private int m_totalNumOfMissions;
    private List<Agent> m_agentIds;
    private List<CandidateStringWithEncryptionInfo> m_allCandidatesForEncryption;

    public FinishStatus(long m_totalTimeInSeconds, int m_totalNumOfMissions, List<Agent> m_agentIds, List<CandidateStringWithEncryptionInfo> m_allCandidatesForEncryption) {
        this.m_totalTimeInSeconds = m_totalTimeInSeconds;
        this.m_totalNumOfMissions = m_totalNumOfMissions;
        this.m_agentIds = m_agentIds;
        this.m_allCandidatesForEncryption = m_allCandidatesForEncryption;
    }

    @Override
    public String toString() {
        long absSeconds = Math.abs(m_totalTimeInSeconds);
        String time = String.format(
                "%02d:%02d",
                (absSeconds % 3600) / 60,
                absSeconds % 60);

        return "FinishingStatus:\n"+
                "Total Time is: " + time +'\n'+
                "Total Number of missions: " + m_totalNumOfMissions+'\n'+
                "Agent id's are: " + m_agentIds + '\n'+
                "Canditates for encryption are: \n" + m_allCandidatesForEncryption;
    }
}
