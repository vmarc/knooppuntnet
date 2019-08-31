import {Component, Input} from "@angular/core";
import {ChangeSetPage} from "../../../kpn/shared/changes/change-set-page";
import {NetworkChangeInfo} from "../../../kpn/shared/changes/details/network-change-info";
import {NodeDiffsData} from "../node-diffs/node-diffs-data";
import {RouteDiffsData} from "../route-diffs/route-diffs-data";

@Component({
  selector: "kpn-change-set-network-change-component",
  template: `
    <kpn-change-set-network-change-type
      [networkChangeInfo]="networkChangeInfo">
    </kpn-change-set-network-change-type>

    <kpn-change-set-network-change-orphan-routes-new
      [networkChangeInfo]="networkChangeInfo"
      [knownElements]="page.knownElements">
    </kpn-change-set-network-change-orphan-routes-new>

    <kpn-change-set-network-change-orphan-routes-old
      [networkChangeInfo]="networkChangeInfo"
      [knownElements]="page.knownElements">
    </kpn-change-set-network-change-orphan-routes-old>

    <kpn-change-set-network-change-orphan-nodes-new
      [networkChangeInfo]="networkChangeInfo"
      [knownElements]="page.knownElements">
    </kpn-change-set-network-change-orphan-nodes-new>

    <kpn-change-set-network-change-orphan-nodes-old
      [networkChangeInfo]="networkChangeInfo"
      [knownElements]="page.knownElements">
    </kpn-change-set-network-change-orphan-nodes-old>


    <!-- networkMetaData() -->
    <div *ngIf="networkChangeInfo.after" class="kpn-detail">
      <kpn-version-change [before]="networkChangeInfo.before" [after]="networkChangeInfo.after"></kpn-version-change>
    </div>

    <kpn-change-set-network-change-nodes-removed [networkChangeInfo]="networkChangeInfo"></kpn-change-set-network-change-nodes-removed>
    <kpn-change-set-network-change-nodes-added [networkChangeInfo]="networkChangeInfo"></kpn-change-set-network-change-nodes-added>
    <kpn-change-set-network-change-nodes-updated [networkChangeInfo]="networkChangeInfo"></kpn-change-set-network-change-nodes-updated>

    <kpn-change-set-network-change-ways-removed [networkChangeInfo]="networkChangeInfo"></kpn-change-set-network-change-ways-removed>
    <kpn-change-set-network-change-ways-added [networkChangeInfo]="networkChangeInfo"></kpn-change-set-network-change-ways-added>
    <kpn-change-set-network-change-ways-updated [networkChangeInfo]="networkChangeInfo"></kpn-change-set-network-change-ways-updated>

    <kpn-change-set-network-change-relations-removed [networkChangeInfo]="networkChangeInfo"></kpn-change-set-network-change-relations-removed>
    <kpn-change-set-network-change-relations-added [networkChangeInfo]="networkChangeInfo"></kpn-change-set-network-change-relations-added>
    <kpn-change-set-network-change-relations-updated [networkChangeInfo]="networkChangeInfo"></kpn-change-set-network-change-relations-updated>

    <kpn-node-diffs [data]="nodeDiffs(networkChangeInfo)"></kpn-node-diffs>

    <kpn-route-diffs [data]="routeDiffs(networkChangeInfo)"></kpn-route-diffs>
  `
})
export class ChangeSetNetworkChangeComponent {

  @Input() page: ChangeSetPage;
  @Input() networkChangeInfo: NetworkChangeInfo;

  nodeDiffs(networkChangeInfo: NetworkChangeInfo): NodeDiffsData {
    return new NodeDiffsData(
      networkChangeInfo.networkNodes,
      this.page.summary.key.changeSetId,
      this.page.knownElements,
      this.page.nodeChanges);
  }

  routeDiffs(networkChangeInfo: NetworkChangeInfo): RouteDiffsData {
    return new RouteDiffsData(
      networkChangeInfo.routes,
      this.page.summary.key.changeSetId,
      this.page.knownElements,
      this.page.routeChanges);
  }

}
