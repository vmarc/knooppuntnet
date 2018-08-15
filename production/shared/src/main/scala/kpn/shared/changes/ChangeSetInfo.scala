package kpn.shared.changes

import kpn.shared.Timestamp
import kpn.shared.data.Tagable
import kpn.shared.data.Tags

/*
<osm version="0.6" generator="OpenStreetMap server" copyright="OpenStreetMap and contributors" attribution="http://www.openstreetmap.org/copyright"
	 license="http://opendatacommons.org/licenses/odbl/1-0/">
	<changeset id="32016913" user="Jakka" uid="2403313" created_at="2015-06-16T20:51:52Z" closed_at="2015-06-16T20:51:52Z" open="false" comments_count="0">
		<tag k="source" v="org.openstreetmap.josm.gui.tagging.TagModel@defc73"/>
		<tag k="created_by" v="JOSM/1.5 (8339 nl)"/>
		<tag k="comment" v="rcn 41-49 volgorde bijgewerkt"/>
	</changeset>
</osm>
*/

case class ChangeSetInfo(
  id: Long,
  createdAt: Timestamp,
  closedAt: Option[Timestamp],
  open: Boolean,
  commentsCount: Int,
  tags: Tags
) extends Tagable
