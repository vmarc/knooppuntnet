import {Coordinate} from "ol/coordinate";
import Point from "ol/geom/Point";
import VectorLayer from "ol/layer/Vector";
import Map from "ol/Map";
import VectorSource from "ol/source/Vector";
import {Marker} from "../../../components/ol/domain/marker";
import {Layers} from "../../../components/ol/layers/layers";
import {PlanFlag} from "../plan/plan-flag";
import {PlanFlagType} from "../plan/plan-flag-type";
import {PlannerMarkerLayer} from "./planner-marker-layer";
import Feature from "ol/Feature";
import {List} from "immutable";
import Geometry from "ol/geom/Geometry";

export class PlannerMarkerLayerImpl extends PlannerMarkerLayer {

  private source = new VectorSource();

  private layer = new VectorLayer({
    zIndex: Layers.zIndexPlannerMarkerLayer,
    source: this.source
  });

  addToMap(map: Map) {
    map.addLayer(this.layer);
  }

  addFlag(flag: PlanFlag): void {
    if (flag !== null && flag.flagType !== PlanFlagType.Invisible) {
      let markerColor = "blue";
      if (flag.flagType === PlanFlagType.End) {
        markerColor = "green";
      } else if (flag.flagType === PlanFlagType.Via) {
        markerColor = "orange";
      }
      const marker = Marker.create(markerColor, flag.coordinate);
      marker.setId(flag.featureId);
      marker.set("layer", "flag");
      marker.set("flag-type", flag.flagType);
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
