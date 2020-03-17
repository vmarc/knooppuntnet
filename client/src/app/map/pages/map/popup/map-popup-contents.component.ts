import {Component} from "@angular/core";
import {MapService} from "../../../../components/ol/map.service";

@Component({
  selector: "kpn-map-popup-contents",
  template: `
    <kpn-map-popup-poi *ngIf="isPoi()"></kpn-map-popup-poi>
    <kpn-map-popup-node *ngIf="isNode()"></kpn-map-popup-node>
    <kpn-map-popup-route *ngIf="isRoute()"></kpn-map-popup-route>
  `
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
