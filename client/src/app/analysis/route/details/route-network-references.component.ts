import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { Reference } from '@api/common/common/reference';

@Component({
  selector: 'kpn-route-network-references',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="references.length === 0" i18n="@@route.no-network-references">
      None
    </div>
    <div *ngFor="let reference of references" class="kpn-line">
      <kpn-network-type-icon [networkType]="reference.networkType" />
      <a
        id="{{ 'network-ref-' + reference.id }}"
        [routerLink]="'/analysis/network/' + reference.id"
      >
        {{ reference.name }}
      </a>
    </div>
  `,
})
export class RouteNetworkReferencesComponent {
  @Input() references: Reference[];
}
