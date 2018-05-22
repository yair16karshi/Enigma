package InputValidation;

import DataTypes.GeneratedMachineDataTypes.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Util {

    public static boolean isAlphabetEven(String abc) {
        return abc.length()%2 == 0 ? true : false;
    }
    

    public static boolean isRotorsCountValid(Machine machine) {
        return machine .getRotorsCount() <= machine.getRotors().getRotor().size() ? true : false;
    }

    public static boolean haveDoubleMappings(Rotors rotors, String[] answer) {
        List<String> to = new ArrayList<>();
        List<String> from = new ArrayList<>();
        for(Rotor rotor: rotors.getRotor()){
            for(Mapping map: rotor.getMapping()){
                if(to.contains(map.getTo()) || from.contains(map.getFrom())){
                    answer[0] = "Rotor id"+rotor.getId()+"have double mapping";
                    return true;
                }
                to.add(map.getTo());
                from.add(map.getFrom());
            }
            to.clear();
            from.clear();
        }

        return false;
    }

    public static boolean checkRotorsId(Rotors rotors, String[] answer) {
        List<Integer> rotorsIds = new ArrayList<>();
        for(Rotor rotor : rotors.getRotor()){
            if(rotorsIds.contains(rotor.getId())){
                answer[0] = "One or more of the rotors id's is nor uniq";
                return false;
            }
            rotorsIds.add(rotor.getId());
        }
        for(int i=1; i<rotorsIds.size()+1; i++){
            if(!rotorsIds.contains(i)){
                answer[0] = "The rotors id's is not run numbering from 1 to the number of rotors";
                return false;
            }
        }

        return true;
    }

    public static boolean rotorsNotchInRange(Rotors rotors, int ABCsize, String[] answer) {
        for(Rotor rotor: rotors.getRotor()){
            if(!(rotor.getNotch()>= 0 && rotor.getNotch()<= ABCsize)){
                answer[0] = "Rotor id: "+ rotor.getId()+" notch is not in range";
                return false;
            }
        }

        return true;
    }

    public static boolean checkReflectorsId(Reflectors reflectors, String[] answer) {
        List<Integer> idList = new ArrayList<>();
        int id;
        for(Reflector reflector: reflectors.getReflector()){
            id = romanToInt(reflector.getId().toUpperCase());
            if(id == -1){
                answer[0] = "Reflector id: "+reflector.getId()+" is not in range";
                return false;
            }
            if(idList.contains(id)){
                answer[0] = "one or more of the reflectors id's is nor uniq";
                return false;
            }
            idList.add(id);
        }
        for(int i=1; i<idList.size()+1; i++){
            if(!idList.contains(i)){
                answer[0] = "The reflectors id's is not run numbering from 1 to the number of reflectors";
                return false;
            }
        }
        return true;
    }

    public static boolean checkReflectorsMapping(Reflectors reflectors, String[] answer) {
        for(Reflector reflector: reflectors.getReflector()){
            for(Reflect reflect: reflector.getReflect()){
                if(reflect.getInput() == reflect.getOutput()){
                    answer[0] = "Reflector id: "+reflector.getId()+" map sign to her self";
                    return false;
                }
            }
        }

        return true;
    }

    public static int romanToInt(String str){
        if(str.equals("I"))
            return 1;
        if(str.equals("II"))
            return 2;
        if(str.equals("III"))
            return 3;
        if(str.equals("IV"))
            return 4;
        if(str.equals("V"))
            return 5;
        return -1;
    }

    public static String fromIntToRoman(int reflectorLetter) {
        switch (reflectorLetter) {
            case 1:
                return "I";
            case 2:
                return "II";
            case 3:
                return "III";
            case 4:
                return "IV";
            case 5:
                return "V";
        }
        return null;
    }

    public static boolean isValidRomanLetter(String reflectorInput) {
        switch (reflectorInput) {
            case "I":
                return true;
            case "II":
                return true;
            case "III":
                return true;
            case "IV":
                return true;
            case "V":
                return true;
        }
        return false;
    }

    public static boolean isRoman(String reflectorInput){
        return reflectorInput.matches("^M{0,4}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$");
    }

    public static boolean isAgentCountValid(int agents, String[] answer) {
        if(agents >= 2 && agents <=50)
            return true;
        answer[0] = "The number of agents must be between 2 to 50";
        return false;
    }

    public static Dictionary removeDoubleWordsAndExcludeChars(Dictionary dictionary) {
        Dictionary res = new Dictionary();
        String str;
        for(String word: dictionary.getWords().split(" ")){
            for (Character excludeChar: dictionary.getExcludeChars().toCharArray()){
                word = word.replace(excludeChar.toString(), "");
            }
        }
        //remove double values
        dictionary.setWords(Arrays.stream(dictionary.getWords().split(" ")).distinct().collect(Collectors.joining(" ")));
        res.setWords(dictionary.getWords());
        res.setExcludeChars(dictionary.getExcludeChars());

        return res;
    }
}
