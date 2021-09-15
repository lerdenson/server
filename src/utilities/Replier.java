package utilities;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class Replier {
    private final Logger logger = (Logger) LoggerFactory.getLogger(Replier.class);
    public void send(DatagramChannel channel, Container container) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(container.getObject());
            out.flush();
            byte[] byteArray = bos.toByteArray();
            ByteBuffer buffer = ByteBuffer.wrap(byteArray);
            logger.debug("Message is wrapped - Replier.send()");
            channel.send(buffer, container.getAddress());
            logger.info("Message is sent - Replier.send()");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
