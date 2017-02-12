/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ApproachImplementation;

import Misc.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author siddh
 */
public class TimeSeriesSimilarity implements IApproachInterface {
    public String toWrite="DistanceFrom,DistanceTo,Distance,\r\n";
    @Override
    public HashMap trainApproach(Individual[] trainingDataSet) {
        HashMap dataset = new HashMap();
        for (int i = 0; i < trainingDataSet.length; i++) {
            dataset.put(trainingDataSet[i].name, trainingDataSet[i].frames);
        }
        return dataset;
    }

    
    public double getEuclidBetweenTwoFrames(HashMap f1, HashMap f2){
       Object[] datapoints=f1.keySet().toArray();
       double dist=0;
       for(int i=0;i<datapoints.length;i++){
           String[] arr1=(""+f1.get(datapoints[i])).split(";");
           String[] arr2=(""+f2.get(datapoints[i])).split(";");
           for(int j=0;j<arr1.length;j++){
               dist+=((Double.parseDouble(arr1[j])-Double.parseDouble(arr2[j]))*(Double.parseDouble(arr1[j])-Double.parseDouble(arr2[j])));
           }
       }
       return dist;
    }
    
    
    public double getDTWDist(ArrayList<HashMap> f1, ArrayList<HashMap> f2) {
        double dist = 0;
        int aFrames = f1.size(), bFrames = f2.size();
        //Skipping 4 frames to make DTW work faster (Similar to Coarsening)... there were several options, this one was most accurate and was the fastest
        double[][] distMat = new double[(int)Math.ceil(aFrames/4)+1][(int)Math.ceil(bFrames/4)+1];
        for (int i = 0; i < aFrames; i=i+4) {
            for (int j = 0; j < bFrames; j=j+4) {
                distMat[i/4][j/4]=getEuclidBetweenTwoFrames(f1.get(i),f2.get(j));
            }
        }
        //System.out.println("Matrix Generated");
        int i = 0, j = 0;
        dist = distMat[i][j];
        int x = 1;
        while (i < aFrames/4 - 1 && j < bFrames/4 - 1) {
            //System.err.println("i="+i+" j="+j+" a="+aFrames+" b="+bFrames);
            double next = distMat[i][j + 1], above = distMat[i + 1][j], abovenext = distMat[i + 1][j + 1];
            if (next < above && next < abovenext) {
                dist += distMat[i][j + 1];
                j++;

            } else if (above < next && above < abovenext) {

                dist += distMat[i + 1][j];
                i++;
            } else if (abovenext < next && abovenext < above) {

                dist += distMat[i + 1][j + 1];
                i++;
                j++;
            } else {

                dist += distMat[i + 1][j + 1];
                i++;
                j++;
            }
            x++;
        }
        dist = dist / x;
        
        return dist;
    }
    
    public double getEuclideanDist(ArrayList<HashMap> f1, ArrayList<HashMap> f2) {
        int minNumFrames = 0;
        double dist = 0.0;
        boolean isF1Smaller = false;
        if (f1.size() < f2.size()) {
            minNumFrames = f1.size();
            isF1Smaller = true;
        } else {
            minNumFrames = f2.size();
        }
        if (("" + ExperimentalConfiguration.extraParameters.get("IsFirstNSelectedForTimeSeries")).equals("TRUE")) {
            for (int i = 0; i < minNumFrames; i++) {
                Object[] points = f1.get(i).keySet().toArray();
                for (int j = 0; j < points.length; j++) {
                    String[] locationf1 = ("" + f1.get(i).get(points[j])).split(";");
                    String[] locationf2 = ("" + f2.get(i).get(points[j])).split(";");
                    dist += (Double.parseDouble(locationf1[0]) - Double.parseDouble(locationf2[0])) * (Double.parseDouble(locationf1[0]) - Double.parseDouble(locationf2[0]));
                    dist += (Double.parseDouble(locationf1[1]) - Double.parseDouble(locationf2[1])) * (Double.parseDouble(locationf1[1]) - Double.parseDouble(locationf2[1]));
                    dist += (Double.parseDouble(locationf1[2]) - Double.parseDouble(locationf2[2])) * (Double.parseDouble(locationf1[2]) - Double.parseDouble(locationf2[2]));

                }
            }
        } else {
            int i, j;
            for (i = f1.size() - 1, j = f2.size() - 1; i > f1.size() - minNumFrames && j > f2.size() - minNumFrames; i--, j--) {
                Object[] points = f1.get(i).keySet().toArray();
                for (int k = 0; k < points.length; k++) {
                    String[] locationf1 = ("" + f1.get(i).get(points[k])).split(";");
                    String[] locationf2 = ("" + f2.get(j).get(points[k])).split(";");
                    dist += (Double.parseDouble(locationf1[0]) - Double.parseDouble(locationf2[0])) * (Double.parseDouble(locationf1[0]) - Double.parseDouble(locationf2[0]));
                    dist += (Double.parseDouble(locationf1[1]) - Double.parseDouble(locationf2[1])) * (Double.parseDouble(locationf1[1]) - Double.parseDouble(locationf2[1]));
                    dist += (Double.parseDouble(locationf1[2]) - Double.parseDouble(locationf2[2])) * (Double.parseDouble(locationf1[2]) - Double.parseDouble(locationf2[2]));

                }
            }
        }

        return Math.sqrt(dist);
    }
    public String[][]results;
    @Override
    public Accuracy testDataSetUsingApproach(HashMap hm, Individual[] testingDataSet) {
        results = new String[testingDataSet.length][2];
        for (int i = 0; i < testingDataSet.length; i++) {
            double mindist = Double.MAX_VALUE;
            String predicted = null;
            Object[] trainingNames = hm.keySet().toArray();
            for (int j = 0; j < trainingNames.length; j++) {
                double diff = 0.0;
                if ((ExperimentalConfiguration.extraParameters.get("IsEDSelectedForTimeSeries")).equals("TRUE")) {
                    diff = getEuclideanDist(testingDataSet[i].frames, (ArrayList) hm.get(trainingNames[j]));
                } else {
                    //DTW or DMW
                    diff=getDTWDist(testingDataSet[i].frames, (ArrayList) hm.get(trainingNames[j]));
                }
                toWrite+=testingDataSet[i].name+","+trainingNames[j]+","+diff+"\r\n";
                if(diff<mindist){
                    mindist=diff;
                    predicted=""+trainingNames[j];
                }
            }
            
            results[i][0]=testingDataSet[i].name;
            results[i][1]=predicted;
            
        }
        for(int i=0;i<results.length;i++){
            System.out.println("---------"+results[i][0]+"-----"+results[i][1]);
        }
        return new Accuracy(results);
    }

    @Override
    public String getName() {
        return "TimeSeriesSimilarity";
    }

    @Override
    public String[][] getActualAndPredicted() {
        return results;
    }

    @Override
    public String getFeaturesToWrite() {
        return toWrite;
    }
}
