import { NodeClick } from '@app/components/ol/domain/node-click';
import { PoiClick } from '@app/components/ol/domain/poi-click';
import { RouteClick } from '@app/components/ol/domain/route-click';
import { Coordinate } from 'ol/coordinate';

export interface PlannerOverlay {
  poiClicked(poiClick: PoiClick): void;

  nodeClicked(nodeClick: NodeClick): void;

  routeClicked(routeClick: RouteClick): void;

  setPosition(coordinate: Coordinate, verticalOffset: number): void;
}
