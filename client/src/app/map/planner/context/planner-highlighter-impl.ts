import {PlanNode} from '../../../kpn/api/common/planner/plan-node';
import {RouteFeature} from '../features/route-feature';
import {PlannerHighlighter} from './planner-highlighter';
import {Point} from 'ol/geom';
import {LineString} from 'ol/geom';
import {MultiLineString} from 'ol/geom';
import {Feature} from 'ol';
import RenderFeature from 'ol/render/Feature';
import GeometryType from 'ol/geom/GeometryType';
import GeometryLayout from 'ol/geom/GeometryLayout';
import {PlannerHighlightLayer} from './planner-highlight-layer';

export class PlannerHighlighterImpl implements PlannerHighlighter {

  constructor(private readonly layer: PlannerHighlightLayer) {
  }

  highlightNode(node: PlanNode): void {
    const point = new Point(node.coordinate);
    const feature = new Feature(point);
    this.layer.highlightFeature(feature);
  }

  highlightRoute(routeFeature: RouteFeature): void {
    if (routeFeature.feature instanceof RenderFeature) {
      const renderFeature = routeFeature.feature as RenderFeature;
      const geometryType = renderFeature.getType();
      if (geometryType === GeometryType.LINE_STRING) {
        const coordinates: number[] = renderFeature.getOrientedFlatCoordinates();
        const lineString = new LineString(coordinates, GeometryLayout.XY);
        const feature = new Feature(lineString);
        this.layer.highlightFeature(feature);
      } else if (geometryType === GeometryType.MULTI_LINE_STRING) {
        const coordinates: number[] = renderFeature.getOrientedFlatCoordinates();
        const ends: number[] = [];
        renderFeature.getEnds().forEach(num => {
          if (typeof num === 'number') {
            ends.push(num);
          }
        });
        const lineString = new MultiLineString(coordinates, GeometryLayout.XY, ends);
        const feature = new Feature(lineString);
        this.layer.highlightFeature(feature);
      } else {
        console.log('OTHER GEOMETRY TYPE ' + geometryType);
      }
    }
  }

  reset(): void {
    this.layer.reset();
  }

}
