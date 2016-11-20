/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Misc;
import java.util.*;
import ApproachImplementation.IApproachInterface;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author Siddhant Kulkarni
 */
public class UtilityClass {

    public static void createAndDisplayBarGraph(String frameTitle, String graphTitle, String xAxisTitle, String yAxisTitle, ParamTypeEnum param) {
      
        
    }
    
    public static long getMemoryRequiredByLearningModule(HashMap hm) throws IOException{
        return sizeof(hm);
    }
     public static int sizeof(Object obj) throws IOException {

        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutputStream);

        objectOutputStream.writeObject(obj);
        objectOutputStream.flush();
        objectOutputStream.close();

        return byteOutputStream.toByteArray().length;
    }

    public static void displaySimpleMessageBox(String message){
        JOptionPane.showMessageDialog(null, message, "InfoBox", JOptionPane.INFORMATION_MESSAGE);
    }
}
