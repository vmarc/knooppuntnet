import Map from "ol/Map";
import Overlay from "ol/Overlay";
import {Coordinate} from "ol/coordinate";
import {PlannerOverlay} from "./planner-overlay";
import {MapService} from "../../../components/ol/map.service";
import {PoiClick} from "../../../components/ol/domain/poi-click";

export class PlannerOverlayImpl implements PlannerOverlay {

  private overlay: Overlay;

  constructor(private mapService: MapService) {
  }

  addToMap(map: Map) {
    this.overlay = map.getOverlayById("popup");
    this.overlay.setOffset([0, -30]);
  }

  poiClicked(poiClick: PoiClick): void {
    this.mapService.poiClicked(poiClick);
  }

  setPosition(coordinate: Coordinate): void {
    this.overlay.setPosition(coordinate);
  }

}
