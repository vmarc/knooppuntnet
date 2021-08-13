# MongoDB

This documents the experiments with MongoDB to see whether it would be a good replacment for CouchDB in knooppuntnet.

- [Install](#install)
- [Security](#security)
- [Install Compass](#install-compass)
- [Migration](#migration)
  

## Install
<a name="install"></a>

Using [instructions](https://docs.mongodb.com/manual/tutorial/install-mongodb-on-ubuntu/) on mongodb site.

Import the public key:
```
wget -qO - https://www.mongodb.org/static/pgp/server-4.4.asc | sudo apt-key add -
```

Create a list file for MongoDB
```
echo "deb [ arch=amd64,arm64 ] https://repo.mongodb.org/apt/ubuntu focal/mongodb-org/4.4 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-4.4.list
```

Reload local package database
```
sudo apt-get update
```

Install the MongoDB packages.
```
sudo apt-get install -y mongodb-org
```

Install shell:
```
sudo apt-get install -y mongodb-mongosh
```

Establish which init system is used (result: systemd):
```
ps --no-headers -o comm 1
```

Comment out line with bindIp in /etc/mongod.conf to allow connections from other hosts:
```
#  bindIp: 127.0.0.1
```

Start MongoDB:
```
sudo systemctl start mongod
```

Make sure MongoDB will start upon reboot:
```
sudo systemctl enable mongod
```

Verify that MongoDB has started successfully

```
sudo systemctl status mongod
ps -ef | grep mongodb
```

Stop MongoDB
```
sudo systemctl stop mongod
```

Restart MongoDB
```
sudo systemctl restart mongod
```

Mongo shell
```
mongo
```

## Security
<a name="security"></a>

Create admin user:
```
use admin
db.createUser(
  {
    user: "admin",
    pwd: passwordPrompt(),
    roles: [ { role: "userAdminAnyDatabase", db: "admin" }, "readWriteAnyDatabase" ]
  }
)
```

Create app user (logged in as admin user):
```
use tryout
db.createUser(
  {
    user: "kpn-app",
    pwd:  passwordPrompt(),
    roles: [ { role: "readWrite", db: "tryout" } ]
  }
)

db.grantRolesToUser(
    "kpn-app",
    [
      { role: "readWrite", db: "kpn-experimental" }
    ]
)
```



## Install Compass
<a name="install-compass"></a>

Use [instructions](https://docs.mongodb.com/compass/current/install) on mongodb site.

Download
```
wget https://downloads.mongodb.com/compass/mongodb-compass_1.26.1_amd64.deb
```

Install
```
sudo apt-get install libgconf-2-4
sudo apt --fix-broken install
sudo dpkg -i mongodb-compass_1.26.1_amd64.deb
```

Start

```
mongodb-compass
```

## Tips

Merge aggregate in scala code:

```scala
  merge(
    "change-stats-summaries",
    MergeOptions().uniqueIdentifier("_id")
  )
```

Compose concatenated _id in scala code:

```scala
  project(
    fields(
      excludeId(),
      Document("_id" ->
        Document(
          "$concat" ->
            Seq(
              Document("$toString" -> "$_id.impact"),
              Document("$toString" -> ":"),
              Document("$toString" -> "$_id.year"),
              Document("$toString" -> ":"),
              Document("$toString" -> "$_id.month"),
              Document("$toString" -> ":"),
              Document("$toString" -> "$_id.day")
            )
        )
      ),
      computed("impact", "$_id.impact"),
      computed("year", "$_id.year"),
      computed("month", "$_id.month"),
      computed("day", "$_id.day"),
      include("count")
    )
  )
```

## Backup strategy

To backup the complete knooppuntnet database on a regular basis and to keep multiple copies, requires way too much diskspace.  It is mainly the changes collections that are the problem here.

```bash
mongodump --db=tryout
```

```
ll -S dump/tryout/*changes.bson
-rw-rw-r-- 1 marcv marcv  52G Jun 10 21:23 route-changes.bson
-rw-rw-r-- 1 marcv marcv  43G Jun 10 21:23 network-changes.bson
-rw-rw-r-- 1 marcv marcv 435M Jun 10 21:07 changeset-summaries.bson
-rw-rw-r-- 1 marcv marcv 313M Jun 10 21:07 node-changes.bson
-rw-rw-r-- 1 marcv marcv  38M Jun 10 21:07 changeset-comments.bson
```

We can do an incremental backup for these collections, for example per day:

```bash
mongodump --db=tryout --collection=route-changes --query='{"routeChange.key.time.year": 2020, "routeChange.key.time.month": 1, "routeChange.key.time.day": 1}' --gzip --out route-changes-2020-01-01

Note: the dump takes a while (5 mins), it does not seem to use the index.
```

```bash
ll route-changes-2020-01-01/tryout
total 2.2M
-rw-rw-r-- 1 marcv marcv 2.2M Jun 10 21:57 route-changes.bson.gz
-rw-rw-r-- 1 marcv marcv  259 Jun 10 21:52 route-changes.metadata.json.gz
```

Per month:
```bash
ll route-changes-2021-01/tryout/
total 133M
-rw-rw-r-- 1 marcv marcv 133M Jun 10 22:07 route-changes.bson.gz
-rw-rw-r-- 1 marcv marcv  259 Jun 10 22:02 route-changes.metadata.json.gz

23845 documents
```

```bash
ll route-changes-2021-02/tryout/
total 130M
-rw-rw-r-- 1 marcv marcv 130M Jun 10 22:12 route-changes.bson.gz
-rw-rw-r-- 1 marcv marcv  259 Jun 10 22:08 route-changes.metadata.json.gz

23548 documents
```

Per year, with multiple collections in same directory:


```bash
mongodump --db=tryout --collection=route-changes --query='{"routeChange.key.time.year": 2013}' --gzip --out changes-2013
mongodump --db=tryout --collection=network-changes --query='{"networkChange.key.time.year": 2013}' --gzip --out changes-2013
mongodump --db=tryout --collection=node-changes --query='{"nodeChange.key.time.year": 2013}' --gzip --out changes-2013
```

```bash
ll -S changes-2013/tryout
total 462M
-rw-rw-r-- 1 marcv marcv 287M Jun 10 22:19 route-changes.bson.gz
-rw-rw-r-- 1 marcv marcv 174M Jun 10 22:27 network-changes.bson.gz
-rw-rw-r-- 1 marcv marcv 638K Jun 10 22:29 node-changes.bson.gz
-rw-rw-r-- 1 marcv marcv  293 Jun 10 22:20 network-changes.metadata.json.gz
-rw-rw-r-- 1 marcv marcv  259 Jun 10 22:29 node-changes.metadata.json.gz
-rw-rw-r-- 1 marcv marcv  259 Jun 10 22:14 route-changes.metadata.json.gz
```

Incremental restore:
```bash
mongorestore --gzip --nsFrom='tryout.*' --nsTo='tryout2.*' changes-2013
mongorestore --gzip --nsFrom='tryout.*' --nsTo='tryout2.*' route-changes-2021-01
mongorestore --gzip --nsFrom='tryout.*' --nsTo='tryout2.*' route-changes-2021-02
```
