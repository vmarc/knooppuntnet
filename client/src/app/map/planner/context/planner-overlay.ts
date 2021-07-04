import { Coordinate } from 'ol/coordinate';
import { NodeClick } from '../../../components/ol/domain/node-click';
import { PoiClick } from '../../../components/ol/domain/poi-click';
import { RouteClick } from '../../../components/ol/domain/route-click';

export interface PlannerOverlay {
  poiClicked(poiClick: PoiClick): void;

  nodeClicked(nodeClick: NodeClick): void;

  routeClicked(routeClick: RouteClick): void;

  setPosition(coordinate: Coordinate, verticalOffset: number): void;
}
