import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { Reference } from '@api/common/common/reference';
import { IconRouteLinkComponent } from '@app/shared/components/link/icon-route-link.component';

@Component({
  selector: 'kpn-node-route-references',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @for (reference of references(); track reference) {
      <p>
        <kpn-icon-route-link [reference]="reference" [mixedRouteScopes]="mixedRouteScopes()" />
      </p>
    } @empty {
      <p i18n="@@node.route-references.none">None</p>
    }
  `,
  imports: [IconRouteLinkComponent],
})
export class NodeRouteReferencesComponent {
  references = input.required<Reference[]>();
  mixedRouteScopes = input.required<boolean>();
}
