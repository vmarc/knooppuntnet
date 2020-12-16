import {NodeMoved} from '@api/common/diff/node/node-moved';
import Feature from 'ol/Feature';
import LineString from 'ol/geom/LineString';
import VectorLayer from 'ol/layer/Vector';
import VectorSource from 'ol/source/Vector';
import Stroke from 'ol/style/Stroke';
import Style from 'ol/style/Style';
import {Util} from '../../shared/util';
import {Marker} from '../domain/marker';
import {MapLayer} from './map-layer';

export class NodeMovedLayer {

  public static build(nodeMoved: NodeMoved): MapLayer {

    const before = Util.latLonToCoordinate(nodeMoved.before);
    const after = Util.latLonToCoordinate(nodeMoved.after);
    const nodeMarker = Marker.create('blue', after);

    const displacement = new Feature(new LineString([before, after]));
    displacement.setStyle(new Style({
      stroke: new Stroke({
        color: 'red',
        width: 5
      })
    }));

    const source = new VectorSource();
    source.addFeature(displacement);
    source.addFeature(nodeMarker);
    const layer = new VectorLayer({
      source
    });

    return new MapLayer('node-moved-layer', layer);
  }

}
