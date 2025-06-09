import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { NodeInfo } from '@api/common/node-info';
import { Reference } from '@api/common/common/reference';
import { NodeNetworkReferenceComponent } from './node-network-reference.component';

@Component({
  selector: 'ui-node-network-references',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @for (reference of references(); track reference.id) {
      <p>
        <ui-node-network-reference
          [nodeInfo]="nodeInfo()"
          [reference]="reference"
          [mixedRouteScopes]="mixedRouteScopes()"
        />
      </p>
    } @empty {
      <p i18n="@@node.network-references.none">None</p>
    }
  `,
  imports: [NodeNetworkReferenceComponent],
})
export class NodeNetworkReferencesComponent {
  readonly nodeInfo = input.required<NodeInfo>();
  readonly references = input.required<Reference[]>();
  readonly mixedRouteScopes = input.required<boolean>();
}
