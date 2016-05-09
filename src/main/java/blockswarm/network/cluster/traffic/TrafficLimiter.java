package blockswarm.network.cluster.traffic;

import io.netty.channel.ChannelHandler;
import io.netty.handler.traffic.GlobalTrafficShapingHandler;
import io.netty.util.concurrent.EventExecutorGroup;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javax.swing.Timer;
import net.tomp2p.connection.PipelineFilter;
import net.tomp2p.utils.Pair;

/**
 *
 * @author cal
 */
public class TrafficLimiter
{

    private final GlobalTrafficShapingHandler traffic;
    private final PipelineFilter pipelineFilter;

    public TrafficLimiter()
    {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
        traffic = new GlobalTrafficShapingHandler(executor, 100 * 1000, 100 * 1000, 60 * 1000);
        pipelineFilter = new PipelineFilter()
        {
            @Override
            public Map<String, Pair<EventExecutorGroup, ChannelHandler>> filter(Map<String, Pair<EventExecutorGroup, ChannelHandler>> map, boolean bln, boolean bln1)
            {
                Map<String, Pair<EventExecutorGroup, ChannelHandler>> retVal = new LinkedHashMap<>();
                retVal.put("traffic", new Pair<>(null, traffic));
                retVal.putAll(map);
                return retVal;
            }
        };

        //Debugging!
        new Timer(1000, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println(traffic.trafficCounter().toString());
            }
        }).start();
    }

    public GlobalTrafficShapingHandler getTrafficHandler()
    {
        return traffic;
    }
    
    public void setWriteLimit(int speed)
    {
        traffic.setWriteLimit(speed);
    }
    
    public void setReadLimit(int speed)
    {
        traffic.setReadLimit(speed);
    }

    public PipelineFilter getPipelineFilter()
    {
        return pipelineFilter;
    }
}
