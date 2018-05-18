//import pukteam.enigma.component.machine.api.EnigmaMachine;
//import pukteam.enigma.component.machine.builder.EnigmaMachineBuilder;
//import pukteam.enigma.component.rotor.Rotor;
//import pukteam.enigma.factory.EnigmaComponentFactory;
//
//public class Main {
//  public static void main(String[] args) {
//      EnigmaMachineBuilder machine1 = EnigmaComponentFactory.INSTANCE.buildMachine(1,"abcdef");
//      machine1.defineRotor(1, "abcdef", "abcdef", 4);
//      machine1.defineReflector(1, new byte[]{1, 2, 3}, new byte[]{4, 5, 6});
//      EnigmaMachine machine = machine1.create();
//      //machine.consumeState(System.out::println);
//      int x = 0;
//    }
//
//}
public class Main{
    public static void main(String[] args){
        UI uiInstance = new UI();
        uiInstance.MenuLoop();
    }
}