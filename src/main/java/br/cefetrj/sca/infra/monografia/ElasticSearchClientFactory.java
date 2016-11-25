package br.cefetrj.sca.infra.monografia;

import com.google.common.util.concurrent.SettableFuture;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexandre Vicente on 23/09/16.
 */
public class ElasticSearchClientFactory {
    private static InetSocketTransportAddress[] addresses;

    public static TransportClient createClient() throws UnknownHostException{
       Settings settings = Settings.settingsBuilder().put("cluster.name", "elasticsearch_xandy").build();

        return TransportClient.builder().settings(settings).build()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
    }


}
