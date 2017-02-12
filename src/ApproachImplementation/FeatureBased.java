/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ApproachImplementation;

import Misc.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author siddh
 */
public class FeatureBased implements IApproachInterface {

    public String toWrite = "";

    public double[] getMeanOfPointLocations(Individual ind) {
        double[] means = new double[ind.frames.get(0).keySet().toArray().length * 3];
        for (int i = 0; i < means.length; i++) {
            means[i] = 0.0;
        }

        for (int i = 0; i < ind.frames.size(); i++) {
            Object[] points = ind.frames.get(i).keySet().toArray();
            for (int j = 0; j < points.length; j++) {
                //System.out.println(""+points[j]);
                String[] vals = (ind.frames.get(i).get(points[j]) + "").split(";");
                means[(j * 3)] += Double.parseDouble(vals[0]);
                means[(j * 3) + 1] += Double.parseDouble(vals[1]);
                means[(j * 3) + 2] += Double.parseDouble(vals[2]);
            }
        }

        for (int i = 0; i < means.length; i++) {
            means[i] = (double) (means[i] / ind.frames.size());
            //System.out.println(""+means[i]);
        }
        return means;
    }

    public double[] getStdDevPointLocations(Individual ind) {
        double[] means = getMeanOfPointLocations(ind);
        double[] stddev = new double[ind.frames.get(0).keySet().toArray().length * 3];
        for (int i = 0; i < stddev.length; i++) {
            stddev[i] = 0.0;
        }

        for (int i = 0; i < ind.frames.size(); i++) {
            Object[] points = ind.frames.get(i).keySet().toArray();
            for (int j = 0; j < points.length; j++) {
                //System.out.println(""+points[j]);
                String[] vals = (ind.frames.get(i).get(points[j]) + "").split(";");
                stddev[(j * 3)] = (means[(j * 3)] - Double.parseDouble(vals[0])) * (means[(j * 3)] - Double.parseDouble(vals[0]));
                stddev[(j * 3) + 1] = (means[(j * 3) + 1] - Double.parseDouble(vals[1])) * (means[(j * 3) + 1] - Double.parseDouble(vals[1]));
                stddev[(j * 3) + 2] = (means[(j * 3) + 2] - Double.parseDouble(vals[2])) * (means[(j * 3) + 2] - Double.parseDouble(vals[2]));
            }
        }

        for (int i = 0; i < means.length; i++) {
            stddev[i] = Math.sqrt((double) (stddev[i] / ind.frames.size()));
            //System.out.println(""+stddev[i]);
        }
        return stddev;
    }

    public double getHorizontalRange(Individual ind) {
        double hormin = Double.MAX_VALUE, hormax = Double.MIN_VALUE;
        for (int i = 0; i < ind.frames.size(); i++) {
            Object[] datapoints = ind.frames.get(i).keySet().toArray();
            for (int j = 0; j < datapoints.length; j++) {
                double tempX = Double.parseDouble(("" + ind.frames.get(i).get(datapoints[j])).split(";")[0]);
                if (tempX < hormin) {
                    hormin = tempX;
                }
                if (tempX > hormax) {
                    hormax = tempX;
                }
            }
        }
        return hormax - hormin;
    }

    public double getVerticalRange(Individual ind) {
        double vermin = Double.MAX_VALUE, vermax = Double.MIN_VALUE;
        for (int i = 0; i < ind.frames.size(); i++) {
            Object[] datapoints = ind.frames.get(i).keySet().toArray();
            for (int j = 0; j < datapoints.length; j++) {
                double tempX = Double.parseDouble(("" + ind.frames.get(i).get(datapoints[j])).split(";")[1]);
                if (tempX < vermin) {
                    vermin = tempX;
                }
                if (tempX > vermax) {
                    vermax = tempX;
                }
            }
        }
        return vermax - vermin;
    }

    public ArrayList<Double> getFeatureForIndividual(Individual ind) throws FileNotFoundException {
        toWrite+=ind.name+",";
        ArrayList<Double> features = new ArrayList<Double>();
        double tempFeature = 0;
        if (("" + ExperimentalConfiguration.extraParameters.get("IsHorizontalRangeSelected")).equals("TRUE")) {
            tempFeature=getHorizontalRange(ind);
            features.add(tempFeature);
            toWrite+=tempFeature+",";
        }
        if (("" + ExperimentalConfiguration.extraParameters.get("IsVerticalRangeSelected")).equals("TRUE")) {
            tempFeature=getVerticalRange(ind);
            features.add(tempFeature);
            toWrite+=tempFeature+",";
        }
        if (("" + ExperimentalConfiguration.extraParameters.get("IsMeanSelected")).equals("TRUE")) {
            double[] meanvals = getMeanOfPointLocations(ind);
            for (int j = 0; j < meanvals.length; j++) {
                features.add(meanvals[j]);
                toWrite+=meanvals[j]+",";
            }
        }
        if (("" + ExperimentalConfiguration.extraParameters.get("IsStdDevSelected")).equals("TRUE")) {
            double[] stddevvals = getStdDevPointLocations(ind);
            for (int j = 0; j < stddevvals.length; j++) {
                features.add(stddevvals[j]);
                toWrite+=stddevvals[j]+",";
            }

        }
        toWrite+="\r\n";
        return features;
    }

