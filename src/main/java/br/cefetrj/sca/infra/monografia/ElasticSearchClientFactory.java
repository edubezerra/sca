package br.cefetrj.sca.infra.monografia;

import com.google.common.util.concurrent.SettableFuture;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexandre Vicente on 23/09/16.
 */
public class ElasticSearchClientFactory {
    private static boolean initialized = false;

    public static TransportClient createClient() throws IOException {
       Settings settings = Settings.settingsBuilder().put("cluster.name", "elasticsearch").build();

        TransportClient transportClient = TransportClient.builder().settings(settings).build()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));

        if(!initialized){
            try {
                ActionFuture<IndicesExistsResponse> existsFuture = transportClient.admin().indices().exists(
                        transportClient.admin().indices().prepareExists("monografias").request()
                );
                IndicesExistsResponse existsResponse = existsFuture.get();
                if(!existsResponse.isExists()){
                    CreateIndexRequestBuilder createIndexRequestBuilder = transportClient.admin().indices().prepareCreate("monografias");
                    createIndexRequestBuilder.addMapping("monografia", "{\n" +
                            "    \"properties\": {\n" +
                            "        \"autor\": {\n" +
                            "            \"type\": \"string\"\n" +
                            "        },\n" +
                            "        \"titulo\": {\n" +
                            "            \"type\": \"string\"\n" +
                            "        },\n" +
                            "        \"orientador\": {\n" +
                            "            \"type\": \"string\"\n" +
                            "        },\n" +
                            "        \"presidenteBanca\": {\n" +
                            "            \"type\": \"string\"\n" +
                            "        },\n" +
                            "        \"membrosBanca\": {\n" +
                            "            \"type\": \"string\"\n" +
                            "        },\n" +
                            "        \"resumoLinguaEstrangeira\": {\n" +
                            "            \"type\": \"string\"\n" +
                            "        },\n" +
                            "        \"resumoPortugues\": {\n" +
                            "            \"type\": \"string\"\n" +
                            "        },\n" +
                            "        \"tags\": {\n" +
                            "            \"type\": \"string\",\n" +
                            "            \"term_vector\": \"with_positions_offsets\"\n" +
                            "        },\n" +
                            "        \"arquivos\": {\n" +
                            "            \"type\": \"nested\",\n" +
                            "            \"properties\": {\n" +
                            "                \"nome\": {\n" +
                            "                    \"type\": \"string\"\n" +
                            "                },\n" +
                            "                \"tamanho\": {\n" +
                            "                    \"type\": \"long\"\n" +
                            "                },\n" +
                            "                \"conteudo\": {\n" +
                            "                    \"type\": \"attachment\",\n" +
                            "                    \"fields\": {\n" +
                            "                        \"content\": {\n" +
                            "                            \"store\": \"yes\"\n" +
                            "                        },\n" +
                            "                        \"author\": {\n" +
                            "                            \"store\": \"yes\"\n" +
                            "                        },\n" +
                            "                        \"title\": {\n" +
                            "                            \"store\": \"yes\",\n" +
                            "                            \"analyzer\": \"english\"\n" +
                            "                        },\n" +
                            "                        \"date\": {\n" +
                            "                            \"store\": \"yes\"\n" +
                            "                        },\n" +
                            "                        \"keywords\": {\n" +
                            "                            \"store\": \"yes\",\n" +
                            "                            \"analyzer\": \"keyword\"\n" +
                            "                        }\n" +
                            "                    }\n" +
                            "                }\n" +
                            "            }\n" +
                            "        }\n" +
                            "    }\n" +
                            "}");
                    createIndexRequestBuilder.addMapping("blacklist", "{\n" +
                            "    \"properties\": {\n" +
                            "        \"blacklist\": {\n" +
                            "            \"type\": \"string\"\n" +
                            "        }\n" +
                            "    }\n" +
                            "}");
                    transportClient.admin().indices().create(createIndexRequestBuilder.request()).get();
                }
            } catch (Exception e) {
                throw new IOException("Error creating index", e);
            }
        }

        return transportClient;

    }

    public static void main(String args[]) throws IOException {
        createClient();
    }

}
