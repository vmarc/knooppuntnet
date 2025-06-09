import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { NodeDiffsAddedComponent } from './node-diffs-added.component';
import { NodeDiffsData } from './node-diffs-data';
import { NodeDiffsRemovedComponent } from './node-diffs-removed.component';
import { NodeDiffsUpdatedComponent } from './node-diffs-updated.component';

@Component({
  selector: 'ui-node-diffs',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-node-diffs-removed [data]="data()" />
    <ui-node-diffs-added [data]="data()" />
    <ui-node-diffs-updated [data]="data()" />
  `,
  imports: [NodeDiffsAddedComponent, NodeDiffsRemovedComponent, NodeDiffsUpdatedComponent],
})
export class NodeDiffsComponent {
  readonly data = input.required<NodeDiffsData>();
}
