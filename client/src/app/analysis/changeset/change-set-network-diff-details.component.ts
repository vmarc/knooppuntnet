import {Component, Input} from "@angular/core";
import {ChangeSetPage} from "../../kpn/shared/changes/change-set-page";
import {NetworkChangeInfo} from "../../kpn/shared/changes/details/network-change-info";
import {NodeDiffsData} from "./node-diffs/node-diffs-data";
import {RouteDiffsData} from "./route-diffs/route-diffs-data";

@Component({
  selector: "kpn-change-set-network-diff-details",
  template: `
    <div *ngFor="let networkChangeInfo of page.networkChanges" class="kpn-level-1">

      <div class="kpn-level-1-header">
        <a [id]="networkChangeInfo.networkId"></a>
        <div class="kpn-line">
          <kpn-network-type-icon [networkType]="networkChangeInfo.networkType"></kpn-network-type-icon>
          <span i18n="@@change-set.network-diffs.network">Network</span>
          <kpn-link-network-details [networkId]="networkChangeInfo.networkId" [title]="networkChangeInfo.networkName"></kpn-link-network-details>
        </div>
      </div>

      <div class="kpn-level-1-body">

        <div *ngIf="networkChangeInfo.changeType.name == 'Create'" class="kpn-detail">
          <b i18n="@@change-set.network-diffs.network-created">
            Network created
          </b>
        </div>
        <div *ngIf="networkChangeInfo.changeType.name == 'Delete'" class="kpn-detail">
          <b i18n="@@change-set.network-diffs.network-deleted">
            Network deleted
          </b>
        </div>
        <!-- no changeType text for "Update" -->


        <div *ngIf="!networkChangeInfo.orphanRoutes.oldRefs.isEmpty()" class="kpn-detail kpn-line">
          <span i18n="@@change-set.network-diffs.orphan-routes-resolved" class="kpn-label">
            Orphan routes added to this network
          </span>
          <div class="kpn-comma-list">
            <kpn-link-route-ref
              *ngFor="let ref of networkChangeInfo.orphanRoutes.oldRefs"
              [ref]="ref"
              [knownElements]="page.knownElements">
            </kpn-link-route-ref>
          </div>
          <kpn-icon-happy></kpn-icon-happy>
        </div>


        <div *ngIf="!networkChangeInfo.orphanRoutes.newRefs.isEmpty()" class="kpn-detail kpn-line">
          <span i18n="@@change-set.network-diffs.orphan-routes-introduced" class="kpn-label">
            Following routes that used to be part of this network have become orphan
          </span>
          <div class="kpn-comma-list">
            <kpn-link-route-ref
              *ngFor="let ref of networkChangeInfo.orphanRoutes.newRefs"
              [ref]="ref"
              [knownElements]="page.knownElements">
            </kpn-link-route-ref>
          </div>
          <kpn-icon-investigate></kpn-icon-investigate>
        </div>


        <div *ngIf="!networkChangeInfo.orphanNodes.oldRefs.isEmpty()" class="kpn-detail kpn-line">
          <span i18n="@@change-set.network-diffs.orphan-nodes-resolved" class="kpn-label">
            Orphan nodes added to this network
          </span>
          <div class="kpn-comma-list">
            <kpn-link-node-ref
              *ngFor="let ref of networkChangeInfo.orphanNodes.oldRefs"
              [ref]="ref"
              [knownElements]="page.knownElements">
            </kpn-link-node-ref>
          </div>
          <kpn-icon-happy></kpn-icon-happy>
        </div>


        <div *ngIf="!networkChangeInfo.orphanNodes.newRefs.isEmpty()" class="kpn-detail kpn-line">
          <span i18n="@@change-set.network-diffs.orphan-nodes-introduced" class="kpn-label">
            Following nodes that used to be part of this network have become orphan
          </span>
          <div class="kpn-comma-list">
            <kpn-link-node-ref
              *ngFor="let ref of networkChangeInfo.orphanNodes.newRefs"
              [ref]="ref"
              [knownElements]="page.knownElements">
            </kpn-link-node-ref>
          </div>
          <kpn-icon-investigate></kpn-icon-investigate>
        </div>


        <!-- networkMetaData() -->
        <div *ngIf="networkChangeInfo.after" class="kpn-detail">
          <kpn-version-change [before]="networkChangeInfo.before" [after]="networkChangeInfo.after"></kpn-version-change>
        </div>


        <!-- removedNodes() -->
        <div *ngIf="!networkChangeInfo.nodes.removed.isEmpty()" class="kpn-level-2">
          <div class="kpn-level-2-header kpn-line">
            <!-- @@ Verwijderde nodes die geen knooppunten zijn -->
            <span i18n="@@change-set.network-diffs.removed-nodes">Removed non-network nodes</span>
            <span class="kpn-thin">{{networkChangeInfo.nodes.removed.size}}</span>
            <kpn-icon-happy></kpn-icon-happy>
          </div>
          <div class="kpn-level-2-body kpn-comma-list">
            <kpn-osm-link-node *ngFor="let nodeId of networkChangeInfo.nodes.removed" [nodeId]="nodeId" [title]="nodeId.toString()"></kpn-osm-link-node>
          </div>
        </div>

        <!-- addedNodes() -->
        <div *ngIf="!networkChangeInfo.nodes.added.isEmpty()" class="kpn-level-2">
          <div class="kpn-level-2-header kpn-line">
            <!-- @@ Toegevoegde nodes die geen knooppunten zijn -->
            <span i18n="@@change-set.network-diffs.added-nodes">Added non-network nodes</span>
            <span class="kpn-thin">{{networkChangeInfo.nodes.added.size}}</span>
            <kpn-icon-investigate></kpn-icon-investigate>
          </div>
          <div class="kpn-level-2-body kpn-comma-list">
            <kpn-osm-link-node *ngFor="let nodeId of networkChangeInfo.nodes.added" [nodeId]="nodeId" [title]="nodeId.toString()"></kpn-osm-link-node>
          </div>
        </div>


        <!-- updatedNodes() -->
        <div *ngIf="!networkChangeInfo.nodes.updated.isEmpty()" class="kpn-level-2">
          <div class="kpn-level-2-header kpn-line">
            <!-- @@ Aangepaste nodes die geen knooppunten zijn -->
            <span i18n="@@change-set.network-diffs.updated-nodes">Updated non-network nodes</span>
            <span class="kpn-thin">{{networkChangeInfo.nodes.updated.size}}</span>
          </div>
          <div class="kpn-level-2-body kpn-comma-list">
            <kpn-osm-link-node *ngFor="let nodeId of networkChangeInfo.nodes.updated" [nodeId]="nodeId" [title]="nodeId.toString()"></kpn-osm-link-node>
          </div>
        </div>

        <!-- removedWays() -->
        <div *ngIf="!networkChangeInfo.ways.removed.isEmpty()" class="kpn-level-2">
          <div class="kpn-level-2-header kpn-line">
            <!-- @@ Verwijderde wegen -->
            <span i18n="@@change-set.network-diffs.removed-ways">Removed ways</span>
            <span class="kpn-thin">{{networkChangeInfo.ways.removed.size}}</span>
            <kpn-icon-happy></kpn-icon-happy>
          </div>
          <div class="kpn-level-2-body kpn-comma-list">
            <kpn-osm-link-way *ngFor="let wayId of networkChangeInfo.ways.removed" [wayId]="wayId" [title]="wayId.toString()"></kpn-osm-link-way>
          </div>
        </div>

        <!-- addedWays() -->
        <div *ngIf="!networkChangeInfo.ways.added.isEmpty()" class="kpn-level-2">
          <div class="kpn-level-2-header kpn-line">
            <!-- @@ Toegevoegde wegen -->
            <span i18n="@@change-set.network-diffs.added-ways">Added ways</span>
            <span class="kpn-thin">{{networkChangeInfo.ways.added.size}}</span>
            <kpn-icon-investigate></kpn-icon-investigate>
          </div>
          <div class="kpn-level-2-body kpn-comma-list">
            <kpn-osm-link-way *ngFor="let wayId of networkChangeInfo.ways.added" [wayId]="wayId" [title]="wayId.toString()"></kpn-osm-link-way>
          </div>
        </div>

        <!-- updatedWays() -->
        <div *ngIf="!networkChangeInfo.ways.updated.isEmpty()" class="kpn-level-2">
          <div class="kpn-level-2-header kpn-line">
            <!-- @@ Aangepaste wegen -->
            <span i18n="@@change-set.network-diffs.updated-ways">Updated ways</span>
            <span class="kpn-thin">{{networkChangeInfo.ways.updated.size}}</span>
          </div>
          <div class="kpn-level-2-body kpn-comma-list">
            <kpn-osm-link-way *ngFor="let wayId of networkChangeInfo.ways.updated" [wayId]="wayId" [title]="wayId.toString()"></kpn-osm-link-way>
          </div>
        </div>

        <!-- removedRelations() -->
        <div *ngIf="!networkChangeInfo.relations.removed.isEmpty()" class="kpn-level-2">
          <div class="kpn-level-2-header kpn-line">
            <!-- @@ Verwijderde relaties die geen route relatie zijn -->
            <span i18n="@@change-set.network-diffs.removed-relations">Removed non-route relations</span>
            <span class="kpn-thin">{{networkChangeInfo.relations.removed.size}}</span>
            <kpn-icon-happy></kpn-icon-happy>
          </div>
          <div class="kpn-level-2-body kpn-comma-list">
            <kpn-osm-link-relation *ngFor="let relationId of networkChangeInfo.relations.removed" [relationId]="relationId"
                                   [title]="relationId.toString()"></kpn-osm-link-relation>
          </div>
        </div>

        <!-- addedRelations() -->
        <div *ngIf="!networkChangeInfo.relations.added.isEmpty()" class="kpn-level-2">
          <div class="kpn-level-2-header kpn-line">
            <!-- @@ Toegevoegde relaties die geen route relatie zijn -->
            <span i18n="@@change-set.network-diffs.added-relations">Added non-route relations</span>
            <span class="kpn-thin">{{networkChangeInfo.relations.added.size}}</span>
            <kpn-icon-investigate></kpn-icon-investigate>
          </div>
          <div class="kpn-level-2-body kpn-comma-list">
            <kpn-osm-link-relation *ngFor="let relationId of networkChangeInfo.relations.added" [relationId]="relationId"
                                   [title]="relationId.toString()"></kpn-osm-link-relation>
          </div>
        </div>

        <!-- updatedRelations() -->
        <div *ngIf="!networkChangeInfo.relations.updated.isEmpty()" class="kpn-level-2">
          <div class="kpn-level-2-header kpn-line">
            <!-- @@ Aangepaste relaties die geen route relatie zijn -->
            <span i18n="@@change-set.network-diffs.updated-relations">Updated non-route relations</span>
            <span class="kpn-thin">{{networkChangeInfo.relations.updated.size}}</span>
          </div>
          <div class="kpn-level-2-body kpn-comma-list">
            <kpn-osm-link-relation *ngFor="let relationId of networkChangeInfo.relations.updated" [relationId]="relationId"
                                   [title]="relationId.toString()"></kpn-osm-link-relation>
          </div>
        </div>

        <!-- UiNodeDiffs -->
        <kpn-node-diffs [data]="nodeDiffs(networkChangeInfo)"></kpn-node-diffs>

        <!-- UiRouteDiffs -->
        <kpn-route-diffs [data]="routeDiffs(networkChangeInfo)"></kpn-route-diffs>

      </div>
    </div>
  `
})
export class ChangeSetNetworkDiffDetailsComponent {

  @Input() page: ChangeSetPage;

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
