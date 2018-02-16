package com.company;

import java.io.*;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static java.lang.System.exit;

public class donation_analytics {
    private static File directory = new File("");
    private static String absolutePath = directory.getAbsolutePath();
    private static final String  input_itcont =absolutePath+"\\input\\itcont.txt";
    private static final String  input_percentile =absolutePath+"\\input\\percentile.txt";
    private static final String outputFileName = absolutePath+"\\output\\repeat_donors.txt";

    private static int totalDollar=0;
    private static List<Integer>  transaction_amts = new ArrayList<Integer>();
    private static List<String> zip_codes = new ArrayList<String>();
    private static List<String> names = new ArrayList<String>();

    private static LinkedList<Integer> repeatTable = new LinkedList<Integer>();

    private static int transaction_amt;
    private static String transaction_dt;
    private static String cmte_id;
    private static String zip_code;
    private static String name;
    private static int percentile;
    private static PrintWriter outputStream= null;


    private static void outputRepeat() {
        int n = (int) (1 + (float)(percentile/100.0) * repeatTable.size());
        String outline = MessageFormat.format("{0}|{1}|{2}|{3}|{4}|{5}\n", cmte_id, zip_code, transaction_dt.substring(4), repeatTable.get(n - 1), Integer.toString(totalDollar),repeatTable.size());
        outputStream.println(outline);
    }

    private static void sortRepeatDonators(){
        Collections.sort(repeatTable);
    }
    //add new donator, name, zip and transaction amount are saved.
    private static void addNewDonator(){
        names.add(name);
        zip_codes.add(zip_code);
        transaction_amts.add(transaction_amt);
    }

    private static void addNewRepeatDonator(){
        repeatTable.add(transaction_amt);
    }
    //read percentile from in_percentile stream
    private void readPercentile() throws IOException {
        FileInputStream in_percentile = null;
        try{
            in_percentile = new FileInputStream(input_percentile);
            
            percentile = in_percentile.read();
            percentile -= 21;    //convert string to integer
            if(percentile >=0 && percentile <=100){
                in_percentile.close();
            }else{
                System.out.println("FILE percentile.txt contains invalid numbers!");
                exit(-1);
            }
        }finally {
            if(in_percentile != null){
                in_percentile.close();
            }
        }
    }
    //read the raw data and discard the useless input. Extract the fields we are interested. 
    private boolean splitLines(String line){  
        String[] parts = line.split("\\|");
        if(!parts[15].isEmpty() || parts[0].isEmpty() || parts[7].isEmpty() || parts[10].isEmpty() || parts[13].isEmpty() || parts[14].isEmpty() || parts[10].length()<5 || parts[13].length()!=8 || Integer.parseInt(parts[13].substring(0,2))>12 || Integer.parseInt(parts[13].substring(2,4))>31)
            return false;        
        cmte_id = parts[0];
        name = parts[7];
        zip_code = parts[10].substring(0,5);
        transaction_dt = parts[13];
        transaction_amt = Integer.parseInt(parts[14]);
        String other_id = parts[15];
        return true;
    }
    //determin whether the new input is repeated donator. 
    private void handleLines(){
        int index;
        if(((index = names.indexOf(name))!=-1 ) && (zip_codes.get(index).equals(zip_code))){
            addNewRepeatDonator();
            totalDollar += transaction_amt;
            sortRepeatDonators();
            outputRepeat();
        }else{
            addNewDonator();
        }
    }

    donation_analytics() throws IOException {
        readPercentile();
    }

    public void run() throws IOException {
        BufferedReader bufferdReader = null;
        File inputitcont = new File(input_itcont);
        DecimalFormat decimalFormat=new DecimalFormat("\rthe data file has been read and processed 0.00%");
        int length = 0;
        try{
            bufferdReader = new BufferedReader(new FileReader(input_itcont));
            outputStream = new PrintWriter(new FileWriter(outputFileName));
            String line;
            while((line = bufferdReader.readLine()) != null){
                if(!splitLines(line)){
                    continue;
                }
                length += line.length();
                System.out.print(decimalFormat.format((float) length / inputitcont.length()));
                handleLines();
            }
        }finally {
            if(bufferdReader != null){
                bufferdReader.close();
            }
            if(outputStream != null){
                outputStream.close();
            }
        }
    }

}
