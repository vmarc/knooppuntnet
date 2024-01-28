import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { NodeDiffsAddedComponent } from './node-diffs-added.component';
import { NodeDiffsData } from './node-diffs-data';
import { NodeDiffsRemovedComponent } from './node-diffs-removed.component';
import { NodeDiffsUpdatedComponent } from './node-diffs-updated.component';

@Component({
  selector: 'kpn-node-diffs',
  changeDetection: ChangeDetectionStrategy.OnPush,

  template: `
    <kpn-node-diffs-removed [data]="data()" />
    <kpn-node-diffs-added [data]="data()" />
    <kpn-node-diffs-updated [data]="data()" />
  `,
  standalone: true,
  imports: [NodeDiffsAddedComponent, NodeDiffsRemovedComponent, NodeDiffsUpdatedComponent],
})
export class NodeDiffsComponent {
  data = input<NodeDiffsData | undefined>();
}
