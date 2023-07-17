package edu.umd.lib.dspace.content.authority;

import org.apache.logging.log4j.Logger;
import org.dspace.content.authority.ChoiceAuthority;
import org.dspace.content.authority.Choices;
import org.dspace.core.NameAwarePlugin;
import org.dspace.services.ConfigurationService;
import org.dspace.services.factory.DSpaceServicesFactory;

/**
 * ChoiceAuthority implementation that returns matches from a SuggestService
 * implementation.
 */
public class SuggestChoiceAuthority implements ChoiceAuthority {
    private Logger log = org.apache.logging.log4j.LogManager.getLogger(SuggestChoiceAuthority.class);

    /**
     * The DSpace configuration service instance
     */
    protected final ConfigurationService configurationService
        = DSpaceServicesFactory.getInstance().getConfigurationService();

    /**
     * The name assigned to the specific instance by the PluginService,
     * @see {@link NameAwarePlugin}
     **/
    protected String authorityName;

    /**
     * The SuggestService implementation to query
     */
    protected SuggestService suggestService;


    @Override
    public Choices getMatches(String text, int start, int limit, String locale) {
        return getMatches(text, start, limit, locale, true);
    }

    @Override
    public Choices getBestMatch(String text, String locale) {
        Choices matches = getMatches(text, 0, 1, locale, false);
        if (matches.values.length != 0 && !matches.values[0].value.equalsIgnoreCase(text)) {
            matches = new Choices(false);
        }
        return matches;
    }

    /**
     * Returns the Choices representing the suggestions for the given query
     * text, which may be empty if there are not matches.
     *
     * @param text the text to use in generating the suggestions
     * @param start the zero-based index of the choice at which to start
     * @param limit the maximum number of choices to return.
     * @return the Choices representing the suggestions for the given query
     * text, which may be empty if there are not matches.
     */
    protected Choices getMatches(String text, int start, int limit, String locale,
        boolean bestMatch) {
        log.debug("Searching for " + text + " in " + authorityName);
        if (limit == 0) {
            limit = 10;
        }

        if (text == null || text.trim().equals("")) {
            // Don't search for blank text, just return that nothing is found.
            return new Choices(Choices.CF_NOTFOUND);
        }

        return suggestService.search(text, start, limit);
    }

    /**
     * This method is required by the "ChoiceAuthority" interface.
     * Since auto-suggest is not an actual authority, this method simply
     * returns the provided "key", which is the text of a suggestion to
     * display to the user.
     *
     * @param key a suggestion returned by Solr for display to the user
     * @param locale ignored
     * @return the given key
     */
    @Override
    public String getLabel(String key, String locale) {
        return key;
    }

    @Override
    public void setPluginInstanceName(String name) {
        configureInstance(name);
    }

    /**
     * Configures this instance based on the given name.
     *
     * @param name the plugin name for this instance.
     */
    protected void configureInstance(String name) {
        authorityName = name;

        org.dspace.kernel.ServiceManager manager = DSpaceServicesFactory.getInstance().getServiceManager();
        suggestService = manager.getServiceByName(SuggestService.class.getName(), SuggestService.class);
        suggestService.configure(authorityName, this);
    }

    @Override
    public String getPluginInstanceName() {
        return authorityName;
    }


    /**
     * Always returns false, because this implementation returns suggestions,
     * not authority records.
     */
    @Override
    public boolean storeAuthorityInMetadata() {
        return false;
    }
}
