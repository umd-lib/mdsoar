package edu.umd.lib.dspace.content.authority;

import org.apache.commons.cli.Options;
import org.dspace.scripts.configuration.ScriptConfiguration;

public class UmdSolrRebuildSuggestersScriptConfiguration<T extends UmdSolrRebuildSuggestersClient>
    extends ScriptConfiguration<T> {

    private Class<T> dspaceRunnableClass;

    @Override
    public Class<T> getDspaceRunnableClass() {
        return dspaceRunnableClass;
    }

    @Override
    public Options getOptions() {
        return new Options();
    }

    /**
     * Generic setter for the dspaceRunnableClass
     * @param dspaceRunnableClass   The dspaceRunnableClass to be set
     */
    @Override
    public void setDspaceRunnableClass(Class<T> dspaceRunnableClass) {
        this.dspaceRunnableClass = dspaceRunnableClass;
    }
}
