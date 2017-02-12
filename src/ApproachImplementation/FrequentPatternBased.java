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
public class FrequentPatternBased implements IApproachInterface {

    public String toWrite = getFirstLine();

    public String getFirstLine() {
        String toReturn = "";
        toReturn = "Individual,FrameNumber,";
        for (int i = 0; i < 8; i++) {
            toReturn += "CubeEndPoint" + i + "(X),";
            toReturn += "CubeEndPoint" + i + "(Y),";
            toReturn += "CubeEndPoint" + i + "(Z),";
        }
        toReturn += "#XDIV,#YDIV,#ZDIV,\r\n";
        return toReturn;
    }

    public HashMap<String, Double> getBlockAttributes(ArrayList<HashMap> ind) {
        HashMap<String, Double> toReturn = new HashMap<String, Double>();
        double minX = Double.MAX_VALUE, maxX = Double.MIN_VALUE, minY = Double.MAX_VALUE, maxY = Double.MIN_VALUE, maxZRange = Double.MIN_VALUE;

        for (int i = 0; i < ind.size(); i++) {
            Object[] datapoints = ind.get(i).keySet().toArray();
            double minZ = Double.MAX_VALUE, maxZ = Double.MIN_VALUE;
            for (int j = 0; j < datapoints.length; j++) {
                double x = Double.parseDouble(("" + ind.get(i).get(datapoints[j])).split(";")[0]);
                double y = Double.parseDouble(("" + ind.get(i).get(datapoints[j])).split(";")[1]);
                double z = Double.parseDouble(("" + ind.get(i).get(datapoints[j])).split(";")[2]);
                if (x < minX) {
                    minX = x;
                }
                if (x > maxX) {
                    maxX = x;
                }
                if (y < minY) {
                    minY = y;
                }
                if (y > maxY) {
                    maxY = y;
                }
                if (z < minZ) {
                    minZ = z;
                }
                if (z > maxZ) {
                    maxZ = z;
                }
            }
            if (maxZ - minZ > maxZRange) {
                maxZRange = maxZ - minZ;
            }
        }
        toReturn.put("MinX", minX);
        toReturn.put("MaxX", maxX);
        toReturn.put("MinY", minY);
        toReturn.put("MaxY", maxY);
        toReturn.put("ZRange", maxZRange);

        return toReturn;
    }

    public double getBlockLocation(String[] refpoint, String[] xyz, HashMap<String, Double> attributes) {
        double refZ = Double.parseDouble(refpoint[2]);
        double targetX = Double.parseDouble(xyz[0]), targetY = Double.parseDouble(xyz[1]), targetZ = Double.parseDouble(xyz[2]);
        double xdiv = Double.parseDouble("" + ExperimentalConfiguration.extraParameters.get("X"));
        double ydiv = Double.parseDouble("" + ExperimentalConfiguration.extraParameters.get("Y"));
        double zdiv = Double.parseDouble("" + ExperimentalConfiguration.extraParameters.get("Z"));
        double xrow, ycol, zrow;
        double zMin = refZ - (double) attributes.get("ZRange");
        double zMax = refZ + (double) attributes.get("ZRange");
        zrow = Math.floor(targetZ / (((zMax - zMin) / zdiv)));
        xrow = Math.floor(targetX / ((((double) attributes.get("MaxX") - (double) attributes.get("MinX")) / xdiv)));
        ycol = Math.floor(targetY / ((((double) attributes.get("MaxY") - (double) attributes.get("MinY")) / ydiv)));
        return ((xdiv * ydiv) * zrow) + (xrow * ycol);
    }

