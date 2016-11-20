/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainpackage;

import UI.*;
import Misc.*;
import ApproachImplementation.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.*;
import ApproachImplementation.*;
import java.io.IOException;
import java.io.PrintWriter;
/**
 *
 * @author siddh
 */
public class ExecuteExperiment {
    public static void main(String []args) throws FileNotFoundException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException{
        //Arguments
        /*
        1. Whether UI or NONUI
        2. CSV configuration file
        */
        if(args.length!=2){
            System.out.println("Incorrect number of arguments");
            System.out.println("Argument 1 should indicate whether or not UI is to be instantiated. Value must be \"UI\" or \"NOUI\"");
            System.out.println("Argument 2 must specify an absolute path to the configuration CSV file.");
            System.out.println("Please specify the correct arguments and try again.");
        }else{
            if(args[0].equals("UI")){
                System.out.println("Launching UI");
                MainWindowAttemptOne.dir=args[1];
                MainWindowAttemptOne.main(args);
            }else{
                System.out.println("Starting Evaluation Using "+args[1]);
                Scanner sc=new Scanner(new File(args[1]));
                System.out.println(sc.nextLine());
                boolean ApprachNamesDone=false;
                ArrayList<IApproachInterface> approaches=new ArrayList<IApproachInterface>();
                //deal with approaches first
                while(sc.hasNext()){
                    String[] words=sc.nextLine().split(",");
                    if(words[0].equals("End of Approaches"))
                    {
                        ApprachNamesDone=true;
                        break;
                    }else{
                        System.out.println(words[0]);
                        try{
                        Class<?>temp=Class.forName(words[0]);
                        IApproachInterface obj=(IApproachInterface) temp.newInstance();
                            
                        approaches.add(obj);
                        }catch(ClassNotFoundException c){
                            System.out.println("Following class was not found:"+words[0]);
                            break;
                        }
                    }
                }
                System.out.println("Out of approach list");
                while(sc.hasNext()){
                    String line=sc.nextLine();
                    String[] values=line.split(",");
                    if(values.length<2)continue;
                    ExperimentalConfiguration.extraParameters.put(values[0], values[1]);
                }
                sc.close();
                ExperimentalConfiguration.applyHashMap(ExperimentalConfiguration.extraParameters);
                ExperimentalConfiguration.pathTODataset=(String) ExperimentalConfiguration.extraParameters.get("Datasetpath");
                HashMap<String,String> tempHm=new HashMap<String,String>();
                DatasetHolder.getDataSet();
                for(IApproachInterface approach:approaches){
                    String temp="";
                    System.out.println("Approach Identified:"+approach.getName());
                    long tStart = System.currentTimeMillis();
                    HashMap learningModel=approach.trainApproach(DatasetHolder.trainingIndividuals);
                    double learningTime=System.currentTimeMillis()-tStart;
                    double sizeOfModel=UtilityClass.getMemoryRequiredByLearningModule(learningModel);
                    tStart = System.currentTimeMillis();
                    Accuracy tempAcc=approach.testDataSetUsingApproach(learningModel, DatasetHolder.testingIndividuals);
                    double testingTime = System.currentTimeMillis()-tStart;
                    temp=""+(((double)(double)tempAcc.accurate/(double)tempAcc.total)*100)+","+sizeOfModel+","+learningTime+","+testingTime;
                    tempHm.put(approach.getName(), temp);
                }
                
                PrintWriter pw=new PrintWriter(new File("Results.csv"));
                pw.write("Approaches,");
                for(IApproachInterface approach: approaches){
                    pw.write(approach.getName()+",");
                }
                pw.write("\r\n");
                pw.write("Accuracy,");
                for(IApproachInterface approach: approaches){
                    pw.write(((String)tempHm.get(approach.getName())).split(",")[0]+",");
                }
                pw.write("\r\n");
                pw.write("MemoryRequired,");
                for(IApproachInterface approach: approaches){
                    pw.write(((String)tempHm.get(approach.getName())).split(",")[1]+",");
                }
                pw.write("\r\n");
                pw.write("TimeToTrain,");
                for(IApproachInterface approach: approaches){
                    pw.write(((String)tempHm.get(approach.getName())).split(",")[2]+",");
                }
                pw.write("\r\n");
                pw.write("TimeToTest,");
                for(IApproachInterface approach: approaches){
                    pw.write(((String)tempHm.get(approach.getName())).split(",")[3]+",");
                }
                pw.write("\r\n");
                pw.write("ExperimentConfiguration,\r\nApproach Class Names,\r\n");
                for(IApproachInterface approach:approaches){
                    pw.write(approach.getName()+",\r\n");
                }
                pw.write("End of Approaches,\r\n");
                Object[] keys=ExperimentalConfiguration.extraParameters.keySet().toArray();
                for(Object key:keys){
                    pw.write(key+","+ExperimentalConfiguration.extraParameters.get(key)+",\r\n");
                }
                pw.write("EndOfConfiguration,\r\n");
                pw.close();
            }
        }
        
            
    }
}
