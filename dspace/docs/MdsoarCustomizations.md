# MD-SOAR Customizations

## Introduction

This document provides information and links to the customizations made to
the stock DSpace code for MD-SOAR.

## Major Customizations

Major customizations have their own documents:

* [MdsoarDOI.md](./MdsoarDOI.md)
* [SubmissionForm.md](./SubmissionForm.md)
* Community Themes - See the [umd-lib/mdsoar-angular](https://github.com/umd-lib/mdsoar-angular)
  documentation

## Minor Customizations

### dspace/config/local.cfg.EXAMPLE

The following settings were added to the "dspace/config/local.cfg.EXAMPLE" file,
to override default settings in the stock DSpace configuration files.

* `webui.user.assumelogin` - Enabled administrators to impersonate non-admin
    users

* `usage-statistics.logBots` - Disable logging of spiders/bots in Solr
  statistics.

* `usage-statistics.authorization.admin.usage` - Do not show "Statistics" menu
  entry in the navbar for non-admins users.

### Email Templates

The email templates in the "dspace/config/emails/" directory were modified to
replace "DSpace" with "MD-SOAR".


