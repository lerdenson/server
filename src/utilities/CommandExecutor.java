package utilities;


import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import commands.AbstractCommand;
import commands.SaveCommand;
import general.Route;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashSet;

    public class CommandExecutor {
        private final Logger logger = (Logger) LoggerFactory.getLogger(CommandExecutor.class);
    public String executeCommand(AbstractCommand command) {
        try {
            FileInputStream fis = new FileInputStream(new File(command.getFileName()));
            fis.close();
        } catch (FileNotFoundException e) {
            logger.warn("File not found - CommandExecutor.executeCommand()");
            return "File not found - check your env variable TEMP to be equal to the name of the database";
        } catch (IOException e) {
            e.printStackTrace();
        }
        LinkedHashSet<Route> collection = establishCollection(command.getFileName());

        command.execute(collection);
        logger.info("Command executed - CommandExecutor.executeCommand() - " + command.getName());
        new SaveCommand(command.getFileName()).execute(collection);
        logger.debug("Successfully saved");
        return command.getMessage();
    }

    private LinkedHashSet<Route> establishCollection(String filename) {

        File file = new File(filename);

        if (!file.exists()) {
            logger.warn("File not found - CommandExecutor.establishCollection()");
        } else if (!file.canRead()) {
            logger.warn("Unable to read file - CommandExecutor.establishCollection()");
        }

        FileManager fileManager = new FileManager(filename);
        LinkedHashSet<Route> routes = fileManager.loadCollection();
        logger.info("Collection established - CommandExecutor.establishCollection()");
        return routes;
    }
}
