package calc;

public class DificultyCalc {

    public static int easy(int rotorsCount, String abc) {
        int res = 1;
        for(int i=0;i<rotorsCount; i++){
            res *= abc.length();
        }
        return res;
    }
}