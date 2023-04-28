import { NgIf } from '@angular/common';
import { NgFor } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { Reference } from '@api/common/common';
import { IconRouteLinkComponent } from '@app/components/shared/link';

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
  standalone: true,
  imports: [NgIf, NgFor, IconRouteLinkComponent],
})
export class NodeRouteReferencesComponent {
  @Input() references: Reference[];
  @Input() mixedNetworkScopes: boolean;
}
