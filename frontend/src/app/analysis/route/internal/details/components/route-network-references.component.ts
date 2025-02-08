import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Reference } from '@api/common/common/reference';
import { NzIconDirective } from 'ng-zorro-antd/icon';

@Component({
  selector: 'kpn-route-network-references',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @for (reference of references(); track reference) {
      <div class="kpn-line">
        <nz-icon [nzType]="reference.routeType" />
        <a [id]="'network-ref-' + reference.id" [routerLink]="'/analysis/network/' + reference.id">
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
  references = input.required<Reference[]>();
}
