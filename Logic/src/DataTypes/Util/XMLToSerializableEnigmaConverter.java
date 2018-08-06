package DataTypes.Util;

import DataTypes.GeneratedMachineDataTypes.Reflectors;
import DataTypes.GeneratedMachineDataTypes.SerializeableMachine.*;

import java.util.List;

public class XMLToSerializableEnigmaConverter {

    public static Enigma CreateEnigmaFromXML(DataTypes.GeneratedMachineDataTypes.Enigma xmlEnigma) {
        Enigma resEnigma = new Enigma();


        Decipher decipher = CreateDecipherFromXML(xmlEnigma.getDecipher());
        resEnigma.setDecipher(decipher);

        Machine machine = CreateMachineFromXML(xmlEnigma.getMachine());
        resEnigma.setMachine(machine);

        return resEnigma;
    }

    private static Decipher CreateDecipherFromXML(DataTypes.GeneratedMachineDataTypes.Decipher decipher) {
        Decipher resDecipher = new Decipher();

        resDecipher.setAgents(decipher.getAgents());

        Dictionary dictionary = CreateDictionaryFromXML(decipher.getDictionary());
        resDecipher.setDictionary(dictionary);

        return resDecipher;
    }

    private static Dictionary CreateDictionaryFromXML(DataTypes.GeneratedMachineDataTypes.Dictionary dictionary) {
        Dictionary resDictionary = new Dictionary();

        resDictionary.setWords(dictionary.getWords());
        resDictionary.setExcludeChars(dictionary.getExcludeChars());

        return resDictionary;
    }

    private static Machine CreateMachineFromXML(DataTypes.GeneratedMachineDataTypes.Machine machine) {
        Machine resMachine = new Machine();

        resMachine.setABC(machine.getABC());

        DataTypes.GeneratedMachineDataTypes.SerializeableMachine.Reflectors reflectors = CreateReflectorsFromXML(machine.getReflectors());
        resMachine.setReflectors(reflectors);

        Rotors rotors = CreateRotorsFromXML(machine.getRotors());
        resMachine.setRotors(rotors);

        resMachine.setRotorsCount(machine.getRotorsCount());

        return resMachine;
    }

    private static Rotors CreateRotorsFromXML(DataTypes.GeneratedMachineDataTypes.Rotors rotors) {
        Rotors resRotors = new Rotors();

        List<Rotor> rotorList = resRotors.getRotor();
        for(DataTypes.GeneratedMachineDataTypes.Rotor rotor : rotors.getRotor()){
            rotorList.add(CreateRotorFromXML(rotor));
        }

        return resRotors;
    }

    private static Rotor CreateRotorFromXML(DataTypes.GeneratedMachineDataTypes.Rotor rotor) {
        Rotor resRotor = new Rotor();

        resRotor.setId(rotor.getId());

        resRotor.setNotch(rotor.getNotch());

        List<Mapping> mappingList = resRotor.getMapping();
        for(DataTypes.GeneratedMachineDataTypes.Mapping mapping : rotor.getMapping()){
            mappingList.add(CreateMappingFromXML(mapping));
        }
        return resRotor;
    }

    private static Mapping CreateMappingFromXML(DataTypes.GeneratedMachineDataTypes.Mapping mapping) {
        Mapping resMapping = new Mapping();

        resMapping.setLeft(mapping.getLeft());
        resMapping.setRight(mapping.getRight());

        return resMapping;
    }

    private static DataTypes.GeneratedMachineDataTypes.SerializeableMachine.Reflectors CreateReflectorsFromXML(Reflectors reflectors) {
        DataTypes.GeneratedMachineDataTypes.SerializeableMachine.Reflectors resReflectors = new DataTypes.GeneratedMachineDataTypes.SerializeableMachine.Reflectors();

        List<Reflector> reflectorsList = resReflectors.getReflector();
        for(DataTypes.GeneratedMachineDataTypes.Reflector reflector : reflectors.getReflector()){
            reflectorsList.add(CreateReflectorFromXML(reflector));
        }

        return resReflectors;
    }

    private static Reflector CreateReflectorFromXML(DataTypes.GeneratedMachineDataTypes.Reflector reflector) {
        Reflector resReflector = new Reflector();

        resReflector.setId(reflector.getId());

        List<Reflect> reflectList = resReflector.getReflect();
        for(DataTypes.GeneratedMachineDataTypes.Reflect reflect : reflector.getReflect())
        {
            reflectList.add(CreateReflectFromXML(reflect));
        }

        return resReflector;
    }

    private static Reflect CreateReflectFromXML(DataTypes.GeneratedMachineDataTypes.Reflect reflect) {
        Reflect resReflect = new Reflect();
        resReflect.setInput(reflect.getInput());
        resReflect.setOutput(reflect.getOutput());

        return  resReflect;
    }


}
