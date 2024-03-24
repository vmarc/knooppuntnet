import { Coordinate } from 'ol/coordinate';
import { NodeClick } from '../interaction/actions/node-click';
import { PoiClick } from '../interaction/actions/poi-click';
import { RouteClick } from '../interaction/actions/route-click';

export interface PlannerPopup {
  poiClicked(poiClick: PoiClick): void;

  nodeClicked(nodeClick: NodeClick): void;

  routeClicked(routeClick: RouteClick): void;

  setPosition(coordinate: Coordinate, verticalOffset: number): void;

  reset(): void;
}
