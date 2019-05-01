import {Component, OnInit} from "@angular/core";
import {MapService} from "../../../components/ol/map.service";

@Component({
  selector: "kpn-map-page",
  template: `
    <div>
      <a routerLink="/" i18n="@@breadcrumb.home">Home</a> >
      <span i18n="@@breadcrumb.map">Map</span>
    </div>

    <h1>
      Map
    </h1>

    <kpn-icon-button
      routerLink="/map/rcn"
      icon="rcn"
      text="Cycling">
    </kpn-icon-button>

    <kpn-icon-button
      routerLink="/map/rwn"
      icon="rwn"
      text="Hiking">
    </kpn-icon-button>

    <kpn-icon-button
      routerLink="/map/rhn"
      icon="rhn"
      text="Horse">
    </kpn-icon-button>

    <kpn-icon-button
      routerLink="/map/rmn"
      icon="rmn"
      text="Motorboat">
    </kpn-icon-button>

    <kpn-icon-button
      routerLink="/map/rpn"
      icon="rpn"
      text="Canoe">
    </kpn-icon-button>

    <kpn-icon-button
      routerLink="/map/rin"
      icon="rin"
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
