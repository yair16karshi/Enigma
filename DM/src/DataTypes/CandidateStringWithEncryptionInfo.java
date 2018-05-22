package DataTypes;

import calc.SecretCalc;
import pukteam.enigma.component.machine.api.Secret;

public class CandidateStringWithEncryptionInfo {
    private String m_string;
    private long m_threadId;
    private Secret m_secret;

    public CandidateStringWithEncryptionInfo(String string, long threadID, Secret secret){
        m_string = string;
        m_threadId = threadID;
        m_secret = secret;
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
}
