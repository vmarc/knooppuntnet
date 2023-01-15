import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { ChangeSetPage } from '@api/common/changes/change-set-page';
import { NetworkChangeInfo } from '@api/common/changes/details/network-change-info';
import { List } from 'immutable';
import { NodeDiffsData } from '../node-diffs/node-diffs-data';
import { RouteDiffsData } from '../route-diffs/route-diffs-data';

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
