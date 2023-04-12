import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { LocationChangesTreeNode } from '@api/common';

@Component({
  selector: 'kpn-change-set-location-tree-node',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- work-in-progress -->
    <!-- eslint-disable @angular-eslint/template/i18n -->

    <div *ngFor="let tree of trees">
      <div class="kpn-line location-block">
        <span>{{ tree.locationName }}</span>
        <kpn-icon-happy *ngIf="tree.happy" />
        <kpn-icon-investigate *ngIf="tree.investigate" />
      </div>
      <div>
        <div *ngIf="tree.routeChanges.removed.length > 0" class="refs-block">
          <div>Removed routes ({{ tree.routeChanges.removed.length }})</div>
          <div *ngFor="let ref of tree.routeChanges.removed" class="refs">
            <div class="kpn-line">
              <kpn-link-route [routeId]="ref.id" [title]="ref.name" />
              <kpn-icon-happy *ngIf="ref.happy" />
              <kpn-icon-investigate *ngIf="ref.investigate" />
            </div>
          </div>
        </div>

        <div *ngIf="tree.routeChanges.added.length > 0" class="refs-block">
          <div>Added routes ({{ tree.routeChanges.added.length }})</div>
          <div *ngFor="let ref of tree.routeChanges.added" class="refs">
            <div class="kpn-line">
              <kpn-link-route [routeId]="ref.id" [title]="ref.name" />
              <kpn-icon-happy *ngIf="ref.happy" />
              <kpn-icon-investigate *ngIf="ref.investigate" />
            </div>
          </div>
        </div>

        <div *ngIf="tree.routeChanges.updated.length > 0" class="refs-block">
          <div>Updated routes ({{ tree.routeChanges.updated.length }})</div>
          <div *ngFor="let ref of tree.routeChanges.updated" class="refs">
            <div class="kpn-line">
              <kpn-link-route [routeId]="ref.id" [title]="ref.name" />
              <kpn-icon-happy *ngIf="ref.happy" />
              <kpn-icon-investigate *ngIf="ref.investigate" />
            </div>
          </div>
        </div>

        <div *ngIf="tree.nodeChanges.removed.length > 0" class="refs-block">
          <div>Updated nodes ({{ tree.nodeChanges.removed.length }})</div>
          <div *ngFor="let ref of tree.nodeChanges.removed" class="refs">
            <div class="kpn-line">
              <kpn-link-node [nodeId]="ref.id" [nodeName]="ref.name" />
              <kpn-icon-happy *ngIf="ref.happy" />
              <kpn-icon-investigate *ngIf="ref.investigate" />
            </div>
          </div>
        </div>

        <div *ngIf="tree.nodeChanges.added.length > 0" class="refs-block">
          <div>Updated nodes ({{ tree.nodeChanges.added.length }})</div>
          <div *ngFor="let ref of tree.nodeChanges.added" class="refs">
            <div class="kpn-line">
              <kpn-link-node [nodeId]="ref.id" [nodeName]="ref.name" />
              <kpn-icon-happy *ngIf="ref.happy" />
              <kpn-icon-investigate *ngIf="ref.investigate" />
            </div>
          </div>
        </div>

        <div *ngIf="tree.nodeChanges.updated.length > 0" class="refs-block">
          <div>Updated nodes ({{ tree.nodeChanges.updated.length }})</div>
          <div *ngFor="let ref of tree.nodeChanges.updated" class="refs">
            <div class="kpn-line">
              <kpn-link-node [nodeId]="ref.id" [nodeName]="ref.name" />
              <kpn-icon-happy *ngIf="ref.happy" />
              <kpn-icon-investigate *ngIf="ref.investigate" />
            </div>
          </div>
        </div>
      </div>

      <div class="children">
        <kpn-change-set-location-tree-node [trees]="tree.children" />
      </div>
    </div>
  `,
  styles: [
    `
      .children {
        margin-left: 1em;
      }

      .location-block {
        margin-top: 0.6em;
      }

      .refs-block {
        margin-top: 0.6em;
        margin-left: 1em;
      }

      .refs {
        margin-top: 0.3em;
        margin-left: 2em;
      }
    `,
  ],
})
export class ChangeSetLocationTreeNodeComponent {
  @Input() trees: LocationChangesTreeNode[];
}