    public String getFrameDetails(String name, ArrayList<HashMap> data, int framenumber, String[] refPoints, HashMap<String, Double> attributes) {
        String temp = name + "," + framenumber + ",";
        double[] refpoint = new double[refPoints.length];
        for (int i = 0; i < refPoints.length; i++) {
            refpoint[i] = Double.parseDouble(refPoints[i]);
        }
        temp += attributes.get("MinX") + "," + attributes.get("MinY") + "," + (refpoint[2] - (double) attributes.get("ZRange")) + ",";
        temp += attributes.get("MaxX") + "," + attributes.get("MinY") + "," + (refpoint[2] - (double) attributes.get("ZRange")) + ",";
        temp += attributes.get("MinX") + "," + attributes.get("MaxY") + "," + (refpoint[2] - (double) attributes.get("ZRange")) + ",";
        temp += attributes.get("MaxX") + "," + attributes.get("MaxY") + "," + (refpoint[2] - (double) attributes.get("ZRange")) + ",";

        temp += attributes.get("MinX") + "," + attributes.get("MinY") + "," + (refpoint[2] + (double) attributes.get("ZRange")) + ",";
        temp += attributes.get("MaxX") + "," + attributes.get("MinY") + "," + (refpoint[2] + (double) attributes.get("ZRange")) + ",";
        temp += attributes.get("MinX") + "," + attributes.get("MaxY") + "," + (refpoint[2] + (double) attributes.get("ZRange")) + ",";
        temp += attributes.get("MaxX") + "," + attributes.get("MaxY") + "," + (refpoint[2] + (double) attributes.get("ZRange")) + ",";

        temp += ExperimentalConfiguration.extraParameters.get("X") + "," + ExperimentalConfiguration.extraParameters.get("Y") + "," + ExperimentalConfiguration.extraParameters.get("Z") + ",\r\n";
        return temp;
    }

    public ArrayList<HashMap> getBlockRepresentationForIndividual(ArrayList<HashMap> ind, String name) {

        ArrayList<HashMap> toReturn = new ArrayList<HashMap>();
        HashMap<String, Double> blockAttributes = getBlockAttributes(ind);

        for (int i = 0; i < ind.size(); i++) {
            HashMap<String, Double> blockLocations = new HashMap<String, Double>();
            Object[] datapoints = ind.get(i).keySet().toArray();
            toWrite += getFrameDetails(name, ind, i, ("" + ind.get(i).get(datapoints[0])).split(";"), blockAttributes);
            for (int j = 0; j < datapoints.length; j++) {
                blockLocations.put(datapoints[j] + "", getBlockLocation(("" + ind.get(i).get(datapoints[0])).split(";"), ("" + ind.get(i).get(datapoints[j])).split(";"), blockAttributes));
            }
            toReturn.add(blockLocations);
        }
        return toReturn;
    }

    public HashMap<String, Integer> applyMinSup(HashMap<String, Integer> incoming) {
        double minSup = Double.parseDouble("" + ExperimentalConfiguration.extraParameters.get("MinSupport"));
        Object[] entries = incoming.keySet().toArray();
        for (int i = 0; i < entries.length; i++) {
            if (incoming.get(entries[i]) < minSup) {
                incoming.remove(entries[i]);
            }
        }
        return incoming;
    }

    public HashMap<String, Integer> getOneItemSet(ArrayList<HashMap> blockRep) {
        HashMap<String, Integer> itemsets = new HashMap<String, Integer>();
        for (int i = 0; i < blockRep.size(); i++) {
            Object[] datapoints = blockRep.get(i).keySet().toArray();
            for (int j = 0; j < datapoints.length; j++) {
                String key = datapoints[j] + ":" + blockRep.get(i).get(datapoints[j]);
                if (itemsets.containsKey(key)) {
                    itemsets.put(key, itemsets.get(key) + 1);
                } else {
                    itemsets.put(key, 1);
                }
            }
        }
        return applyMinSup(itemsets);
    }

    public boolean isMergeable(String pat1, String pat2) {
        String[] pat1arr = pat1.split("=");
        String[] pat2arr = pat2.split("=");
        for (int i = 0; i < pat1arr.length - 1; i++) {
            if (!pat1arr[i].equals(pat2arr[i])) {
                return false;
            }
        }
        return true;
    }

