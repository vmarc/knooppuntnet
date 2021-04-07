import { Coordinate } from 'ol/coordinate';
import Map from 'ol/Map';
import Overlay from 'ol/Overlay';
import { NodeClick } from '../../../components/ol/domain/node-click';
import { PoiClick } from '../../../components/ol/domain/poi-click';
import { RouteClick } from '../../../components/ol/domain/route-click';
import { MapService } from '../../../components/ol/services/map.service';
import { PlannerOverlay } from './planner-overlay';

export class PlannerOverlayImpl implements PlannerOverlay {
  private overlay: Overlay;

  constructor(private mapService: MapService) {}

  addToMap(map: Map) {
    this.overlay = map.getOverlayById('popup');
  }

  poiClicked(poiClick: PoiClick): void {
    this.mapService.nextPoiClick(poiClick);
  }

  nodeClicked(nodeClick: NodeClick): void {
    this.mapService.nextNodeClick(nodeClick);
  }

  routeClicked(routeClick: RouteClick): void {
    this.mapService.nextRouteClick(routeClick);
  }

  setPosition(coordinate: Coordinate, verticalOffset: number): void {
    this.overlay.setOffset([0, verticalOffset]);
    this.overlay.setPosition(coordinate);
  }
}
