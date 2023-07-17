package edu.umd.lib.dspace.content.authority;

import org.dspace.content.authority.ChoiceAuthority;
import org.dspace.content.authority.Choices;

public interface SuggestService {
    /**
     * Returns the Choices given query text, or an empty Choices object if
     * there are not results, or an error occurs.
     */
    public Choices search(String queryText, int start, int limit);

    /**
     * Configures this service instance using the given authorityName and
     * ChoiceAuthority.
     *
     * @param authorityName the name used to retrive the Solr dictionary to
     * associated with this instance of the service.
     * @param choiceAuthority the ChoiceAuthority that is using this service.
     */
    public void configure(String authorityName, ChoiceAuthority choiceAuthority);
}
