import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { Reference } from '@api/common/common';
import { IconRouteLinkComponent } from '@app/components/shared/link';

@Component({
  selector: 'kpn-node-route-references',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (references.length === 0) {
      <p i18n="@@node.route-references.none">None</p>
    }
    @for (reference of references; track reference) {
      <p>
        <kpn-icon-route-link [reference]="reference" [mixedNetworkScopes]="mixedNetworkScopes" />
      </p>
    }
  `,
  standalone: true,
  imports: [IconRouteLinkComponent],
})
export class NodeRouteReferencesComponent {
  @Input() references: Reference[];
  @Input() mixedNetworkScopes: boolean;
}
