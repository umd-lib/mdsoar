# MD-SOAR Customizations

## Introduction

This document provides information and links to the customizations made to
the stock DSpace code for MD-SOAR.

## Major Customizations

Major customizations have their own documents:

* [MdsoarDOI.md](./MdsoarDOI.md)
* [SubmissionForm.md](./SubmissionForm.md)
* Community Themes - See the [umd-lib/medsoar-angular](https://github.com/umd-lib/mdsoar-angular)
  documentation

## Minor Customizations

The following customizations were made to the DSpace configuration settings in
the "dspace/config/local.cfg.EXAMPLE" file, to override the default settings in
the stock DSpace configuration files.

* `usage-statistics.logBots` - ("false") Disable logging of spiders/bots in Solr
  statistics.

* `usage-statistics.authorization.admin.usage` - Do not show "Statistics" menu
  entry in the navbar for non-admins users.
