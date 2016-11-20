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
 * @author siddh
 */
public class DummyApproach2 implements IApproachInterface {
 
    @Override
    public HashMap trainApproach(Individual[] trainingDataSet) {
        return new HashMap();
    }

    @Override
    public Accuracy testDataSetUsingApproach(HashMap hm, Individual[] testingDataSet) {
        String[][] temp={{"A","A"},{"B","C"},{"C","C"},{"D","D"},{"E","E"}};
        return new Accuracy(temp);
    }
   
    @Override
    public String getName() {
        return "DummyApproach2";
    }
}
