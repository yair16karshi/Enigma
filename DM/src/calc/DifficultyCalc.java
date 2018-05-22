package calc;

import java.util.*;

public class DifficultyCalc {

    public static int easy(int rotorsCount, String abc) {
        int res = 1;
        for(int i=0;i<rotorsCount; i++){
            res *= abc.length();
        }
        return res;
    }

    public static void getAllCombinationsOfList(Set<Integer> items, Stack<Integer> permutation, int size, Set<Integer[]> res) {

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
            getAllCombinationsOfList(items, permutation, size, res);

        /* pop and put the removed item back */
            items.add(permutation.pop());
        }
    }

    private static void combinationUtil(List<Integer> arr, int[] data, int start, int end, int index, int r, Set<List<Integer>> res)
    {
        if (index == r)
        {
            List<Integer> combination = new ArrayList<>();
            for (int j=0; j<r; j++)
                combination.add(data[j]);
            res.add(combination);
            return;
        }

        for (int i=start; i<=end && end-i+1 >= r-index; i++)
        {
            data[index] = arr.get(i);
            combinationUtil(arr, data, i+1, end, index+1, r, res);
        }
    }

    public static void allCombinationsWithSizeN(List<Integer> arr, int n, int r, Set<List<Integer>> res)
    {
        int data[]=new int[r];
        combinationUtil(arr, data, 0, n-1, 0, r, res);
    }

    public static int factorial(int rotorsCount) {
        int res=1;
        for(int i=1; i<=rotorsCount; i++){
            res*=i;
        }
        return res;
    }

    public static float getPrecentageWorkDone(int total, int howManyDone){
        return (howManyDone*100)/total;
    }

    public static List<Set<Integer>> getSubsets(List<Integer> superSet, int k) {
        List<Set<Integer>> res = new ArrayList<>();
        getSubsets(superSet, k, 0, new HashSet<Integer>(), res);
        return res;
    }

    private static void getSubsets(List<Integer> superSet, int k, int idx, Set<Integer> current,List<Set<Integer>> solution) {
        //successful stop clause
        if (current.size() == k) {
            solution.add(new HashSet<>(current));
            return;
        }
        //unseccessful stop clause
        if (idx == superSet.size()) return;
        Integer x = superSet.get(idx);
        current.add(x);
        //"guess" x is in the subset
        getSubsets(superSet, k, idx+1, current, solution);
        current.remove(x);
        //"guess" x is not in the subset
        getSubsets(superSet, k, idx+1, current, solution);
    }
}
