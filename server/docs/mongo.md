# MongoDB

This documents the experiments with MongoDB to see whether it would be a good replacment for CouchDB in knooppuntnet.

## Todo
- [x] Install
- [ ] Security (port, admin user, application user, ssl)
- [ ] Verify security implications of commenting out line with bindIp in /etc/mongod.conf to allow connections from other hosts.
- [ ] Populate database
- [ ] Queries

## Install

Using instructions found on:
https://docs.mongodb.com/manual/tutorial/install-mongodb-on-ubuntu/.

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

## Install Compass

Use instructions on https://docs.mongodb.com/compass/current/install.

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




