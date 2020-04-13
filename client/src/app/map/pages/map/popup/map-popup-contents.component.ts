import {Component} from "@angular/core";
import {MapService} from "../../../../components/ol/services/map.service";

@Component({
  selector: "kpn-map-popup-contents",
  template: `
    <div [ngClass]="{hidden: !isPoi()}">
      <kpn-map-popup-poi></kpn-map-popup-poi>
    </div>
    <div [ngClass]="{hidden: !isNode()}">
      <kpn-map-popup-node></kpn-map-popup-node>
    </div>
    <div [ngClass]="{hidden: !isRoute()}">
      <kpn-map-popup-route></kpn-map-popup-route>
    </div>
  `,
  styles: [`
    .hidden {
      display: none;
    }
  `]
})
export class MapPopupContentsComponent {

  constructor(private mapService: MapService) {
  }

  isPoi() {
    return this.mapService.popupType === "poi";
  }

  isNode() {
    return this.mapService.popupType === "node";
  }

  isRoute() {
    return this.mapService.popupType === "route";
  }
}
