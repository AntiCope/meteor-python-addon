package cloudburst.pythonaddon.pyclasses;

import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.orbit.listeners.ConsumerListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.python.core.*;


public class PyListener {
    private static final Logger LOG = LogManager.getLogger();

    public static void addListener(PyFunction function, Class<?> type) {
        MeteorClient.EVENT_BUS.subscribe(new ConsumerListener<>(type, event -> {
            try {
                function.__call__(Py.java2py(event));
            } catch (PyException e) {
                LOG.error(e);
            }
        }));
    }
}
