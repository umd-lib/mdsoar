# MD-SOAR Local Build Instructions

## Clone the project from Github
```
cd /apps/git
git clone git@github.com:<YOUR_ACCOUNT>/mdsoar.git
```

## Setup local.properties
Setup local.properties using the build.properties template file. The [mdsoar-vagrant](https://github.com/umd-lib/mdsoar-vagrant) project includes a local.properties that can used for deploying the webapps to vagrant.

## Full build
Build the project from the root directory and install the artifacts to the local maven repository. A full build is only required on a project version change or on changes to the core dspace codebase.

```
cd /apps/git/mdsoar
mvn install -Denv=local -P all-modules
```

## Quick build
It is sufficient to build modules under the dspace directory during the development process.

```
cd /apps/git/mdsoar/dspace
mvn package -Denv=local
```

## Build profiles

### Default Profile
The default profile for the local development environment `-Denv=local` is configured to only build the modules the are being actively developed or their dependencies.

```
DSpace Addon Modules
DSpace Kernel :: Additions and Local Customizations
DSpace XML-UI (Manakin) :: Local Customizations
DSpace SOLR :: Local Customizations
DSpace XML-UI Mirage2 Theme :: Local Customisations
DSpace Assembly and Configuration
```

In contrast, the default profile on the server environments (for example: `-Denv=dev`) builds all the modules except the depeciated lni module.

### All modules profile
To build all modules, the `all-modules` profile can be used. 

```
mvn package -Denv=local -P all-modules
```

### Selective modules
Modules can be built selectively to cut down on the build time on unnecessary modules. Make sure to include all dependent modules. See examples below:

```
mvn package -Denv=local -P dspace-xmlui,mirage2
mvn package -Denv=local -P mirage2
```

## Local Deployment
Follow the documentation on the [mdsoar-vagrant](https://github.com/umd-lib/mdsoar-vagrant) project for deploying to a local vagrant vm.

### Ant local build option
Use the `ant update_local` option to do updates without creating backups of the previous deployment files. This would cut down on the time taken create the backups.