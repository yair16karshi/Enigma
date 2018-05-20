package calc;

import pukteam.enigma.component.machine.api.Secret;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SecretCalc {
    public static Secret addPositions(Integer iMissionSizeSelection, Integer i_missionSizeSelection, String abc) {

    }

    public static Secret resetRotorsPositions(Secret m_secret, int rotorsCount) {
        List<Integer> newPosition = new ArrayList<>(Collections.nCopies(rotorsCount, 0));
        m_secret.setInitialPosition(newPosition);
        return m_secret;
    }

    public static Secret AdvanceSecret(Secret secret, String abc) {
        for(Integer i : secret.getSelectedRotorsPositions()){

    }
    }
}
