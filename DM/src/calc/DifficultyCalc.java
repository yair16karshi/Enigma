package calc;

import java.util.Set;
import java.util.Stack;

public class DifficultyCalc {

    public static int easy(int rotorsCount, String abc) {
        int res = 1;
        for(int i=0;i<rotorsCount; i++){
            res *= abc.length();
        }
        return res;
    }

    public static void getAllCombinations(Set<Integer> items, Stack<Integer> permutation, int size, Set<Integer[]> res) {

    /* permutation stack has become equal to size that we require */
        if(permutation.size() == size) {
        /* print the permutation */
            res.add(permutation.toArray(new Integer[0]));
        }

    /* items available for permutation */
        Integer[] availableItems = items.toArray(new Integer[0]);
        for(Integer i : availableItems) {
        /* add current item */
            permutation.push(i);

        /* remove item from available item set */
            items.remove(i);

        /* pass it on for next permutation */
            getAllCombinations(items, permutation, size, res);

        /* pop and put the removed item back */
            items.add(permutation.pop());
        }
    }
}
