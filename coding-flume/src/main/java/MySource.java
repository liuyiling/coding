import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.PollableSource;
import org.apache.flume.conf.Configurable;
import org.apache.flume.event.SimpleEvent;
import org.apache.flume.source.AbstractSource;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuyl on 16/4/12.
 * flume的定制化source例子
 */
public class MySource extends AbstractSource implements Configurable, PollableSource{

    private String myProp;

    @Override
    public void configure(Context context) {
        String myProp = context.getString("myProp", "defaultValue");

        this.myProp = myProp;
    }

    @Override
    public synchronized void start() {
        super.start();
    }

    @Override
    public synchronized void stop() {
        super.stop();
    }

    @Override
    public Status process() throws EventDeliveryException {
        Status status = null;

        try{
            Event event = new SimpleEvent();
            Map map = new HashMap<>();
            map.put("type", "simple-evnet");
            event.setHeaders(map);
            String body = "event body";
            event.setBody(body.getBytes());
            getChannelProcessor().processEvent(event);

            status = Status.READY;
        } catch (Throwable t){

            status = Status.BACKOFF;

            if( t instanceof Error){
                throw (Error)t;
            }
        } finally {
        }

        return status;
    }
}
