import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { ChangeSetDetail } from '@api/common/changes/change-set-detail';
import { NetworkChangeInfo } from '@api/common/changes/details/network-change-info';
import { List } from 'immutable';
import { NodeDiffsData } from '../node-diffs/node-diffs-data';
import { NodeDiffsComponent } from '../node-diffs/node-diffs.component';
import { RouteDiffsData } from '../route-diffs/route-diffs-data';
import { RouteDiffsComponent } from '../route-diffs/route-diffs.component';
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
    <kpn-cs-nc-type [networkChangeInfo]="networkChangeInfo()" />

    @if (networkChangeInfo().after) {
      <div class="kpn-detail">
        <kpn-version-change
          [before]="networkChangeInfo().before"
          [after]="networkChangeInfo().after"
        />
      </div>
    }

    <kpn-cs-nc-nodes-removed [networkChangeInfo]="networkChangeInfo()" />
    <kpn-cs-nc-nodes-added [networkChangeInfo]="networkChangeInfo()" />
    <kpn-cs-nc-nodes-updated [networkChangeInfo]="networkChangeInfo()" />

    <kpn-cs-nc-ways-removed [networkChangeInfo]="networkChangeInfo()" />
    <kpn-cs-nc-ways-added [networkChangeInfo]="networkChangeInfo()" />
    <kpn-cs-nc-ways-updated [networkChangeInfo]="networkChangeInfo()" />

    <kpn-cs-nc-relations-removed [networkChangeInfo]="networkChangeInfo()" />
    <kpn-cs-nc-relations-added [networkChangeInfo]="networkChangeInfo()" />
    <kpn-cs-nc-relations-updated [networkChangeInfo]="networkChangeInfo()" />

    <kpn-node-diffs [data]="nodeDiffs(networkChangeInfo())" />

    <kpn-route-diffs [data]="routeDiffs(networkChangeInfo())" />
  `,
  imports: [
    CsNcNodesAddedComponent,
    CsNcNodesRemovedComponent,
    CsNcNodesUpdatedComponent,
    CsNcRelationsAddedComponent,
    CsNcRelationsRemovedComponent,
    CsNcRelationsUpdatedComponent,
    CsNcTypeComponent,
    CsNcWaysAddedComponent,
    CsNcWaysRemovedComponent,
    CsNcWaysUpdatedComponent,
    NodeDiffsComponent,
    RouteDiffsComponent,
    VersionChangeComponent,
  ],
})
export class CsNcComponent {
  detail = input.required<ChangeSetDetail>();
  networkChangeInfo = input.required<NetworkChangeInfo>();

  nodeDiffs(networkChangeInfo: NetworkChangeInfo): NodeDiffsData {
    return new NodeDiffsData(
      networkChangeInfo.networkNodes,
      this.detail().summary.key.changeSetId,
      this.detail().knownElements,
      List(this.detail().nodeChanges)
    );
  }

  routeDiffs(networkChangeInfo: NetworkChangeInfo): RouteDiffsData {
    return new RouteDiffsData(
      networkChangeInfo.routes,
      this.detail().summary.key.changeSetId,
      this.detail().knownElements,
      List(this.detail().routeChanges)
    );
  }
}
