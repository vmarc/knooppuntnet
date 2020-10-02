Summary of release 3.0.0 tasks

Route planner
- [x] issue #91 missing links in network graph
- [x] issue #90 location edit
- [x] issue #89 via marker on route
- [x] issue #86 lower zoom level tiles with surface/surveydate info
- [ ] better handling of "no route to destination"
- [ ] show spinner while preparing pdf export from planner map
- [x] review/test directions (left and right still mixed up?), remove when not good enough
- [x] user guide


Analysis client
- [ ] location based analysis
- [ ] location names in prefered language
- [ ] location changes
- [ ] user guide


Client in general
- [ ] better error handling when server not available
- [x] cleanup status pages or make private
- [x] German translation
- [x] French translation


Server
- [x] improve stability of database connections after move to Germany (retries)
- [x] investigate improved styling for background tiles?
- [x] improve route analysis performance (indexes vs hash)
- [x] further complete installation of new server and migration from old server
- [x] configure nginx to forward old url's to new url's
- [x] historical processing complete


Post 3.0.0 release
- [ ] further elaborate/finetune point of interest definitions
- [ ] issue #82 multiple markers on same node
- [ ] issue #83 surface preference
- [ ] issue #85 elevation profile
- [ ] issue #95 ferry
- [ ] issue #106 cobblestone surface
- [ ] improved strategy for update of routing graph
- [ ] realtime network and poi tile generation (batch lower zoom levels)
- [ ] database maintenance job (compact databases and database views) 
- [ ] automate background tile generation (+show update timestamp in ui)
- [ ] Talk to owner of knotenpunktnetz.de?
- [ ] monitor servers
- [ ] Planner demo video?
- [ ] Spain
- [ ] Technical guide (English only)
