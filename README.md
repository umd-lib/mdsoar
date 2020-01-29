# Maryland Shared Open Access Repository (MD-SOAR)

Home: http://mdsoar.lib.umd.edu/

## Documentation

* [Original DSpace README](README-DSPACE.md)
* [DSpace Manual](dspace/docs/pdf/DSpace-Manual.pdf)
* [Theme Customization](dspace/docs/ThemeCustomization.md)
* [Mirage2 Prerequisites on Server](dspace/docs/Mirage2PrerequisitesOnServer.md)
* [Vagrant Deployment](https://github.com/umd-lib/mdsoar-vagrant)

### Installation

Instructions for installing in UMD Libraries development environments (Mac OS X):

*Important:* Until the upcoming DSpace 7 upgrade, we will be using Java 7 for both the maven build (local) and ant deployment (server). Set your JAVA_HOME and PATH environment variables to run maven build using Java 7.

```
# Build the base modules and overlay modules (Slower)
# Full build is only required after a version change (checking out a different version
# or a local project version change)
cd /apps/git/mdsoar
mvn clean install -Dmirage2.deps.included=false

# Build only the overlay modules (Faster)
# Can be run only after a full build is done at least once after a version change.
cd /apps/git/mdsoar/dspace
mvn install -Dmirage2.deps.included=false
```

*Note:* Due to a outdated Java 7 security certificate issue, the mirage2 build using native tools is no longer working. The `-Dmirage2.deps.included=false` needs to be added to all maven build commands.

[Old Build Instuctions](dspace/docs/LocalBuildInstructions.md)

### Deployment

The `dspace-installer` directory that contains all the artifacts and the ant script to perform the deployment. The `installer-dist` maven profile creates a tar file of the installer directory which can be pushed to the UMD nexus by using the `deploy-release` or `deploy-snapshot` profile.

```
# Switch to the dspace directory
cd /apps/git/mdsoar/dspace

# Deploy a snapshot version to nexus
# (use this profile if the current project version is a SNAPSHOT version)
mvn -P installer-dist,deploy-snapshot -Dmirage2.deps.included=false

# Deploy a release version to nexus
mvn -P installer-dist,deploy-release -Dmirage2.deps.included=false
```

*NOTE:* For the Nexus deployment to succeed, the nexus server, username and password needs to be configured in the `.m2/setting.xml` and a prior successful `mvn install`.
