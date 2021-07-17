import com.vgu.cs.common.logger.VLogger;
import org.apache.logging.log4j.Logger;

public class VLoggerTest {

    private static final Logger LOGGER = VLogger.getLogger(VLoggerTest.class);

    public static void main(String[] args) {
        LOGGER.error("hehe");
    }
}
