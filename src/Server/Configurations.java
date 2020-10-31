package Server;
import algorithms.mazeGenerators.EmptyMazeGenerator;
import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.mazeGenerators.SimpleMazeGenerator;
import algorithms.search.*;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Properties;

public class Configurations {

    private static Properties prop = new Properties();
    private static String path = System.getProperty("user.dir") + "\\resources\\config.properties";


    public static void setSearchingAlgorithm(String algorithm) {

        try {
            FileInputStream in = new FileInputStream(path);
            Properties props = new Properties();
            props.load(in);
            in.close();

            FileOutputStream out = new FileOutputStream(path);
            props.setProperty("IMazeGenerator", algorithm);
            props.store(out, null);
            out.close();

            
        } catch (java.lang.Exception exception) {
            exception.printStackTrace();
        }
    }

    public static ISearchingAlgorithm getSearchingAlgorithm() {

        try (InputStream input = new FileInputStream(path)) {
            prop.load(input);

        } catch (IOException e) {
            e.printStackTrace();
        }
            String algorithm = prop.getProperty("ISearchingAlgorithm");

            if (algorithm.equals("bfs"))
                return new BreadthFirstSearch();
            else if (algorithm.equals("best"))
                return new BestFirstSearch();
            else
                return new DepthFirstSearch();
    }

    public static void setMaxThreads(String maxThreads){

        try {
            FileInputStream in = new FileInputStream(path);
            Properties props = new Properties();
            props.load(in);
            in.close();

            FileOutputStream out = new FileOutputStream(path);
            props.setProperty("MaxThread", maxThreads);
            props.store(out, null);
            out.close();

        } catch (java.lang.Exception exception) {
            exception.printStackTrace();
        }
    }

    public static int getMaxThreads() {

        try (InputStream input = new FileInputStream(path)) {
            prop.load(input);

        } catch (IOException e) {
            e.printStackTrace();
        }

        String max_threads = prop.getProperty("MaxThread");
        return  Integer.parseInt(max_threads);
    }
    public static void setGenerateAlgorithm(String generateAlgorithm){

        try {
            FileInputStream in = new FileInputStream(path);
            Properties props = new Properties();
            props.load(in);
            in.close();

            FileOutputStream out = new FileOutputStream(path);
            props.setProperty("IMazeGenerator", generateAlgorithm);
            props.store(out, null);
            out.close();

        } catch (java.lang.Exception exception) {
            exception.printStackTrace();
        }
    }

    public static IMazeGenerator getGenerateAlgorithm() {

        try (InputStream input = new FileInputStream(path)) {
            prop.load(input);

        } catch (IOException e) {
            e.printStackTrace();
        }
        String mazeGenerator = prop.getProperty("IMazeGenerator");
        System.out.println(mazeGenerator);
        if(mazeGenerator.equals("MyMazeGenerator"))
           return new MyMazeGenerator();
        else if(mazeGenerator.equals("SimpleMazeGenerator"))
            return new SimpleMazeGenerator();
        else
           return new EmptyMazeGenerator();
    }
}