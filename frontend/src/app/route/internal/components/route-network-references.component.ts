import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Reference } from '@api/common/common/reference';
import { NzIconDirective } from 'ng-zorro-antd/icon';

@Component({
  selector: 'ui-route-network-references',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @for (reference of references(); track reference) {
      <div class="kpn-line">
        <nz-icon [nzType]="reference.routeType" />
        <a [id]="networkId(reference)" [routerLink]="networkLink(reference)">
          {{ reference.name }}
        </a>
      </div>
    } @empty {
      <div i18n="@@route.no-network-references">None</div>
    }
  `,
  imports: [RouterLink, NzIconDirective],
})
export class RouteNetworkReferencesComponent {
  readonly references = input.required<ReadonlyArray<Reference>>();

  networkId(reference: Reference): string {
    return `network-ref-${reference.id}`;
  }

  networkLink(reference: Reference): string {
    return `/analysis/network/${reference.id}`;
  }
}
