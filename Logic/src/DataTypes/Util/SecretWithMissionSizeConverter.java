package DataTypes.Util;

import DataTypes.GeneratedMachineDataTypes.SerializeableMachine.Secret;
import DataTypes.GeneratedMachineDataTypes.SerializeableMachine.SecretWithMissionSize;
import pukteam.enigma.component.machine.secret.SecretBuilder;
import pukteam.enigma.component.machine.secret.SecretImpl;

public class SecretWithMissionSizeConverter{
    public static SecretWithMissionSize AviadToSerializable(DataTypes.SecretWithMissionSize secretWithMissionSize){
        Secret resSecret = new Secret(
                secretWithMissionSize.getSecret().getSelectedRotorsInOrder(),
                secretWithMissionSize.getSecret().getSelectedRotorsPositions(),
                secretWithMissionSize.getSecret().getSelectedReflector());
        SecretWithMissionSize resSecretWithMissionSize = new SecretWithMissionSize();
        resSecretWithMissionSize.setMissionSize(secretWithMissionSize.getMissionSize());
        resSecretWithMissionSize.setSecret(resSecret);
        return resSecretWithMissionSize;
    }
    public static DataTypes.SecretWithMissionSize SerializableToAviad(SecretWithMissionSize secretWithMissionSize,SecretBuilder secretBuilder){
        DataTypes.SecretWithMissionSize resSecretWithMissionSize = new DataTypes.SecretWithMissionSize();

        secretBuilder.selectReflector(secretWithMissionSize.getSecret().getSelectedReflector());

        for(int i=0; i<secretWithMissionSize.getSecret().getSelectedRotorsInOrder().size(); i++){
            secretBuilder.selectRotor(
                    secretWithMissionSize.getSecret().getSelectedRotorsInOrder().get(i),
                    secretWithMissionSize.getSecret().getSelectedRotorsPositions().get(i));
        }
        pukteam.enigma.component.machine.api.Secret resSecret = secretBuilder.create();
        resSecretWithMissionSize.setSecret(resSecret);
        resSecretWithMissionSize.setMissionSize(secretWithMissionSize.getMissionSize());

        return resSecretWithMissionSize;
    }
}
