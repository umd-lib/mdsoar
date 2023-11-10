# MD-SOAR Customizations

## Introduction

This document provides information and links to the customizations made to
the stock DSpace code for MD-SOAR.

## Major Customizations

Major customizations have their own documents:

* [DataCiteDOI.md](./DataCiteDOI.md)
* [SimpleItemPage.md](./SimpleItemPage.md)
* [SolrAutosuggest.md](./SolrAutosuggest.md)
* [SubmissionForm.md](./SubmissionForm.md)
* Community Themes - See the [umd-lib/mdsoar-angular](https://github.com/umd-lib/mdsoar-angular)
  documentation

## Minor Customizations

### dspace/config/local.cfg.EXAMPLE

The following settings were added to the "dspace/config/local.cfg.EXAMPLE" file,
to override default settings in the stock DSpace configuration files.

* `eperson.subscription.onlynew` - Only send subscription emails for new items

* `usage-statistics.logBots` - Disable logging of spiders/bots in Solr
  statistics.

* Modified `webui.browse.index.<n>` entries, adding the ability to browse by
  "Type".

* `webui.user.assumelogin` - Enabled administrators to impersonate non-admin
    users

* `org.dspace.content.Collection.findAuthorizedPerformanceOptimize` - Enabling
  this property significantly speeds up the display of the submission form
  for non-admins when selecting the “Submit item to MD-SOAR” link on the
  home page. See the description for this property in "dspace/config/dspace.cfg"
  for caveats on enabling this property (which do not apply to MD-SOAR).

* Only system admins should be able to permanently delete items, so the
  following properties were added:
  * `core.authorization.community-admin.item.delete`
  * `core.authorization.collection-admin.item.delete`

  Note: This also means that only system admins can move items to another
  collection.

### Email Templates

The email templates in the "dspace/config/emails/" directory were modified to
replace "DSpace" with "MD-SOAR".

### Google Analytics

MD-SOAR uses stock DSpace Google Analytics 4 functionality to track site usage,
including file/bitstream downloads (see
<https://wiki.lyrasis.org/display/DSDOC7x/DSpace+Google+Analytics+Statistics>).

In order to track file downloads, the following properties must be set in
the "dspace/config/local.cfg" file:

* google.analytics.key
* google.analytics.buffer
* google.analytics.cron
* google.analytics.api-secret
