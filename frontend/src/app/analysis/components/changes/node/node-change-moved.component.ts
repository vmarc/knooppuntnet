import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { NodeChangeInfo } from '@api/common/node/node-change-info';
import { TimestampComponent } from '@app/shared/components/timestamp/timestamp.component';
import { NodeMovedMapComponent } from './node-moved-map.component';

@Component({
  selector: 'ui-node-change-moved',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (nodeMoved) {
      @if (nodeMoved.distance > 0) {
        <div class="kpn-detail" i18n="@@node-change.moved.meters">
          The node has moved {{ nodeMoved.distance }} meters.
        </div>
      }

      @if (nodeMoved.distance === 0) {
        <div class="kpn-detail" i18n="@@node-change.moved.less-than-1-meter">
          The node has moved less than 1 meter.
        </div>
      }

      <ui-node-moved-map [nodeMoved]="nodeMoved" />

      <div class="note">
        <span i18n="@@node-change.moved.note.1"> Note: Node position is shown as it was at </span>
        <ui-timestamp [timestamp]="nodeChangeInfo().changeKey.timestamp" />
        <span i18n="@@node-change.moved.note.2">
          , while the map background is shown as it is today.
        </span>
      </div>
    }
  `,
  styles: `
    .note {
      margin-top: 5px;
    }
  `,
  imports: [NodeMovedMapComponent, TimestampComponent],
})
export class NodeChangeMovedComponent {
  nodeChangeInfo = input.required<NodeChangeInfo>();

  get nodeMoved() {
    return this.nodeChangeInfo().nodeMoved;
  }
}
