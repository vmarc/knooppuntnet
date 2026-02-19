import { GeometryDiff } from '@api/common/route/geometry-diff';
import { WayGeometry } from '@api/common/route/way-geometry';
import { CoordinateCodec } from '@app/ol/layers/coordinate-codec';
import { Color } from 'ol/color';
import Feature from 'ol/Feature';
import LineString from 'ol/geom/LineString';
import VectorLayer from 'ol/layer/Vector';
import { fromLonLat } from 'ol/proj';
import VectorSource from 'ol/source/Vector';
import { Stroke } from 'ol/style';
import { Style } from 'ol/style';
import { OldOldLayers } from './old-old-layers';
import { OldOldMapLayer } from './old-old-map-layer';

export class RouteChangeLayers {
  build(geometryDiff: GeometryDiff): OldOldMapLayer[] {
    const unchanged = this.unchangedLayer(
      'map.layer.unchanged',
      $localize`:@@map.layer.unchanged:Unchanged`,
      geometryDiff,
      5,
      [0, 0, 255]
    );

    const added = this.addedLayer(
      'map.layer.added',
      $localize`:@@map.layer.added:Added`,
      geometryDiff,
      12,
      [0, 255, 0]
    );

    const deleted = this.removedLayer(
      'map.layer.deleted',
      $localize`:@@map.layer.deleted:Deleted`,
      geometryDiff,
      3,
      [255, 0, 0]
    );

    return [unchanged, added, deleted].filter((layer) => layer !== null);
  }

  private unchangedLayer(
    id: string,
    name: string,
    geometryDiff: GeometryDiff,
    width: number,
    color: Color
  ): OldOldMapLayer {
    const unchanged: WayGeometry[] = [];
    unchanged.push(...geometryDiff.common);
    const wayGeometries = geometryDiff.update.flatMap((update) => {
      if (update.common && update.common.length > 0) {
        return update.common.map((line) => {
          const wg: WayGeometry = { wayId: update.wayId, line: line };
          return wg;
        });
      }
      return [];
    });
    unchanged.push(...wayGeometries);

    if (unchanged.length === 0) {
      return null;
    }

    const style = new Style({
      stroke: new Stroke({
        color,
        width,
      }),
    });

    const source = new VectorSource();
    unchanged.forEach((wayGeometry) => {
      const line = wayGeometry.line;
      const coordinates = CoordinateCodec.decode(line.line);
      const transformedCoordinates = coordinates.map((coord) => fromLonLat([coord[1], coord[0]]));
      const feature = new Feature(new LineString(transformedCoordinates));
      feature.set('wayId', wayGeometry.wayId);
      feature.setStyle(style);
      source.addFeature(feature);
    });

    const layer = new VectorLayer({
      zIndex: OldOldLayers.zIndexNetworkLayer,
      source,
    });
    return OldOldMapLayer.build(id, name, layer);
  }

  private addedLayer(
    id: string,
    name: string,
    geometryDiff: GeometryDiff,
    width: number,
    color: Color
  ): OldOldMapLayer {
    const wayGeometries = geometryDiff.update.flatMap((update) => {
      if (update.added && update.added.length > 0) {
        return update.added.map((line) => {
          const wg: WayGeometry = { wayId: update.wayId, line: line };
          return wg;
        });
      }
      return [];
    });

    if (wayGeometries.length === 0) {
      return null;
    }

    const style = new Style({
      stroke: new Stroke({
        color,
        width,
      }),
    });

    const source = new VectorSource();
    wayGeometries.forEach((wayGeometry) => {
      const line = wayGeometry.line;
      const coordinates = CoordinateCodec.decode(line.line);
      const transformedCoordinates = coordinates.map((coord) => fromLonLat([coord[1], coord[0]]));
      const feature = new Feature(new LineString(transformedCoordinates));
      feature.set('wayId', wayGeometry.wayId);
      feature.setStyle(style);
      source.addFeature(feature);
    });

    const layer = new VectorLayer({
      zIndex: OldOldLayers.zIndexNetworkLayer,
      source,
    });
    return OldOldMapLayer.build(id, name, layer);
  }

  private removedLayer(
    id: string,
    name: string,
    geometryDiff: GeometryDiff,
    width: number,
    color: Color
  ): OldOldMapLayer {
    const wayGeometries = geometryDiff.update.flatMap((update) => {
      if (update.removed && update.removed.length > 0) {
        return update.removed.map((line) => {
          const wg: WayGeometry = { wayId: update.wayId, line: line };
          return wg;
        });
      } else {
        return [];
      }
    });

    if (wayGeometries.length === 0) {
      return null;
    }

    const style = new Style({
      stroke: new Stroke({
        color,
        width,
      }),
    });

    const source = new VectorSource();
    wayGeometries.forEach((wayGeometry) => {
      const line = wayGeometry.line;
      const coordinates = CoordinateCodec.decode(line.line);
      const transformedCoordinates = coordinates.map((coord) => fromLonLat([coord[1], coord[0]]));
      const feature = new Feature(new LineString(transformedCoordinates));
      feature.set('wayId', wayGeometry.wayId);
      feature.setStyle(style);
      source.addFeature(feature);
    });

    const layer = new VectorLayer({
      zIndex: OldOldLayers.zIndexNetworkLayer,
      source,
    });
    return OldOldMapLayer.build(id, name, layer);
  }
}
