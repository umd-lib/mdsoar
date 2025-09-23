# Submission Form

## Introduction

This document describes the customizations made to the stock DSpace submission
form for MD-SOAR.

## Form Fields

Fields are listed in the order that they appear on the form.

| Schema Field               | Label                            | Repeatable | Required | Notes |
| -------------------------- | -------------------------------- | ---------- | -------- | ----- |
| dc.contributor.author      | Author(s)                        | Yes        | No       |       |
| dcterms.creator            | Author ORCIDS(s)                 | Yes        | No       | (1)   |
| dc.contributor             | Contributor(s)                   | Yes        | No       |       |
| dc.contributor.advisor     | Advisor(s)                       | Yes        | No       |       |
| dc.title                   | Title                            | No         | Yes      |       |
| dc.title.alternative       | Other Titles(s)                  | Yes        | No       |       |
| dc.date.issued             | Date of Issue                    | No         | No       |       |
| dc.publisher               | Publisher                        | No         | No       |       |
| dc.identifier.citation     | Citation of Original Publication | No         | No       |       |
| dc.relation.ispartofseries | Series/Report                    | Yes        | No       |       |
| dc.contributor.department  | Department                       | No         | No       |       |
| dc.contributor.program     | Program                          | No         | No       |       |
| dc.identifier              | Identifiers                      | Yes        | No       | (2)   |
| dc.description.uri         | External Link                    | No         | No       |       |
| dc.type                    | Type                             | Yes        | Yes      | (3)   |
| dc.genre                   | Format                           | Yes        | No       |       |
| dc.format.extent           | Extent                           | No         | No       |       |
| dc.language.iso            | Language                         | No         | No       | (4)   |
| dc.subject                 | Subject Keywords                 | Yes        | No       | (5)   |
| dc.description.abstract    | Abstract                         | No         | No       |       |
| dc.description.sponsorship | Sponsors                         | No         | No       |       |
| dc.rights                  | Rights Statement                 | No         | No       |       |
| dc.description             | Description                      | No         | No       |       |

* Note (1): Input is validated using a regular expression
* Note (2): Dropdown using "common_identifiers" value-pairs list
* Note (3): Dropdown using "common_types" value-pairs list
* Note (4): Dropdown using "common_iso_languages" value-pairs list
* Note (5): In DSpace 6, uses Solr-based auto-suggest

## Form Field Hints

The "hint" attribute for the following fields uses an I18n key instead of
hard-coding the text:

| Schema Field           | "hint" I18n Key                        |
| ---------------------- | -------------------------------------- |
| dc.contributor.author  | submission.hint.dc.contributor.author  |
| dc.contributor.advisor | submission.hint.dc.contributor.advisor |
| dc.contributor         | submission.hint.dc.contributor         |

The text for the hints is specified in the “src/assets/i18n/en.json5”
localization file in the Angular front-end.

I18n keys are used for these fields to allow for HTML tags to be used as part of
hint text.

## Value-Pairs Lists

The following value-pair lists were customized in DSpace 6, and seemed
worthwhile to maintain in DSpace 7.

### common_identifiers

The stock DSpace list was maintained, with one additional entry:

| displayed-value | stored-value |
| --------------- | ------------ |
| DOI             | uri          |

### common_iso_languages

"English (United States)" has been commented out, and "English" has been made
the first item in the list (instead of stock DSpace "N/A" entry).

### common_types

The following list was copied from the DSpace 6 version of MD-SOAR:

| displayed-value      | stored-value         |
| -------------------- | -------------------- |
| Text                 | Text                 |
| Collection           | Collection           |
| Dataset              | Dataset              |
| Event                | Event                |
| Image                | Image                |
| Interactive Resource | Interactive Resource |
| Moving Image         | Moving Image         |
| Physical Object      | Physical Object      |
| Service              | Service              |
| Software             | Software             |
| Sound                | Sound                |
| Still Image          | Still Image          |

**Note:** As it is the most commonly selected entry, "Text" should be the first
entry in the drop-down list on the submission form.

## Creative Commons License

The Creative Commons license field is enabled, and should appear on the form.

## File Uploads

A file upload is *not* required.

## Workflow

There is only one workflow, which uses the default DSpace workflow.
