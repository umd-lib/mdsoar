# Docker Development Environment

This document contains instructions for building a local development instance
of a DSpace 8-based MD-SOAR using Docker.

## Development Setup

This repository uses the "GitHub Flow" branching model, with "mdsoar-main" as
the main branch for MD-SOAR development.

1) Clone the Git repository and switch to the directory:

    ```bash
    $ git clone -b mdsoar-main git@github.com:umd-lib/mdsoar.git mdsoar
    $ cd mdsoar
    ```

2) Optional: Build the dependent images.

    ```bash
    $ docker build -f Dockerfile.dependencies -t docker.lib.umd.edu/mdsoar-dependencies-8_x:latest .
    $ docker build -f Dockerfile.ant -t docker.lib.umd.edu/mdsoar-ant:latest .
    $ cd dspace/src/main/docker/dspace-postgres-pgcrypto
    $ docker build -t docker.lib.umd.edu/mdsoar-postgres:latest .
    $ cd -
    ```

3) Create the local configuration file

    ```zsh
    $ cp dspace/config/local.cfg.EXAMPLE dspace/config/local.cfg
    ```

4) Optional: Edit the local configuration file, if necessary.

   ```zsh
   $ vi dspace/config/local.cfg
   ```

   No changes are needed for basic operations, but the following functionality
   is not enabled by default in the local development environment:

   * DataCite DOI generation

     To enable DOI generation, fill out the following properties from the
     "DRUM/MD-SOAR DataCite Credentials" note in LastPass:

       * identifier.doi.user
       * identifier.doi.password

   * Google Analytics

     To enable Google Analytics, fill out the following properties from the
     "MD-SOAR Google Analytics" note in LastPass:

       * google.analytics.key
       * google.analytics.api-secret

5) Follow the instructions at
   [dspace/docs/MdsoarDBRestore.md](MdsoarDBRestore.md)
   to populate the Postgres database with a DSpace 7 database dump from
   Kubernetes.

6) Build the application and client Docker images:

   **Note:** If building for development, use the instructions in the
   "Quick Builds for development" section below, in place of the following
   steps.

    ```zsh
    # Build the dspace image
    $ docker compose -f docker-compose.yml build
    ```

7) Start all the containers

    ```zsh
    $ docker compose -p d8 up
    ```

    Once the REST API starts, it should be accessible at
    <http://localhost:8080/server>

## Quick Builds for development

To shortcut the long build time that is needed to build the entire project, we
can do a two stage build where the base build does a full Maven build, and for
subsequent changes, only build the "overlays" modules that contain our
customized Java classes.

```zsh
# Base build
$ docker build -f Dockerfile.dev-base -t docker.lib.umd.edu/mdsoar:8_x-dev-base .

# Overlay modules build
$ docker build -f Dockerfile.dev-additions -t docker.lib.umd.edu/mdsoar:8_x-dev .
```

Also, we can start the "dspace" container and the dependencies ("dspacedb"
and "dspacesolr") in separate commands. This allows the "dspace"
container to be started/stopped individually.

```zsh
# Start the db and solr container in detached mode
$ docker compose -p d8 up -d dspacedb dspacesolr

# Start the dspace container
$ docker compose -p d8 up dspace
```

Once the REST API starts, it should be accessible at
<http://localhost:8080/server>

## Visual Studio Code IDE Setup

The following is the suggested setup for Visual Studio Code for DSpace
development:

* Install the "Extension Pack for Java" (vscjava.vscode-java-pack) extension
* Install the "Checkstyle for Java" (shengchen.vscode-checkstyle) extension
  * Follow the instructions in the Lyrasis
    ["Code Style Guide"](https://wiki.lyrasis.org/display/DSPACE/Code+Style+Guide#CodeStyleGuide-VSCode)
    to configure the Checkstyle plugin and formatting options.
* The debug configuration necessary for the VS Code to attach to the Tomcat
  running on the Docker is maintained within in ".vscode" directory.

## Debugging

The `JPDA_OPTS` configuration included in the Docker compose starts the
JPDA debugger for Tomcat. The [.vscode/launch.json](/.vscode/launch.json)
file contains the VS Code debug configuration needed to attach to Tomcat. See
the "Visual Studio Code IDE Setup" section for the extensions needed for
debugging.

To start debugging,

1) Ensure that the dspace Docker container is up and running.

2) Open to the "Run and Debug" panel (CMD + SHIFT + D) on VS Code.

3) Click the green triangle (Play)  "Debug (Attach to Tomcat)" button on top of
   the debug panel.

## Useful commands

```zsh
# To stop all the containers
$ docker compose -p d8 stop

# To stop just the dspace container
$ docker compose -p d8 stop dspace

# To restart just the dspace container
$ docker compose -p d8 restart dspace

# To attach to the dspace container
$ docker exec -it dspace bash
```

## Create an administrator user

```zsh
$ docker compose -p d8 -f docker-compose-cli.yml run dspace-cli create-administrator
$ docker exec -it dspace /dspace/bin/dspace create-administrator
Creating d8_dspace-cli_run ... done
Creating an initial administrator account
E-mail address: <EMAIL_ADDRESS>
First name: <FIRST_NAME>
Last name: <LAST_NAME>
Password will not display on screen.
Password:
Again to confirm:
Is the above data correct? (y or n): y
Administrator account created
```

## Populate the Solr search index

