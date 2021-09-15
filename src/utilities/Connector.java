package utilities;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;

public class Connector {
    SocketAddress a;
    private final Logger logger = (Logger) LoggerFactory.getLogger(Connector.class);
    public DatagramChannel connect(int port) {
        try {
            a = new InetSocketAddress(port);
            logger.info("Port is set - Connector.connect()");
            DatagramChannel s = DatagramChannel.open();
            s.bind(a);
            logger.info("DatagramChannel is opened - Connector.connect()");
            return s;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public SocketAddress getA() {
        return a;
    }
}
