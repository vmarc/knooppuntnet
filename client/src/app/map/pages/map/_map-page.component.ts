import {Component, OnInit} from "@angular/core";
import {MapService} from "../../../components/ol/map.service";

@Component({
  selector: "kpn-map-page",
  template: `
    <div>
      <a routerLink="/" i18n="@@breadcrumb.home">Home</a> >
      <span i18n="@@breadcrumb.map">Map</span>
    </div>

    <kpn-page-header subject="planner" i18n="@@planner.map">Map</kpn-page-header>

    <kpn-icon-button
      routerLink="/map/cycling"
      icon="cycling"
      text="Cycling"
      i18n-text="@@network-type.cycling">
    </kpn-icon-button>

    <kpn-icon-button
      routerLink="/map/hiking"
      icon="hiking"
      text="Hiking"
      i18n-text="@@network-type.hiking">
    </kpn-icon-button>

    <kpn-icon-button
      routerLink="/map/horse"
      icon="horse"
      text="Horse"
      i18n-text="@@network-type.horse">
    </kpn-icon-button>

    <kpn-icon-button
      routerLink="/map/motorboat"
      icon="motorboat"
      text="Motorboat"
      i18n-text="@@network-type.motorboat">
    </kpn-icon-button>

    <kpn-icon-button
      routerLink="/map/canoe"
      icon="canoe"
      text="Canoe"
      i18n-text="@@network-type.canoe">
    </kpn-icon-button>

    <kpn-icon-button
      routerLink="/map/inline-skating"
      icon="inline-skating"
      text="Inline skating"
      i18n-text="@@network-type.inline-skating">
    </kpn-icon-button>
  `
})
export class MapPageComponent implements OnInit {

  constructor(private mapService: MapService) {
  }

  ngOnInit(): void {
    this.mapService.networkType.next(null);
  }

}
