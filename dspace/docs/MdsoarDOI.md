# MD-SOAR DOI

## Introduction

This document describes the MD-SOAR DOI (Digital Object Identifier)
functionality, as of DSpace 7. See
<https://wiki.lyrasis.org/display/DSDOC7x/DOI+Digital+Object+Identifier>
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
