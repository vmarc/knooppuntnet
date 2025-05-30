import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { Reference } from '@api/common/common/reference';
import { IconLinkComponent } from './icon-link.component';

@Component({
  selector: 'ui-icon-route-link',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-icon-link
      [reference]="reference()"
      [mixedRouteScopes]="mixedRouteScopes()"
      elementType="route"
    />
  `,
  imports: [IconLinkComponent],
})
export class IconRouteLinkComponent {
  reference = input.required<Reference>();
  mixedRouteScopes = input.required<boolean>();
}
