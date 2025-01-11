import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { Reference } from '@api/common/common';
import { IconLinkComponent } from './icon-link.component';

@Component({
  selector: 'kpn-icon-network-link',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-icon-link
      [reference]="reference()"
      [mixedRouteScopes]="mixedRouteScopes()"
      elementType="network"
    />
  `,
  imports: [IconLinkComponent],
})
export class IconNetworkLinkComponent {
  reference = input.required<Reference>();
  mixedRouteScopes = input.required<boolean>();
}
