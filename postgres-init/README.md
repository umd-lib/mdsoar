# Database initialization for docker-compose

Place your database dump file in this directory, and the "dspacedb" container
will use it to initialize the database, via the "pg_restore.sh" script.

## pg_restore.sh and CVE-2023-2454

Prior to DSpace 7.6.2, it was possible to use "pg_dump" to create a "plain" SQL
file (with a ".sql" extension), and the "postgres" Docker image
would automatically use it to initialize the database.

This stopped working when Postgres was updated to correct
[CVE-2023-2454][CVE-2023-2454], although the reason why the fix breaks this
functionality is unclear (it may be due to the database initialization
process running as the "postgres" user, and the use of the "pgcrypto"
extension). Note that plain SQL dumps *will* work when run from the Docker
container's command-line.

The workaround was to create the "postgres-init/pg_restore.sh", script
which uses "pg_restore" to populate the database with a Postgres "custom" dump
file. This is used in preference to a plain SQL file, to keep the automatic
Docker initialization from interfering with the process.

The "postgres-init/pg_restore.sh" will run automatically when the Docker
container starts, *if* a database doesn't already exist.

## Creating the Postgres dump

To get a dump from k8s environment:

```zsh
$ kubectl exec mdsoar-db-0 -- pg_dump -Fc -C -O -U mdsoar -d mdsoar > postgres-init/mdsoar-db.dump
```

**Note:** The output file MUST use a ".dump" extension, in order for the
"pg_restore.sh" script to process it.

## (Optional) Verifying the Postgres dump

Use of the custom dump format is slightly risky because the Kubernetes
connection might have terminated before completing the dump.

Assuming that Postgres is installed on the local workstation, one way to
verify that the dump is complete, is to convert the database dump into a
plain SQL file (such as "verify-db.sql") by running:

```zsh
$ pg_restore -f verify-db.sql postgres-init/mdsoar-db.dump
```

and then running the "tail" command against the plain SQL file:

```zsh
$ tail verify-db.sql
```

If the dump is complete, the last lines will be:

```text
--
-- PostgreSQL database dump complete
--
```

## Delete local database data

The postgres container will use the initialization dump only if the
database is not previously initialized. If you would like to reinitialize the
database, stop the dspacedb container and delete the volume.

```zsh
$ docker-compose -p d8 down
$ docker volume rm d8_pgdata
```

---
[CVE-2023-2454]: https://www.postgresql.org/support/security/CVE-2023-2454/
