/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ApproachImplementation;
import Misc.*;
import java.util.HashMap;

/**
 *
 * @author Siddhant Kulkarni
 */
public interface IApproachInterface {
    public String[][] getActualAndPredicted();//Every approach must return this array to represent the classification result
    public String getFeaturesToWrite();//If an approach is being executed and you want to write something to a file with the same name as the approach name return it or return empty String
    public HashMap trainApproach(Individual[] trainingDataSet); // store the learning model in the form of a <key,value> pair. Size of this Hashmap will be used 
    public Accuracy testDataSetUsingApproach(HashMap hm,Individual[] testingDataSet);//return accuracy object for system to handle representation
    public String getName();
}

