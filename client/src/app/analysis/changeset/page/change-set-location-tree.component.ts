import { FlatTreeControl } from '@angular/cdk/tree';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { MatTreeFlattener } from '@angular/material/tree';
import { MatTreeFlatDataSource } from '@angular/material/tree';
import { LocationTreeItem } from '@api/common/location-tree-item';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { PageService } from '../../../components/shared/page.service';
import { LocationFlatNode } from '../../location/selection/location-flat-node';

@Component({
  selector: 'kpn-change-set-location-tree',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-tree [dataSource]="dataSource" [treeControl]="treeControl">
      <mat-tree-node *matTreeNodeDef="let node" matTreeNodePadding>
        <div
          mat-icon-button
          matTreeNodeToggle
          [attr.aria-label]="'toggle ' + node.locationName"
        >
          <div class="node-label" *ngIf="node.expandable">
            <kpn-network-type-icon
              *ngIf="node.networkType"
              [networkType]="node.networkType"
            ></kpn-network-type-icon>

            <mat-icon
              svgIcon="expand"
              *ngIf="treeControl.isExpanded(node)"
              class="expand-collapse-icon"
            ></mat-icon>
            <mat-icon
              svgIcon="collapse"
              *ngIf="!treeControl.isExpanded(node)"
              class="expand-collapse-icon"
            ></mat-icon>

            <a (click)="select(node)">{{ node.locationName }}</a
            ><span class="node-count">{{ node.nodeCount }}</span>

            <kpn-icon-happy
              *ngIf="!treeControl.isExpanded(node) && node.happy"
            ></kpn-icon-happy>
            <kpn-icon-investigate
              *ngIf="!treeControl.isExpanded(node) && node.investigate"
            ></kpn-icon-investigate>
          </div>

          <div
            *ngIf="!node.expandable && treeControl.isExpanded(node)"
            [ngClass]="(sidebarClass$ | async) + contentsClass(node)"
          >
            <p>abcdefghijklmnopqrstuvwxyz</p>
            <p>bcdefghijklmnopqrstuvwxyza</p>
            <p>cdefghijklmnopqrstuvwxyzab</p>
          </div>
        </div>
      </mat-tree-node>
    </mat-tree>
  `,
  styles: [
    `
      .node-label {
        display: flex;
        align-items: center;
      }

      .contents {
        background-color: lightyellow;
        position: relative;
        left: 0;
        margin-top: 1em;
        margin-bottom: 1em;
        margin-right: 100em;
        padding: 1em;
        border-top: 1px solid lightgray;
        border-bottom: 1px solid lightgray;
      }

      .sidebar-open {
        width: calc(100vw - 360px - 80px);
      }

      .sidebar-closed {
        width: calc(100vw - 80px);
      }

      .contents-level-1 {
        background-color: lightskyblue;
        left: -40px;
      }

      .contents-level-2 {
        background-color: lightgreen;
        left: -80px;
      }

      .contents-level-3 {
        background-color: lightpink;
        left: -120px;
      }

      .contents-level-4 {
        background-color: lightcyan;
        left: -160px;
      }

      ::ng-deep .expand-collapse-icon > svg {
        width: 12px;
        height: 12px;
        vertical-align: top;
        padding-top: 7px;
      }

      .node-count {
        padding-left: 20px;
        color: gray;
      }

      .buttons {
        margin-top: 20px;
      }

      .location-button {
        margin-right: 10px;
      }

      .hidden {
        display: none;
      }
    `,
  ],
})
export class ChangeSetLocationTreeComponent implements OnInit {
  @Input() trees: LocationTreeItem[];

  sidebarClass$: Observable<string>;

  treeControl = new FlatTreeControl<LocationTreeItem>(
    (node) => node.level,
    (node) => node.expandable
  );

  treeFlattener = new MatTreeFlattener(
    (node: LocationTreeItem, level: number) => node,
    (node) => node.level,
    (node) => node.expandable,
    (node) => node.children
  );

  dataSource = new MatTreeFlatDataSource(this.treeControl, this.treeFlattener);

  constructor(private pageService: PageService) {
    this.sidebarClass$ = pageService.sidebarOpen.pipe(
      map((open) => (open ? 'sidebar-open' : 'sidebar-closed'))
    );
  }

  ngOnInit() {
    this.dataSource.data = this.trees;
    // this.treeControl.expand(this.treeControl.dataNodes[0]);
    this.treeControl.expandAll();
  }

  select(expandableNode: LocationFlatNode): void {
    const locationName =
      expandableNode.path.length > 0
        ? expandableNode.path + ':' + expandableNode.name
        : expandableNode.name;

    //this.selection.emit(locationName);
  }

  contentsClass(node: LocationTreeItem): string {
    return ` contents contents-level-${node.level}`;
  }
}
