package br.cefetrj.sca.dominio;

import br.cefetrj.sca.infra.monografia.ElasticSearchClientFactory;
import com.google.common.util.concurrent.SettableFuture;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;

import java.io.IOException;
import java.util.*;

/**
 * Created by Alexandre Vicente on 04/09/16.
 */
public class TagMonografia implements Comparable {
    private String tag;
    private Long ocorrencias;

    public TagMonografia(String tag, Long ocorrencias) {
        this.tag = tag;
        this.ocorrencias = ocorrencias;
    }

    public String getTag() {
        return tag;
    }

    public Long getOcorrencias() {
        return ocorrencias;
    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof TagMonografia){
            TagMonografia tag = (TagMonografia) o;
            return ocorrencias.compareTo(tag.getOcorrencias());
        } else {
            return 1;
        }
    }

    static public String[] getBlacklist(){
        return new String[]{"a"};
    }

    static public List<TagMonografia> getTopTags(String[] blacklist) throws IOException {
        return getTopTags(400, blacklist);
    }

    static public List<TagMonografia> getTopTags(int amount, String[] blacklist) throws IOException{
        SettableFuture<List<TagMonografia>> future = SettableFuture.create();

        TransportClient client = ElasticSearchClientFactory.createClient();
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("monografias");
        searchRequestBuilder.addFields();

        TermsBuilder aggregationBuilder = new TermsBuilder("tags");
        aggregationBuilder.field("textoPuro");

        aggregationBuilder.exclude(blacklist);

        aggregationBuilder.size(amount);

        searchRequestBuilder.addAggregation(aggregationBuilder);

        searchRequestBuilder.execute(new ActionListener<SearchResponse>() {
            @Override
            public void onResponse(SearchResponse searchResponse) {
                Terms tags = searchResponse.getAggregations().get("tags");
                List<Terms.Bucket> buckets = tags.getBuckets();
                List<TagMonografia> results = new ArrayList<>(buckets.size());

                for(Terms.Bucket bucket : buckets){
                    String tag = bucket.getKeyAsString();
                    long ocorrencias = bucket.getDocCount();
                    results.add(new TagMonografia(tag, ocorrencias));
                }

                synchronized (future){
                    future.set(results);
                    future.notifyAll();
                }
            }

            @Override
            public void onFailure(Throwable e) {
                synchronized (future) {
                    future.setException(e);
                    future.notifyAll();
                }
            }
        });

        List<TagMonografia> response;

        try {
            synchronized (future) {
                future.wait();
                response = future.get();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException(e);
        }

        client.close();
        return response;
    }

    @Override
    public int hashCode() {
        return tag.hashCode() + 2;
    }

    @Override
    public String toString() {
        return tag;
    }

    public String toJSON(){
        return "{" +
                    "\"tag\":\"" + tag + "\"," +
                    "\"ocorrencias\":" + ocorrencias.toString() +
                "}";
    }
}
