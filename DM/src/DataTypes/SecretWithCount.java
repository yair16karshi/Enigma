package DataTypes;

import pukteam.enigma.component.machine.api.Secret;

import java.util.List;

public class SecretWithCount {
    private Secret m_secret;
    private int m_count;


    public SecretWithCount(Secret m_secret, int m_count) {
        this.m_secret = m_secret;
        this.m_count = m_count;

    }

    public int getCount() {
        return m_count;
    }

    public Secret getSecret() {
        return m_secret;
    }


}
