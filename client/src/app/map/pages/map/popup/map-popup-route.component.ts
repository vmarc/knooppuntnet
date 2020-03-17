import {Component} from "@angular/core";
import {MapPopupRouteService} from "./map-popup-route.service";

@Component({
  selector: "kpn-map-popup-route",
  template: `
    <div *ngIf="service.response | async as response">
      <h2>
        <span i18n="@@map.route-popup.title">Route</span> {{response.result.name}}
      </h2>

      <div>
        <span *ngIf="response.result.networkReferences.size === 1" class="kpn-label" i18n="@@map.route-popup.network">Network</span>
        <span *ngIf="response.result.networkReferences.size !== 1" class="kpn-label" i18n="@@map.route-popup.networks">Networks</span>
        <span *ngIf="response.result.networkReferences.isEmpty()" i18n="@@map.route-popup.no-networks">None</span>
        <div *ngFor="let ref of response.result.networkReferences" class="reference">
          <a [routerLink]="'/analysis/network/' + ref.id">{{ref.name}}</a>
        </div>
      </div>

      <p class="more-details">
        <kpn-link-route [routeId]="response.result.id" title="More details" i18n-title="@@map.route-popup.more-details"></kpn-link-route>
      </p>

    </div>
  `,
  styles: [`
    .reference {
      margin: 0.5em 0 0.5em 1em;
    }

    .more-details {
      margin-top: 2em;
    }
  `],
  providers: [
    MapPopupRouteService
  ]
})
export class MapPopupRouteComponent {
  constructor(public service: MapPopupRouteService) {
  }
}
