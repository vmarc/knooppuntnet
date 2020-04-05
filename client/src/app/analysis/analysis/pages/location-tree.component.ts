import {FlatTreeControl} from "@angular/cdk/tree";
import {Component, EventEmitter, Input, OnDestroy, OnInit, Output} from "@angular/core";
import {MatTreeFlatDataSource, MatTreeFlattener} from "@angular/material/tree";
import {ActivatedRoute} from "@angular/router";
import {AppService} from "../../../app.service";
import {LocationNode} from "../../../kpn/api/common/location/location-node";
import {Country} from "../../../kpn/api/custom/country";
import {NetworkType} from "../../../kpn/api/custom/network-type";
import {Subscriptions} from "../../../util/Subscriptions";
import {LocationFlatNode} from "./location-flat-node";

/* tslint:disable:template-i18n work-in-progress */
@Component({
  selector: "kpn-location-tree",
  template: `
    <div class="buttons">
      <button mat-stroked-button class="location-button" (click)="expandAll()">Expand all</button>
      <button mat-stroked-button class="location-button" (click)="collapseAll()">Collapse all</button>
      <mat-radio-group [value]="all" (change)="allChanged()">
        <mat-radio-button
          [value]="true"
          title="All"
          class="location-button">All
        </mat-radio-button>
        <mat-radio-button
          [value]="false"
          title="In use only"
          class="location-button">In use only
        </mat-radio-button>
      </mat-radio-group>
    </div>

    <mat-tree [dataSource]="dataSource" [treeControl]="treeControl">
      <mat-tree-node *matTreeNodeDef="let leafNode" matTreeNodePadding [ngClass]="{'hidden': !all && leafNode.nodeCount === 0}">
        <a (click)="select(leafNode.name)">{{leafNode.name}}</a><span class="node-count">{{leafNode.nodeCount}}</span>
      </mat-tree-node>
      <mat-tree-node *matTreeNodeDef="let expandableNode;when: hasChild" matTreeNodePadding [ngClass]="{'hidden': !all && expandableNode.nodeCount === 0}">
        <div mat-icon-button matTreeNodeToggle
             [attr.aria-label]="'toggle ' + expandableNode.name">
          <mat-icon svgIcon="expand" *ngIf="treeControl.isExpanded(expandableNode)" class="expand-collapse-icon"></mat-icon>
          <mat-icon svgIcon="collapse" *ngIf="!treeControl.isExpanded(expandableNode)" class="expand-collapse-icon"></mat-icon>
        </div>
        <a (click)="select(expandableNode.name)">{{expandableNode.name}}</a><span class="node-count">{{expandableNode.nodeCount}}</span>
      </mat-tree-node>
    </mat-tree>
  `,
  styles: [`
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
  `]
})
export class LocationTreeComponent implements OnInit, OnDestroy {

  @Input() networkType: NetworkType;
  @Input() country: Country;
  @Output() selection = new EventEmitter<string>();

  all = false;

  treeControl = new FlatTreeControl<LocationFlatNode>(node => node.level, node => node.expandable);
  treeFlattener = new MatTreeFlattener(this.transformer(), node => node.level, node => node.expandable, node => node.children.toArray());
  dataSource = new MatTreeFlatDataSource(this.treeControl, this.treeFlattener);
  private readonly subscriptions = new Subscriptions();

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService) {
  }

  hasChild = (_: number, node: LocationFlatNode) => node.expandable;

  ngOnInit() {
    this.appService.locations(this.networkType, this.country).subscribe(response => {
      // this.dataSource.data = [this.toFlatNode(response.result.locationNode, 0)];
      this.dataSource.data = [response.result.locationNode];
      this.treeControl.expand(this.treeControl.dataNodes[0]);
    });
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
    return (node: LocationNode, level: number) => {
      return new LocationFlatNode(
        !!node.children && node.children.size > 0,
        node.name,
        node.nodeCount,
        level
      );
    };
  }

}
