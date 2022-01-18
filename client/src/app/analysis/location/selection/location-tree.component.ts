import { FlatTreeControl } from '@angular/cdk/tree';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { EventEmitter } from '@angular/core';
import { Input } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatTreeFlatDataSource } from '@angular/material/tree';
import { MatTreeFlattener } from '@angular/material/tree';
import { Country } from '@api/custom/country';
import { NetworkType } from '@api/custom/network-type';
import { BehaviorSubject } from 'rxjs';
import { Subscriptions } from '../../../util/Subscriptions';
import { LocalLocationNode } from './local-location-node';
import { LocationExpandAllDialogComponent } from './location-expand-all-dialog.component';
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

    <div class="progress-bar">
      <mat-progress-bar
        *ngIf="showSpinner$ | async"
        mode="indeterminate"
      ></mat-progress-bar>
    </div>

    <mat-tree [dataSource]="dataSource" [treeControl]="treeControl">
      <mat-tree-node
        *matTreeNodeDef="let leafNode"
        matTreeNodePadding
        [ngClass]="{ hidden: !all && leafNode.nodeCount === 0 }"
      >
        <a (click)="select(leafNode)">{{ leafNode.name }}</a
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
        <a (click)="select(expandableNode)">{{ expandableNode.name }}</a
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

      .progress-bar {
        padding-top: 5px;
        height: 10px;
      }
    `,
  ],
})
export class LocationTreeComponent implements OnInit, OnDestroy {
  @Input() networkType: NetworkType;
  @Input() country: Country;
  @Input() locationNode: LocalLocationNode;

  @Output() selection = new EventEmitter<string>();

  all = false;
  showSpinner$ = new BehaviorSubject(false);

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

  constructor(private dialog: MatDialog) {}

  ngOnInit() {
    this.dataSource.data = [this.locationNode];
    this.treeControl.expand(this.treeControl.dataNodes[0]);
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  select(expandableNode: LocationFlatNode): void {
    const locationName =
      expandableNode.path.length > 0
        ? expandableNode.path + ':' + expandableNode.name
        : expandableNode.name;

    this.selection.emit(locationName);
  }

  expandAll(): void {
    const confirm = this.dialog.open(LocationExpandAllDialogComponent, {
      maxWidth: 500,
    });
    const subscription = confirm.afterClosed().subscribe((isConfirmed) => {
      if (isConfirmed) {
        subscription.unsubscribe();
        this.doExpandAll();
      }
    });
  }

  private doExpandAll(): void {
    this.showSpinner$.next(true);
    setTimeout(() => {
      this.treeControl.expandAll();
      this.showSpinner$.next(false);
    }, 0);
  }

  collapseAll(): void {
    this.showSpinner$.next(true);
    setTimeout(() => {
      this.treeControl.collapseAll();
      this.showSpinner$.next(false);
    }, 0);
  }

  allChanged(): void {
    this.all = !this.all;
  }

  private transformer() {
    return (node: LocalLocationNode, level: number) =>
      new LocationFlatNode(
        !!node.children && node.children.length > 0,
        node.path,
        node.name,
        node.nodeCount,
        level
      );
  }
}
