import { FlatTreeControl } from '@angular/cdk/tree';
import { NgClass } from '@angular/common';
import { output } from '@angular/core';
import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { input } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatRadioModule } from '@angular/material/radio';
import { MatTreeFlatDataSource } from '@angular/material/tree';
import { MatTreeModule } from '@angular/material/tree';
import { MatTreeFlattener } from '@angular/material/tree';
import { RouterLink } from '@angular/router';
import { Country } from '@api/common/country';
import { RouteType } from '@api/common/route-type';
import { Subscriptions } from '@app/util/subscriptions';
import { LocalLocationNode } from './local-location-node';
import { LocationFlatNode } from './location-flat-node';
import { LocationTreeNodeComponent } from './location-tree-node.component';

@Component({
  selector: 'ui-location-tree',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-small-spacer-above kpn-small-spacer-below">
      @if (country() !== 'fr') {
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
      }
      <mat-radio-group [value]="all()" (change)="allChanged()">
        <mat-radio-button [value]="true" class="location-button" i18n="@@location.tree.all">
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

    @if (routeType() === 'hiking' && country() === 'fr') {
      <div class="kpn-small-spacer-below">
        <a routerLink="/analysis/hiking/fr/Parc du Vercors/details">Parc du Vercors</a>
      </div>
    }
    <mat-tree [dataSource]="dataSource" [treeControl]="treeControl">
      <mat-tree-node
        *matTreeNodeDef="let leafNode"
        matTreeNodePadding
        [ngClass]="{ hidden: !(all() || leafNode.isUsed()) }"
      >
        <ui-location-tree-node [node]="leafNode" (selection)="selection.emit($event)" />
      </mat-tree-node>
      <mat-tree-node
        *matTreeNodeDef="let expandableNode; when: hasChild"
        matTreeNodePadding
        [ngClass]="{ hidden: !(all() || expandableNode.isUsed()) }"
      >
        <div class="expandable">
          <div
            mat-icon-button
            matTreeNodeToggle
            [attr.aria-label]="'toggle ' + expandableNode.name"
          >
            @if (treeControl.isExpanded(expandableNode)) {
              <mat-icon svgIcon="expand" class="expand-collapse-icon" />
            } @else {
              <mat-icon svgIcon="collapse" class="expand-collapse-icon" />
            }
          </div>
          <ui-location-tree-node [node]="expandableNode" (selection)="selection.emit($event)" />
        </div>
      </mat-tree-node>
    </mat-tree>
  `,
  styles: `
    ::ng-deep .expand-collapse-icon > svg {
      width: 12px;
      height: 12px;
      vertical-align: top;
      padding-top: 7px;
    }

    .location-button {
      margin-right: 10px;
    }

    .hidden {
      display: none;
    }

    mat-tree {
      padding-left: 1em;
    }

    .expandable {
      display: flex;
      align-items: flex-start;
    }
  `,
  imports: [
    LocationTreeNodeComponent,
    MatButtonModule,
    MatIconModule,
    MatRadioModule,
    MatTreeModule,
    NgClass,
    RouterLink,
  ],
})
export class LocationTreeComponent implements OnInit, OnDestroy {
  readonly routeType = input.required<RouteType>();
  readonly country = input.required<Country>();
  readonly locationNode = input.required<LocalLocationNode>();

  readonly selection = output<string>();

  private readonly dialog = inject(MatDialog);

  readonly all = signal<boolean>(false);

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
    this.dataSource.data = [this.locationNode()];
    this.treeControl.expand(this.treeControl.dataNodes[0]);
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  expandAll(): void {
    this.treeControl.expandAll();
  }

  collapseAll(): void {
    this.treeControl.collapseAll();
  }

  allChanged(): void {
    this.all.set(!this.all());
  }

  private transformer() {
    return (node: LocalLocationNode, level: number) => {
      const expandable = !!node.children && node.children.length > 0;
      return new LocationFlatNode(
        expandable,
        node.path,
        node.name,
        node.nodeCount,
        node.routeCount,
        node.factCount,
        level
      );
    };
  }
}
