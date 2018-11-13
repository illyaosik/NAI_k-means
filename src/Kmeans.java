import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class Kmeans {
    public static void main(String[] args) throws IOException {

        System.out.println("Provide number of clusters");
        Scanner sc = new Scanner(System.in);
        NUMBER_OF_CLUSTERS = sc.nextInt();
        CENTROIDS = new double[NUMBER_OF_CLUSTERS][5];
        number_of_flowers = new int[NUMBER_OF_CLUSTERS][3];


        myReader(TRAINING_PATH, 1); // 1 for train

        System.out.println(CENTROIDS[0][0]);
        System.out.println(Arrays.toString(rowList.get(0)));

        myReader(TEST_PATH, 0); // other number for test

    }

    static String TRAINING_PATH = "/Users/illiaosiyuk/IdeaProjects/NAI_k-means/src/test.txt";
    static String TEST_PATH = "/Users/illiaosiyuk/IdeaProjects/NAI_k-means/src/test.txt";
    public static int virginica = 0;
    public static int versicolor = 1;
    public static int setosa = 2;

    static int alreadyRead = 0;

    public static List<String[]> rowList = new ArrayList<String[]>(); // Вся считанная информация
    public static List<String[]> rowList_test = new ArrayList<>();// Вся информация для теста
    public static int ACCURACY = 0;



    public static int COUNTER_INPUT = 0;
    public static int NUMBER_OF_CLUSTERS;
    public static int[][] number_of_flowers;
    public static double[][] CENTROIDS; //= new double[NUMBER_OF_CLUSTERS][5];// Количество кластеров и по одному элементу в них в методе
                                                                            // first_myClusters


    public static void makeMatrix(String line){

        rowList.add(line.split(","));
        COUNTER_INPUT++;
    }
    public static void makeTestMatrix(String line){

        rowList_test.add(line.split(","));
        COUNTER_INPUT++;
    }

    public static void myReader(String path, int choose) throws IOException {
        if(choose==1) {
            Stream<String> stream = Files.lines(Paths.get(path), StandardCharsets.UTF_8);
            stream.forEach(Kmeans::makeMatrix);
        }else {
            Stream<String> stream = Files.lines(Paths.get(path), StandardCharsets.UTF_8);
            stream.forEach(Kmeans::makeTestMatrix);
        }
        first_myClusters();
        //doTest();
    }

    public static void first_myClusters(){
        int counterOfCluster = 0;
        for(String[] row : rowList){
            if(counterOfCluster < NUMBER_OF_CLUSTERS) {
                if(row[4].equals("Iris-virginica")){
                    number_of_flowers[counterOfCluster][0] += 1;
                }else if(row[4].equals("Iris-versicolor")){
                    number_of_flowers[counterOfCluster][1] += 1;
                }else {
                    number_of_flowers[counterOfCluster][2] += 1;
                }
                for (int i = 0; i < 4; i++) {

                    CENTROIDS[counterOfCluster][i] = Double.parseDouble(row[i]);
                }
                CENTROIDS[counterOfCluster][4] = 1;

                counterOfCluster++;
                alreadyRead++;
            }
        }
        doTest();
    }

    public static double calculateDistance(String[] point) {
        double shortest = Math.pow((Double.parseDouble(point[0]) - CENTROIDS[0][0]),2) +
                Math.pow((Double.parseDouble(point[1]) - CENTROIDS[0][1]),2) +
                Math.pow((Double.parseDouble(point[2]) - CENTROIDS[0][2]),2) +
                Math.pow((Double.parseDouble(point[3]) - CENTROIDS[0][3]),2);
        System.out.println(shortest + " distance to the first centroid");
        int number_of_cluster = 0;
        for(int i = 1; i < NUMBER_OF_CLUSTERS; i ++) {
            double distance = Math.pow((Double.parseDouble(point[0]) - CENTROIDS[i][0]),2) +
                    Math.pow((Double.parseDouble(point[1]) - CENTROIDS[i][1]),2) +
                    Math.pow((Double.parseDouble(point[2]) - CENTROIDS[i][2]),2) +
                    Math.pow((Double.parseDouble(point[3]) - CENTROIDS[i][3]),2);
            System.out.println(distance + " distance to the "+ i + " centroid");
            if(shortest > distance){
                shortest = distance;
                number_of_cluster = i;
            }
        }
        updateCentroids(number_of_cluster,point);
        System.out.println("Shortest centroid: " + (number_of_cluster+1));
        return shortest;
    }

    public static void updateCentroids(int number_of_cluster, String[] point){
        for(int i = 0; i < 4; i ++) {
            CENTROIDS[number_of_cluster][i] = (CENTROIDS[number_of_cluster][i] * CENTROIDS[number_of_cluster][4] +
            Double.parseDouble(point[i])) / CENTROIDS[number_of_cluster][4] + 1;
        }
        CENTROIDS[number_of_cluster][4] += 1;

//        if(alreadyRead < NUMBER_OF_CLUSTERS) {
//            if (point[4].equals("Iris-virginica")) {
//                number_of_flowers[number_of_cluster][0] += 1;
//                System.out.println("done");
//            } else if (point[4].equals("Iris-versicolor")) {
//                number_of_flowers[number_of_cluster][1] += 1;
//            } else if (point[4].equals("Iris-setosa")) {
//                number_of_flowers[number_of_cluster][2] += 1;
//            }
//        }else {
            if (number_of_flowers[number_of_cluster][0] > number_of_flowers[number_of_cluster][1] &&
                    number_of_flowers[number_of_cluster][0] > number_of_flowers[number_of_cluster][2]) {
                number_of_flowers[number_of_cluster][0] += 1;
                System.out.println("it's IRIS-VIRGINICA");


                //if()
            } else if (number_of_flowers[number_of_cluster][1] > number_of_flowers[number_of_cluster][0] &&
                    number_of_flowers[number_of_cluster][1] > number_of_flowers[number_of_cluster][2]) {
                number_of_flowers[number_of_cluster][1] += 1;
                System.out.println("it's IRIS-VERSICOLOR");

            } else {
                number_of_flowers[number_of_cluster][2] += 1;
                System.out.println("it's IRIS-SETOSA");

            }
//        }

        for (int i1 = 0; i1 < NUMBER_OF_CLUSTERS; i1++) {
            System.out.println("Updated centroid: " + (i1+1) + " " + Arrays.toString(CENTROIDS[i1]));
        }
    }

    public static void doTraining(){
        for(int i = alreadyRead; i < rowList.size(); i++){
            calculateDistance(rowList.get(i));
        }
        alreadyRead = rowList.size();
    }

    public static void doTest(){
        for(int i = 0; i < rowList.size(); i++){
            System.out.println(calculateDistance(rowList.get(i)));

            System.out.println();
        }
    }
}