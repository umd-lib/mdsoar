# Solr Autosuggest

## Introduction

This page describes the Solr autosuggest functionality for MD-SOAR.

## Background

This functionality is implemented as a "ChoiceAuthority" plugin based on the
stock DSpace authority control functionality. See
<https://wiki.lyrasis.org/display/DSPACE/Authority+Control+of+Metadata+Values>
for more information.

This implementation only performs "choice management", i.e., it generates a
list of possible choices. It does *not* implement "authority control", as no
authority key is provided with the chosen value.

The suggestions are provided by the Solr "suggester" functionality. See
<https://solr.apache.org/guide/8_11/suggester.html> for more information.

## Behavior

Provides suggestions for the "Format" (dc.genre) and "Subject Keywords"
(dc.subject) fields on the submission form from the "search" Solr core, based
on the text typed in the fields by the user.

Both the "Format" and "Subject Keywords" fields are "open", in that the user
may type in any value (i.e., is not required to choose one of the suggested
values).

Note that user-entered values will not appear immediately in future suggestions,
as the Solr suggester indexes must be rebuilt to incorporate them. While Solr
has a "buildOnCommit" option, which automatically rebuilds the Solr suggester
indexes when an item is committed to Solr, experience with the submission form
in DSpace 7.5 has shown that this setting has a debilitating performance impact,
and so should not be used.

A "umd-solr-rebuild-suggesters" DSpace script (see below) has been provided to
trigger a rebuild of the indexes, and should be run periodically via cron.

## Configuration

### Solr configuration

The Solr "search" core is configured with two "suggester" search components in
the `dspace/solr/search/conf/solrconfig.xml` file.

The "formatSuggest" suggester looks up entries in the "dc.genre" field, while
the "subjectSuggest" suggester looks up entries in the "dc.subject" field. Both
suggesters use the "BlendedInfixLookupFactory" as the lookup implementation
as it seems to give the best results (compared to the
"AnalyzingInfixLookupFactory" used in DSpace 6 MD-SOAR which seemed to give
duplicate suggestions).

A "suggest" request handler is also added to the Solr "search" core for use in
retrieving suggestions. This can be tested directly using `curl`. For example,
to search the "formatSuggest" suggester (i.e., the "dc.genre" field) for entries
with "book":

```zsh
$ curl 'http://localhost:8983/solr/search/suggest?indent=true&q.op=OR&suggest.dictionary=formatSuggest&suggest.q=book&wt=json&highlight=false'
```

**Note:** In Kubernetes, the "solrconfig.xml" files for each Solr core are part
of the "mdsoar-solr" persistent volume. This means that any changes to the
`dspace/solr/search/conf/solrconfig.xml` will *not* be picked up, unless the
Solr persistent volume is destroyed (which, because of the Solr "statistics"
core is not advisable). Therefore, any changes made to this file that are not
present in Kubernetes must be handled manually, either via the
`k8s-stack-utils/volume-helper` script, or by copying the "solrconfig.xml" to
the "mdsoar-solr" pod, using `kubectl cp`, and then restarting Solr:

```zsh
# In the appropriate Kubernetes namespace
$ kubectl cp dspace/solr/search/conf/solrconfig.xml mdsoar-solr-0:/var/solr/data/search/conf/solrconfig.xml
$ kubectl delete pod mdsoar-solr-0
```

### DSpace configuration

Two files require configuration for this functionality:

* `dspace/modules/additions/src/main/resources/spring/spring-dspace-addon-umd-custom-services.xml`
* `dspace/config/local.cfg`

The `spring-dspace-addon-umd-custom-services.xml` file uses Spring to configure
the "edu.umd.lib.dspace.content.authority.SuggestService" interface with the
"edu.umd.lib.dspace.content.authority.SolrSuggestService" implementation.

The `local.cfg` configures the autosuggest functionality for use
with the "Format" and "Subject Keywords" submission form fields. The
`dspace/config/local.cfg.EXAMPLE` contains the appropriate configuration:

```text
# Configuration settings for Solr autosuggest
solr.suggest.server=${solr.server}/${solr.multicorePrefix}search

# "Format" submission form field
choices.plugin.dc.genre = SolrFormatAuthority
choices.presentation.dc.genre = suggest
choices.SolrFormatAuthority.dictionary = formatSuggest

# "Subject Keywords" submission form field
choices.plugin.dc.subject = SolrSubjectAuthority
choices.presentation.dc.subject = suggest
choices.SolrSubjectAuthority.dictionary = subjectSuggest
```

The `solr.suggest.server` property is used to define the Solr server and core
to use for the suggestion lookups. The same Solr server (and core) is used for
both suggestion lookups.

The `choices.plugin.<SCHEMA.ELEMENT>` and
`choices.presentation.<SCHEMA.ELEMENT>` properties are used to configure the
form submission field to use the autosuggest functionality. See
<https://wiki.lyrasis.org/display/DSPACE/Authority+Control+of+Metadata+Values>.

The `choices.<AUTHORITY>.dictionary` properties are used by the
"SolrSuggestService" class to configure the Solr suggester to use for
each field.

The DSpace suggestion endpoint can be tested directly using `curl`. For example,
to search the "formatSuggest" suggester (i.e., the "dc.genre" field) for entries
with "book":

```zsh
$ curl 'http://localhost:8080/server/api/submission/vocabularies/SolrFormatAuthority/entries?filter=book&exact=false'
```

## "umd-solr-rebuild-suggesters" DSpace Script

Both the "Subject Keyword" and "Format" fields allow submitters to add terms
not currently in the Solr suggester indexes. The indexes must, therefore, be
periodically rebuilt to insert these new terms.

A "umd-solr-rebuild-suggesters" DSpace script has been provided to send a
`suggest.buildAll` request to the "suggest" request handler, which triggers
a rebuild of both the "formatSuggest" and "subjectSuggest" suggesters.

This script can be run manually from the DSpace container, using the following
command:

```bash
$ /dspace/bin/dspace umd-solr-rebuild-suggesters
```

### Cron Job

A "umd-solr-rebuild-suggesters" entry should be added to the "crontab.txt" in
Kubernetes, to ensure that the suggesters are periodically rebuilt. For example,
to rebuild the suggesters once a day at 6am:

```text
# Rebuild the Solr suggester indexes, adding any new terms added by users
0 6 * * * root $DSPACE/bin/dspace umd-solr-rebuild-suggesters
```
