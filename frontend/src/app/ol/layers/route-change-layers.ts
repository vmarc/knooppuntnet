import { GeometryDiff } from '@api/common/route/geometry-diff';
import { PointSegment } from '@api/common/route/point-segment';
import { OlUtil } from '@app/ol/ol-util';
import { List } from 'immutable';
import { Color } from 'ol/color';
import Feature from 'ol/Feature';
import LineString from 'ol/geom/LineString';
import VectorLayer from 'ol/layer/Vector';
import VectorSource from 'ol/source/Vector';
import { Stroke } from 'ol/style';
import { Style } from 'ol/style';
import { OldLayers } from './old-layers';
import { OldMapLayer } from './old-map-layer';

export class RouteChangeLayers {
  build(geometryDiff: GeometryDiff): List<OldMapLayer> {
    const unchanged = this.segmentLayer(
      'map.layer.unchanged',
      $localize`:@@map.layer.unchanged:Unchanged`,
      geometryDiff.common,
      5,
      [0, 0, 255]
    );

    const added = this.segmentLayer(
      'map.layer.added',
      $localize`:@@map.layer.added:Added`,
      geometryDiff.after,
      12,
      [0, 255, 0]
    );

    const deleted = this.segmentLayer(
      'map.layer.deleted',
      $localize`:@@map.layer.deleted:Deleted`,
      geometryDiff.before,
      3,
      [255, 0, 0]
    );

    return List([unchanged, added, deleted]).filter((layer) => layer !== null);
  }

  private segmentLayer(
    id: string,
    name: string,
    segments: string[],
    width: number,
    color: Color
  ): OldMapLayer {
    if (segments.length === 0) {
      return null;
    }

    const style = new Style({
      stroke: new Stroke({
        color,
        width,
      }),
    });

    const source = new VectorSource();
    segments.forEach((segment) => {
      const feature = new Feature(new LineString(JSON.parse(segment)));
      feature.setStyle(style);
      source.addFeature(feature);
    });

    const layer = new VectorLayer({
      zIndex: OldLayers.zIndexNetworkLayer,
      source,
    });
    return OldMapLayer.build(id, name, layer);
  }
}
