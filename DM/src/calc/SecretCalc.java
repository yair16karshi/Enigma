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
            pos = positions.get(positions.size()-1);
            pos++;
            pos = (pos%(i_abc.length()+1));
            if(pos == 0){
                pos++;
            }
            positions.set(positions.size()-1, pos);
            if(pos == 1){
                for(int j=positions.size()-2; j>=0; j--){
                    pos = positions.get(j);
                    pos++;
                    pos = (pos%(i_abc.length()+1));
                    if(pos == 0){
                        pos++;
                    }
                    positions.set(j, pos);
                    if(pos != 0)
                        break;
                }
            }
        }
        m_secret = m_secret.setInitialPosition(positions);

        return m_secret;
    }

    public static Secret resetRotorsPositions(Secret m_secret, int rotorsCount) {
        List<Integer> newPosition = new ArrayList<>();
        for(int i=0; i<rotorsCount; i++){
            newPosition.add(1);
        }
        m_secret = m_secret.setInitialPosition(newPosition);
        return m_secret;
    }
}