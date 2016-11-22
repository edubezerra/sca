package br.cefetrj.sca.infra.monografia;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.servlet.ServletContext;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by Alexandre Vicente on 15/11/16.
 */

public class TagExtractor {
    final static List<String> englishTokenTypes = Arrays.asList(new String[]{"NN","NNS","NNP","NNPS"});
    final static List<String> portugueseTokenTypes = Arrays.asList(new String[]{"n","prop"});

    static TagExtractor instance;
    TokenizerME englishTokenizer;
    TokenizerME portugueseTokenizer;
    POSTaggerME englishTagger;
    POSTaggerME portugueseTagger;

    private enum Language {
        english,
        portuguese
    }

    public static TagExtractor getInstance(ServletContext servletContext) throws IOException {
        if(instance == null){
            instance = new TagExtractor(servletContext);
        }
        return instance;
    }

    private TagExtractor(ServletContext servletContext) throws IOException {
        InputStream englishTokenizerIS = servletContext.getResourceAsStream("/WEB-INF/opennlp/en-token.bin");
        TokenizerModel englishTokenizerModel = new TokenizerModel(englishTokenizerIS);
        englishTokenizerIS.close();
        englishTokenizer = new TokenizerME(englishTokenizerModel);

        InputStream englishTaggerIS = servletContext.getResourceAsStream("/WEB-INF/opennlp/en-pos-maxent.bin");
        POSModel englishTaggerModel = new POSModel(englishTaggerIS);
        englishTaggerIS.close();
        englishTagger = new POSTaggerME(englishTaggerModel);

        InputStream portugueseTokenizerIS = servletContext.getResourceAsStream("/WEB-INF/opennlp/pt-token.bin");
        TokenizerModel portugueseTokenizerModel = new TokenizerModel(portugueseTokenizerIS);
        portugueseTokenizerIS.close();
        portugueseTokenizer = new TokenizerME(portugueseTokenizerModel);

        InputStream portugueseModelIS = servletContext.getResourceAsStream("/WEB-INF/opennlp/pt-pos-maxent.bin");
        POSModel portugueseModel = new POSModel(portugueseModelIS);
        portugueseModelIS.close();
        portugueseTagger = new POSTaggerME(portugueseModel);
    }

    public String[] getTokens(Language language, final String string){
        TokenizerME tokenizer = null;

        switch (language){
            case english:
                tokenizer = englishTokenizer;
                break;
            case portuguese:
                tokenizer = portugueseTokenizer;
                break;
        }

        return tokenizer.tokenize(string);
    }

    public Set<String> getNounPhrases(Language language, final String string) {
        String[] tokens = getTokens(language, string);
        List<String> tokenTypes = null;
        POSTaggerME tagger = null;
        Set<String> nounPhrases = new HashSet<>();

        switch (language){
            case english:
                tagger = englishTagger;
                tokenTypes = englishTokenTypes;
                break;
            case portuguese:
                tagger = portugueseTagger;
                tokenTypes = portugueseTokenTypes;
                break;
        }

        String[] tags = tagger.tag(tokens);

        for(int i=0; i<tags.length; i++){
            String tag = tags[i];
            if(tokenTypes.contains(tag)){
                String np = tokens[i];
                if(np.length() > 1){
                    nounPhrases.add(np);
                }
            }
        }

        return nounPhrases;
    }

    public String getTags(final String string){
        Set<String> englishTags = getNounPhrases(Language.english, string);
        Set<String> portugueseTags = getNounPhrases(Language.portuguese, string);
        Set<String> tags = new HashSet<>();

        if(englishTags != null){
            if(portugueseTags == null){
                tags = englishTags;
            } else if(englishTags.size() > portugueseTags.size()){
                tags = portugueseTags;
            } else {
                tags = englishTags;
            }
        } else if(portugueseTags != null) {
            tags = portugueseTags;
        }

        return StringUtils.join(tags, '\n');
    }
}