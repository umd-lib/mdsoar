# MD-SOAR Test Plan

## Introduction

This document provides a basic MD-SOAR test plan that verifies as many UMD
customizations and other concerns (such as functionality when using read-only
containers) as feasible.

The intention is to provide basic assurance that the UMD customizations are in
place, and guard against regressions.

**This document is not intended to be an exhaustive test plan.**

As this test plan adds, modifies, and deletes data, it should ***not*** be used
to test the production system.

## Test Plan Assumptions

This test plan assumes that the user has an MD-SOAR account with "administrator"
privilege.

The test plan steps are specified using URLs for the Kubernetes "test"
namespace, as that seems to be the most useful. Unless otherwise specified,
test steps should work in the local development environment as well.

## Test Plan

### 1) MD-SOAR API Server

1.1) In a web browser, go to

<https://api.mdsoar-test.lib.umd.edu/server>

The HAL Browser "Explorer" page will be displayed.

1.2) On the HAL Browser "Explorer" page, verify that:

* in the "Properties" pane the appropriate "dspaceVersion" is displayed.

### 2) MD-SOAR Home Page

2.1) In a web browser, go to

<https://mdsoar-test.lib.umd.edu/>

The MD-SOAR home page will be displayed.

2.2) On the MD-SOAR home page, verify that:

* The MD-SOAR favicon (looks like a graduation cap) is displayed in the browser
  tab, and that the text in the browser tab is
  "MD-SOAR Open Access Repository Home"
* The appropriate SSDR environment banner is displayed.
* There is an “Institutions in MD-SOAR” section

### 3) Institution Pages

3.1) On the MD-SOAR home page, left-click the “Frostburg State University” link
in the “Institutions in MD-SOAR” section. Verify that the
“eScholarship@Frostburg” page is displayed.

3.2) On the “eScholarship@Frostburg” page, verify the following:

* A small “eScholarship@Frostburg” logo appears in the
  “breadcrumb bar” above the page header.
* The “eScholarship@Frostburg” banner image is displayed below the
  “eScholarship@Frostburg” page header.
* At the bottom of the page, there is a footer with the “eScholarship@Frostburg”
  logo with library address information and contact phone number and email.

3.3) Left-click the MD-SOAR logo in the navigation bar to return to the MD-SOAR
home page.

### 4) Login

4.1) On the MD-SOAR home page, left-click the "Log In" dropdown in the
navigation bar, type in your email address and password, and then left-click the
“Log in” button.

4.2) Once returned to the MD-SOAR home page, verify that an administrative
sidebar is displayed along the left edge of the page.

4.3) In the navigation bar, left-click the "User profile menu and logout"
dropdown in the navigation bar (the icon is a person in silhouette) and verify
that the dropdown displays the name associated with the account.

Left-click the "Profile" menu entry in the dropdown. The "Profile" page will
be displayed.

4.4) On the "Profile" page, verify that a "Researcher Profile" panel is *not*
displayed. At the bottom of the page verify that there is a section:

* "Authorization groups you belong to"

Verify that the "Authorization groups you belong to includes "Administrator"
(there may be other entries in these groups).

### 5) Community Creation

5.1) From the administrative sidebar, select "New | Community". The
"New community" modal dialog will be displayed.

5.2) In the "New community" modal dialog, left-click the
"Create a new top-level community" button. The "Create a Community" page will be
displayed.

5.3) On the "Create a Community" page, fill out the following fields:

| Field | Value |
| ----- | ----- |
| Name  | SSDR Test Community |

then left-click the "Save" button. A notification will display indicating that
the community was successfully added. The "SSDR Test Community" page will be
displayed.

### 6) Collection Creation

6.1) From the administrative sidebar, select "New | Collection". The
"New collection" modal dialog will be displayed.

6.2) In the "New collection" modal dialog, left-click the "SSDR Test Community"
entry. The "Create a Collection" page will be displayed.

6.3) On the "Create a Collection" page, fill out the following fields:

| Field | Value |
| ----- | ----- |
| Name  | SSDR Test Collection |

then left-click the "Save" button. A notification will display indicating that
the collection was successfully added. The "SSDR Test Collection" page will be
displayed.

### 7) Item Submission

7.1) From the administrative sidebar, select "New | Item". The "New item" modal
dialog will be displayed.

7.2) In the "New item" modal dialog, left-click the "SSDR Test Collection"
entry. The "Edit Submission" page will be displayed.

7.3) Upload a PDF file to the page by dragging and dropping it onto the page.

7.4) Fill out the following fields:

| Field  | Value           |
| ------ | --------------- |
| Title  | SSDR Test Item  |
| Type   | Text            |

In the "Creative commons license" section, left-click the
"Select a license type..." button and verify that the following two options are
shown (but do not select either of them):

* CC0
* Creative Commons

In the "Deposit license" section, left-click the "I confirm the license above"
checkbox, and then left-click the "Deposit" button. A notification will be
displayed indicating that the item was successfully deposited. The
"Your submissions" page will be displayed.

7.5) On the "Your submissions" page, verify that the item has been added as one
of the submissions. Left-click the "View" button for the submitted item. The
summary page for the item will be shown.

7.6) On the item summary page, verify that:

* In the left sidebar, a "Permanent Link" field is displayed with a URL.

7.7) Check your email (or the email address associated with the login) and
verify that an email with the subject line
"MD-SOAR[-QA|-TEST]: Submission Approved and Archived" was received.

