import {Component} from '@angular/core';

@Component({
  selector: 'kpn-map-page',
  template: `
    <h1>
      Kaart
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
export class MapPageComponent {
}
