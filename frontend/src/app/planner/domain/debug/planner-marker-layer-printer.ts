import { OlUtil } from '@app/ol/ol-util';
import { PlannerMarkerLayer } from '../context/planner-marker-layer';
import { PlannerMarkerLayerImpl } from '../context/planner-marker-layer-impl';
import Feature from 'ol/Feature';
import Geometry from 'ol/geom/Geometry';
import { Printer } from './printer';
import { Point } from 'ol/geom';

export class PlannerMarkerLayerPrinter {
  private out = new Printer();

  layer(plannerMarkerLayer: PlannerMarkerLayer): Printer {
    if (plannerMarkerLayer instanceof PlannerMarkerLayerImpl) {
      const features = (plannerMarkerLayer as PlannerMarkerLayerImpl).features();
      this.out.println(`markerLayer (${features.size} features)`);
      features.forEach((feature) => {
        const featureLayer = OlUtil.featureLayer(feature);
        if (featureLayer === 'flag') {
          this.printFlag(feature);
        } else {
          this.out.println('  layer=' + featureLayer);
        }
      });
    }
    return this.out;
  }

  private printFlag(flag: Feature<Geometry>): void {
    const point = flag.getGeometry() as Point;
    const flagType = flag.get('flag-type');
    this.out.println(`  flag ${flagType} ${this.out.coordinate(point.getCoordinates())}`);
  }
}
