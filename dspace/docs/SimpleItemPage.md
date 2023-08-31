# Simple Item Page

## Introduction

This page describes the customizations made to the MD-SOAR "simple item"
page display.

## Changes to DSpace 6 MD-SOAR

The following changes were made when migrating the DSpace 6 MD-SOAR
configuration to DSpace 7, largely based on discrepancies between the
specifications in the Jira issues and the implementation in the
DSpace 6 MD-SOAR.

### "Author/Creator" Field

The original specification in LIBCIR-78 lists using both the
"dc.contributor.author" and "dc.creator" metadata values for the
"Author/Creator" field.

When the "Author/Creator ORCID" field was added in LIBCIR-263, the "dc.creator"
field was removed from the "Author/Creator" field, likely in error (as the
"Author/Creator ORCID" uses the similarly named, but different,
"dcterms.creator" metadata value).

In DSpace 7 MD-SOAR, the "Author/Creator" field uses both the
"dc.contributor.author" and "dc.creator" metadata values.

### "Type of Work" Field

In the DSpace 6 MD-SOAR, the "Type of Work" field would display the
"dc.format.extent" metadata values. As these field values are usually simple
numbers such as "176" or file metadata information such as
"File Size: 100689 Bytes", they do not seem to be genuine "types of work".

This behavior is likely a result of an error in the field handling logic
where the "dc.format" metadata is (correctly) included, but descendant metadata
such as "dc.format.extent" was not excluded.

In DSpace 7 MD-SOAR, the "dc.format.extent" metadata in *not* included in the
"Type of Work" field.

Additionally, the "Type of Work" field was not hyperlinked ("crosslinked") to
the "Browsing by Type" index in DSpace 6 MD-SOAR, but is hyperlinked in
DSpace 7 MD-SOAR.

### "Metadata" Field

The "Metadata" field in the left sidebar, which provided a
"Show full item record" hyperlink to the "full item" page, was replaced
(without a "Metadata" label) by the "Full item page" button in
Dspace 7 MD-SOAR, to better conform to the DSpace 7 look-and-feel.

## Simple Item Page Layout

### Left sidebar

* Thumbnail (No label)
  * `<From the DSpace object>`
* Files
  * `<Links to all files in the DSpace object>`
* Links to Files
  * dc.description.uri
* Permanent Link
  * dc.identifier.uri
* Collections
  * `<From DSpace>`
* Metadata (No Label)
  * `<Link to “full item record” page>`
  * **Note:** DSpace 7 provides a "Full item page" button, which is used in
    place of the "Metadata" label and link.

### Center of Page

* Author/Creator
  * dc.creator
  * dc.contributor.author
* Author/Creator ORCID
  * dcterms.creator
* Date
  * dc.date
  * dc.date.issued
  * dc.date.created
  * dc.date.copyright
  * dctermscreated
  * dctermsdate
  * dcterms.dateAccepted
  * dcterms.dateCopyrighted
* Type of Work
  * dc.genre
  * dc.type
  * dc.format
  * dcterms.format
* Department
  * dc.contributor.department
* Program
  * dc.contributor.program
* Citation of Original Publication
  * dc.identifier.citation
  * dcterms.bibiographicCitation
* Rights
  * dc.rights
  * dcterms.accessRights
* Subjects
  * dc.subject
  * dc.subject.lcsh
  * dc.subject.mesh
  * dc.coverage.temporal
  * dc.coverage.spatial
* Abstract
  * dc.description.abstract

## Field Value Hyperlinks/CrossLinks

The following fields display their values with hyperlinks:

* Files
* Permanent Link
* Collections
* Author/Creator
* Author/Creator ORCID
* Type of Work
* Subjects

The "Date" field could potentially be hyperlinked, but was not in
DSpace 6 MD-SOAR. In DSpace 7 MD-SOAR, it was not hyperlinked (crosslinked)
because the "Date" field includes a number of date-related metadata values in
addition to the stock DSpace "dc.date.issued" metadata value ("dc.date.created",
"dc.date.copyright", etc.) and they would all be linked to the "dateissued"
browse index. Some of the dates returned by the metadata properties include
ranges, i.e. "1984-2001", which do not work with the browse index.

The crosslinks (and which index they connect to) is controlled by the
"webui.browse.link.\<n>" entries in the "dspace/config/local.cfg" file.

Each "webui.browse.link" entry must specify an existing index in the
"webui.browse.index" list to link to.
