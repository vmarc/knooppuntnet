import Map from "ol/Map";
import Overlay from "ol/Overlay";
import Coordinate from "ol/coordinate";
import {PlannerOverlay} from "./planner-overlay";
import {MapService} from "../../../components/ol/map.service";
import {PoiId} from "../../../components/ol/domain/poi-id";

export class PlannerOverlayImpl implements PlannerOverlay {

  private overlay: Overlay;

  constructor(private mapService: MapService) {
  }

  addToMap(map: Map) {
    this.overlay = map.getOverlayById("popup");
    this.overlay.setOffset([0, -30]);
  }

  setPosition(coordinate: Coordinate, poiId: PoiId): void {
    this.mapService.poiClicked(poiId);
    this.overlay.setPosition(coordinate);
  }

}
