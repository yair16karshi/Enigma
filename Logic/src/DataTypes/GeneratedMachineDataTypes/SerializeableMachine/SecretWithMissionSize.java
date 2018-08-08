package DataTypes.GeneratedMachineDataTypes.SerializeableMachine;

import java.io.Serializable;

public class SecretWithMissionSize implements Serializable {
    private int m_missionSize;
    private Secret m_secret;

    public SecretWithMissionSize() {
    }

    public SecretWithMissionSize(int m_missionSize, Secret m_secret) {
        this.m_missionSize = m_missionSize;
        this.m_secret = m_secret;
    }

    public int getMissionSize() {
        return m_missionSize;
    }

    public void setMissionSize(int m_missionSize) {
        this.m_missionSize = m_missionSize;
    }

    public Secret getSecret() {
        return m_secret;
    }

    public void setSecret(Secret m_secret) {
        this.m_secret = m_secret;
    }
}
