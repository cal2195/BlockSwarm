package blockswarm.blocksites;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersAdapter;
import org.littleshoot.proxy.HttpFiltersSourceAdapter;
import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;

/**
 *
 * @author cal
 */
public class ProxyServer
{

    HttpProxyServer server;
    int PORT = 8080;

    public ProxyServer()
    {
        startServer();
    }

    public void startServer()
    {
        server = DefaultHttpProxyServer.bootstrap().withPort(PORT).withFiltersSource(new HttpFiltersSourceAdapter()
        {
            public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext ctx)
            {
                return new HttpFiltersAdapter(originalRequest)
                {
                    @Override
                    public HttpResponse clientToProxyRequest(HttpObject httpObject)
                    {
                        return handleRequest(originalRequest);
                    }

                    @Override
                    public HttpObject serverToProxyResponse(HttpObject httpObject)
                    {
                        // TODO: implement your filtering here
                        return httpObject;
                    }
                };
            }
        }).start();
    }

    private HttpResponse handleRequest(HttpRequest originalRequest)
    {
        URI uri = null;
        try
        {
            uri = new URI(originalRequest.getUri());
        } catch (URISyntaxException ex)
        {
            Logger.getLogger(ProxyServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        String host = uri.getHost(), path = "site" + ((uri.getPath().equals("/")) ? "/index.html" : uri.getPath());
        System.out.println(host + " : " + path);
        
        ByteBuf buffer = Unpooled.wrappedBuffer(getFile(host, path));
        
        HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buffer);
        HttpHeaders.setContentLength(response, buffer.readableBytes());
        HttpHeaders.setHeader(response, HttpHeaders.Names.CONTENT_TYPE, "text/html");
        HttpHeaders.setHeader(response, HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
        return response;
    }
    
    private byte[] getFile(String host, String path)
    {
        try
        {
            return Files.readAllBytes(Paths.get(path));
        } catch (IOException ex)
        {
            Logger.getLogger(ProxyServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
