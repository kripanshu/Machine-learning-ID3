/**
 * Created by kripanshubhargava on 9/29/16.
 */
import java.io.*;
import java.util.Scanner;
import java.util.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Tree1 extends  IOException{



    static int n= 0; // number of features
    static int m=0; //instances
    static int p = 0; // partition count
    static int[][] getInput;
    static int[] Ccount;
    static String[] classNames;
    static int[][] Data;
    static ArrayList<Double> classEntropy=new ArrayList<Double> ();
   // static ArrayList<Double> classDataProb= new ArrayList<Double>();
    static double[][] featureprob; // array for storing probability of partitions individually
    static double[][] conditionalEntropy;
    static double[][] Gain;
    static int ClassID;
   static int FeatureID;
    static String IdName,FeatureName;
    static String outputFile;

    public static void readPartitions(String fileName) {
        String line = null;
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(fileName));
            while (null != (line = br.readLine())) {
                p++;
            }
            br = new BufferedReader(new FileReader(fileName));
            classNames = new String[p];
            Data = new int[p][m];
            Ccount = new int[p];
            int j = 0;
            while (null != (line = br.readLine())) {
                String[] s = line.split(" ");
                classNames[j] = s[0];
                Ccount[j] = 0;
                for (int i = 0; i < s.length - 1; i++) {
                    Ccount[j]++;
                    Data[j][i] = Integer.parseInt(s[i + 1]);
                }
                j++;
            }
            br.close();
        } catch (FileNotFoundException ex) {
            System.out
                    .println("Partition file not found ");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public static void readDataset(String fileName) {
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(fileName));
            String line = br.readLine();
            String[] countString = line.split(" ");
            m = Integer.parseInt(countString[0]);
            n = Integer.parseInt(countString[1]);
            getInput = new int[m][n];

            int rowCount = 0;
            while (null != (line = br.readLine())) {
                String[] features = line.split(" ");
                for (int i = 0; i < features.length-1; i++) {
                    Tree1.getInput[rowCount][i] = Integer.parseInt(features[i]);
                }
                rowCount++;
            }

            br.close();
        } catch (FileNotFoundException ex) {
            System.out
                    .println("Dataset file not found ");
        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }






    public static void input() {
        Scanner scanner = new Scanner(new InputStreamReader(System.in));
        System.out
                .println("Enter names of the files dataset input-partition operation-output");
        String input = scanner.nextLine();

        String[] fileNames = input.split(" ");
        String dataset = fileNames[0];
        String inputPartition = fileNames[1];
       outputFile = fileNames[2];
        readDataset(dataset);
        readPartitions(inputPartition);
    }


    public static double calculateEntropyfortarget(ArrayList<Integer> a, double b)
    {
        double zero=0,one=0,two=0;
        double prob []=new double[3];
        double entrop=0;
        for(int i=0; i<b;i++)
        {
            if(a.get(i) ==0)
            {

                zero++;
            }
            else
                if(a.get(i)==1)
                {

                    one++;
                }
            else if(a.get(i)==2 )
                {
                    two++;

                }


        }
        prob[0]=(zero/b);
        prob[1]=(one/b);
        prob[2]=(two/b);



        if(prob[0] != 0 && prob[1] !=0 && prob[2]!=0)
        {
            entrop=(- 1) *(prob[0]*((Math.log(prob[0]) / Math.log(2)))+prob[1]*((Math.log(prob[1]) / Math.log(2)))+prob[2]*((Math.log(prob[2]) / Math.log(2))));
        }
        else
            if (prob[0]== 0 && prob[1] !=0 && prob[2]!=0)
            {
                entrop=(- 1) *(prob[1]*((Math.log(prob[1]) / Math.log(2)))+prob[2]*((Math.log(prob[2]) / Math.log(2))));

            }
            else
            if (prob[0]!= 0 && prob[1] ==0 && prob[2]!=0)
            {
                entrop=(- 1) *(prob[0]*((Math.log(prob[0]) / Math.log(2)))+prob[2]*((Math.log(prob[2]) / Math.log(2))));
            }
            else
            if (prob[0]!= 0 && prob[1] !=0 && prob[2]==0)
            {
                entrop=(- 1) *(prob[0]*((Math.log(prob[0]) / Math.log(2)))+prob[1]*((Math.log(prob[1]) / Math.log(2))));
            }
            else
            if (prob[0]== 0 && prob[1] ==0 && prob[2]!=0)
            {
                entrop=(- 1) *(prob[2]*((Math.log(prob[2]) / Math.log(2))));
            }
            else
            if (prob[0]== 0 && prob[1] !=0 && prob[2]==0)
            {
                entrop=(- 1) *(prob[1]*((Math.log(prob[1]) / Math.log(2))));
            }
            else
            if (prob[0]!= 0 && prob[1] ==0 && prob[2]==0)
            {
                entrop=(- 1) *(prob[0]*((Math.log(prob[0]) / Math.log(2))));
                if (entrop == -0.0) // as entropy can never be negitive
                {
                    entrop=0.0;
                }
            }
        else entrop =0.0;


       // entrop=(- 1) *(prob[0]*((Math.log(prob[0]) / Math.log(2)))+prob[1]*((Math.log(prob[1]) / Math.log(2)))+prob[2]*((Math.log(prob[2]) / Math.log(2))));



       // System.out.println(prob[0] + " b :"+ b + " c : "+a.get(0) + " prob 1 " + prob[1] + " zero : " + zero + " one " + one/b + " two" + two + "entrop :" + entrop);
        return entrop;
    }
    public static double calculateEntropyinstance(int zero, int one) {
        double ans = 0;
        double totalCount = zero + one;
        double a1 = (zero / totalCount);
        double a2 = (one / totalCount);

        if (a1 == 0 && a2 == 0)
            ans = 0;
        else if (a1 == 0 && a2 != 0)
            ans = (-1) * (a2 * ((Math.log(a2) / Math.log(2))));
        else if (a1 != 0 && a2 == 0)
            ans = (-1) * (a1 * ((Math.log(a1) / Math.log(2))));
        else
            ans = (-a1 * (Math.log(a1) / Math.log(2)) - (a2 * (Math.log(a2) / Math
                    .log(2))));

        if (ans == -0.0)
            return 0;
        else
            return ans;
    }

    public static double probability(ArrayList<Integer> a,double b,int c)
    {
        double zero=0,one=0,two=0;
        double prob []=new double[3];
        double entrop=0;
        for(int i=0; i<b;i++)
        {
            if(a.get(i) ==0)
            {

                zero++;
            }
            else
            if(a.get(i)==1)
            {

                one++;
            }
            else if(a.get(i)==2 )
            {
                two++;

            }


        }
        prob[0]=(zero/b);
        prob[1]=(one/b);
        prob[2]=(two/b);
featureprob[c][0]=prob[0];
        featureprob[c][1]=prob[1];
        featureprob[c][2]=prob[2];


        return 0;
    }
    public static void writeOutput() {
        try {
            BufferedWriter output = new BufferedWriter(new FileWriter(
                    outputFile));
            String line = "";
            for (int i = 0; i < Tree1.p; i++) {
                if (i == ClassID) {
                    String split1 = Tree1.classNames[i] + "0";
                    String split2 = Tree1.classNames[i] + "1";
                    String split3 = Tree1.classNames[i] + "2";
                    for (int j = 0; j < Tree1.Ccount[i]; j++) {
                        if (getInput[Data[i][j] - 1][FeatureID] == 1)
                            split1 = split1 + " " + Data[i][j];
                        if (getInput[Data[i][j] - 1][FeatureID] == 0)
                            split2 = split2 + " " + Data[i][j];
                        if (getInput[Data[i][j] - 1][FeatureID] == 2)
                            split3 = split3 + " " + Data[i][j];
                    }
                    output.write(split1);
                    output.write("\n");
                    output.write(split2);
                    output.write("\n");
                    output.write(split3);
                    if (i < Tree1.p)
                        output.write("\n");
                    System.out.println("Partition " + classNames[i]
                            + " was replaced with partitions "
                            + classNames[i] + "0" + "," + classNames[i]
                            + "1" + " using Feature "
                            + (FeatureID + 1));
                } else {
                    line =classNames[i];
                    for (int j = 0; j < Ccount[i]; j++) {
                        line = line + " " + Data[i][j];
                    }
                    output.write(line);
                    if (i < p)
                        output.write("\n");
                }
            }
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }}

    public static double calculateConditionalEntropy(int z0, int y0, int z1, int y1,int y2, int z2)
    { double entrop1=0;
        double zero= z0 + y0;
        double one
                = z1 + y1;
        double two = z2 + y2;
        double totalCount = zero + one + two;


        if (zero == 0 && one == 0 && two==0)
            return 0;

        if (zero == 0 && one == 0 && two!=0)
            return (two / totalCount) * calculateEntropyinstance(z2, y2);

        if (zero== 0 && one != 0 && two==0)
            return (one / totalCount) * calculateEntropyinstance(z1, y1);

        if (zero == 0 && one != 0 && two!=0)
            return (two/ totalCount) * calculateEntropyinstance(z2, y2) + (one / totalCount) * calculateEntropyinstance(z1, y1);

        if (zero != 0 && one == 0 && two==0)
            return (zero / totalCount) * calculateEntropyinstance(z0, y0);

        if (zero != 0 && one == 0 && two!=0)
            return (zero / totalCount) * calculateEntropyinstance(z0, y0) + (two / totalCount) * calculateEntropyinstance(z2, y2);

        if (zero != 0 && one != 0 && two==0)
            return (zero / totalCount) * calculateEntropyinstance(z0, y0) + (one / totalCount) * calculateEntropyinstance(z1, y1);

        if (zero != 0 && one != 0 && two!=0)
            return (zero / totalCount) * calculateEntropyinstance(z0, y0) + (one / totalCount) * calculateEntropyinstance(z1, y1) + (two/ totalCount) * calculateEntropyinstance(z2, y2);
        return 0;

    }
   public static void main(String[]args) {

       input();
       conditionalEntropy = new double[p][n - 1];



 /*         //System.out.println("class count"+classCount);  
  //System.out.println(classNames); 
   S ystem.out.println(m); 
      System.out.println(n); 
      // System.out.println(getInput);  
         f or(int i=0; i<p;i++) 
          { 
            System.out.println("class count : "+ classCount[i]); 
                 }  
                for(int i=0; i<p;i++) 
                      { 
                                  System.out.println("class count : "+ classNames[i]); 
                                         } 
                      for(int i=0;i<p;i++) 
                       { 
                         for(int j=0;j<m;j++) 
                          System.out.println(" yo" +Data[i][j]); 
                                 } 
                               for(int i=0;i<m;i++) 
                                { 
                                  for(int j=0;j<n;j++) 
                                    System.out.println(" po" +getInput[i][j]); 
                                     }  
                                       } */

// for classdata


       for (int i = 0; i < p; i++)

       { //int temp []=new int [classCount[i]];

           ArrayList<Integer> temp = new ArrayList<Integer>();
           for (int j = 0; j < Ccount[i]; j++) {
               if (Data[i][j] != 0) {
                   temp.add(getInput[Data[i][j] - 1][n - 1]); // this is the value of Target attribute corresponding to the partitions dataset
                   //temp[j] =getInput[Data[i][j]][n-1];
                   // System.out.println(temp);

               }


           }

           // entropy of target attribute for each partition
           classEntropy.add(calculateEntropyfortarget(temp, Ccount[i]));
           //System.out.println(classEntropy);


       }
       Gain = new double[p][n - 1];


       for (int i = 0; i < p; i++) {
           for (int j = 0; j < n - 1; j++) {
               int one0 = 0;
               int zero0 = 0;
               int one1 = 0;
               int zero1 = 0;
               int zero2 = 0;
               int one2 = 0;

               for (int k = 0; k < Ccount[i]; k++) {
                   if (getInput[Data[i][k] - 1][j] == 0 && getInput[Data[i][k] - 1][n - 1] == 0)
                       zero0++;
                   else if (getInput[Data[i][k] - 1][j] == 0 && getInput[Data[i][k] - 1][n - 1] == 1)
                       one0++;
                   else if (getInput[Data[i][k] - 1][j] == 1 && getInput[Data[i][k] - 1][n - 1] == 0)
                       zero1++;
                   else if (getInput[Data[i][k] - 1][j] == 1 && getInput[Data[i][k] - 1][n - 1] == 1)
                       one1++;
                   else if (getInput[Data[i][k] - 1][j] == 2 && getInput[Data[i][k] - 1][n - 1] == 0)
                       zero2++;
                   else if (getInput[Data[i][k] - 1][j] == 2 && getInput[Data[i][k] - 1][n - 1] == 1)
                       one2++;
               }

               conditionalEntropy[i][j] = calculateConditionalEntropy(
                       zero0, one0, zero1, one1, zero2, one2);
               Gain[i][j] = classEntropy.get(i)
                       - conditionalEntropy[i][j];


           }
       }
       double maximum = 0;
       for (int i = 0; i < p; i++) {
           double f = 0;
           double maxValue = 0;
           int maxFeatureID = 0;
           for (int j = 0; j < n - 1; j++) {
               if (Gain[i][j] > maxValue) {
                   maxValue = Gain[i][j];
                   maxFeatureID = j;
               }
           }
           f = maxValue * ((double) Ccount[i] / m);

           //System.out.println("\n Information Gain:"+f);
           if (f > maximum) {
               maximum = f;
               ClassID = i;
               FeatureID = maxFeatureID;
               IdName = classNames[ClassID];


           }
       }

       //System.out.println(" class :  " + IdName + "   feature :" + FeatureID);
       writeOutput();



    }

}
