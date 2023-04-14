import { PlanNode } from '@api/common/planner';
import { Feature } from 'ol';
import { Coordinate } from 'ol/coordinate';
import { Point } from 'ol/geom';
import { LineString } from 'ol/geom';
import { MultiLineString } from 'ol/geom';
import GeometryLayout from 'ol/geom/GeometryLayout';
import GeometryType from 'ol/geom/GeometryType';
import RenderFeature from 'ol/render/Feature';
import { RouteFeature } from '../features/route-feature';
import { PlannerHighlightLayer } from './planner-highlight-layer';
import { PlannerHighlighter } from './planner-highlighter';

export class PlannerHighlighterImpl implements PlannerHighlighter {
  constructor(private readonly layer: PlannerHighlightLayer) {}

  mouseDown(coordinate: Coordinate): void {
    const point = new Point(coordinate);
    const feature = new Feature(point);
    feature.set('mouse-down', 'true');
    this.layer.highlightFeature(feature);
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
        const coordinates: number[] =
          renderFeature.getOrientedFlatCoordinates();
        const lineString = new LineString(coordinates, GeometryLayout.XY);
        const feature = new Feature(lineString);
        this.layer.highlightFeature(feature);
      } else if (geometryType === GeometryType.MULTI_LINE_STRING) {
        const coordinates: number[] =
          renderFeature.getOrientedFlatCoordinates();
        const ends: number[] = [];
        renderFeature.getEnds().forEach((num) => {
          if (typeof num === 'number') {
            ends.push(num);
          }
        });
        const lineString = new MultiLineString(
          coordinates,
          GeometryLayout.XY,
          ends
        );
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
