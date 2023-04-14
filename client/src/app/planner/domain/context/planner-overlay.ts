import { NodeClick } from '@app/components/ol/domain';
import { PoiClick } from '@app/components/ol/domain';
import { RouteClick } from '@app/components/ol/domain';
import { Coordinate } from 'ol/coordinate';

export interface PlannerOverlay {
  poiClicked(poiClick: PoiClick): void;

  nodeClicked(nodeClick: NodeClick): void;

  routeClicked(routeClick: RouteClick): void;

  setPosition(coordinate: Coordinate, verticalOffset: number): void;
}
