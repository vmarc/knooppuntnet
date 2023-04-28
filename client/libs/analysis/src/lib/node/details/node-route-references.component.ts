import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { Reference } from '@api/common/common';

@Component({
  selector: 'kpn-node-route-references',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p *ngIf="references.length === 0" i18n="@@node.route-references.none">
      None
    </p>
    <p *ngFor="let reference of references">
      <kpn-icon-route-link
        [reference]="reference"
        [mixedNetworkScopes]="mixedNetworkScopes"
      />
    </p>
  `,
})
export class NodeRouteReferencesComponent {
  @Input() references: Reference[];
  @Input() mixedNetworkScopes: boolean;
}
