# Database initialization for docker-compose

Place your database dump file in this directory, and the dspacedb container
will use it to initialize the database.

To get a dump from k8s environment:

```bash
$ kubectl exec mdsoar-db-0 -- pg_dump -O -U mdsoar -d mdsoar > postgres-init/mdsoar.sql
```

## Delete local database data

The postgres container will use the initialization dump only if the
database is not previously initialized. If you would like to reinitialize the
database, stop the dspacedb container and delete the volume.

```bash
$ docker-compose down

$ docker volume rm d7_pgdata
```
