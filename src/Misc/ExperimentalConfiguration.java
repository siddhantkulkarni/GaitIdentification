/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Misc;

import java.util.HashMap;

/**
 *
 * @author Siddhant Kulkarni
 */
public class ExperimentalConfiguration {
    public static DatasetHolder dataset;
    public static boolean[] approachesSelected = new boolean[ApproachEnum.values().length];
    //Generic Configuration
     public static String pathTODataset = "";
    //Evaluation Techniques
    public static boolean isBinaryClassification = true;
    public static int numberOfInstancesPerIndividual = 2;
    public static int numberOfIndividualsForTraining = 10;
    public static int numberOfIndividualsForTesting = 2;
    //Feature based
    public static boolean useStrideLength = true;
    public static boolean useGaitCycleTime = true;
    public static boolean useVelocity = true;
    public static boolean useHeight = true;
    public static boolean useStdDeviation = true;
    public static boolean useEuclidFeatureBased = true;
    //Pattern based
    public static int sizeOfWindow = 20;
    public static int minItemSetSize = 4;
    public static int x = 10, y = 10, z = 10;
    //Similarity based
    public static boolean useEuclidSimilarityBased=true;
    public static boolean useFirstFrames=true;
    //More
    public static String exportFilePath="";
    public static HashMap extraParameters=new HashMap();
    
    public static void applyHashMap(HashMap hm){
        Object[] keys=hm.keySet().toArray();
        for(Object key:keys){
            switch((String)key){
                case "Datasetpath":
                    pathTODataset=(String)hm.get(key);
                    break;
                case "TrainingIndividuals":
                    numberOfIndividualsForTraining=Integer.parseInt((String)hm.get(key));
                    break;
                case "TestingIndividuals":
                    numberOfIndividualsForTesting=Integer.parseInt((String)hm.get(key));
                    break;
                case "Instances":
                    numberOfInstancesPerIndividual=Integer.parseInt((String)hm.get(key));
                    break;
                case "IsStrideLengthSelected":
                    useStrideLength=Boolean.parseBoolean((String)hm.get(key));
                    break;
                case "IsGaitCycleTimeSelected":
                    useGaitCycleTime=Boolean.parseBoolean((String)hm.get(key));
                    break;
                case "IsVelocitySelected":
                    useVelocity=Boolean.parseBoolean((String)hm.get(key));
                    break;
                case "IsHeightSelected":
                    useHeight=Boolean.parseBoolean((String)hm.get(key));
                    break;
                case "IsStdDevSelected":
                    useStdDeviation=Boolean.parseBoolean((String)hm.get(key));
                    break;
                case "IsEDSelectedForFeatureBased":
                    useEuclidFeatureBased=Boolean.parseBoolean((String)hm.get(key));
                    break;
                
                case "IsEDSelectedForTimeSeries":
                    useEuclidSimilarityBased=Boolean.parseBoolean((String)hm.get(key));
                    break;
                case "IsFirstNSelectedForTimeSeries":
                    useFirstFrames=Boolean.parseBoolean((String)hm.get(key));
                    break;
                case "SlidingWinSize":
                    sizeOfWindow=Integer.parseInt((String)hm.get(key));
                    break;
                case "ItemSetSize":
                    minItemSetSize=Integer.parseInt((String)hm.get(key));
                    break;
                case "X":
                    x=Integer.parseInt((String)hm.get(key));
                    break;
                case "Y":
                    y=Integer.parseInt((String)hm.get(key));
                    break;
                case "Z":
                    z=Integer.parseInt((String)hm.get(key));
                    break;
                    
                
                        
                default:
                    break;
                    
            }
                    
        }
    }
}
