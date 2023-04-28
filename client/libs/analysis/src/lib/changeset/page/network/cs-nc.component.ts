import { NgIf } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { ChangeSetPage } from '@api/common/changes';
import { NetworkChangeInfo } from '@api/common/changes/details';
import { List } from 'immutable';
import { NodeDiffsComponent } from '../node-diffs/_node-diffs.component';
import { NodeDiffsData } from '../node-diffs/node-diffs-data';
import { RouteDiffsComponent } from '../route-diffs/_route-diffs.component';
import { RouteDiffsData } from '../route-diffs/route-diffs-data';
import { VersionChangeComponent } from '../version-change.component';
import { CsNcNodesAddedComponent } from './cs-nc-nodes-added.component';
import { CsNcNodesRemovedComponent } from './cs-nc-nodes-removed.component';
import { CsNcNodesUpdatedComponent } from './cs-nc-nodes-updated.component';
import { CsNcRelationsAddedComponent } from './cs-nc-relations-added.component';
import { CsNcRelationsRemovedComponent } from './cs-nc-relations-removed.component';
import { CsNcRelationsUpdatedComponent } from './cs-nc-relations-updated.component';
import { CsNcTypeComponent } from './cs-nc-type.component';
import { CsNcWaysAddedComponent } from './cs-nc-ways-added.component';
import { CsNcWaysRemovedComponent } from './cs-nc-ways-removed.component';
import { CsNcWaysUpdatedComponent } from './cs-nc-ways-updated.component';

@Component({
  selector: 'kpn-cs-nc-component',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-cs-nc-type [networkChangeInfo]="networkChangeInfo" />

    <div *ngIf="networkChangeInfo.after" class="kpn-detail">
      <kpn-version-change
        [before]="networkChangeInfo.before"
        [after]="networkChangeInfo.after"
      />
    </div>

    <kpn-cs-nc-nodes-removed [networkChangeInfo]="networkChangeInfo" />
    <kpn-cs-nc-nodes-added [networkChangeInfo]="networkChangeInfo" />
    <kpn-cs-nc-nodes-updated [networkChangeInfo]="networkChangeInfo" />

    <kpn-cs-nc-ways-removed [networkChangeInfo]="networkChangeInfo" />
    <kpn-cs-nc-ways-added [networkChangeInfo]="networkChangeInfo" />
    <kpn-cs-nc-ways-updated [networkChangeInfo]="networkChangeInfo" />

    <kpn-cs-nc-relations-removed [networkChangeInfo]="networkChangeInfo" />
    <kpn-cs-nc-relations-added [networkChangeInfo]="networkChangeInfo" />
    <kpn-cs-nc-relations-updated [networkChangeInfo]="networkChangeInfo" />

    <kpn-node-diffs [data]="nodeDiffs(networkChangeInfo)" />

    <kpn-route-diffs [data]="routeDiffs(networkChangeInfo)" />
  `,
  standalone: true,
  imports: [
    CsNcTypeComponent,
    NgIf,
    VersionChangeComponent,
    CsNcNodesRemovedComponent,
    CsNcNodesAddedComponent,
    CsNcNodesUpdatedComponent,
    CsNcWaysRemovedComponent,
    CsNcWaysAddedComponent,
    CsNcWaysUpdatedComponent,
    CsNcRelationsRemovedComponent,
    CsNcRelationsAddedComponent,
    CsNcRelationsUpdatedComponent,
    NodeDiffsComponent,
    RouteDiffsComponent,
  ],
})
export class CsNcComponent {
  @Input() page: ChangeSetPage;
  @Input() networkChangeInfo: NetworkChangeInfo;

  nodeDiffs(networkChangeInfo: NetworkChangeInfo): NodeDiffsData {
    return new NodeDiffsData(
      networkChangeInfo.networkNodes,
      this.page.summary.key.changeSetId,
      this.page.knownElements,
      List(this.page.nodeChanges)
    );
  }

  routeDiffs(networkChangeInfo: NetworkChangeInfo): RouteDiffsData {
    return new RouteDiffsData(
      networkChangeInfo.routes,
      this.page.summary.key.changeSetId,
      this.page.knownElements,
      List(this.page.routeChanges)
    );
  }
}
