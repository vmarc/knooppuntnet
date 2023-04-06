import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { NodeDiffsData } from './node-diffs-data';

@Component({
  selector: 'kpn-node-diffs',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-node-diffs-removed [data]="data" />
    <kpn-node-diffs-added [data]="data" />
    <kpn-node-diffs-updated [data]="data" />
  `,
})
export class NodeDiffsComponent {
  @Input() data: NodeDiffsData;
}
