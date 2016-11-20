/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Misc;

/**
 *
 * @author siddh
 */
public class Accuracy{
   public int accurate=0,total=0;
    public Accuracy(String [][] results){ 
        //this hashmap will have 2 columns Actual Name of individual, identified by your approach
        total=results.length;
        for(int i=0;i<results.length;i++){
            if(results[i][0].equals(results[i][1]))
                accurate++;
        }
        
    }
}
