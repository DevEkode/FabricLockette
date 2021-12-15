package fr.ekode.fabriclockette.core;

import fr.ekode.fabriclockette.utils.FabricLogger;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Properties;

/**
 * https://mkyong.com/java/java-read-a-file-from-resources-folder/.
 */
public class FileResourcesUtils {
    /**
     * Get a file from the resources folder.
     * works everywhere, IDEA, unit test and JAR file.
     *
     * @param fileName ressource file name
     * @return The ressource input stream
     */
    public InputStream getFileFromResourceAsStream(final String fileName) {

        // The class loader that loaded the class
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }

    }

    /*

     */

    /**
     * The resource URL is not working in the JAR.
     * If we try to access a file that is inside a JAR,
     * It throws NoSuchFileException (linux), InvalidPathException (Windows)
     * Resource URL Sample: file:java-io.jar!/json/file1.json
     *
     * @param fileName Ressource file name
     * @return Ressource file
     * @throws URISyntaxException
     */
    public File getFileFromResource(final String fileName) throws URISyntaxException {

        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return new File(resource.toURI());
        }

    }

    /**
     * Print input stream.
     *
     * @param is InputStream to print
     */
    public static void printInputStream(final InputStream is) {

        try (InputStreamReader streamReader =
                     new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {

            String line;
            while ((line = reader.readLine()) != null) {
                FabricLogger.logError(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Print a file.
     *
     * @param file File to print
     */
    public static void printFile(final File file) {

        List<String> lines;
        try {
            lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            for(String line : lines){
                FabricLogger.logInfo(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Return the properties of a .properties file.
     * @param fileName properties file name
     * @return Properties of the file
     * @throws IOException
     */
    public static Properties readPropertiesFile(final String fileName) throws IOException {
        Properties prop = null;
        try(FileInputStream fis = new FileInputStream(fileName);) {
            prop = new Properties();
            prop.load(fis);
        } catch (IOException fnfe) {
            fnfe.printStackTrace();
        }
        return prop;
    }
}
