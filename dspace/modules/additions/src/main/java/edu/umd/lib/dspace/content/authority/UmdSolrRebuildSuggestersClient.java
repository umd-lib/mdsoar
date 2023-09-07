package edu.umd.lib.dspace.content.authority;

import java.io.IOException;

import org.apache.commons.cli.ParseException;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.dspace.scripts.DSpaceRunnable;
import org.dspace.service.impl.HttpConnectionPoolService;
import org.dspace.services.ConfigurationService;
import org.dspace.services.factory.DSpaceServicesFactory;
import org.dspace.utils.DSpace;

/**
 * Sends a request to Solr to rebuild all Solr suggester indexes.
 */
public class UmdSolrRebuildSuggestersClient extends DSpaceRunnable<UmdSolrRebuildSuggestersScriptConfiguration> {
    @Override
    public UmdSolrRebuildSuggestersScriptConfiguration getScriptConfiguration() {
        return new DSpace().getServiceManager().getServiceByName("umd-solr-rebuild-suggesters",
        UmdSolrRebuildSuggestersScriptConfiguration.class);
    }

    @Override
    public void setup() throws ParseException {
    }

    @Override
    public void internalRun() throws Exception {
        ConfigurationService configurationService
            = DSpaceServicesFactory.getInstance().getConfigurationService();
        String solrServerUrl = configurationService.getProperty("solr.suggest.server");

        HttpSolrClient solrServer = getSolrClient(solrServerUrl);
        SolrQuery solrQuery = createSolrQuery();

        QueryResponse response = sendRequest(solrServer, solrQuery);
        logResult(response);
    }

    protected HttpSolrClient getSolrClient(String solrServerUrl) {
        HttpConnectionPoolService httpConnectionPoolService
            = DSpaceServicesFactory.getInstance()
                .getServiceManager()
                .getServiceByName("solrHttpConnectionPoolService",
                                    HttpConnectionPoolService.class);

        HttpSolrClient solrServer = new HttpSolrClient.Builder(solrServerUrl)
            .withHttpClient(httpConnectionPoolService.getClient())
            .withBaseSolrUrl(solrServerUrl)
            .build();

        return solrServer;
    }

    /**
     * Sends the given SolrQuery to the Solr server via the given HttpSolrClient
     * and returns the QueryResponse.
     *
     * @param solrServer the HttpSolrClient to sent the request to
     * @param solrQuery the SolrQuery to send
     * @return the QueryResponse from the Solr server
     * @throws SolrServerException
     * @throws IOException
     */
    protected QueryResponse sendRequest(HttpSolrClient solrServer, SolrQuery solrQuery)
        throws SolrServerException, IOException {
        handler.logInfo(
            "Sending suggest.buildAll request to server: " + solrServer.getBaseURL() +
            ", requestHandler: " + solrQuery.getRequestHandler()
        );

        return solrServer.query(solrQuery);
    }

    /**
     * Creates the Solr query requesting that all Solr suggesters be rebuilt.
     *
     * @return a SolrQuery requesting that all Solr suggesters be rebuilt.
     */
    protected SolrQuery createSolrQuery() {
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setRequestHandler(SolrSuggestService.REQUEST_HANDLER);
        solrQuery.setParam("suggest.buildAll", "true");
        return solrQuery;
    }

    /**
     * Parses the given QueryResponse, outputting a string representation of
     * the response and a success/failure message via the handler.
     *
     * @param response the QueryResponse to evaluate
     */
    protected void logResult(QueryResponse response) {
        handler.logInfo("Solr response=" + response);
        if (response.getStatus() == 0) {
            handler.logInfo("SUCCESS.");
        } else {
            handler.logError("FAILED. An error as occurred");
        }
    }
}
