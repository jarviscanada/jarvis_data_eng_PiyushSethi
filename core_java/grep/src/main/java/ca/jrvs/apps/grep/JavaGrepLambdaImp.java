package ca.jrvs.apps.grep;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class JavaGrepLambdaImp extends JavaGrepImp {
    final Logger logger = LoggerFactory.getLogger(JavaGrepLambdaImp.class);
    public static void main(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("Usage: JavaGrep regex rootPath outFile");
        }
        BasicConfigurator.configure();

        JavaGrepLambdaImp javaGrepLambdaImp = new JavaGrepLambdaImp();
        javaGrepLambdaImp.setRegex(args[0]);
        javaGrepLambdaImp.setRootPath(args[1]);
        javaGrepLambdaImp.setOutFile(args[2]);
        try {
            javaGrepLambdaImp.process();
        } catch (IOException ex) {

            javaGrepLambdaImp.logger.error("Error: Unable to process", ex);
        }
    }
    @Override
    public List<String> readLines(File inputFile) {
        List<String> lines = new ArrayList<>();
        try(Stream<String> lineStream = Files.lines(inputFile.toPath()))
        {
            lines = lineStream.collect(Collectors.toList());
        }
        catch (IOException | UncheckedIOException e){
            logger.error("Error reading file " + inputFile.getName(), e);
        }
        return lines;
    }

    @Override
    public List<File> listFiles(String rootPath) {
        List<File> files = new ArrayList<>();
        try (Stream<Path> pathStream = Files.walk(Paths.get(rootPath))) {
            files = pathStream
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .filter(file -> !isBinaryFile(file))
                    .collect(Collectors.toList());
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
        return files;
    }

    /**
     * Utility method to check if a file is binary.
     * @param file the file to check
     * @return true if the file is binary, false otherwise
     */
    private boolean isBinaryFile(File file) {
        try (InputStream is = new FileInputStream(file)) {
            int size = Math.min(is.available(), 1024); // Check first 1KB
            byte[] buffer = new byte[size];
            is.read(buffer);
            for (byte b : buffer) {
                if (b < 0x09 || (b > 0x0D && b < 0x20) && b != 0x7F) {
                    return true; // Non-printable character detected
                }
            }
            return false;
        } catch (IOException ex) {
            logger.error("Error checking file type: " + file.getName(), ex);
            return true; // Treat as binary if an error occurs
        }
    }

}