package DataTypes.Util;

import DataTypes.GeneratedMachineDataTypes.*;
import DataTypes.GeneratedMachineDataTypes.SerializeableMachine.Enigma;

import java.util.List;

public class SerializableToXMLEnigmaConverter {

    public static DataTypes.GeneratedMachineDataTypes.Enigma CreateEnigmaFromSerializeable(Enigma serializeableEnigma) {
        DataTypes.GeneratedMachineDataTypes.Enigma resMachine = new DataTypes.GeneratedMachineDataTypes.Enigma();

        Decipher decipher = CreateDecipherFromSerializeable(serializeableEnigma.getDecipher());
        resMachine.setDecipher(decipher);

        Machine machine = CreateMachineFromSerializeable(serializeableEnigma.getMachine());
        resMachine.setMachine(machine);

        return resMachine;
    }

    private static Decipher CreateDecipherFromSerializeable(DataTypes.GeneratedMachineDataTypes.SerializeableMachine.Decipher decipher) {
        Decipher resDecipher = new Decipher();

        resDecipher.setAgents(decipher.getAgents());

        Dictionary dictionary = CreateDictionaryFromSerializeable(decipher.getDictionary());
        resDecipher.setDictionary(dictionary);

        return resDecipher;
    }

    private static Dictionary CreateDictionaryFromSerializeable(DataTypes.GeneratedMachineDataTypes.SerializeableMachine.Dictionary dictionary) {
        Dictionary resDictionary = new Dictionary();

        resDictionary.setWords(dictionary.getWords());
        resDictionary.setExcludeChars(dictionary.getExcludeChars());

        return resDictionary;
    }

    private static Machine CreateMachineFromSerializeable(DataTypes.GeneratedMachineDataTypes.SerializeableMachine.Machine machine) {
        Machine resMachine = new Machine();

        resMachine.setABC(machine.getABC());

        Reflectors reflectors = CreateReflectorsFromSerializeable(machine.getReflectors());
        resMachine.setReflectors(reflectors);

        Rotors rotors = CreateRotorsFromSerializeable(machine.getRotors());
        resMachine.setRotors(rotors);

        resMachine.setRotorsCount(machine.getRotorsCount());

        return resMachine;
    }

    private static Rotors CreateRotorsFromSerializeable(DataTypes.GeneratedMachineDataTypes.SerializeableMachine.Rotors rotors) {
        Rotors resRotors = new Rotors();

        List<Rotor> rotorList = resRotors.getRotor();
        for(DataTypes.GeneratedMachineDataTypes.SerializeableMachine.Rotor rotor : rotors.getRotor()){
            rotorList.add(CreateRotorFromSerializable(rotor));
        }

        return resRotors;
    }

    private static Rotor CreateRotorFromSerializable(DataTypes.GeneratedMachineDataTypes.SerializeableMachine.Rotor rotor) {
        Rotor resRotor = new Rotor();

        resRotor.setId(rotor.getId());

        resRotor.setNotch(rotor.getNotch());

        List<Mapping> mappingList = resRotor.getMapping();
        for(DataTypes.GeneratedMachineDataTypes.SerializeableMachine.Mapping mapping : rotor.getMapping()){
            mappingList.add(CreateMappingFromSerializable(mapping));
        }
        return resRotor;
    }

    private static Mapping CreateMappingFromSerializable(DataTypes.GeneratedMachineDataTypes.SerializeableMachine.Mapping mapping) {
        Mapping resMapping = new Mapping();

        resMapping.setLeft(mapping.getLeft());
        resMapping.setRight(mapping.getRight());

        return resMapping;
    }

    private static Reflectors CreateReflectorsFromSerializeable(DataTypes.GeneratedMachineDataTypes.SerializeableMachine.Reflectors reflectors) {
        Reflectors resReflectors = new Reflectors();

        List<DataTypes.GeneratedMachineDataTypes.Reflector> reflectorsList = resReflectors.getReflector();
        for(DataTypes.GeneratedMachineDataTypes.SerializeableMachine.Reflector reflector : reflectors.getReflector()){
            reflectorsList.add(CreateReflectorFromSerializeable(reflector));
        }

        return resReflectors;
    }

    private static DataTypes.GeneratedMachineDataTypes.Reflector CreateReflectorFromSerializeable(DataTypes.GeneratedMachineDataTypes.SerializeableMachine.Reflector reflector) {
        DataTypes.GeneratedMachineDataTypes.Reflector resReflector = new DataTypes.GeneratedMachineDataTypes.Reflector();

        resReflector.setId(reflector.getId());

        List<Reflect> reflectList = resReflector.getReflect();
        for(DataTypes.GeneratedMachineDataTypes.SerializeableMachine.Reflect reflect : reflector.getReflect())
        {
            reflectList.add(CreateReflectFromSerializeable(reflect));
        }

        return resReflector;
    }

    private static Reflect CreateReflectFromSerializeable(DataTypes.GeneratedMachineDataTypes.SerializeableMachine.Reflect reflect) {
        Reflect resReflect = new Reflect();
        resReflect.setInput(reflect.getInput());
        resReflect.setOutput(reflect.getOutput());

        return  resReflect;
    }


}
