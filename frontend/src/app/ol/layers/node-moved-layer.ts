import { NodeMoved } from '@api/common/diff/node';
import { OlUtil } from '@app/ol';
import Feature from 'ol/Feature';
import LineString from 'ol/geom/LineString';
import VectorLayer from 'ol/layer/Vector';
import VectorSource from 'ol/source/Vector';
import Stroke from 'ol/style/Stroke';
import Style from 'ol/style/Style';
import { Marker } from '../domain';
import { OldMapLayer } from './old-map-layer';

export class NodeMovedLayer {
  public static build(nodeMoved: NodeMoved): OldMapLayer {
    const before = OlUtil.latLonToCoordinate(nodeMoved.before);
    const after = OlUtil.latLonToCoordinate(nodeMoved.after);
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

    const name = $localize`:@@map.layer.node-moved:Node moved`;
    return OldMapLayer.build('node-moved-layer', name, layer);
  }
}