    public HashMap<String, Integer> getNextCandidateSet(HashMap<String, Integer> prevSet, int setSize) {
        HashMap<String, Integer> nextSet = new HashMap<String, Integer>();
        String[] prevPatterns = prevSet.keySet().toArray(new String[0]);
        if (setSize == 2) {
            for (int i = 0; i < prevPatterns.length - 1; i++) {
                for (int j = i + 1; j < prevPatterns.length; j++) {
                    String newpatterntoadd = prevPatterns[i] + "=" + prevPatterns[j];
                    nextSet.put(newpatterntoadd, 0);
                }
            }
        } else {
            for (int i = 0; i < prevPatterns.length - 1; i++) {
                for (int j = i + 1; j < prevPatterns.length; j++) {
                    if (isMergeable(prevPatterns[i], prevPatterns[j])) {
                        nextSet.put(prevPatterns[i] + "=" + prevPatterns[j].split("=")[prevPatterns[j].split("=").length - 1], 0);
                    }
                }
            }
        }
        //System.out.println("---------------"+nextSet.toString());
        return nextSet;
    }

    public boolean windowContainsPattern(String[] patternContributors, ArrayList<HashMap> frames, int startframe, int endframe) {
        for (int i = 0; i < patternContributors.length; i++) {
            boolean flag = false;
            String jointname = patternContributors[i].split(":")[0];
            double blockloc = Double.parseDouble("" + patternContributors[i].split(":")[1]);
            for (int j = startframe; j < endframe; j++) {
                double a = Double.parseDouble("" + frames.get(j).get(jointname));
                if (a == blockloc) {
                    flag = true;
                    break;
                }
            }
            if (flag == false) {
                return false;
            }
        }
        return true;
    }

    public HashMap<String, Integer> performFrequencyCount(HashMap<String, Integer> currentSet, ArrayList<HashMap> blockRepresentation) {
        String[] currentPatterns = currentSet.keySet().toArray(new String[0]);
        double winsize = Double.parseDouble("" + ExperimentalConfiguration.extraParameters.get("SlidingWinSize"));
        for (int i = 0; i < currentPatterns.length; i++) {
            String[] jtArr = currentPatterns[i].split("=");
            for (int j = 0; j < blockRepresentation.size() - winsize; j++) {
                if (windowContainsPattern(jtArr, blockRepresentation, j, j + (int) winsize)) {
                    currentSet.put(currentPatterns[i], currentSet.get(currentPatterns[i]) + 1);
                }
            }
        }
        return applyMinSup(currentSet);
    }

    public HashMap<String, HashMap> getPatterns(Individual incoming) {
        ArrayList<HashMap> individual = incoming.frames;
        ArrayList<HashMap> blockRepresentation = getBlockRepresentationForIndividual(individual, incoming.name);
        HashMap<String, HashMap> allPatterns = new HashMap<String, HashMap>();
        HashMap<String, Integer> currentSet = getOneItemSet(blockRepresentation);
        int patternSizeCounter = 2;
        allPatterns.put("1", currentSet);
        int setsize = currentSet.keySet().toArray().length;
        while (currentSet.keySet().toArray().length > 0) {
            currentSet = getNextCandidateSet(currentSet, patternSizeCounter);
            currentSet = performFrequencyCount(currentSet, blockRepresentation);
            setsize = currentSet.keySet().toArray().length;
            if (currentSet.keySet().toArray().length > 0) {
                allPatterns.put("" + patternSizeCounter, currentSet);
            }
            patternSizeCounter++;
        }
        //System.out.println(""+allPatterns);
        return allPatterns;
    }

    public HashMap<String, HashMap> applyMinItemSetSize(HashMap<String, HashMap> patterns) {
        String[] individuals = patterns.keySet().toArray(new String[0]);
        int minItemSetSize = Integer.parseInt("" + ExperimentalConfiguration.extraParameters.get("ItemSetSize"));
        for (String name : individuals) {
            HashMap hm = patterns.get(name);
            Object[] keys = hm.keySet().toArray();
            for (int i = 0; i < keys.length; i++) {
                if (Integer.parseInt("" + keys[i]) < minItemSetSize) {
                    hm.remove(keys[i]);
                }
            }
            patterns.put(name, hm);
        }
        return patterns;
    }

