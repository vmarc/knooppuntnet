import {Component, OnInit} from "@angular/core";
import {MapService} from "../../../components/ol/map.service";

@Component({
  selector: "kpn-map-page",
  template: `
    <div>
      <a routerLink="/" class="breadcrumb-link" i18n="@@breadcrumb.home">Home</a>
      <span i18n="@@breadcrumb.map">Map</span>
    </div>

    <kpn-page-header subject="planner" i18n="@@planner.map">Map</kpn-page-header>

    <kpn-icon-button routerLink="/map/cycling" icon="cycling" i18n="@@network-type.cycling">
      Cycling
    </kpn-icon-button>

    <kpn-icon-button routerLink="/map/hiking" icon="hiking" i18n="@@network-type.hiking">
      Hiking
    </kpn-icon-button>

    <kpn-icon-button routerLink="/map/horse-riding" icon="horse-riding" i18n="@@network-type.horse-riding">
      Horse riding
    </kpn-icon-button>

    <kpn-icon-button routerLink="/map/motorboat" icon="motorboat" i18n="@@network-type.motorboat">
      Motorboat
    </kpn-icon-button>

    <kpn-icon-button routerLink="/map/canoe" icon="canoe" i18n="@@network-type.canoe">
      Canoe
    </kpn-icon-button>

    <kpn-icon-button routerLink="/map/inline-skating" icon="inline-skating" i18n="@@network-type.inline-skating">
      Inline skating
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