```zsh
$ docker exec -it dspace /dspace/bin/dspace index-discovery
The script has started
Updating Index
Done with indexing
The script has completed
```

## Running the tests

By default the unit and integration tests are not run when building the project.

To run both the unit and integration tests:

```zsh
$ mvn install -DskipUnitTests=false -DskipIntegrationTests=false
```

### Test Environments

Typically, the unit and integration tests will require a test environment
consisting of the DSpace and Spring configurations files.

The stock DSpace uses the Maven "assembly" plugin to generate a Zip file
(named "dspace-parent-\<VERSION>-testEnvironment.zip", where \<VERSION> is the
project version), storing it as an artifact in the local Maven repository. This
Zip file contains the necessary configuration for the tests, based on the
standard configuration files, overlaid with files from the
"src/test/data/dspaceFolder" folder of each module (see
<https://wiki.lyrasis.org/display/DSPACE/Code+Testing+Guide> and
[src/main/assembly/testEnvironment.xml][testenv]).

This "testEnvironment.zip" file is not suitable, however, for the "additions"
module, which modifies some of the DSpace and Spring configuration files to
support the database entities and services added by the module. Therefore a
second Maven assembly file
[src/main/assembly/testEnvironment-additions.xml][testenv-add] has
been created, which generates a
"dspace-parent-\<VERSION>-testEnvironment-additions.zip" artifact.

Both of these "testEnvironment" artifacts are generated by running `mvn install`
in the project root directory. Therefore, after making any changes to the
standard DSpace or Spring configuration files, or any changes in a module's
"src/test/data/dspaceFolder" folder, run the following command in the project
root directory:

```zsh
$ mvn install
```

## DSpace Scripts and Email Setup

Some DSpace functionality may send email as part of their operation. The
development Docker images do not, by themselves, support sending emails.

The following changes enable the DSpace scripts to be run in the "dspace"
Docker container, with email being captured by the "MailHog" application.

**Note:** After making the following changes, the "Dockerfile.dev-base" and
"Dockerfile.dev-additions" Docker images need to be rebuilt.

### Dockerfile.dev-additions

Replace the "RUN apt-get update" in the section "Dockerfile.dev-additions" file,
just after the `FROM docker.io/eclipse-temurin:${JDK_VERSION}` line, to include
the packages needed for the script and email functionality:

```text
FROM docker.io/eclipse-temurin:${JDK_VERSION}

...

# Dependencies for email functionality
RUN apt-get update && \
    DEBIAN_FRONTEND=noninteractive apt-get install -y \
      csh \
      postfix \
      s-nail \
      libgetopt-complete-perl \
      libconfig-properties-perl \
    && apt-get purge -y --auto-remove \
    && rm -rf /var/lib/apt/lists/* \
    && mkfifo /var/spool/postfix/public/pickup
# End Dependencies for email functionality
```

### docker-compose.yml

Add the following lines to the "docker-compose.yml" file, in the "service"
stanza, to enable the "MailHog" (<https://github.com/mailhog/MailHog>) SMTP
capture tool as part of the Docker Compose stack:

```yaml
services:
  ...
  # MailHog SMTP Capture
  mailhog:
    container_name: mailhog
    image: mailhog/mailhog:v1.0.1
    networks:
      dspacenet:
    logging:
      driver: 'none'  # disable saving logs
    ports:
      - 1025:1025 # smtp server
      - 8025:8025 # web ui
  # End MailHog SMTP Capture
```

### dspace/config/local.cfg

Set the following values in the "dspace/config/local.cfg" file, replacing the
existing values:

```text
mail.server = mailhog
mail.server.port = 1025
```

### Running the MailHog application

With the above changes, the MailHog application can be run using:

```zsh
$ docker compose -p d8 up mailhog
```

The MailHog application will be accessible at <http://localhost:8025/>.

## Testing File Download Counts

Testing the file download counts, generated by the Solr "statistics" core,
requires the "GeoIP" database to be added to the local development environment.

These steps should be done at Step 4 in the "Development Setup" process,
*before* building the Docker images.

**Note:** These steps use the "lite" version of the GeoIP
database, which does not require account credentials. The version running in
Kubernetes uses the "full" GeoIP database, which requires account
credentials.

1) Download the “IP to City Lite” (in “mmdb” format) from
<https://db-ip.com/db/download/ip-to-city-lite> and put in the “/tmp” directory,
and extract the file, where “\<YYYY-MM>” is the year/month of the download:

```zsh
$ cd /tmp
$ gunzip dbip-city-lite-<YYYY-MM>.mmdb.gz
```

This will result in a file named “dbip-city-lite-\<YYYY-MM>.mmdb”. For
simplicity, rename the file to “dbip-city-lite.mmdb”:

```zsh
$ mv /tmp/dbip-city-lite-<YYYY-MM>.mmdb /tmp/dbip-city-lite.mmdb
```

2) Copy the "/tmp/dbip-city-lite.mmdb" file into the "dspace/config/" directory:

```zsh
$ cp /tmp/dbip-city-lite.mmdb dspace/config/
```

3) Add the following line to the “dspace/config/local.cfg” file:

```zsh
usage-statistics.dbfile = /dspace/config/dbip-city-lite.mmdb
```

---
[testenv]: <../../src/main/assembly/testEnvironment.xml>
[testenv-add]: <../../src/main/assembly/testEnvironment-additions.xml>
