package at.ac.tuwien.sepm.groupphase.backend.util;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetProperties {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public Properties readProperties() {
        LOG.trace("readProperties()");
        Properties prop = new Properties();
        try {
            // load a properties file from class path, inside static method
            prop.load(GetProperties.class.getClassLoader().getResourceAsStream("config.properties"));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return prop;

    }
}
