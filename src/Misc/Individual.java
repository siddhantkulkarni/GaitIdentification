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
import java.util.*;

public class Individual {

    public String name;
    public ArrayList<HashMap> frames = new ArrayList<HashMap>();

    public Individual(String folder, int fileCount) throws FileNotFoundException {
        File[] files = new File(folder).listFiles();
        name=new File(folder).getName();
        for (int i = 0; i < fileCount; i++) {
            int selected = (int) Math.abs(Math.random() * files.length);
            addFramesForThisIndividual(files[selected]);
        }
    }

    public void addFramesForThisIndividual(File selectedFile) throws FileNotFoundException {
        Scanner sc = new Scanner(selectedFile);
        int counter = 0;
        String[] temp;
        HashMap<String,String> hm=new HashMap<String,String>();
        while (sc.hasNext()) {
            temp= sc.nextLine().split(";");
            hm.put(temp[0], temp[1]+";"+temp[2]+";"+temp[3]);
            counter++;
            if (counter == MainWindowAttemptOne.numberOfJoints) {
                this.frames.add(hm);
                counter=0;hm=new HashMap<String,String>();
            }
        }
        sc.close();
        
    }
}
