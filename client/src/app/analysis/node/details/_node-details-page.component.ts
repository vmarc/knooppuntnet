import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { Ref } from '@api/common/common/ref';
import { NodeDetailsPage } from '@api/common/node/node-details-page';
import { Store } from '@ngrx/store';
import { filter } from 'rxjs/operators';
import { PageService } from '../../../components/shared/page.service';
import { InterpretedTags } from '../../../components/shared/tags/interpreted-tags';
import { AppState } from '../../../core/core.state';
import { FactInfo } from '../../fact/fact-info';
import { actionNodeDetailsPageInit } from '../store/node.actions';
import { selectNodeDetailsPage } from '../store/node.selectors';
import { selectNodeName } from '../store/node.selectors';
import { selectNodeId } from '../store/node.selectors';
import { selectNodeChangeCount } from '../store/node.selectors';

@Component({
  selector: 'kpn-node-details-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li>
        <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a>
      </li>
      <li i18n="@@breadcrumb.node">Node</li>
    </ul>

    <kpn-node-page-header
      pageName="details"
      [nodeId]="nodeId$ | async"
      [nodeName]="nodeName$ | async"
      [changeCount]="changeCount$ | async"
    >
    </kpn-node-page-header>

    <kpn-error></kpn-error>

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <div *ngIf="!response.result" i18n="@@node.node-not-found">
        Node not found
      </div>
      <div *ngIf="response.result as page">
        <kpn-data title="Summary" i18n-title="@@node.summary">
          <kpn-node-summary [nodeInfo]="page.nodeInfo"></kpn-node-summary>
        </kpn-data>

        <kpn-data title="Situation on" i18n-title="@@node.situation-on">
          <kpn-timestamp [timestamp]="response.situationOn"></kpn-timestamp>
        </kpn-data>

        <kpn-data title="Last updated" i18n-title="@@node.last-updated">
          <kpn-timestamp
            [timestamp]="page.nodeInfo.lastUpdated"
          ></kpn-timestamp>
        </kpn-data>

        <kpn-data title="Tags" i18n-title="@@node.tags">
          <kpn-tags-table [tags]="buildTags(page)"></kpn-tags-table>
        </kpn-data>

        <kpn-data title="Location" i18n-title="@@node.location">
          <kpn-node-location
            [location]="page.nodeInfo.location"
          ></kpn-node-location>
        </kpn-data>

        <kpn-data title="Integrity" i18n-title="@@node.integrity">
          <kpn-node-integrity [integrity]="page.integrity"></kpn-node-integrity>
        </kpn-data>

        <kpn-data title="Networks" i18n-title="@@node.networks">
          <kpn-node-network-references
            [nodeInfo]="page.nodeInfo"
            [references]="page.references.networkReferences"
          >
          </kpn-node-network-references>
        </kpn-data>

        <kpn-data title="Orphan routes" i18n-title="@@node.orphan-routes">
          <kpn-node-orphan-route-references
            [references]="page.references.routeReferences"
          >
          </kpn-node-orphan-route-references>
        </kpn-data>

        <kpn-data title="Facts" i18n-title="@@node.facts">
          <kpn-facts [factInfos]="buildFactInfos(page)"></kpn-facts>
        </kpn-data>
      </div>
    </div>
  `,
})
export class NodeDetailsPageComponent implements OnInit {
  nodeId$ = this.store.select(selectNodeId);
  nodeName$ = this.store.select(selectNodeName);
  changeCount$ = this.store.select(selectNodeChangeCount);

  response$ = this.store
    .select(selectNodeDetailsPage)
    .pipe(filter((x) => x !== null));

  constructor(
    private pageService: PageService,
    private store: Store<AppState>
  ) {}

  ngOnInit(): void {
    this.store.dispatch(actionNodeDetailsPageInit());
    this.pageService.showFooter = true;
  }

  buildTags(page: NodeDetailsPage) {
    return InterpretedTags.nodeTags(page.nodeInfo.tags);
  }

  buildFactInfos(page: NodeDetailsPage): FactInfo[] {
    const nodeFacts = page.nodeInfo.facts.map((fact) => new FactInfo(fact));
    page.references.networkReferences.forEach((networkReference) => {
      networkReference.facts.forEach((fact) => {
        const networkRef = new Ref(
          networkReference.networkId,
          networkReference.networkName
        );
        nodeFacts.push(new FactInfo(fact, networkRef, null, null));
      });
    });
    return nodeFacts;
  }
}
