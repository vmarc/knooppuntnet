import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { LocationChangesTree } from '@api/common/location-changes-tree';

@Component({
  selector: 'kpn-change-set-location-tree',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-level-1">
      <div class="kpn-level-1-header">Locations</div>
      <div *ngFor="let tree of trees" class="tree-block">
        <div class="kpn-line">
          <kpn-network-type-icon
            [networkType]="tree.networkType"
          ></kpn-network-type-icon>
          <span>{{ tree.locationName.toUpperCase() }}</span>
          <kpn-icon-happy *ngIf="tree.happy"></kpn-icon-happy>
          <kpn-icon-investigate *ngIf="tree.investigate"></kpn-icon-investigate>
        </div>
        <div class="children">
          <kpn-change-set-location-tree-node [trees]="tree.children">
          </kpn-change-set-location-tree-node>
        </div>
      </div>
    </div>
  `,
  styles: [
    `
      .tree-block {
        margin-top: 1em;
      }

      .children {
        margin-left: 1em;
      }
    `,
  ],
})
export class ChangeSetLocationTreeComponent {
  @Input() trees: LocationChangesTree[];
}
