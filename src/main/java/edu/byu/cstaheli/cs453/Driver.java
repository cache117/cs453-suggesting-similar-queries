package edu.byu.cstaheli.cs453;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Created by cstaheli on 5/19/2017.
 */
public class Driver
{
    public static void main(String[] args)
    {
        String resourcesDirectory = "src/main/resources";
        Driver driver = new Driver();
        driver.readInQueries(resourcesDirectory);
    }

    private void readInQueries(String directory)
    {
        try (Stream<Path> paths = Files.walk(Paths.get(directory)))
        {
            paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".txt"))
                    .filter(path -> path.toString().contains("Clean-Data-"))
                    .forEach(path ->
                    {
                        String fileName = path.toString();
                    });

        }

        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public Driver()
    {

    }
}
