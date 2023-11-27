import { NgIf } from '@angular/common';
import { NgFor } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { Ref } from '@api/common/common';
import { NetworkType } from '@api/custom';
import { LinkRouteComponent } from '@app/components/shared/link';

@Component({
  selector: 'kpn-network-fact-routes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <span *ngIf="routes.length === 1" i18n="@@network-facts.route" class="kpn-label">Route</span>
    <span *ngIf="routes.length > 1" i18n="@@network-facts.routes" class="kpn-label">Routes</span>
    <div class="kpn-comma-list">
      <kpn-link-route
        *ngFor="let route of routes"
        [routeId]="route.id"
        [routeName]="route.name"
        [networkType]="networkType"
      />
    </div>
  `,
  standalone: true,
  imports: [NgIf, NgFor, LinkRouteComponent],
})
export class NetworkFactRoutesComponent {
  @Input() networkType: NetworkType;
  @Input() routes: Ref[];
}
