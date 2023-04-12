import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { Ref } from '@api/common/common';
import { NetworkType } from '@api/custom';

@Component({
  selector: 'kpn-network-fact-routes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <span
      *ngIf="routes.length === 1"
      i18n="@@network-facts.route"
      class="kpn-label"
      >Route</span
    >
    <span
      *ngIf="routes.length > 1"
      i18n="@@network-facts.routes"
      class="kpn-label"
      >Routes</span
    >
    <div class="kpn-comma-list">
      <kpn-link-route
        *ngFor="let route of routes"
        [routeId]="route.id"
        [title]="route.name"
        [networkType]="networkType"
      />
    </div>
  `,
})
export class NetworkFactRoutesComponent {
  @Input() networkType: NetworkType;
  @Input() routes: Ref[];
}
