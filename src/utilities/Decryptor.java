package utilities;

import ch.qos.logback.classic.Logger;
import commands.AbstractCommand;
import commands.SaveCommand;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;


public class Decryptor {

    private final Logger logger = (Logger) LoggerFactory.getLogger(Decryptor.class);

    public Decryptor(int port){
        Connector connection = new Connector();
        DatagramChannel channel = connection.connect(port);
        try {
            logger.info("Server established, local adress is " + channel.getLocalAddress().toString() + " - {}", Decryptor.class.getSimpleName());
            channel.configureBlocking(false);
            logger.debug("Blocking configuration is set as false - {}", Decryptor.class.getSimpleName());
        } catch (IOException e) {
            logger.warn("I/O exception occurred - {}", Decryptor.class.getSimpleName());
            e.printStackTrace();
        }

        Container command;
        CommandExecutor executor = new CommandExecutor();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            command = receive(channel, connection.getA());
            try {
                new Replier().send(channel, new Container(executor.executeCommand((AbstractCommand) command.getObject()), command.getAddress()));
            } catch (NullPointerException ignored) {
            }
            try {
                if (reader.ready()) {
                    String commandLine = reader.readLine();
                    String[] line = commandLine.split(" ");
                    if ("save".equals(line[0])) {
                        executor.executeCommand(new SaveCommand(line[1]));
                    } else if ("exit".equals(line[0])) {
                        System.exit(0);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("You should pick a file where to save");
            }
        }
    }


    private Container receive(DatagramChannel channel, SocketAddress a) {
        byte[] byteArray = new byte[1024];
        try {
            ByteBuffer buffer = ByteBuffer.wrap(byteArray);
            a = channel.receive(buffer);
            byteArray = buffer.array();
            ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
            ObjectInput in = new ObjectInputStream(bis);
            Object o = in.readObject();
            logger.info("New command received - Decryptor.receive()");
            return new Container(o, a);
        } catch (StreamCorruptedException ignored) {
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


}
