package DataTypes.Util;

import DataTypes.CandidateStringWithEncryptionInfo;
import DataTypes.GeneratedMachineDataTypes.SerializeableMachine.CandidtaeStringWithEncInfo;
import DataTypes.GeneratedMachineDataTypes.SerializeableMachine.Secret;
import pukteam.enigma.component.machine.secret.SecretBuilder;

/**
 * Created by yairk on Aug, 2018
 */
public class CandidateWithEncInfoConverter {
    public static CandidateStringWithEncryptionInfo SerielizableToAviad(CandidtaeStringWithEncInfo input, SecretBuilder secretBuilder){
        CandidateStringWithEncryptionInfo res = new CandidateStringWithEncryptionInfo();
        res.setId(input.getId());
        res.setString(input.getM_string());
        res.setM_threadId(input.getM_threadId());

        secretBuilder.selectReflector(input.getM_secret().getSelectedReflector());

        for(int i=0; i<input.getM_secret().getSelectedRotorsInOrder().size(); i++){
            secretBuilder.selectRotor(
                    input.getM_secret().getSelectedRotorsInOrder().get(i),
                    input.getM_secret().getSelectedRotorsPositions().get(i));
        }
        pukteam.enigma.component.machine.api.Secret resSecret = secretBuilder.create();

        res.setSecret(resSecret);
        res.setLeftMissions(input.getM_leftMissions());
        res.setCandidates(input.getM_candidates());

        return res;
    }

    public static CandidtaeStringWithEncInfo AviadToSerielizable(CandidateStringWithEncryptionInfo input, SecretBuilder secretBuilder){
        CandidtaeStringWithEncInfo res = new CandidtaeStringWithEncInfo();
        res.setId(input.getId());
        res.setM_string(input.getString());
        res.setM_threadId(input.getThreadId());

        Secret resSecret = new Secret(
                input.getSecret().getSelectedRotorsInOrder(),
                input.getSecret().getSelectedRotorsPositions(),
                input.getSecret().getSelectedReflector());

        res.setM_secret(resSecret);
        res.setM_leftMissions(input.getLeftMissions());
        res.setM_candidates(input.getCandidates());

        return res;
    }
}
