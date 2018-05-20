package calc;

import pukteam.enigma.component.machine.api.Secret;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SecretCalc {
    public static Secret addPositions(Integer[] count, Secret m_secret, Integer i_missionSizeSelection, Integer rotorsCount, String i_abc) {
        List<Integer> positions = m_secret.getSelectedRotorsPositions();
        Integer pos;

        for(int i=0; i<i_missionSizeSelection; i++){
            pos = positions.get(positions.size());
            pos++;
            pos = Integer.parseInt(Integer.toString(pos, i_abc.length()));

        }
    }

    public static Secret resetRotorsPositions(Secret m_secret, int rotorsCount) {
        List<Integer> newPosition = new ArrayList<>(Collections.nCopies(rotorsCount, 0));
        m_secret.setInitialPosition(newPosition);
        return m_secret;
    }
}
