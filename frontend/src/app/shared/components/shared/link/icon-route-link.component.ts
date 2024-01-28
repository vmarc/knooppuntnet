import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { Reference } from '@api/common/common';
import { IconLinkComponent } from './icon-link.component';

@Component({
  selector: 'kpn-icon-route-link',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-icon-link
      [reference]="reference()"
      [mixedNetworkScopes]="mixedNetworkScopes()"
      elementType="route"
    />
  `,
  standalone: true,
  imports: [IconLinkComponent],
})
export class IconRouteLinkComponent {
  reference = input.required<Reference>();
  mixedNetworkScopes = input.required<boolean>();
}
