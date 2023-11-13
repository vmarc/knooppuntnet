import { Marker } from '@app/components/ol/domain';
import { Layers } from '@app/components/ol/layers';
import { List } from 'immutable';
import { Coordinate } from 'ol/coordinate';
import Feature from 'ol/Feature';
import Geometry from 'ol/geom/Geometry';
import Point from 'ol/geom/Point';
import VectorLayer from 'ol/layer/Vector';
import Map from 'ol/Map';
import VectorSource from 'ol/source/Vector';
import { PlanFlag } from '../plan/plan-flag';
import { PlanFlagType } from '../plan/plan-flag-type';
import { PlannerMarkerLayer } from './planner-marker-layer';

export class PlannerMarkerLayerImpl extends PlannerMarkerLayer {
  private source = new VectorSource();

  private layer = new VectorLayer({
    zIndex: Layers.zIndexPlannerMarkerLayer,
    source: this.source,
  });

  addToMap(map: Map) {
    map.addLayer(this.layer);
  }

  addFlag(flag: PlanFlag): void {
    if (flag !== null && flag.flagType !== PlanFlagType.invisible) {
      let markerColor = 'blue';
      if (flag.flagType === PlanFlagType.end) {
        markerColor = 'green';
      } else if (flag.flagType === PlanFlagType.via) {
        markerColor = 'orange';
      }
      const marker = Marker.create(markerColor, flag.coordinate);
      marker.setId(flag.featureId);
      marker.set('layer', 'flag');
      marker.set('flag-type', flag.flagType);
      this.source.addFeature(marker);
    }
  }

  removeFlag(flag: PlanFlag): void {
    if (flag !== null) {
      const feature = this.source.getFeatureById(flag.featureId);
      if (feature != null) {
        this.source.removeFeature(feature);
      }
    }
  }

  removeFlagWithFeatureId(featureId: string): void {
    const feature = this.source.getFeatureById(featureId);
    if (feature != null) {
      this.source.removeFeature(feature);
    }
  }

  updateFlag(flag: PlanFlag): void {
    if (flag !== null) {
      this.removeFlagWithFeatureId(flag.featureId);
      this.addFlag(flag);
    }
  }

  updateFlagCoordinate(featureId: string, coordinate: Coordinate): void {
    const feature = this.source.getFeatureById(featureId);
    if (feature) {
      (feature.getGeometry() as Point).setCoordinates(coordinate);
    }
  }

  features(): List<Feature<Geometry>> {
    return List(this.source.getFeatures());
  }
}
