import {Component, OnInit} from "@angular/core";
import {MapService} from "../../../components/ol/map.service";

@Component({
  selector: "kpn-map-page",
  template: `
    <div>
      <a routerLink="/" i18n="@@breadcrumb.home">Home</a> >
      <span i18n="@@breadcrumb.map">Map</span>
    </div>

    <kpn-page-header [subject]="'planner'" [title]="'Map'"></kpn-page-header>
    
    <kpn-icon-button
      routerLink="/map/cycling"
      icon="cycling"
      text="Cycling">
    </kpn-icon-button>

    <kpn-icon-button
      routerLink="/map/hiking"
      icon="hiking"
      text="Hiking">
    </kpn-icon-button>

    <kpn-icon-button
      routerLink="/map/horse"
      icon="horse"
      text="Horse">
    </kpn-icon-button>

    <kpn-icon-button
      routerLink="/map/motorboat"
      icon="motorboat"
      text="Motorboat">
    </kpn-icon-button>

    <kpn-icon-button
      routerLink="/map/canoe"
      icon="canoe"
      text="Canoe">
    </kpn-icon-button>

    <kpn-icon-button
      routerLink="/map/inline-skating"
      icon="inline-skating"
      text="Inline skating">
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
