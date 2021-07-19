# Maryland Shared Open Access Repository (MD-SOAR)

Home: http://mdsoar.lib.umd.edu/

## Documentation

* [Original DSpace README](README-DSPACE.md)
* [DSpace Manual](dspace/docs/pdf/DSpace-Manual.pdf)
* [Theme Customization](dspace/docs/ThemeCustomization.md)
* [Mirage2 Prerequisites on Server](dspace/docs/Mirage2PrerequisitesOnServer.md)
* [Vagrant Deployment](https://github.com/umd-lib/mdsoar-vagrant)

### Installation

Instructions for building and running mdsoar locally.
#### Prerequisites

The following images needs to be built once for the Dockerfile.dev to build successfully.

```
docker build -t docker.lib.umd.edu/mdsoar-dependencies-6_x:latest -f Dockerfile.dependencies .
docker build -t docker.lib.umd.edu/mdsoar-ant:latest -f Dockerfile.ant .
docker build -t docker.lib.umd.edu/mdsoar-tomcat:latest -f Dockerfile.tomcat .
```

#### Build

To build the dspace development image used by the docker-compose.yml

```
docker build -t docker.lib.umd.edu/mdsoar:dev -f Dockerfile.dev .
```

#### Run

Start the mdsoar using docker-compose.

```
docker-compose up -d
```

Useful commands
```
# To stop all the containers
docker-compose down

# To stop just the dspace container
docker-compose stop dspace

# To attach to the dspace container
docker exec -it $(docker ps -f name=dspace$ --format {{.ID}}) bash
```

[Old Build Instuctions](dspace/docs/LocalBuildInstructions.md)

### Building Images for K8s Deployment

#### DSpace Image

Dockerfile.dependencies is used to pre-cache maven downloads that will be used in subsequent DSpace docker builds.

```
docker build -t docker.lib.umd.edu/mdsoar-dependencies-6_x:latest -f Dockerfile.dependencies .
```

This dockefile builds a mdsoar tomcat image.

```
docker build -t docker.lib.umd.edu/mdsoar:<VERSION> .
```

The version would follow the mdsoar project version. For example, a release version could be `6.3/mdsoar-4.2`, and we can suffix the version number with `-rcX` or use `latest` as the version for non-production images.

#### Postgres Image

To build postgres image with pgcrypto module.

```
cd dspace/src/main/docker/dspace-postgres-pgcrypto
docker build -t docker.lib.umd.edu/dspace-postgres:<VERSION> .
```

We could follow the same versioning scheme as the main mdsoar image, but we don't necessariliy have create new image versions for postgres for every patch or hotfix version increments. The postgres image can be built when there is a relevant change.

#### Solr Image

To build postgres image with pgcrypto module.

```
cd dspace/solr
docker build -t docker.lib.umd.edu/mdsoar-solr:<VERSION> .
```

We could follow the same versioning scheme as the main mdsoar image, but we don't necessariliy have create new image versions for solr for every patch or hotfix version increments. The solr image can be built when there is a relevant change.

### Deployment (Old VM based deployment)

The `dspace-installer` directory that contains all the artifacts and the ant script to perform the deployment. The `installer-dist` maven profile creates a tar file of the installer directory which can be pushed to the UMD nexus by using the `deploy-release` or `deploy-snapshot` profile.

```
# Switch to the dspace directory
cd /apps/git/mdsoar/dspace

# Deploy a snapshot version to nexus
# (use this profile if the current project version is a SNAPSHOT version)
mvn -P installer-dist,deploy-snapshot -rf :dspace

# Deploy a release version to nexus
mvn -P installer-dist,deploy-release -Dmirage2.deps.included=false -rf :dspace
```

*NOTE:* For the Nexus deployment to succeed, the nexus server, username and password needs to be configured in the `.m2/setting.xml` and a prior successful `mvn install`.
