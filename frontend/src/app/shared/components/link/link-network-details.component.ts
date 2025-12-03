import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { RouteType } from '@api/common/route-type';

@Component({
  selector: 'ui-link-network-details',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a
      [routerLink]="networkLink()"
      [state]="{ routeType: routeType(), networkName: networkName() }"
    >
      {{ networkName() }}
    </a>
  `,
  imports: [RouterLink],
})
export class LinkNetworkDetailsComponent {
  readonly networkId = input.required<number>();
  readonly networkName = input.required<string>();
  readonly routeType = input<RouteType>();
  protected readonly networkLink = computed(() => `/analysis/network/${this.networkId()}`);
}