    public HashMap removeNonUnique(HashMap<String, HashMap> patterns) {
        String[] individuals = patterns.keySet().toArray(new String[0]);
        for (int i = 0; i < individuals.length; i++) {
            HashMap itemsets = patterns.get(individuals[i]);
            Object[] targetItemsetIDs = itemsets.keySet().toArray();
            for (int itemSizeCnt = 0; itemSizeCnt < targetItemsetIDs.length; itemSizeCnt++) {
                HashMap<String, Integer> targetPatterns = (HashMap<String, Integer>) itemsets.get(targetItemsetIDs[itemSizeCnt]);
                for (int j = 0; j < individuals.length; j++) {
                    if (j == i) {
                        continue;
                    }
                    HashMap comparewith = patterns.get(individuals[j]);
                    if (!comparewith.containsKey(targetItemsetIDs[itemSizeCnt])) {
                        continue;
                    }
                    HashMap<String, Integer> comparePatterns = (HashMap<String, Integer>) comparewith.get(targetItemsetIDs[itemSizeCnt]);
                    Object[] patStr = targetPatterns.keySet().toArray(new String[0]);
                    for (Object pat : patStr) {
                        if (comparePatterns.containsKey(pat)) {
                            targetPatterns.remove(pat);
                        }
                    }

                }
                itemsets.put(targetItemsetIDs[itemSizeCnt], targetPatterns);
            }
            patterns.put(individuals[i], itemsets);
        }
        return patterns;
    }

    public HashMap removeSetSizes(HashMap in) {
        HashMap out = new HashMap();
        for (Object key : in.keySet().toArray()) {
            HashMap patterns = (HashMap) in.get(key);
            for (Object patKey : patterns.keySet().toArray()) {
                out.put(patKey, patterns.get(patKey));
            }
        }
        return out;
    }

    @Override
    public HashMap trainApproach(Individual[] trainingDataSet) {
        HashMap<String, HashMap> patterns = new HashMap<String, HashMap>();
        for (int i = 0; i < trainingDataSet.length; i++) {//for every individual
            System.out.println("Training " + i);
            patterns.put(trainingDataSet[i].name, (getPatterns(trainingDataSet[i])));
        }

        return patterns;
        //return removeNonUnique((setSizes));
    }

    public double minimumOf(Object A, Object B) {
        if (Double.parseDouble("" + A) > Double.parseDouble("" + B)) {
            return Double.parseDouble("" + B);
        }

        return Double.parseDouble("" + A);
    }

    public double getSimilarity(HashMap A, HashMap B) {
        double similarityValue = 0;
        Object[] setSizes = A.keySet().toArray();
        for (int i = 0; i < setSizes.length; i++) {
            if (B.containsKey(setSizes[i])) {
                HashMap<String, Double> patA = (HashMap<String, Double>) A.get(setSizes[i]);
                HashMap<String, Double> patB = (HashMap<String, Double>) B.get(setSizes[i]);
                for (Object patStrA : patA.keySet().toArray()) {
                    if (patB.containsKey(patStrA)) {
                        similarityValue += (minimumOf(patA.get(patStrA), patB.get(patStrA)) * Double.parseDouble("" + setSizes[i]));
                    }
                }
            }
        }
        return similarityValue;
    }
    public HashMap<String, Double> idfs = new HashMap<String, Double>();

    public double getTotalCount(HashMap docset, Object pattern) {
        Object[] trainingNames = docset.keySet().toArray();
        double count = 0.0;
        for (int i = 0; i < trainingNames.length; i++) {
            HashMap person = (HashMap) docset.get(trainingNames[i]);
            Object[] setSizes = person.keySet().toArray();
            for (int j = 0; j < setSizes.length; j++) {
                HashMap targetSet = (HashMap) person.get(setSizes[j]);
                Object[] patterns = targetSet.keySet().toArray();
                for (int k = 0; k < patterns.length; k++) {
                    count += Double.parseDouble("" + targetSet.get(patterns[k]));
                }
            }
        }
        return count;
    }

