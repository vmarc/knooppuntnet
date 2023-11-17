import { AsyncPipe, NgFor, NgIf } from '@angular/common';
import { OnDestroy } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NodeDetailsPage } from '@api/common/node';
import { FactInfo } from '@app/analysis/fact';
import { FactsComponent } from '@app/analysis/fact';
import { NetworkTypeIconComponent } from '@app/components/shared';
import { DataComponent } from '@app/components/shared/data';
import { ErrorComponent } from '@app/components/shared/error';
import { PageComponent } from '@app/components/shared/page';
import { InterpretedTags } from '@app/components/shared/tags';
import { TagsTableComponent } from '@app/components/shared/tags';
import { TimestampComponent } from '@app/components/shared/timestamp';
import { Store } from '@ngrx/store';
import { AnalysisSidebarComponent } from '../../analysis/analysis-sidebar.component';
import { NodePageHeaderComponent } from '../components/node-page-header.component';
import { actionNodeDetailsPageDestroy } from '../store/node.actions';
import { actionNodeDetailsPageInit } from '../store/node.actions';
import { selectNodeNetworkTypes } from '../store/node.selectors';
import { selectNodeDetailsPage } from '../store/node.selectors';
import { selectNodeName } from '../store/node.selectors';
import { selectNodeId } from '../store/node.selectors';
import { selectNodeChangeCount } from '../store/node.selectors';
import { NodeIntegrityComponent } from './node-integrity.component';
import { NodeLocationComponent } from './node-location.component';
import { NodeNetworkReferencesComponent } from './node-network-references.component';
import { NodeRouteReferencesComponent } from './node-route-references.component';
import { NodeSummaryComponent } from './node-summary.component';

@Component({
  selector: 'kpn-node-details-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <ul class="breadcrumb">
        <li><a [routerLink]="'/'" i18n="@@breadcrumb.home">Home</a></li>
        <li>
          <a [routerLink]="'/analysis'" i18n="@@breadcrumb.analysis"
            >Analysis</a
          >
        </li>
        <li i18n="@@breadcrumb.node">Node</li>
      </ul>

      <kpn-node-page-header
        pageName="details"
        [nodeId]="nodeId()"
        [nodeName]="nodeName()"
        [changeCount]="changeCount()"
      />

      <kpn-error />

      <div *ngIf="apiResponse() as response" class="kpn-spacer-above">
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
            <div *ngIf="networkTypes() as networkTypes">
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
      <kpn-analysis-sidebar sidebar />
    </kpn-page>
  `,
  styleUrl: '../../../shared/components/shared/data/data.component.scss',
  standalone: true,
  imports: [
    AnalysisSidebarComponent,
    AsyncPipe,
    DataComponent,
    ErrorComponent,
    FactsComponent,
    NetworkTypeIconComponent,
    NgFor,
    NgIf,
    NodeIntegrityComponent,
    NodeLocationComponent,
    NodeNetworkReferencesComponent,
    NodePageHeaderComponent,
    NodeRouteReferencesComponent,
    NodeSummaryComponent,
    PageComponent,
    RouterLink,
    TagsTableComponent,
    TimestampComponent,
  ],
})
export class NodeDetailsPageComponent implements OnInit, OnDestroy {
  readonly nodeId = this.store.selectSignal(selectNodeId);
  readonly nodeName = this.store.selectSignal(selectNodeName);
  readonly changeCount = this.store.selectSignal(selectNodeChangeCount);
  readonly networkTypes = this.store.selectSignal(selectNodeNetworkTypes);
  readonly apiResponse = this.store.selectSignal(selectNodeDetailsPage);

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionNodeDetailsPageInit());
  }

  ngOnDestroy(): void {
    this.store.dispatch(actionNodeDetailsPageDestroy());
  }

  buildTags(page: NodeDetailsPage) {
    return InterpretedTags.nodeTags(page.nodeInfo.tags);
  }

  buildFactInfos(page: NodeDetailsPage): FactInfo[] {
    return page.nodeInfo.facts.map((fact) => new FactInfo(fact));
  }
}
