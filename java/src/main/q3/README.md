
# Check In Service Design Document

### The Problem and Requirements

A user A can check in to a venue X via a *real-time* service
that accepts an input of `(user, venue)`.

Below are the two queries we expect to run.

- Where is user A right now?
- What users are at venue X right now?

Below are business rules that apply.

- A user can only be at one venue at a time. If user A checks
in at venue X and then at venue Y, they are no longer at venue X.
- A check-in only “lasts” for at most 3 hours. If user A checks
in at venue X and then does nothing for 3 hours, they are no
longer at venue X.

Below are technical requirements that apply.

- The service should have durable enough storage that you can
restart it without losing all of your data. It should not
store everything in memory.
- Scalability / Availability — If you are going to have multiple
servers in your service, describe how you will distribute work
between them. What would happen if you suddenly had 100x more
requests? What would occur if one or more servers failed
(in whatever way they could fail)?

### Proposed Architecture

Because this service is user facing, it sounds like resolving
to an HTTP service makes the most sense – a user could check in
from mobile (iOS/Android), mobile web, or desktop web by hitting
an HTTP API, and pass their user ID and venue ID to the service.

According to the technical requirements, the service should
be stateless such that restarts will not lose in-memory data.
Thus, perhaps a publish-subscribe pattern might be appealing
to this use case. That is, messages are published and handed off
to a broker, which emits the messages to all subscribers.

By incorporating streaming data via pub/sub, this architecture
supports both (1) the real-time requirement as well as (2)
the stateless application requirement.

The primary subscriber for our use case would be a service that
accepts the message payload, which includes a user ID and a venue
ID. This subscriber would then write to our database. For this,
we might choose a traditional relational database, or alternatively
a key-value store may work because we know exactly what queries
we want to run. We could go over the benefits and trade-offs
for both.

Below, we'll take a look at how me might build our web service,
and then we'll take a look at how we might store our state
in a relational database or a key-value store.

#### Web Service

High-throughput web services can be load balanced to web servers
or containers, with monitoring to scale servers or containers up
or down if logging metrics are surpassed. For example, for
CPU-bound applications, if queries are causing the existing 
web servers or containers to use more than X% CPU resources,
more web servers or containers may be scaled up to handle
incoming load.

One good way to manage a distributed system is to use Kubernetes,
which has a few benefits and tooling, like service discovery and
self-healing, which may be a good fit for our use case, as well
as properly contain our application as a 12 Factor Application.

For the technical requirement that asks how our service may fail
(and recover) Kubernetes uses health checks that would ensure
that if a health check to our web service failed, a new
container would be spun up to replace the failing web service
container. The service might fail for many reasons – from
exceeding resource allocation, to the unknown uncaught
server error, to a service dependency that's no longer unavailable,
such as a database no longer being available (if the web
service was configured to require the database in order to run).

### State and Database

#### Traditional Relational Database

There's nothing categorically bad about traditional
relational databases. There are only benefits and trade-offs.

We'd probably want to choose a normalized data model, which 
has benefits such as flexibility with future requirements.

We'd want a User table, a Venue table, and a UserVenue table
for check-ins.

The User table would contain the user ID as a primary key, with
user data.

The Venue table would similarly contain the venue ID as a primary
key, with venue data.

The UserVenue table would contain our check-ins, with a composite
key consisting of a user ID foreign key, venue ID foreign key,
and a timestamp for the check-in.

When we receive a message from the publisher with the
`(userFOO, venueBAR)` pair at time `2017-01-01-00-00-00`,
we'd insert a new check-in entity.

By indexing the timestamp in the UserVenue table, we could
run SQL queries (or corresponding ORM queries) to answer
what users are checked-in to venueBAR:

```
SELECT
    u.username
FROM
    UserVenue c
LEFT OUTER JOIN
    User u
ON c.userId = u.id
WHERE
    venue = "venueBAR"
    AND c.timestamp >= DATEADD(HOUR, -3, GETDATE())
    AND c.timestamp <  DATEADD(HOUR, -1, GETDATE())
LIMIT 3
```

To find what venue that userFOO is checked into, we could
similarly query:

```
SELECT
    v.venueName
FROM
    UserVenue c
LEFT OUTER JOIN
    Venue v
ON c.venueId = v.id
WHERE
    user = "userFOO"
    AND c.timestamp >= DATEADD(HOUR, -3, GETDATE())
    AND c.timestamp <  DATEADD(HOUR, -1, GETDATE())
LIMIT 1
```

#### Key-Value (Column) Store

Alternatively, we can consider a key-value store and build
our data model around the 2 queries we have in our requirements.

When we receive a message containing the user ID and the venue ID
pair, we'll want to insert into two tables. One table will
be used for looking up users to query what venue
the user is at, and the other table will be used for looking up
venues to query what users are at a venue.

For the users table, we can have a key be `<user>|<datetime>`
and store the venue within a venue column. So let's say we receive
a message `(userFOO, venueBAR)` at time `2017-01-01-00-00-00`
where the time is in format `YYYY-MM-DD-HH-mm-dd`. We'd insert
a new row in our users table with the key
`userFOO|2017-01-01-00-00-00`, and a venue column value of
`venueBAR`. When we go to query what venue the user is at, 
we do a row prefix scan with the user ID, the date i.e. a
row prefix scan of `userFoo|2017-01-01`. Then, we can limit
to one result, and check that the datetime is within three hours.
If the datetime is within three hours, then we can check the
venue column and find that `userFOO` is checked in at `venueBAR`.

For the venuse table, we can have a key be
`<venue>|<datetime>|<user>`. When we receive a message
`(userFOO, venueBAR)` at time `2017-01-01-00-00-00`, then we
insert a new row `venueBAR|2017-01-01-00-00-00|userFOO`. Our
query to find what users are checked into the venue is a bit
simpler: we do a row prefix scan of `venueBar|2017-01-01` and
sort alphanumerically. Then, we check each check-in to see if
the check-in was within the past 3 hours. We then look at the
user ID part of the row key and get only unique users to
satisfy our query.

A big trade-off in choosing a key-value store is we're a bit
locked in and inflexible. That is, we designed around only two
queries in the initial requirements. With new requirements,
we can't run simple migrations of our schema – we'd need to
replicate and reorganize our data into a new table for any
new queries or use cases that rise up.

A benefit is that look-ups and row prefix scans are very fast in
comparison to traditional relational database lookups with indices.
