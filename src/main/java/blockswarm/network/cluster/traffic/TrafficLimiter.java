package blockswarm.network.cluster.traffic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javax.swing.Timer;

/**
 *
 * @author cal
 */
public class TrafficLimiter
{

//    private final GlobalTrafficShapingHandler traffic;
//    private final PipelineFilter pipelineFilter;
//    private final ScheduledExecutorService executor;

    public TrafficLimiter()
    {
//        executor = Executors.newScheduledThreadPool(20);
//        traffic = new GlobalTrafficShapingHandler(executor, 60 * 1000);
//        pipelineFilter = new PipelineFilter()
//        {
//            @Override
//            public Map<String, Pair<EventExecutorGroup, ChannelHandler>> filter(Map<String, Pair<EventExecutorGroup, ChannelHandler>> map, boolean bln, boolean bln1)
//            {
//                Map<String, Pair<EventExecutorGroup, ChannelHandler>> retVal = new LinkedHashMap<>();
//                retVal.put("traffic", new Pair<>(null, traffic));
//                retVal.putAll(map);
//                return retVal;
//            }
//        };
    }
//
//    public void shutdown()
//    {
//        executor.shutdownNow();
//    }
//
//    public GlobalTrafficShapingHandler getTrafficHandler()
//    {
//        return traffic;
//    }
//
//    public void setWriteLimit(int speed)
//    {
//        traffic.setWriteLimit(speed * 1024);
//    }
//
//    public void setReadLimit(int speed)
//    {
//        traffic.setReadLimit(speed * 1024);
//    }
//
//    public PipelineFilter getPipelineFilter()
//    {
//        return pipelineFilter;
//    }
}
