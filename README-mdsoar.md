# Maryland Shared Open Access Repository (MD-SOAR)

Home: <http://mdsoar.lib.umd.edu/>

## Documentation

The original Dspace documentation is in the "README.md" file.

## Development Environment

Instructions for building and running mdsoar locally can be found in
[dspace/docs/DockerDevelopmentEnvironment.md](/dspace/docs/DockerDevelopmentEnvironment.md)

## Building Images for K8s Deployment

As of May 2023,  MacBooks utilizing Apple Silicon (the "arm64" architecture)
are unable to directly generate the "amd64" Docker images used by Kubernetes.

The following procedure uses the Docker "buildx" functionality and the
Kubernetes "build" namespace to build the Docker images. This procedure should
work on both "arm64" and "amd64" MacBooks.

All images will be automatically pushed to the Nexus.

### Local Machine Setup

See <https://confluence.umd.edu/display/LIB/Docker+Builds+in+Kubernetes> in
Confluence for information about setting up a MacBook to use the Kubernetes
"build" namespace.

### Creating the Docker images

1) In an empty directory, checkout the Git repository and switch into the
   directory:

    ```bash
    $ git clone git@github.com:umd-lib/mdsoar.git mdsoar
    $ cd mdsoar
    ```

2) Checkout the appropriate Git tag, branch, or commit for the Docker images.

3) Set up an "MDSOAR_TAG" environment variable:

    ```bash
    $ export MDSOAR_TAG=<MDSOAR_TAG>
    ```

   where \<MDSOAR_TAG> is the Docker image tag to associate with the
   Docker images. This will typically be the Git tag for the MD-SOAR version,
   or some other identifier, such as a Git commit hash. For example, using the
   Git tag of "7.5-mdsoar-0":

    ```bash
    $ export MDSOAR_TAG=7.5-mdsoar-0
    ```

4) Set up a "MDSOAR_DIR" environment variable referring to the current
   directory:

    ```bash
    $ export MDSOAR_DIR=`pwd`
    ```

5) Switch to the Kubernetes "build" namespace:

    ```bash
    $ kubectl config use-context build
    ```

6) Create the "docker.lib.umd.edu/mdsoar-dependencies-7_x" Docker image. This
   image is used to pre-cache Maven downloads that will be used in subsequent
   DSpace docker builds:

    ```bash
    $ docker buildx build --platform linux/amd64 --builder=kube --push --no-cache -t docker.lib.umd.edu/mdsoar-dependencies-7_x:latest -f Dockerfile.dependencies .
    ```

7) Create the "docker.lib.umd.edu/mdsoar" Docker image:

    ```bash
    $ docker buildx build --platform linux/amd64 --builder=kube --push --no-cache -f Dockerfile -t docker.lib.umd.edu/mdsoar:$MDSOAR_TAG .
    ```

8) Create the "docker.lib.umd.edu/dspace-postgres", which is a Postgres image
   with "pgcrypto" module:

    **Note:** The "Dockerfile" for the "dspace-postgres" image specifies
    only the major Postgres version as the base image. This allows Postgres
    minor version updates to be retrieved automatically. It may not be
    necessary to create new "dspace-postgres" image versions for every MD-SOAR
    patch or hotfix version increment.

    ```bash
    $ cd $MDSOAR_DIR/dspace/src/main/docker/dspace-postgres-pgcrypto

    $ docker buildx build --platform linux/amd64 --builder=kube --push --no-cache -f Dockerfile -t docker.lib.umd.edu/mdsoar-postgres:$MDSOAR_TAG .
    ```

9) Create the "docker.lib.umd.edu/mdsoar-solr":

    **Note:** The "Dockerfile" for the "mdsoar-solr" image specifies only the
    major Solr version as the base image. This allows Solr minor version updates
    to be retrieved automatically. It may not be necessary to create new
    "mdsoar-solr" image versions for every MD-SOAR patch or hotfix version
    increment.

    ```bash
    $ cd $MDSOAR_DIR/dspace/solr

    $ docker buildx build --platform linux/amd64 --builder=kube --push --no-cache -f Dockerfile -t docker.lib.umd.edu/mdsoar-solr:$MDSOAR_TAG .
    ```

### Features

* [MdsoarCustomizations](dspace/docs/MdsoarCustomizations.md) - Summary of
  MD-SOAR customizations to base DSpace functionality
* [docs](dspace/docs) - additional documentation

## Customization Markings

UMD customizations to stock DSpace code should be marked, if possible, with
a starting comment "UMD Customization" and an ending comment of
"End UMD Customization", for example, in a Java file:

```java
// UMD Customization
... New or modified code ...
// End UMD Customization
```

The following customizations *do not* need to be commented:

* Updates to the "\<version>" identifier in "pom.xml" files
* "Branding" changes in email templates such as "dspace/config/emails/" or
  the default DSpace license in "dspace/config/default.license", as these files
  do not have a convenient "comment" mechanism
* Files that do not have a "comment" mechanism, such as JSON files
* Extremely trivial whitespace changes unrelated to UMD customizations, such as
  tabs in the modified DSpace file being automatically converted to spaces by
  VS Code, or an end-of-file line.

The main goal is to make it immediately when performing DSpace version upgrades
whether a change in a file is due to an explicit UMD customization.

## License

The DSpace license can be found at <https://github.com/DSpace/DSpace>
