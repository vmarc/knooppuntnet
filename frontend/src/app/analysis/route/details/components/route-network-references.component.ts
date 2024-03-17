import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Reference } from '@api/common/common';
import { NetworkTypeIconComponent } from '@app/components/shared';

@Component({
  selector: 'kpn-route-network-references',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (references().length === 0) {
      <div i18n="@@route.no-network-references">None</div>
    }
    @for (reference of references(); track reference) {
      <div class="kpn-line">
        <kpn-network-type-icon [networkType]="reference.networkType" />
        <a
          id="{{ 'network-ref-' + reference.id }}"
          [routerLink]="'/analysis/network/' + reference.id"
        >
          {{ reference.name }}
        </a>
      </div>
    }
  `,
  standalone: true,
  imports: [NetworkTypeIconComponent, RouterLink],
})
export class RouteNetworkReferencesComponent {
  references = input.required<Reference[]>();
}