**Note**: An email will not be sent when using the local development
environment unless SMTP has been configured -- see the
"DSpace Scripts and Email Setup" section in
[dspace/docs/DockerDevelopmentEnvironment.md](DockerDevelopmentEnvironment.md).

### 8) Impersonate Functionality

8.1) From the administrative sidebar, select "Access Control | People". The
"EPeople" page will be displayed.

8.2) On the "EPeople" page, enter "11d9c3a9-2b54-4bda-a59a-0fa02eed39f1"
into the search box. Verify that one person is found. If an entry is not found
look for a faculty member who is *not* an administrator.

8.3) Left-click the "Edit" button on the search result entry. The
"Edit EPerson" page will be displayed.

8.4) Left-click the "Impersonate EPerson" button on the page. The MD-SOAR home
page will be displayed. Left-click the "User profile menu and logout" dropdown
in the navigation bar (the icon is a person in silhouette) and verify that the
dropdown displays the name and email of the person being impersonated.

8.5) Left-click the "Stop impersonating EPerson" button in the navigation bar
to stop the impersonation. The MD-SOAR home page will be displayed.

### 9) Batch Export (Zip)

9.1) From the administrative sidebar, select "Export | Batch Export (Zip)". A
"Export Batch (ZIP) from" modal dialog will be displayed.

9.2) In the "Export Batch (ZIP) from" modal dialog, select the
"SSDR Test Collection" and in the resulting dialog, left-click the "Export"
button. A "Process" page will be shown.

9.3) After the process completes, the "Process" page will refresh. Download the
"saf-export.zip" file to the local workstation and extract it. Verify that the
extracted contents include the PDF that was originally loaded in the steps
above, as well as additional files containing the metadata.

### 10) Batch Import (Zip)

10.1) Download the [mdsoar-batch-import.zip](resources/mdsoar-batch-import.zip)
file to the local workstation.

10.2) From the administrative sidebar, select “Import | Batch Import (ZIP)”.
The “Import Batch” page will be displayed.

10.3) Drag-and-drop the "mdsoar-batch-import.zip” file onto the “Import Batch”
page. Then do the following:

* Left-click the “Select Collection” button, and left-click the
  “SSDR Test Collection”
* Uncheck the “Validate Only” checkbox
* Left-click the “Proceed” button. A notification will be displayed indicating
  that a process was successfully created and a “Process” page will be
  displayed.

10.4) Once the “Process” page has a “Status” field of “COMPLETED”, left-click
the MD-SOAR logo on the left-side of the navigation bar to return to the home
page.

10.5) On the home page, left-click the “Communities and Collections”, then on
the “List of Communities” page, left-click the arrow to the left of the
“SSDR Test Community” to expand it, then left-click the “SSDR Test Collection”
link. The “SSDR Test Collection” page will be shown. Verify that there is an
“SSDR Test Item - Meno” item from the import (you may need to refresh the page).

### 11) OAI PMH

11.1) In a web browser, go to

<https://mdsoar-test.lib.umd.edu/oai/request?verb=Identify>

and verify that a “DSpace OAI-PMH Data Provider” page with repository
information is displayed.

**Note:** In the local development environment, go to
<http://localhost:8080/server/oai/request?verb=Identify> instead.

### 12) Open Search

12.1) In a web browser, go to

<https://mdsoar-test.lib.umd.edu/open-search/discover?query=author:smith>

and verify that an XML file can be downloaded, and contains item information.

**Note:** In the local development environment, go to
<http:/localhost:8080/server/opensearch/search?query=author:smith>
instead.

### 13) robots.txt

**Note:** This step cannot be tested in the local development environment.

13.1) In a web browser go to

<https://mdsoar-test.lib.umd.edu/robots.txt>

Verify the contents of a "robots.txt" file is displayed, and contains the
following uncommented line (among many others):

```text
Disallow: /browse/*
```

**Note:** On MD-SOAR QA, the “robots.txt” file disallows all crawling, as it is
available on the public internet (to allow access by non-UMD stakeholders), and
we do not want it included in search results.

13.2) In a web browser go to

<https://api.mdsoar-test.lib.umd.edu/robots.txt>

and verify that the "robots.txt" file disallows all crawling.

### 14) sitemap.xml

**Note:** This step cannot be tested in the local development environment.

14.1) In a web browser go to

<https://mdsoar-test.lib.umd.edu/sitemap_index.xml>

Verify that a "sitemap" file is returned (with a pointer to "sitemap0.xml").

14.2) In a web browser go to

<https://api.mdsoar-test.lib.umd.edu/sitemap.xml>

and verify that either an empty page is returned (Chrome) or a page indicating
and XML Parsing error (Firefox) is returned.

### 15) Collection and Community Deletion

15.1) From the administrative sidebar, select "Edit | Collection".
The "Edit collection" modal dialog will be displayed.

15.2) In the "Edit collection" modal dialog, select the
"SSDR Test Collection" entry. The "Edit Collection" page will
be displayed.

15.3) On the "Edit Collection" page, left-click the "Delete the collection"
button. A "Delete Collection" confirmation page will be displayed. Left-click
the "Confirm" button on the page, and verify that a notification is displayed
indicating that the collection was deleted.

15.4) From the administrative sidebar, select "Edit | Community".
The "Edit community" modal dialog will be displayed.

15.5) In the "Edit community" modal dialog, select the
"SSDR Test Community" entry. The "Edit Community" page will
be displayed.

15.6) On the "Edit Community" page, left-click the "Delete the community"
button. A "Delete Community" confirmation page will be displayed. Left-click
the "Confirm" button on the page, and verify that a notification is displayed
indicating that the community was deleted.
