/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Misc;

import UI.MainWindowAttemptOne;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author siddh
 */
public class DatasetHolder {

    public static Individual[] trainingIndividuals, testingIndividuals;


    public static void getDataSet() throws FileNotFoundException {
        
        trainingIndividuals=new Individual[ExperimentalConfiguration.numberOfIndividualsForTraining];
        testingIndividuals=new Individual[ExperimentalConfiguration.numberOfIndividualsForTesting];
        System.out.println("Path to Dataset="+ExperimentalConfiguration.pathTODataset);
        File[] foldersForIndividuals = new File(ExperimentalConfiguration.pathTODataset).listFiles();
        boolean flag = false;
        int trainingDone = 0, testingDone = 0;
        System.out.println("Total train to get:"+ExperimentalConfiguration.numberOfIndividualsForTraining);
        System.out.println("Total test to get:"+ExperimentalConfiguration.numberOfIndividualsForTesting);
        while (!flag) {
            int selected = (int) Math.abs(Math.random() * foldersForIndividuals.length);
            if(!foldersForIndividuals[selected].isDirectory()) continue;
            if(trainingDone>=ExperimentalConfiguration.numberOfIndividualsForTraining)
                break;
            //System.err.println(""+ExperimentalConfiguration.numberOfInstancesPerIndividual);
            trainingIndividuals[trainingDone]=new Individual(foldersForIndividuals[selected].getAbsolutePath(), ExperimentalConfiguration.numberOfInstancesPerIndividual);
            trainingDone++;
            //System.out.println("Training done:"+trainingDone);
            if(testingDone<ExperimentalConfiguration.numberOfIndividualsForTesting){
                testingIndividuals[testingDone]=new Individual(foldersForIndividuals[selected].getAbsolutePath(), 1);
                testingDone++;
            }
        }

    }

    public static void printDataSet(){
        System.out.println("*****Printing Training Dataset****");
        for(int i=0;i<trainingIndividuals.length;i++){
            System.out.println(""+trainingIndividuals[i].name);
        }
        System.out.println("*****Printing Testing Dataset****");
        for(int i=0;i<testingIndividuals.length;i++){
            System.out.println(""+testingIndividuals[i].name);
            System.out.println(""+testingIndividuals[i].frames.size());
         /*   for(int j=0;j<testingIndividuals[i].frames.size();j++){
                System.out.println(""+testingIndividuals[i].frames.get(j).toString());
            }*/
        }
    }
}

