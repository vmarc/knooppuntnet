import {Section} from "../../../../../model";
import {fromLonLat} from 'ol/proj';
import {LineString} from 'ol/geom';
import VectorSource from 'ol/source/Vector';
import Feature from 'ol/Feature';
import Style from 'ol/style/Style';
import Stroke from 'ol/style/Stroke';
import {MapNodeStyling} from "../styling";

const style = new Style({
  stroke: new Stroke({
    color: [0, 0, 255, 0.8],
    width: 10
  })
});


export function buildVectorSource(sections: Section[], features: Feature[]): VectorSource {
  let featureArray = [];

  sections.forEach((value: Section) => {
    const routeCoordinates = value.coordinates.map(c => fromLonLat([+c.lon, +c.lat]));
    const feature = new Feature({
      geometry: new LineString(routeCoordinates)
    });

    feature.setStyle(style);
    featureArray.push(feature);
  });

  features.forEach(f => {
    let feature = f.clone();
    feature.setStyle(MapNodeStyling.selectedNodeStyle(f.values_.name));
    featureArray.push(feature);
  });

  return new VectorSource({
    features: featureArray
  })
}

