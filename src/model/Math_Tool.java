/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.List;
import java.util.regex.Pattern;

/**
 *
 * @author Wei Wang
 */
public class Math_Tool {

//    public static void main(String[] args) {
//        
//
//        System.out.println("184574".matches((".*[a-z].*")));
//
//    }
    
    public static float avg(List<Float> list){
        if(list == null || list.size() == 0){
            return -1;
        }
        float sum = 0;
        for(float i : list){
            sum += i;
        }
        return sum / list.size();
    }
    
    
    public static float findMedian(List<Float> list) {
        if (list == null || list.size() == 0) {
            return -1.0f;
        }

        if (list.size() % 2 == 1) {
            return partition(list, 0, list.size() - 1, list.size() / 2);
        } else {
            return (partition(list, 0, list.size() - 1, (list.size() - 1) / 2)
                    + partition(list, 0, list.size() - 1, list.size() / 2)) / 2;
        }

    }

    private static float partition(List<Float> list, int s, int e, int k) {
        if (s >= e) {
            return list.get(k);
        }
        int l = s, r = e;
        float pivot = list.get((l + r) / 2);

        while (l <= r) {
            while (l <= r && list.get(l) < pivot) {
                l++;
            }
            while (l <= r && list.get(r) > pivot) {
                r--;
            }
            if (l <= r) {
                float tmp = list.get(l);
                list.set(l, list.get(r));
                list.set(r, tmp);
                l++;
                r--;
            }

        }
        if (k <= r) {
            return partition(list, s, r, k);
        }
        if (k >= l) {
            return partition(list, l, e, k);
        }
        return list.get(k);
    }
}
