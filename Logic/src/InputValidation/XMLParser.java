package InputValidation;

import DataTypes.GeneratedMachineDataTypes.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class XMLParser {
    public Enigma machine;

//    public static void main(String[] args) throws Exception {
//        XMLParser parser = new XMLParser();
//        parser.XMLIsValid("hello");
//    }

    public boolean XMLIsValid(String fileContent) throws Exception {
        //path = "C:\\WS\\Enigma\\Logic\\src\\Resources\\master.xml";
        String[] answer = new String[1];

//        if (!Files.exists(xmlPath)) {
//            throw new Exception("File does not exist");
//        }
//
//        if (!getFileExtension(xmlPath.toString()).equals("xml")) {
//            throw new Exception("The file given is not in XML format");
//        }
        machine = fromXmlFileToObject(fileContent);

        if(!Util.isAlphabetEven(machine.getMachine().getABC())){
            throw new Exception("The ABC is not even");
        }

        if(!Util.isRotorsCountValid(machine.getMachine())){
            throw new Exception("Rotor-count is bigger then number of existing rotors");
        }

        if(machine.getMachine().getRotorsCount() < 2){
            throw new Exception("Rotor-count is smaller then 2");
        }

        if(!(Util.checkRotorsId(machine.getMachine().getRotors(), answer))){
            throw new Exception(answer[0]);
        }

        if(Util.haveDoubleMappings(machine.getMachine().getRotors(), answer)){
            throw new Exception(answer[0]);
        }

        if(!(Util.rotorsNotchInRange(machine.getMachine().getRotors(), machine.getMachine().getABC().length(), answer))){
            throw new Exception(answer[0]);
        }

        if(!(Util.checkReflectorsId(machine.getMachine().getReflectors(), answer))){
            throw new Exception(answer[0]);
        }

        if(!(Util.checkReflectorsMapping(machine.getMachine().getReflectors(), answer))){
            throw new Exception(answer[0]);
        }

        if(!(Util.isAgentCountValid(machine.getDecipher().getAgents(), answer))){
            throw new Exception(answer[0]);
        }

        return true;
    }

    private static Enigma fromXmlFileToObject(String fileContent) {
        try {

            StringReader stringReader = new StringReader(fileContent);
            JAXBContext jaxbContext = JAXBContext.newInstance(Enigma.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Enigma res = (Enigma) jaxbUnmarshaller.unmarshal(stringReader);
            res.getMachine().setABC(res.getMachine().getABC().trim());
            return res;

        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getFileExtension(String fileName) {
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }

    private int getNumOfRotors(){
        return machine.getMachine().getRotorsCount();

    }
}
