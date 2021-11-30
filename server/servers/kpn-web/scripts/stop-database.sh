mongosh mongodb://127.0.0.1:9000 <<***eof***
db.adminCommand( { shutdown: 1 } )
***eof***
