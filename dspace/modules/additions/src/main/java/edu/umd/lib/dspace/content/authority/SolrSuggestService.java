package edu.umd.lib.dspace.content.authority;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.dspace.content.authority.Choice;
import org.dspace.content.authority.ChoiceAuthority;
import org.dspace.content.authority.Choices;
import org.dspace.service.impl.HttpConnectionPoolService;
import org.dspace.services.ConfigurationService;
import org.dspace.services.factory.DSpaceServicesFactory;

/**
 * SuggestService implementation that uses the Solr "suggester" functionality
 * to generate suggestions.
 */
public class SolrSuggestService implements SuggestService {
    private static final Logger log = LogManager.getLogger(SolrSuggestService.class);
    public static final String REQUEST_HANDLER = "/suggest";

    @Inject @Named("solrHttpConnectionPoolService")
    private HttpConnectionPoolService httpConnectionPoolService;

    protected final ConfigurationService configurationService
        = DSpaceServicesFactory.getInstance().getConfigurationService();


    /**
     * The ChoiceAuthority to use in formatting Choice results
     */
    protected ChoiceAuthority choiceAuthority;

    /**
     * Cached instance of the SolrClient
     */
    protected SolrClient solr = null;

    /**
     * The "suggest dictionary" associated with this service.
     */
    protected String suggestDictionary;

    protected  SolrSuggestService() {
    }

    @Override
    public Choices search(String queryText, int start, int limit) {
        Choices result;
        try {
            int max = 0;
            boolean hasMore = false;

            SolrQuery solrQuery = new SolrQuery();
            solrQuery.setRequestHandler(REQUEST_HANDLER);
            solrQuery.setParam("suggest", "true");
            solrQuery.setParam("suggest.dictionary", suggestDictionary);
            solrQuery.setParam("suggest.q", queryText);

            QueryResponse searchResponse = search(solrQuery);

            Map<String, List<String>> suggestedTerms = searchResponse.getSuggesterResponse().getSuggestedTerms();
            List<String> suggestions = suggestedTerms.get(suggestDictionary);
            List<Choice> choices = new ArrayList<>();
            for (String suggestion: suggestions) {
                Choice choice = choiceAuthority.getChoice(suggestion, null);
                choices.add(choice);
            }

            int confidence;
            if (choices.isEmpty()) {
                confidence = Choices.CF_NOTFOUND;
            } else if (choices.size() == 1) {
                confidence = Choices.CF_UNCERTAIN;
            } else {
                confidence = Choices.CF_AMBIGUOUS;
            }

            result = new Choices(choices.toArray(new Choice[choices.size()]), start,
                                hasMore ? max : choices.size() + start, confidence, hasMore);
        } catch (SolrServerException | IOException e) {
            log.error(
                "Error while retrieving authority values {" +
                "suggestDictionary: " + suggestDictionary +
                ", text: '" + queryText +
                "'}",
                e
            );
            result = new Choices(true);
        }
        return result;
    }

    @Override
    public void configure(String authorityName, ChoiceAuthority choiceAuthority) {
        this.choiceAuthority = choiceAuthority;
        suggestDictionary = configurationService.getProperty("choices." + authorityName + ".dictionary");
    }

    /**
     * Returns the QueryResponse from Solr for the given SolrQuery.
     *
     * @param solrQuery the SolrQuery to perform
     * @return the QueryResponse from Solr, or null if an error occurs.
     */
    protected QueryResponse search(SolrQuery solrQuery)
            throws SolrServerException, MalformedURLException, IOException {
        SolrClient solrClient = getSolrClient();
        return querySolr(solrClient, solrQuery);
    }

    /**
     * Returns a SolrClient, returning null if a SolrClient could not be created.
     *
     * @return a SolrClient, or null if a SolrClient could not be created.
     */
    protected SolrClient getSolrClient() {
        if (solr == null) {
            String solrServerUrl = getSolrServerUrl();
            solr = configureSolrClient(solrServerUrl);
        }

        return solr;
    }


    /**
     * Performs the given SolrQuery against the given SolrCient, returning
     * either the QueryResponse, or null if an error occurs.
     *
     * @param solrClient the SolrClient to query
     * @param solrQuery the SolrQuery to perform
     * @return a QueryResponse, or null if an error occurs.
     */
    protected QueryResponse querySolr(SolrClient solrClient, SolrQuery solrQuery)
            throws SolrServerException, IOException {
        if (solrClient == null ) {
            return null;
        }
        return solrClient.query(solrQuery);
    }

    /**
     * Returns the base Solr URL to use in performing queries, or null if
     * the "solr.suggest.server" property is not provided.
     *
     * @return the base Solr URL to use in performing queries, or null if
     * the "solr.suggest.server" property is not provided.
     */
    protected String getSolrServerUrl() {
        ConfigurationService configurationService
                = DSpaceServicesFactory.getInstance().getConfigurationService();
        String solrServerUrl = configurationService.getProperty("solr.suggest.server");
        return solrServerUrl;
    }

    protected SolrClient configureSolrClient(String solrServerUrl) {
        log.debug("Solr suggest URL: " + solrServerUrl);
        if (solrServerUrl == null) {
            return null;
        }

        HttpSolrClient solrServer = new HttpSolrClient.Builder(solrServerUrl)
                .withHttpClient(httpConnectionPoolService.getClient())
                .withBaseSolrUrl(solrServerUrl)
                .build();

        if (canConnect(solrServer)) {
            return solrServer;
        }

        log.error("Cannot connect to Solr suggest URL: " + solrServerUrl);
        return null;
    }

    /**
     * Returns true if a Solr connection can be made, false otherwise.
     *
     * @param solrServer the HttpSolrClient to use in making the connection
     * @return true if a Solr connection can be made, false otherwise.
     */
    protected boolean canConnect(HttpSolrClient solrServer) {
        SolrQuery solrQuery = new SolrQuery().setQuery("*:*");

        try {
            solrServer.query(solrQuery);
        } catch (Exception ex) {
            log.error("An error occurred querying the solr suggest server: '"  + solrServer.getBaseURL() + "'", ex);
            return false;
        }
        return true;
    }
}
