# DataCite DOI

## Introduction

This document describes the changes made to integrate DataCite DOI
(Digital Object Identifier) into MD-SOAR.

See <https://wiki.lyrasis.org/display/DSDOC7x/DOI+Digital+Object+Identifier>
for the DSpace documentation on DOI handling.

This document focuses on the customizations made for MD-SOAR.

## DataCite API

MD-SOAR uses the DataCite "MDS" API to mint DOIs. See
<https://support.datacite.org/docs/mds-api-guide>

## Random DOIs

The stock DSpace creates ("mints") DOIs as integers that increment
sequentially. This requires that there be only one source doing the DOI minting,
as having two minting sources could potentially generate overlapping numbers.

Since both DRUM and MD-SOAR use the same DOI prefix ("10.13016") in production,
using the stock DSpace functionality is not possible. Instead, DOIs are minted
as random 8 character codes in the format "XXXX-XXXX" (4 alphanumeric
characters, a hypen, then 4 more alphanumeric characters). This eliminates the
requirement to have a single minting source.

The generation of random DOIs is controlled by the "identifier.doi.mintRandom"
property. If "true", random DOIs are minted, if "false" the stock DSpace DOIs
are minted.

## MD-SOAR DOI metadata field

In MD-SOAR, the DOI is assigned to the unqualified "dc.identifier" field, unlike
in stock DSpace, where it is assigned to the "dc.identifier.doi" field.

Additionally, in MD-SOAR, the DOI is stored using the "doi:" format,
i.e., `doi:10.13016/m2xzog-gouv`, instead of the “URL” form used by DRUM
`https://doi.org/10.13016/dspace/g84g-gnfp`.

## DSpace to DataCite Crosswalk

When registering a DOI with DataCite, DSpace sends an XML file containing
metadata about the registered item. The XML file is generated using a
"DSpace Intermediate Model" (DIM) to DataCite "crosswalk" file -
"dspace/config/crosswalks/DIM2DataCite.xsl".

This file is an XSLT document that describes how to convert DSpace metadata into
the XML format expected by DataCite.

### "dc.creator" added to DataCite (2) "Creator" property

The default DSpace crosswalk file only uses the "dc.contributor.author" metadata
entries for the DataCite (2) "creator" property. For MD-SOAR, this was
modified to also include "dc.creator" entries.

It is not clear from the historical record why this change was made, as the
submission form never appears to have used the "dc.creator" field, but
there are entries in the MD-SOAR database (and Solr index) with "dc.creator"
entries, so it may have been done as part of a bulk import.

### "dc.type' Additions to DataCite (10) resourceType property

In "dspace/config/submission-forms.xml", the "common_types" value-pairs list,
used by the "Type" field in the submission form, was modified to match the list
from DSpace 6.

The DataCite (10) resourceType and DataCite (10.1) resourceTypeGeneral crosswalk
was also modified in DSpace 6 to support additional types, and change some of
the defaults, particularly:

| DSpace stored-value  | DataCite Value      | Note       |
| -------------------- | ------------------- | ---------- |
| Collection           | Collection          | See Note 1 |
| Interactive Resource | InteractiveResource | See Note 2 |
| Moving Image         | Audiovisual         | See Note 1 |
| Physical Object      | PhysicalObject      | See Note 1 |
| Still Image          | Image               | See Note 2 |
| Service              | Service             |            |
| Sound                | Sound               |            |
| Text                 | Text                | See Note 1 |
| Other                | Collection          |            |
| \<"otherwise" Value> | Collection          |            |

----

**Note 1:** These entries were added to match the MD-SOAR changes to the
the "common_types" value-pairs list in "dspace/config/submission-forms.xml",
using the DataCite types suggested in Table 7 of
<https://schema.datacite.org/meta/kernel-3.1/doc/DataCite-MetadataKernel_v3.1.pdf>.

These entries were *not* in the DSpace 6 MD-SOAR crosswalk, but probably should
have been.

----

**Note 2:** In the DSpace 6 MD-SOAR, the "Interactive Resource" and
"Still Image" entries were configured as one word (i.e.,
"InteractiveResource" and "StillImage"), as that is how the
"stored-values" in the "common_types" value-pairs list in
"dspace/config/submission-forms.xml" were originally added.

In the DSpace 6 MD-SOAR issue LIBCIR-264, the "stored-value" entries were
changed to use spaces ("Interactive Resource" and "Still Image") because the
"Type" field in the submission form was changed from a dropdown to a multi-value
list (which used the "stored-value" parameter, instead of the "displayed-value"
parameter). The "DIM2DataCite.xsl" file was *not* updated to reflect this
change, which resulted in the type not being provided to DataCite.

For the DSpace 7 MD-SOAR, the stored-value entries with spaces
("Interactive Resources" and "Still Image") were retained to maintain
consistency with the values stored in the database, but the DataCite crosswalk
mapping was corrected as above so that DataCite would get the proper type.

----
