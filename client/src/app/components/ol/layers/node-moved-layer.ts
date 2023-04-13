import { NodeMoved } from '@api/common/diff/node';
import Feature from 'ol/Feature';
import LineString from 'ol/geom/LineString';
import VectorLayer from 'ol/layer/Vector';
import VectorSource from 'ol/source/Vector';
import Stroke from 'ol/style/Stroke';
import Style from 'ol/style/Style';
import { Util } from '../../shared';
import { Marker } from '../domain';
import { MapLayer } from './map-layer';

export class NodeMovedLayer {
  public static build(nodeMoved: NodeMoved): MapLayer {
    const before = Util.latLonToCoordinate(nodeMoved.before);
    const after = Util.latLonToCoordinate(nodeMoved.after);
    const nodeMarker = Marker.create('blue', after);

    const displacement = new Feature(new LineString([before, after]));
    displacement.setStyle(
      new Style({
        stroke: new Stroke({
          color: 'red',
          width: 5,
        }),
      })
    );

    const source = new VectorSource();
    source.addFeature(displacement);
    source.addFeature(nodeMarker);
    const layer = new VectorLayer({
      source,
    });

    return MapLayer.simpleLayer('node-moved-layer', layer);
  }
}
