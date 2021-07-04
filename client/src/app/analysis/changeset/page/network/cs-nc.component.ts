import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { ChangeSetPage } from '@api/common/changes/change-set-page';
import { NetworkChangeInfo } from '@api/common/changes/details/network-change-info';
import { NodeDiffsData } from '../node-diffs/node-diffs-data';
import { RouteDiffsData } from '../route-diffs/route-diffs-data';

@Component({
  selector: 'kpn-cs-nc-component',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-cs-nc-type [networkChangeInfo]="networkChangeInfo"></kpn-cs-nc-type>

    <kpn-cs-nc-orphan-routes-new
      [networkChangeInfo]="networkChangeInfo"
      [knownElements]="page.knownElements"
    ></kpn-cs-nc-orphan-routes-new>
    <kpn-cs-nc-orphan-routes-old
      [networkChangeInfo]="networkChangeInfo"
      [knownElements]="page.knownElements"
    ></kpn-cs-nc-orphan-routes-old>
    <kpn-cs-nc-orphan-nodes-new
      [networkChangeInfo]="networkChangeInfo"
      [knownElements]="page.knownElements"
    ></kpn-cs-nc-orphan-nodes-new>
    <kpn-cs-nc-orphan-nodes-old
      [networkChangeInfo]="networkChangeInfo"
      [knownElements]="page.knownElements"
    ></kpn-cs-nc-orphan-nodes-old>

    <div *ngIf="networkChangeInfo.after" class="kpn-detail">
      <kpn-version-change
        [before]="networkChangeInfo.before"
        [after]="networkChangeInfo.after"
      ></kpn-version-change>
    </div>

    <kpn-cs-nc-nodes-removed
      [networkChangeInfo]="networkChangeInfo"
    ></kpn-cs-nc-nodes-removed>
    <kpn-cs-nc-nodes-added
      [networkChangeInfo]="networkChangeInfo"
    ></kpn-cs-nc-nodes-added>
    <kpn-cs-nc-nodes-updated
      [networkChangeInfo]="networkChangeInfo"
    ></kpn-cs-nc-nodes-updated>

    <kpn-cs-nc-ways-removed
      [networkChangeInfo]="networkChangeInfo"
    ></kpn-cs-nc-ways-removed>
    <kpn-cs-nc-ways-added
      [networkChangeInfo]="networkChangeInfo"
    ></kpn-cs-nc-ways-added>
    <kpn-cs-nc-ways-updated
      [networkChangeInfo]="networkChangeInfo"
    ></kpn-cs-nc-ways-updated>

    <kpn-cs-nc-relations-removed
      [networkChangeInfo]="networkChangeInfo"
    ></kpn-cs-nc-relations-removed>
    <kpn-cs-nc-relations-added
      [networkChangeInfo]="networkChangeInfo"
    ></kpn-cs-nc-relations-added>
    <kpn-cs-nc-relations-updated
      [networkChangeInfo]="networkChangeInfo"
    ></kpn-cs-nc-relations-updated>

    <kpn-node-diffs [data]="nodeDiffs(networkChangeInfo)"></kpn-node-diffs>

    <kpn-route-diffs [data]="routeDiffs(networkChangeInfo)"></kpn-route-diffs>
  `,
})
export class CsNcComponent {
  @Input() page: ChangeSetPage;
  @Input() networkChangeInfo: NetworkChangeInfo;

  nodeDiffs(networkChangeInfo: NetworkChangeInfo): NodeDiffsData {
    return new NodeDiffsData(
      networkChangeInfo.networkNodes,
      this.page.summary.key.changeSetId,
      this.page.knownElements,
      this.page.nodeChanges
    );
  }

  routeDiffs(networkChangeInfo: NetworkChangeInfo): RouteDiffsData {
    return new RouteDiffsData(
      networkChangeInfo.routes,
      this.page.summary.key.changeSetId,
      this.page.knownElements,
      this.page.routeChanges
    );
  }
}
