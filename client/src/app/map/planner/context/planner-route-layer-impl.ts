import { Coordinate } from 'ol/coordinate';
import Feature from 'ol/Feature';
import LineString from 'ol/geom/LineString';
import Point from 'ol/geom/Point';
import VectorLayer from 'ol/layer/Vector';
import Map from 'ol/Map';
import VectorSource from 'ol/source/Vector';
import Stroke from 'ol/style/Stroke';
import Style from 'ol/style/Style';
import { Marker } from '../../../components/ol/domain/marker';
import { Layers } from '../../../components/ol/layers/layers';
import { PlanFlag } from '../plan/plan-flag';
import { PlanFlagType } from '../plan/plan-flag-type';
import { PlanLeg } from '../plan/plan-leg';
import { PlanUtil } from '../plan/plan-util';
import { PlannerRouteLayer } from './planner-route-layer';

export class PlannerRouteLayerImpl extends PlannerRouteLayer {
  private legStyle = new Style({
    stroke: new Stroke({
      color: 'rgba(255, 0, 255, 0.5)',
      width: 12,
    }),
  });

  private source = new VectorSource();

  private layer = new VectorLayer({
    zIndex: Layers.zIndexPlannerRouteLayer,
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

  addPlanLeg(leg: PlanLeg): void {
    this.removePlanLeg(leg.featureId);
    const feature = new Feature(
      new LineString(
        leg.routes
          .flatMap((route) => PlanUtil.planRouteCoordinates(route).toArray())
          .toArray()
      )
    );
    feature.setId(leg.featureId);
    feature.set('layer', 'leg');
    feature.setStyle(this.legStyle);
    this.source.addFeature(feature);
  }

  removePlanLeg(legId: string): void {
    const feature = this.source.getFeatureById(legId);
    if (feature) {
      this.source.removeFeature(feature);
    }
  }
}
