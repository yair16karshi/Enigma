package DataTypes.Util;

import DataTypes.GeneratedMachineDataTypes.Battlefield;
import DataTypes.GeneratedMachineDataTypes.Machine;
import DataTypes.GeneratedMachineDataTypes.Reflector;
import InputValidation.Util;
import machine.EnigmaMachineWrapper;
import pukteam.enigma.component.machine.api.Secret;
import pukteam.enigma.component.machine.secret.SecretBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by yairk on Aug, 2018
 */
public class Competition {
    private UBoat uBoat;
    private List<Ally> alies = new ArrayList<>();
    private boolean isActive;
    private boolean competitionFinish;
    private BattlefieldWrapper battlefield;
    private boolean isReadyToRegister;
    private List<String> candidatesList = new ArrayList<>();
    private List<String> winners = new ArrayList<>();

    public UBoat getuBoat() {
        return uBoat;
    }


    public boolean isReadyToRegister() {
        return isReadyToRegister;
    }

    public void setReadyToRegister(boolean readyToRegister) {
        isReadyToRegister = readyToRegister;
    }

    public void setuBoat(UBoat uBoat) {
        this.uBoat = uBoat;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<Ally> getAlies() {
        return alies;
    }

    public Competition(UBoat uBoat) {
        this.uBoat = uBoat;
    }

    public void addAlly(Ally ally) {
        synchronized (alies){
            alies.add(ally);
        }
    }


    public List<String> getCandidatesList() {
        return candidatesList;
    }

    public void setCandidatesList(List<String> candidatesList) {
        this.candidatesList = candidatesList;
    }

    public BattlefieldWrapper getBattlefield() {
        return battlefield;
    }

    public void setBattlefield(BattlefieldWrapper battlefield) {
        this.battlefield = battlefield;
    }

    public ArrayList<Integer> getReflectorsIDs() {
        ArrayList<Integer> res = new ArrayList<>();
        List<Reflector> reflectors = uBoat.getMachineWrapper().getXMLMachine().getReflectors().getReflector();
        for(Reflector reflector: reflectors){
            res.add(Util.romanToInt(reflector.getId()));
        }

        return res;
    }

    public int getRotorsCount() {
        return uBoat.getMachineWrapper().getXMLMachine().getRotorsCount();
    }

    public int getNumOfRotors() {
        return uBoat.getMachineWrapper().getXMLMachine().getRotors().getRotor().size();
    }

    public ArrayList<Character> getABC() {
        ArrayList<Character> res = new ArrayList<>();
        for(char c: uBoat.getMachineWrapper().getXMLMachine().getABC().toCharArray()){
            res.add(c);
        }

        return res;
    }

    public String checkInputAndInitSecret(String rotors, String positions, String reflector) {
        for (int i=0; i<rotors.length(); i++)
            for (int j=i+1; j<rotors.length(); j++)
                if (rotors.charAt(i) == rotors.charAt(j))
                    return "Don't choose same rotor more then once";

        SecretBuilder secretBuilder = uBoat.getMachineWrapper().createSecret();
        secretBuilder.selectReflector(Integer.parseInt(reflector));
        for(int i=0; i<rotors.length(); i++){
            secretBuilder.selectRotor(Integer.parseInt(rotors.substring(i, i+1)), positions.toCharArray()[i]);
        }
        Secret secret = secretBuilder.create();
        uBoat.getMachineWrapper().initFromSecret(secret);
        uBoat.getMachineWrapper().setSecretHasBeenSet();

        return null;
    }

    public boolean checkInDictionary(String stringToProcess) {
        String[] splitMsg = stringToProcess.split(" ");

        return Arrays.stream(splitMsg).allMatch(word -> uBoat.getDecipher().getDictionary().getWords().contains(word));
    }

    public boolean isAllAlliesReady() {
        return (alies.size() == battlefield.getAllies())
                && alies.stream().allMatch(Ally::isReady);
    }

    public void startCompetition() {
        uBoat.EncryptWord();
        alies.forEach(Ally::startCompetition);
    }

    public boolean isCompetitionFull() {
        return battlefield.getAllies() == alies.size() ? true :false;
    }

    public void logOut() {
        isActive = false;
        competitionFinish = true;
        alies.clear();
    }

    public void startNewCompetition() {
        uBoat.setReady(false);
        isActive = false;
        competitionFinish = false;
        candidatesList.clear();
        winners.clear();
    }

    public void addWinner(String allyName) {
        winners.add(allyName);
    }
}
