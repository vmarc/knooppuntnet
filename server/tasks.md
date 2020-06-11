Summary of release 3.0.0 tasks

Route planner
- [ ] issue #91 missing links in network graph
- [x] issue #90 location edit
- [ ] issue #89 via marker on route
- [ ] issue #86 lower zoom level tiles with surface/surveydate info
- [ ] issue #82 multiple markers on same node
- [ ] better handling of "no route to destination"
- [ ] show spinner while preparing pdf export from planner map
- [ ] review/test directions (left and right still mixed up?), remove when not good enough
- [ ] user guide

Analysis client
- [ ] location based analysis
- [ ] location names in prefered language
- [ ] location changes
- [ ] user guide

Client in general
- [ ] better error handling when server not available
- [ ] cleanup status pages or make private
- [ ] German translation
- [ ] French translation

Server
- [ ] further complete installation of new server and migration from old server
- [ ] realtime network and poi tile generation (batch lower zoom levels)
- [ ] monitor servers
- [x] improve stability of database connections after move to Germany (retries)
- [ ] database maintenance jobs (compaction)
- [ ] automate background tile generation (+show update timestamp in ui)
- [ ] investigate improved styling for background tiles?
- [ ] improve route analysis performance (indexes vs hash)
- [ ] configure nginx to forward old url's to new url's
- [ ] Talk to owner of knotenpunktnetz.de?
- [ ] further complete historical processing
- [ ] Technical guide (English only)

Docs
- [ ] Planner demo video?
- [ ] GPDR/cookies?

Post 3.0.0 release
- [ ] further elaborate/finetune point of interest definitions
- [ ] issue #83 Surface preference
- [ ] issue #85 Elevation profile
- [ ] improved strategy for update of routing graph