    public double getDocCount(HashMap docset, Object pattern) {
        Object[] trainingNames = docset.keySet().toArray();
        double count = 0.0;
        for (int i = 0; i < trainingNames.length; i++) {
            if (((HashMap) docset.get(trainingNames[i])).containsKey(pattern)) {
                count++;
            }
        }
        return count;
    }

    public HashMap getNormalizedTF(HashMap nonnorm) {
        HashMap tempNorm = (HashMap) nonnorm.clone();
        Object[] patterns = nonnorm.keySet().toArray();
        double total = 0.0;
        for (int i = 0; i < patterns.length; i++) {
            //System.out.println(""+(nonnorm.get(setSizes[i])));
            Object[] strings = ((HashMap) (nonnorm.get(patterns[i]))).keySet().toArray();
            for (int j = 0; j < strings.length; j++) {
                total += Double.parseDouble("" + ((HashMap) (nonnorm.get(patterns[i]))).get(strings[j]));
            }
        }
        for (int i = 0; i < patterns.length; i++) {
            Object[] strings = ((HashMap) (nonnorm.get(patterns[i]))).keySet().toArray();
            for (int j = 0; j < strings.length; j++) {
                //System.out.println(""+Double.parseDouble(((HashMap)(nonnorm.get(setSizes[i]))).get(strings[j])+""));
                tempNorm.put(strings[j], (Double.parseDouble("" + ((HashMap) (nonnorm.get(patterns[i]))).get(strings[j]))) / (double) total);
            }
        }
        return tempNorm;
    }

    public HashMap getNormalizedTFInt(HashMap nonnorm) {
        Object[] patterns = nonnorm.keySet().toArray();
        double total = 0.0;
        for (int i = 0; i < patterns.length; i++) {
            total += Double.parseDouble("" + ((HashMap) (nonnorm.get(patterns[i]))));
        }
        for (int i = 0; i < patterns.length; i++) {
            nonnorm.put(patterns[i], (Double.parseDouble("" + nonnorm.get(patterns[i]))) / (double) total);
        }
        return nonnorm;
    }
    HashMap<String, HashMap> tfidf = new HashMap<String, HashMap>();

    public void buildTFIDF(HashMap traininghash) {
        //System.out.println(""+traininghash.toString());

        HashMap normalizedCounts = (HashMap) traininghash.clone();
        //System.out.println(""+normalizedCounts.toString());
        Object[] trainingdata = normalizedCounts.keySet().toArray();

        for (int i = 0; i < trainingdata.length; i++) {
            try {
                normalizedCounts.put(trainingdata[i], getNormalizedTF((HashMap) normalizedCounts.get(trainingdata[i])));
            } catch (Exception ex) {
                // System.out.println(((HashMap)normalizedCounts.get(trainingdata[i])).toString());
                ex.printStackTrace();
                System.exit(0);
            }
        }
        for (int i = 0; i < trainingdata.length; i++) {
            HashMap targetset = ((HashMap) traininghash.get(trainingdata[i]));
            Object[] setsizes = ((HashMap) traininghash.get(trainingdata[i])).keySet().toArray();
            for (int j = 0; j < setsizes.length; j++) {
                HashMap targetPattern = (HashMap) targetset.get(setsizes[j]);
                Object[] patterns = targetPattern.keySet().toArray();
                for (int k = 0; k < patterns.length; k++) {
                    if (!idfs.containsKey(patterns[k])) {
                        double totalCount = getTotalCount(traininghash, patterns[k]);
                        double docCount = trainingdata.length;
                        idfs.put(patterns[k] + "", 1 + Math.log(totalCount / docCount));
                    }
                }
            }
        }
        //System.out.println(""+idfs.toString());
        tfidf = new HashMap();
        double var1 = 0, var2;
        for (int i = 0; i < trainingdata.length; i++) {
            HashMap person = ((HashMap) traininghash.get(trainingdata[i]));
            Object[] setSizes = ((HashMap) traininghash.get(trainingdata[i])).keySet().toArray();
            HashMap tempHashMap = new HashMap();
            for (int j = 0; j < setSizes.length; j++) {
                HashMap<String, Double> targetset = (HashMap) person.get(setSizes[j]);
                Object[] patterns = targetset.keySet().toArray();
                for (int k = 0; k < patterns.length; k++) {
                    var1 = Double.parseDouble("" + targetset.get(patterns[k]));
                    var2 = Double.parseDouble("" + idfs.get(patterns[k]));
                    tempHashMap.put(patterns[k], var1 * var2);
                }

            }
            tfidf.put("" + trainingdata[i], person);
        }

    }

