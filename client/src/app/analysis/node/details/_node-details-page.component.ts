import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { NodeDetailsPage } from '@api/common/node';
import { FactInfo } from '@app/analysis/fact';
import { InterpretedTags } from '@app/components/shared/tags';
import { selectDefined } from '@app/core';
import { Store } from '@ngrx/store';
import { actionNodeDetailsPageInit } from '../store/node.actions';
import { selectNodeNetworkTypes } from '../store/node.selectors';
import { selectNodeDetailsPage } from '../store/node.selectors';
import { selectNodeName } from '../store/node.selectors';
import { selectNodeId } from '../store/node.selectors';
import { selectNodeChangeCount } from '../store/node.selectors';

@Component({
  selector: 'kpn-node-details-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a [routerLink]="'/'" i18n="@@breadcrumb.home">Home</a></li>
      <li>
        <a [routerLink]="'/analysis'" i18n="@@breadcrumb.analysis">Analysis</a>
      </li>
      <li i18n="@@breadcrumb.node">Node</li>
    </ul>

    <kpn-node-page-header
      pageName="details"
      [nodeId]="nodeId$ | async"
      [nodeName]="nodeName$ | async"
      [changeCount]="changeCount$ | async"
    />

    <kpn-error />

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <div *ngIf="!response.result" i18n="@@node.node-not-found">
        Node not found
      </div>
      <div *ngIf="response.result as page">
        <kpn-data title="Summary" i18n-title="@@node.summary">
          <kpn-node-summary [nodeInfo]="page.nodeInfo" />
        </kpn-data>

        <div class="data2">
          <div class="title">
            <span i18n="@@node.situation-on">Situation on</span>
          </div>
          <div class="body">
            <kpn-timestamp [timestamp]="response.situationOn" />
          </div>
        </div>

        <kpn-data title="Last updated" i18n-title="@@node.last-updated">
          <kpn-timestamp [timestamp]="page.nodeInfo.lastUpdated" />
        </kpn-data>

        <kpn-data title="Tags" i18n-title="@@node.tags">
          <kpn-tags-table [tags]="buildTags(page)" />
        </kpn-data>

        <kpn-data title="Location" i18n-title="@@node.location">
          <div *ngIf="networkTypes$ | async as networkTypes">
            <div *ngIf="networkTypes.length > 1">
              <div *ngFor="let networkType of networkTypes" class="kpn-line">
                <kpn-network-type-icon [networkType]="networkType" />
                <kpn-node-location
                  [networkType]="networkType"
                  [locations]="page.nodeInfo.locations"
                />
              </div>
            </div>
            <div *ngIf="networkTypes.length === 1">
              <div *ngFor="let networkType of networkTypes">
                <kpn-node-location
                  [networkType]="networkTypes[0]"
                  [locations]="page.nodeInfo.locations"
                />
              </div>
            </div>
          </div>
        </kpn-data>

        <kpn-data title="Integrity" i18n-title="@@node.integrity">
          <kpn-node-integrity
            [integrity]="page.integrity"
            [mixedNetworkScopes]="page.mixedNetworkScopes"
          />
        </kpn-data>

        <kpn-data title="Routes" i18n-title="@@node.routes">
          <kpn-node-route-references
            [references]="page.routeReferences"
            [mixedNetworkScopes]="page.mixedNetworkScopes"
          />
        </kpn-data>

        <kpn-data title="Networks" i18n-title="@@node.networks">
          <kpn-node-network-references
            [nodeInfo]="page.nodeInfo"
            [references]="page.networkReferences"
            [mixedNetworkScopes]="page.mixedNetworkScopes"
          />
        </kpn-data>

        <kpn-data title="Facts" i18n-title="@@node.facts">
          <kpn-facts [factInfos]="buildFactInfos(page)" />
        </kpn-data>
      </div>
    </div>
  `,
  styleUrls: ['../../../components/shared/data/data.component.scss'],
})
export class NodeDetailsPageComponent implements OnInit {
  readonly nodeId$ = this.store.select(selectNodeId);
  readonly nodeName$ = this.store.select(selectNodeName);
  readonly changeCount$ = this.store.select(selectNodeChangeCount);
  readonly networkTypes$ = this.store.select(selectNodeNetworkTypes);
  readonly response$ = selectDefined(this.store, selectNodeDetailsPage);

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionNodeDetailsPageInit());
  }

  buildTags(page: NodeDetailsPage) {
    return InterpretedTags.nodeTags(page.nodeInfo.tags);
  }

  buildFactInfos(page: NodeDetailsPage): FactInfo[] {
    return page.nodeInfo.facts.map((fact) => new FactInfo(fact));
  }
}
