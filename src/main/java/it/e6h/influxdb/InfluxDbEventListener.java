package it.e6h.influxdb;

import com.influxdb.client.write.events.AbstractWriteEvent;
import com.influxdb.client.write.events.EventListener;
import com.influxdb.client.write.events.WriteSuccessEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfluxDbEventListener implements EventListener<AbstractWriteEvent> {
    private static Logger logger = LoggerFactory.getLogger(InfluxDbEventListener.class);


    @Override
    public void onEvent(AbstractWriteEvent event) {
        synchronized (this) {
            logger.debug(Constants.LOG_MARKER, "InfluxDB write event received: " + event);

            if(event instanceof WriteSuccessEvent) {
                String data = ((WriteSuccessEvent) event).getLineProtocol();
                long n = data.chars().filter(ch -> ch == '\n').count() + 1;
                logger.info(Constants.LOG_MARKER, String.format("Successfully written %d InfluxDB points", n));
            }

            this.notify();
        }
    }
}