    @Override
    public HashMap trainApproach(Individual[] trainingDataSet) {
        toWrite = buildFirstLine();
        HashMap featureVectors = new HashMap();
        for (int i = 0; i < trainingDataSet.length; i++) {
            try {
                featureVectors.put(trainingDataSet[i].name, getFeatureForIndividual(trainingDataSet[i]));
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        }
        return featureVectors;
    }

    public double getManhattanDistance(ArrayList<Double> f1, ArrayList<Double> f2) {

        double diff = 0.0;
        for (int i = 0; i < f1.size(); i++) {
            diff += Math.abs(f1.get(i) - f2.get(i));
        }
        return diff;
    }

    public double getEuclidDistance(ArrayList<Double> f1, ArrayList<Double> f2) {
        double diff = 0.0;
        for (int i = 0; i < f1.size(); i++) {
            diff += ((f1.get(i) - f2.get(i)) * (f1.get(i) - f2.get(i)));
        }
        return Math.sqrt(diff);
    }

    public String[][] results;

    @Override
    public Accuracy testDataSetUsingApproach(HashMap hm, Individual[] testingDataSet) {
        results = new String[testingDataSet.length][2];

        System.out.println("Tested using Feature Based");
        HashMap distances = new HashMap();
        for (int i = 0; i < testingDataSet.length; i++) {
            ArrayList<Double> features=null;
            try {
                features = getFeatureForIndividual(testingDataSet[i]);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            Object[] trainedInds = hm.keySet().toArray();
            double mindist = Double.MAX_VALUE;
            String predicted = null;
            for (int j = 0; j < trainedInds.length; j++) {
                if (("" + ExperimentalConfiguration.extraParameters.get("IsEDSelectedForFeatureBased")).equals("TRUE")) {
                    double tempdist = getEuclidDistance(features, (ArrayList<Double>) hm.get(trainedInds[j]));
                    if (tempdist < mindist) {
                        mindist = tempdist;
                        predicted = trainedInds[j] + "";
                    }
                } else {
                    double tempdist = getManhattanDistance(features, (ArrayList<Double>) hm.get(trainedInds[j]));
                    if (tempdist < mindist) {
                        mindist = tempdist;
                        predicted = trainedInds[j] + "";
                    }
                }

            }
            results[i][0] = testingDataSet[i].name;
            results[i][1] = predicted;

        }
        for (int i = 0; i < results.length; i++) {
            System.out.println("---------" + results[i][0] + "-----" + results[i][1]);
        }
        return new Accuracy(results);
    }

    @Override
    public String getName() {
        return "FeatureBased";
    }

    @Override
    public String[][] getActualAndPredicted() {
        return results;
    }

    private String buildFirstLine() {
        String line = "Individual,";

        if (("" + ExperimentalConfiguration.extraParameters.get("IsHorizontalRangeSelected")).equals("TRUE")) {
            line += "HorizontalRange,";
        }
        if (("" + ExperimentalConfiguration.extraParameters.get("IsVerticalRangeSelected")).equals("TRUE")) {
            line += "VerticalRange,";
        }
        if (("" + ExperimentalConfiguration.extraParameters.get("IsMeanSelected")).equals("TRUE")) {
            String[] selSeries = (ExperimentalConfiguration.extraParameters.get("SeriesToConsider") + "").split("-");
            for (int i = 0; i < selSeries.length; i++) {
                line += "MeanOfDataPoint" + selSeries[i] + "(X),";
                line += "MeanOfDataPoint" + selSeries[i] + "(Y),";
                line += "MeanOfDataPoint" + selSeries[i] + "(Z),";
            }
        }
        if (("" + ExperimentalConfiguration.extraParameters.get("IsStdDevSelected")).equals("TRUE")) {
            String[] selSeries = (ExperimentalConfiguration.extraParameters.get("SeriesToConsider") + "").split("-");
            for (int i = 0; i < selSeries.length; i++) {
                line += "StdDevOfDataPoint" + selSeries[i] + "(X),";
                line += "StdDevOfDataPoint" + selSeries[i] + "(Y),";
                line += "StdDevOfDataPoint" + selSeries[i] + "(Z),";
            }
        }
        line+="\r\n";
        return line;
    }

    @Override
    public String getFeaturesToWrite() {
        return toWrite;
    }
}