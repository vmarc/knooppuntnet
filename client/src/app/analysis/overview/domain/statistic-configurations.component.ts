import {AfterViewInit, ChangeDetectorRef, Component, ViewChildren} from "@angular/core";
import {List} from "immutable";
import {Subset} from "src/app/kpn/api/custom/subset";
import {OverviewService} from "../overview/overview.service";
import {StatisticConfigurationComponent} from "./statistic-configuration.component";

@Component({
  selector: "kpn-statistic-configurations",
  template: `

    <div *ngIf="!isRegistryUpdated()">

      <kpn-statistic-configuration
        id="km"
        name="Length (km)"
        i18n-name="@@stats.km.name"
        i18n="@@stats.km.comment">
        Total length in kilometers.
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="NetworkCount"
        name="NetworkCount"
        i18n-name="@@stats.network-count.name"
        i18n="@@stats.network-count.comment"
        [linkFunction]="networks">
        Number of networks.
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="NodeCount"
        name="Node count"
        i18n-name="@@stats.node-count.name"
        i18n="@@stats.node-count.comment">
        Number of network nodes.
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="RouteCount"
        name="Route count"
        i18n-name="@@stats.route-count.name"
        i18n="@@stats.route-count.comment">
        Number of routes.
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="RouteNotContiniousNetworkCount"
        name="RouteNotContiniousNetworkCount"
        i18n-name="@@stats.route-not-continious-network-count.name"
        i18n="@@stats.route-not-continious-network-count.comment">
        Number of networks with broken routes.
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="RouteNotContiniousCount"
        fact="RouteNotContinious"
        name="RouteNotContinious"
        i18n-name="@@stats.route-not-continious.name"
        i18n="@@stats.route-not-continious.comment"
        [linkFunction]="factDetailCounts">
        Number of broken routes.
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="RouteNotContiniousPercentage"
        name="RouteNotContiniousPercentage"
        i18n-name="@@stats.route-not-continious-percentage.name"
        i18n="@@stats.route-not-continious-percentage.comment">
        Percentage of broken routes.
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="RouteBrokenNetworkCount"
        name="RouteBrokenNetworkCount"
        i18n-name="@@stats.route-broken-network-count.name"
        i18n="@@stats.route-broken-network-count.comment">
        Number of networks containing routes with issues.
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="RouteBrokenCount"
        name="RouteBrokenCount"
        i18n-name="@@stats.route-broken-count.name"
        i18n="@@stats.route-broken-count.comment">
        Number of routes with issues.
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="RouteBrokenPercentage"
        name="RouteBrokenPercentage"
        i18n-name="@@stats.route-broken-percentage.name"
        i18n="@@stats.route-broken-percentage.comment">
        Percentage of routes with issues.
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="RouteIncompleteCount"
        fact="RouteIncomplete"
        name="RouteIncomplete"
        i18n-name="@@stats.route-incomplete.name"
        i18n="@@stats.route-incomplete.comment"
        [markdownEnabled]="true"
        [linkFunction]="factDetailCounts">
        Number of routes that are marked as having an incomplete definition.\\
        A route definition is explicitely marked incomplete by adding a tag _"fixme"_ with
        value _"incomplete"_ in the route relation.
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="RouteIncompleteOkCount"
        fact="RouteIncompleteOk"
        name="RouteIncompleteOk"
        i18n-name="@@stats.route-incomplete-ok.name"
        i18n="@@stats.route-incomplete-ok.comment"
        [markdownEnabled]="true"
        [linkFunction]="factDetailCounts">
        Number of routes that are marked as having an incomplete definition, but
        that look ok after analysis.\\
        A route definition is explicitely marked incomplete by adding a tag _"fixme"_ with
        value _"incomplete"_ in the route relation.
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="RouteFixmetodoCount"
        fact="RouteFixmetodo"
        name="RouteFixmetodo"
        i18n-name="@@stats.route-fixmetodo.name"
        i18n="@@stats.route-fixmetodo.comment"
        [markdownEnabled]="true"
        [linkFunction]="factDetailCounts">
        Number of routes that are marked with _"fixmetodo"_.
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="OrphanNodeCount"
        name="Orphan nodes"
        i18n-name="@@stats.orphan-node-count.name"
        i18n="@@stats.orphan-node-count.comment"
        [markdownEnabled]="true"
        [linkFunction]="orphanNodes">
        Number of network nodes that do not belong to a network.\\
        The [node](glossary#node) was not added as a member to a valid
        [network relation](glossary#network-relation)
        and also not added as a member to a valid
        [route relation](glossary#route-relation)
        (that itself was added as a member to a valid network relation or is an orphan route).
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="OrphanRouteCount"
        name="Orphan routes"
        i18n-name="@@stats.orphan-route-count.name"
        i18n="@@stats.orphan-route-count.comment"
        [markdownEnabled]="true"
        [linkFunction]="orphanRoutes">
        Number of network routes that do not belong to a network.\\
        The route was not added as a member to a valid
        [network relation](glossary#network-relation).
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="OrphanRouteKm"
        name="Orphan route length (km)"
        i18n-name="@@stats.orphan-route-km.name"
        i18n="@@stats.orphan-route-km.comment">
        Total length (km) of the orphan routes (not included in network length above).
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="IntegrityCheckNetworkCount"
        name="IntegrityCheck"
        i18n-name="@@stats.integrity-check-network-count.name"
        i18n="@@stats.integrity-check-network-count.comment">
        Number of networks that include integrity check.
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="IntegrityCheckCount"
        name="IntegrityCheck"
        i18n-name="@@stats.integrity-check.name"
        i18n="@@stats.integrity-check.comment">
        Number of nodes with integrity check.
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="IntegrityCheckFailedCount"
        fact="IntegrityCheckFailed"
        name="IntegrityCheckFailed"
        i18n-name="@@stats.integrity-check-failed.name"
        i18n="@@stats.integrity-check-failed.comment"
        [linkFunction]="factDetailCounts">
        Number of failed integrity checks.
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="IntegrityCheckPassRate"
        name="IntegrityCheckPassRate"
        i18n-name="@@stats.integrity-check-pass-rate.name"
        i18n="@@stats.integrity-check-pass-rate.comment">
        Integrity check pass rate (percentage of ok checks).
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="IntegrityCheckCoverage"
        name="IntegrityCheckCoverage"
        i18n-name="@@stats.integrity-check-coverage.name"
        i18n="@@stats.integrity-check-coverage.comment">
        Integrity check coverage (percentage of nodes that do have integrity check information).
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="RouteUnusedSegmentsCount"
        fact="RouteUnusedSegments"
        name="RouteUnusedSegments"
        i18n-name="@@stats.route-unused-segments.name"
        i18n="@@stats.route-unused-segments.comment"
        [linkFunction]="factDetailCounts">
        Number of routes where one or more of the ways (or part of ways) are not used in the
        forward or backward path or in one of the tentacles.
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="RouteNodeMissingInWaysCount"
        fact="RouteNodeMissingInWays"
        name="RouteNodeMissingInWays"
        i18n-name="@@stats.route-node-missing-in-ways.name"
        i18n="@@stats.route-node-missing-in-ways.comment"
        [linkFunction]="factDetailCounts">
        Number of routes for which the end node and/or the begin node is not found in any of the ways of the route.
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="RouteRedundantNodesCount"
        fact="RouteRedundantNodes"
        name="RouteRedundantNodes"
        i18n-name="@@stats.route-redundant-nodes.name"
        i18n="@@stats.route-redundant-nodes.comment"
        [linkFunction]="factDetailCounts">
        Number of routes where the ways of the route contain extra nodes other than the start and end nodes.
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="RouteWithoutWaysCount"
        fact="RouteWithoutWays"
        name="RouteWithoutWays"
        i18n-name="@@stats.route-without-ways.name"
        i18n="@@stats.route-without-ways.comment"
        [markdownEnabled]="true"
        [linkFunction]="factDetailCounts">
        Routes without ways (a route is expected to have at least 1 way).
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="RouteNameMissingCount"
        fact="RouteNameMissing"
        name="RouteNameMissing"
        i18n-name="@@stats.route-name-missing.name"
        i18n="@@stats.route-name-missing.comment"
        [markdownEnabled]="true"
        [linkFunction]="factDetailCounts">
        Routes without a _"note"_ tag with the route name.
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="RouteTagMissingCount"
        fact="RouteTagMissing"
        name="RouteTagMissing"
        i18n-name="@@stats.route-tag-missing.name"
        i18n="@@stats.route-tag-missing.comment"
        [markdownEnabled]="true"
        [linkFunction]="factDetailCounts">
        The route relation does not contain a _"route"_ tag.
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="RouteTagInvalidCount"
        fact="RouteTagInvalid"
        name="RouteTagInvalid"
        i18n-name="@@stats.route-tag-invalid.name"
        i18n="@@stats.route-tag-invalid.comment"
        [markdownEnabled]="true"
        [linkFunction]="factDetailCounts">
        The value in the _"route"_ tag in the route relation is unexpected.
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="RouteUnexpectedNodeCount"
        fact="RouteUnexpectedNode"
        name="RouteUnexpectedNode"
        i18n-name="@@stats.route-unexpected-node.name"
        i18n="@@stats.route-unexpected-node.comment"
        [markdownEnabled]="true"
        [linkFunction]="factDetailCounts">
        Number of routes with one or more unexpected node members.\\
        In route relations we expect only nodes with tag _"rwn_ref"_ or _"rcn_ref"_.
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="RouteUnexpectedRelationCount"
        fact="RouteUnexpectedRelation"
        name="RouteUnexpectedRelation"
        i18n-name="@@stats.route-unexpected-relation.name"
        i18n="@@stats.route-unexpected-relation.comment"
        [markdownEnabled]="true"
        [linkFunction]="factDetailCounts">
        Number of routes with one or more unexpected members.\\In route relations we expect
        only members of type _"way"_, or members of type _"node"_ with a
        tag _"rwn_ref"_ or _"rcn_ref"_.
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="NetworkExtraMemberNodeCount"
        fact="NetworkExtraMemberNode"
        name="NetworkExtraMemberNode"
        i18n-name="@@stats.network-extra-member-node.name"
        i18n="@@stats.network-extra-member-node.comment"
        [markdownEnabled]="true"
        [linkFunction]="factDetailCounts">
        Number of network relation members of type _"node"_ that are unexpected (expect only
        [network nodes](glossary#node) or [information maps](glossary#info-map) as members
        in the network relation).
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="NetworkExtraMemberWayCount"
        fact="NetworkExtraMemberWay"
        name="NetworkExtraMemberWay"
        i18n-name="@@stats.network-extra-member-way.name"
        i18n="@@stats.network-extra-member-way.comment"
        [markdownEnabled]="true"
        [linkFunction]="factDetailCounts">
        Number of network relation members of type _"way"_ (expect only route relations
        or network nodes as members in the node network relation).
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="NetworkExtraMemberRelationCount"
        fact="NetworkExtraMemberRelation"
        name="NetworkExtraMemberRelation"
        i18n-name="@@stats.network-extra-member-relation.name"
        i18n="@@stats.network-extra-member-relation.comment"
        [markdownEnabled]="true"
        [linkFunction]="factDetailCounts">
        Number of network relation members of type _"relation"_ that are unexpected (expect only
        valid route relations or network nodes as members in the node network relation).
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="NodeMemberMissingCount"
        fact="NodeMemberMissing"
        name="NodeMemberMissing"
        i18n-name="@@stats.node-member-missing.name"
        i18n="@@stats.node-member-missing.comment"
        [linkFunction]="factDetailCounts">
        Number of nodes that are not included in the network relation
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="NameMissingCount"
        fact="NameMissing"
        name="NameMissing"
        i18n-name="@@stats.name-missing.name"
        i18n="@@stats.name-missing.comment"
        [markdownEnabled]="true"
        [linkFunction]="factDetailCounts">
        Number of networks without _"name"_ tag in the network relation.
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="RouteUnaccessibleCount"
        fact="RouteUnaccessible"
        name="RouteUnaccessible"
        i18n-name="@@stats.route-unaccessible.name"
        i18n="@@stats.route-unaccessible.comment"
        [markdownEnabled]="true"
        [linkFunction]="factDetailCounts">
        Number of [unaccessible](glossary#accessible) routes.
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="RouteInvalidSortingOrderCount"
        fact="RouteInvalidSortingOrder"
        name="RouteInvalidSortingOrder"
        i18n-name="@@stats.route-invalid-sorting-order.name"
        i18n="@@stats.route-invalid-sorting-order.comment"
        [linkFunction]="factDetailCounts">
        Number of routes with ways in wrong sorting order.
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="RouteNodeNameMismatchCount"
        fact="RouteNodeNameMismatch"
        name="RouteNodeNameMismatch"
        i18n-name="@@stats.route-node-name-mismatch.name"
        i18n="@@stats.route-node-name-mismatch.comment"
        [linkFunction]="factDetailCounts">
        Routes where the route name does not match with the names of the start node and the end node. The
        route name is expected to contain the start node name and the end node name, separated by a dash. The
        start node is expected to have a lower number than the end node.
      </kpn-statistic-configuration>

    </div>
  `,
  styles: [`
    :host {
      display: none;
    }
  `]
})
export class StatisticConfigurationsComponent implements AfterViewInit {

  @ViewChildren(StatisticConfigurationComponent) children: StatisticConfigurationComponent[];

  readonly networks = (id: string, subset: Subset) => subset.key() + "/networks";
  readonly orphanNodes = (id: string, subset: Subset) => subset.key() + "/orphan-nodes";
  readonly orphanRoutes = (id: string, subset: Subset) => subset.key() + "orphan-routes";
  readonly factDetailCounts = (id: string, subset: Subset) => subset.key() + "/" + id;

  constructor(private overviewService: OverviewService,
              private cdr: ChangeDetectorRef) {
  }

  ngAfterViewInit(): void {
    if (!this.isRegistryUpdated()) {
      const statistics = this.children.map(child => child.toStatistic());
      if (statistics != null) {
        this.overviewService.statisticConfigurations = List(statistics);
        this.cdr.detectChanges();
      }
    }
  }

  isRegistryUpdated() {
    return this.overviewService.statisticConfigurations !== null;
  }
}
