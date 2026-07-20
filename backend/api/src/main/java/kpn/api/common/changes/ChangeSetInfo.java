package kpn.api.common.changes;

import kpn.api.common.data.Tagable;
import kpn.api.custom.Tag;
import kpn.api.custom.Timestamp;
import kpn.core.doc.WithId;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

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
public record ChangeSetInfo(
  Long _id,
  Long id,
  Timestamp createdAt,
  Optional<Timestamp> closedAt,
  Boolean open,
  Long commentsCount,
  ImmutableList<Tag> tags
) implements Tagable, WithId {
}
