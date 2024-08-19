package org.example;
import com.beust.jcommander.JCommander;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App 
{

    public static void main( String[] argv )
    {
        Args args = new Args();
        JCommander jc = new JCommander(args);

        try {
            jc.parse(argv);

        } catch (Exception e) {
            jc.usage();
        }

        if (args.help) {
            jc.usage();
            System.exit(0);
        }

        ArrayList<String> input_files_paths = args.input_files_paths;
        String output_path = args.path;
        String prefix = args.prefix;
        boolean append = args.append;

        if (!output_path.isEmpty()){
            output_path +="\\";
        }

        List<String> scanned_inputs = new ArrayList<>();

        int statistics_mode = 0;
        if (args.full_stat){
            statistics_mode = 2;
        }else if (args.short_stat){
            statistics_mode = 1;
        }

        input_files_paths.forEach((n) -> ReadFile(n, scanned_inputs) );

        Map<String, ArrayList<String>> sorted = SortLines(scanned_inputs);

        WriteSorted(sorted.get("Integer"), append, output_path + prefix + "integers.txt" );
        WriteSorted(sorted.get("Float"), append, output_path + prefix + "floats.txt" );
        WriteSorted(sorted.get("String"), append, output_path + prefix + "strings.txt" );

        switch (statistics_mode){
            case 1:
                System.out.println("the number of integers is equal to " + sorted.get("IntegerStats").get(0));
                System.out.println("the number of floats is equal to " + sorted.get("FloatStats").get(0));
                System.out.println("the number of strings is equal to " + sorted.get("StringStats").get(0));
                break;
            case 2:
                System.out.println("the number of integers is equal to " + sorted.get("IntegerStats").get(0));
                System.out.println("the minimal integer is equal to " + sorted.get("IntegerStats").get(1));
                System.out.println("the maximal integer is equal to " + sorted.get("IntegerStats").get(2));
                System.out.println("the sum of integers is equal to " + sorted.get("IntegerStats").get(3));
                System.out.println("the average of integers is equal to " + sorted.get("IntegerStats").get(4) + "\n");

                System.out.println("the number of floats is equal to " + sorted.get("FloatStats").get(0));
                System.out.println("the minimal float is equal to " + sorted.get("FloatStats").get(1));
                System.out.println("the maximal float is equal to " + sorted.get("FloatStats").get(2));
                System.out.println("the sum of floats is equal to " + sorted.get("FloatStats").get(3));
                System.out.println("the average of floats is equal to " + sorted.get("FloatStats").get(4) + "\n");

                System.out.println("the number of strings is equal to " + sorted.get("StringStats").get(0));
                System.out.println("the minimal length of string is equal to " + sorted.get("StringStats").get(1));
                System.out.println("the maximal length of string is equal to " + sorted.get("StringStats").get(2));

                break;
        }
    }

    private static void ReadFile(String FilePath, List<String> scanned_inputs) {

        File file = new File(FilePath);
        List<String> lines = null;

        try {
            lines = FileUtils.readLines(file, "UTF-8");
        } catch (IOException e) {
            System.out.println("\n Error occurred while opening file  " + FilePath + ", check file path and try again\n");
        }

        if (lines != null) {
            scanned_inputs.addAll(lines);
        }
    }

    private static String DetermineLineType(String line){

        try {
            Long.parseLong(line);
            return "Integer";
        }

        catch( NumberFormatException nfe1 ) {

            try {
                Float.parseFloat( line );

                if(!line.contains(".") && !line.contains("E") ) {
                    return "Integer";
                }

                return "Float";
            }

            catch( NumberFormatException nfe2 ) {
                return "String";
            }
        }

    }

    private static Map<String, ArrayList<String>> SortLines(List<String> LinesList){

        Map<String, ArrayList<String>> sorted = new HashMap<>();

        ArrayList<String> IntegerList = new ArrayList<>();
        ArrayList<String> FloatList = new ArrayList<>();
        ArrayList<String> StringList = new ArrayList<>();

        ArrayList<String> IntegerStats = new ArrayList<>();
        ArrayList<String> FloatStats = new ArrayList<>();
        ArrayList<String> StringStats= new ArrayList<>();

        int IntegerQuantity =  0;
        long IntegerMinimum = Long.MAX_VALUE;
        long IntegerMaximum = Long.MIN_VALUE;
        long IntegerSum =  0;
        double IntegerAverage;

        int FloatQuantity =  0;
        float FloatMinimum =  0;
        float FloatMaximum =  0;
        double FloatSum =  0;
        double FloatAverage;

        int StringQuantity =  0;
        int StringMinimumLength = Integer.MAX_VALUE;
        int StringMaximumLength =  Integer.MIN_VALUE;

        ArrayList<Long> int_list = new ArrayList<>();
        ArrayList<Float> float_list = new ArrayList<>();

        for (String line: LinesList){
            switch (DetermineLineType(line)){

                case "Integer":
                    IntegerList.add(line);
                    IntegerQuantity++;
                    IntegerMinimum = Math.min(IntegerMinimum, Long.parseLong(line));
                    IntegerMaximum = Math.max(IntegerMaximum, Long.parseLong(line));
                    IntegerSum += Long.parseLong(line);
                    int_list.add(Long.parseLong(line));
                    break;

                case "Float":
                    FloatList.add(line);
                    FloatQuantity++;
                    FloatMinimum = Math.min(FloatMinimum, Float.parseFloat(line));
                    FloatMaximum = Math.max(FloatMaximum, Float.parseFloat(line));
                    FloatSum += Float.parseFloat(line);
                    float_list.add(Float.parseFloat(line));
                    break;

                case "String":
                    StringList.add(line);
                    StringQuantity++;
                    StringMinimumLength = Math.min(StringMinimumLength, line.length());
                    StringMaximumLength = Math.max(StringMaximumLength, line.length());
                    break;
            }
        }

        IntegerAverage = int_list.stream().mapToDouble(val -> val).average().orElse(0.0);
        FloatAverage = float_list.stream().mapToDouble(val -> val).average().orElse(0.0);

        IntegerStats.add(Integer.toString(IntegerQuantity));
        IntegerStats.add(Long.toString(IntegerMinimum));
        IntegerStats.add(Long.toString(IntegerMaximum));
        IntegerStats.add(Long.toString(IntegerSum));
        IntegerStats.add(Double.toString(IntegerAverage));

        FloatStats.add(Integer.toString(FloatQuantity));
        FloatStats.add(Float.toString(FloatMinimum));
        FloatStats.add(Float.toString(FloatMaximum));
        FloatStats.add(Double.toString(FloatSum));
        FloatStats.add(Double.toString(FloatAverage));

        StringStats.add(Integer.toString(StringQuantity));
        StringStats.add(Integer.toString(StringMinimumLength));
        StringStats.add(Integer.toString(StringMaximumLength));

        sorted.put("Integer", IntegerList);
        sorted.put("Float", FloatList);
        sorted.put("String", StringList);
        sorted.put("IntegerStats", IntegerStats);
        sorted.put("FloatStats", FloatStats);
        sorted.put("StringStats", StringStats);

        return sorted;
    }

    private static void WriteSorted(ArrayList<String> Sorted, boolean append, String FilePath){
        if (Sorted.isEmpty()){
            return;
        }

        File file = new File(FilePath);

        try {
            boolean created =  file.createNewFile();
        } catch (IOException e) {
            System.out.println("It was not possible to create a file at the specified path, check the correctness of the path and try again");
        }

        try {
            FileUtils.writeLines(file, Sorted, append);
        } catch (IOException e) {
            System.out.println("An error occurred when transferring lines to files. Check the input data for correctness and try again");
        }
    }
}
