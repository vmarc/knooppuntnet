import { NgIf } from '@angular/common';
import { NgFor } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Reference } from '@api/common/common';
import { NetworkTypeIconComponent } from '@app/components/shared';

@Component({
  selector: 'kpn-route-network-references',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="references.length === 0" i18n="@@route.no-network-references">None</div>
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
  standalone: true,
  imports: [NgIf, NgFor, NetworkTypeIconComponent, RouterLink],
})
export class RouteNetworkReferencesComponent {
  @Input() references: Reference[];
}
