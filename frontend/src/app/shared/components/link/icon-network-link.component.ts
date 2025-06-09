import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { Reference } from '@api/common/common/reference';
import { IconLinkComponent } from './icon-link.component';

@Component({
  selector: 'ui-icon-network-link',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-icon-link
      [reference]="reference()"
      [mixedRouteScopes]="mixedRouteScopes()"
      elementType="network"
    />
  `,
  imports: [IconLinkComponent],
})
export class IconNetworkLinkComponent {
  readonly reference = input.required<Reference>();
  readonly mixedRouteScopes = input.required<boolean>();
}
