import { FlatTreeControl } from '@angular/cdk/tree';
import { ChangeDetectionStrategy } from '@angular/core';
import {
  Component,
  EventEmitter,
  Input,
  OnDestroy,
  OnInit,
  Output,
} from '@angular/core';
import {
  MatTreeFlatDataSource,
  MatTreeFlattener,
} from '@angular/material/tree';
import { LocationNode } from '@api/common/location/location-node';
import { Country } from '@api/custom/country';
import { NetworkType } from '@api/custom/network-type';
import { Subscriptions } from '../../../util/Subscriptions';
import { LocationFlatNode } from './location-flat-node';

@Component({
  selector: 'kpn-location-tree',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="buttons">
      <button
        mat-stroked-button
        class="location-button"
        (click)="expandAll()"
        i18n="@@location.tree.expand-all"
      >
        Expand all
      </button>
      <button
        mat-stroked-button
        class="location-button"
        (click)="collapseAll()"
        i18n="@@location.tree.collapse-all"
      >
        Collapse all
      </button>
      <mat-radio-group [value]="all" (change)="allChanged()">
        <mat-radio-button
          [value]="true"
          class="location-button"
          i18n="@@location.tree.all"
        >
          All
        </mat-radio-button>
        <mat-radio-button
          [value]="false"
          class="location-button"
          i18n="@@location.tree.in-use-only"
        >
          In use only
        </mat-radio-button>
      </mat-radio-group>
    </div>

    <mat-tree [dataSource]="dataSource" [treeControl]="treeControl">
      <mat-tree-node
        *matTreeNodeDef="let leafNode"
        matTreeNodePadding
        [ngClass]="{ hidden: !all && leafNode.nodeCount === 0 }"
      >
        <a (click)="select(leafNode.name)">{{ locationName(leafNode) }}</a
        ><span class="node-count">{{ leafNode.nodeCount }}</span>
      </mat-tree-node>
      <mat-tree-node
        *matTreeNodeDef="let expandableNode; when: hasChild"
        matTreeNodePadding
        [ngClass]="{ hidden: !all && expandableNode.nodeCount === 0 }"
      >
        <div
          mat-icon-button
          matTreeNodeToggle
          [attr.aria-label]="'toggle ' + expandableNode.name"
        >
          <mat-icon
            svgIcon="expand"
            *ngIf="treeControl.isExpanded(expandableNode)"
            class="expand-collapse-icon"
          ></mat-icon>
          <mat-icon
            svgIcon="collapse"
            *ngIf="!treeControl.isExpanded(expandableNode)"
            class="expand-collapse-icon"
          ></mat-icon>
        </div>
        <a (click)="select(expandableNode.name)">{{
          locationName(expandableNode)
        }}</a
        ><span class="node-count">{{ expandableNode.nodeCount }}</span>
      </mat-tree-node>
    </mat-tree>
  `,
  styles: [
    `
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
export class LocationTreeComponent implements OnInit, OnDestroy {
  @Input() networkType: NetworkType;
  @Input() country: Country;
  @Input() locationNode: LocationNode;

  @Output() selection = new EventEmitter<string>();

  all = false;

  treeControl = new FlatTreeControl<LocationFlatNode>(
    (node) => node.level,
    (node) => node.expandable
  );
  treeFlattener = new MatTreeFlattener(
    this.transformer(),
    (node) => node.level,
    (node) => node.expandable,
    (node) => node.children
  );
  dataSource = new MatTreeFlatDataSource(this.treeControl, this.treeFlattener);
  private readonly subscriptions = new Subscriptions();

  hasChild = (_: number, node: LocationFlatNode) => node.expandable;

  ngOnInit() {
    this.dataSource.data = [this.locationNode];
    this.treeControl.expand(this.treeControl.dataNodes[0]);
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  select(locationName: string): void {
    this.selection.emit(locationName);
  }

  expandAll(): void {
    this.treeControl.expandAll();
  }

  collapseAll(): void {
    this.treeControl.collapseAll();
  }

  allChanged(): void {
    this.all = !this.all;
  }

  private transformer() {
    return (node: LocationNode, level: number) =>
      new LocationFlatNode(
        !!node.children && node.children.length > 0,
        node.name,
        node.localName,
        node.nodeCount,
        level
      );
  }

  locationName(leafNode: LocationNode): string {
    if (leafNode.name === 'Flanders') {
      console.log(`${leafNode.name} ${leafNode.localName}`);
    }

    if (leafNode.localName) {
      return leafNode.localName;
    }
    return leafNode.name;
  }
}