    public double getSumSq(HashMap A) {
        double sq = 0;
        Object[] keys = A.keySet().toArray();
        for (Object key : keys) {
            sq += (Double.parseDouble("" + A.get(key)) * Double.parseDouble("" + A.get(key)));
        }
        return Math.sqrt(sq);
    }

    public HashMap getMergedTrain(HashMap hm) {
        HashMap toreturn = new HashMap();
        Object[] setsizes = hm.keySet().toArray();
        for (int i = 0; i < setsizes.length; i++) {
            HashMap currentSet = (HashMap) hm.get(setsizes[i]);
            Object[] patterns = currentSet.keySet().toArray();
            for (int j = 0; j < patterns.length; j++) {
                toreturn.put(patterns[j], currentSet.get(patterns[j]));
            }
        }
        return toreturn;
    }

    public double getTFIDF(HashMap query, HashMap train) {

        double similarityValue = 0;
        query = getNormalizedTF(query);
        train = getMergedTrain(train);
        Object[] setsizes = query.keySet().toArray();
        HashMap tempq = new HashMap();
        for (int i = 0; i < setsizes.length; i++) {
            if (idfs.containsKey(setsizes[i])) {
                double ax = Double.parseDouble("" + query.get(setsizes[i])) * Double.parseDouble("" + idfs.get(setsizes[i]));
                tempq.put(setsizes[i] + "", ax);
            }
        }
        query = tempq;

        double dotprod = 0;

        for (int i = 0; i < setsizes.length; i++) {
            if (train.containsKey(setsizes[i])) {
                dotprod += ((Double.parseDouble("" + train.get(setsizes[i])) * Double.parseDouble("" + query.get(setsizes[i]))));
            }
        }

        double sqrtsumtrain = getSumSq(train);

        double sqrtsumquery = getSumSq((query));

        return (double) (dotprod / (sqrtsumquery * sqrtsumtrain));
    }
    public String[][] results;

    @Override
    public Accuracy testDataSetUsingApproach(HashMap hm, Individual[] testingDataSet) {

        System.out.println("--------------------");
        buildTFIDF(hm);
        System.out.println("--------------------");
        results = new String[testingDataSet.length][2];
        Object[] trainingNames = hm.keySet().toArray();
        for (int i = 0; i < testingDataSet.length; i++) {
            System.out.println("Testing " + i);
            HashMap<String, Double> similarity = new HashMap<String, Double>();
            HashMap<String, HashMap> testPatterns = getPatterns(testingDataSet[i]);
            for (int j = 0; j < trainingNames.length; j++) {
                //similarity.put("" + trainingNames[j], getTFIDF(testPatterns,(HashMap) hm.get(trainingNames[j])));
                similarity.put("" + trainingNames[j], getTFIDF(testPatterns, (HashMap) tfidf.get(trainingNames[j])));
                System.out.println(""+getTFIDF(testPatterns, (HashMap) tfidf.get(trainingNames[j])));
            }
            System.out.println("------------------------------=!=!=!");
            Object[] keys = similarity.keySet().toArray();
            double maxsim = -1;
            String closest = "";
            for (int k = 0; k < keys.length; k++) {
                System.out.println(keys[k] + "=" + similarity.get(keys[k]));
                if (similarity.get(keys[k]) > maxsim) {
                    maxsim = similarity.get(keys[k]);
                    closest = keys[k] + "";
                }
            }
            results[i][0] = testingDataSet[i].name;
            results[i][1] = closest;
            System.out.println("--------" + results[i][0] + "-------" + results[i][1]);
        }
        return new Accuracy(results);
    }

    @Override
    public String getName() {
        return "FrequentPatternBased";
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
