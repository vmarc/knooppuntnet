import {AfterViewInit, ChangeDetectorRef, Component, ViewChildren} from '@angular/core';
import {StatisticConfigurationComponent} from "./statistic-configuration.component";
import {OverviewService} from "./overview.service";
import {List} from "immutable";
import {Subset} from 'src/app/kpn/shared/subset';

@Component({
  selector: 'kpn-statistic-configurations',
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
        name="RouteIncompleteOk"
        i18n-name="@@stats.route-incomplete-ok.name"
        i18n="@@stats.route-incomplete-ok.comment"
        [markdownEnabled]="true"
        [linkFunction]="factDetailCounts">
        Number of routes that are marked as having an incomplete definition, but
        that look ok after analysis.\\
        A route definition is explicitely marked incomplete by adding a tag _"fixme"_ with
        value _"incomplete"_ in the route relation.
        <!-- Aantal routes die gemarkeerd zijn als onvolledig, maar die na analyse ok lijken te zijn.\\ -->
        <!-- Deze routes zijn uitdrukkelijk gemarkeerd als onvolledig door het toevoegen van -->
        <!-- een tag met sleutel _"fixme"_ en waarde _"incomplete"_ in de route relatie. -->
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="RouteFixmetodoCount"
        name="RouteFixmetodo"
        i18n-name="@@stats.route-fixmetodo.name"
        i18n="@@stats.route-fixmetodo.comment"
        [markdownEnabled]="true"
        [linkFunction]="factDetailCounts">
        Number of routes that are marked with _"fixmetodo"_.
        <!-- Aantal routes gemarkeerd met _"fixmetodo"_. -->
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="OrphanNodeCount"
        name="Orphan nodes"
        i18n-name="@@stats.orphan-node-count.name"
        i18n="@@stats.xxx.comment"
        [markdownEnabled]="true"
        [linkFunction]="orphanNodes"> <!-- Knooppunt wezen -->
        Number of network nodes that do not belong to a network.\\
        The [node](glossary#node) was not added as a member to a valid
        [network relation](glossary#network-relation)
        and also not added as a member to a valid
        [route relation](glossary#route-relation)
        (that itself was added as a member to a valid network relation or is an orphan route).
        <!-- Aantal op zichzelf staande knooppunten (niet teruggevonden in een netwerk).
        Deze [knooppunten](glossary#node)
        werden niet teruggevonden als deel van een geldige 
        [netwerk relatie](network-relation),
        of als deel van een geldige 
        [route relatie](glossary#route-relation)
        (dit kan een route relatie zijn die deel uitmaakt van een network relatie, maar ook een op zichzelf 
        staande route relatie die geen deel uitmaakt van een network relatie).-->
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="OrphanRouteCount"
        name="Orphan routes"
        i18n-name="@@stats.orphan-route-count.name"
        i18n="@@stats.orphan-route-count.comment"
        [markdownEnabled]="true"
        [linkFunction]="orphanRoutes"> <!-- Route wezen -->
        Number of network routes that do not belong to a network.\\
        The route was not added as a member to a valid
        [network relation](glossary#network-relation).
        <!-- Aantal routes die niet tot een netwerk behoren.
        Deze routes worden niet teruggevonden in een geldige 
        [netwerk relatie](glossary#network-relation). -->
      </kpn-statistic-configuration>


      <kpn-statistic-configuration
        id="OrphanRouteKm"
        name="Orphan route length (km)"
        i18n-name="@@stats.orphan-route-km.name"
        i18n="@@stats.orphan-route-km.comment"> <!-- Route wezen lengte (km) -->
        Total length (km) of the orphan routes (not included in network length above).
        <!-- Totale lengte (km) van de route wezen (deze worden niet meegeteld in de netwerk lengte hierboven). -->
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="IntegrityCheckNetworkCount"
        name="IntegrityCheck"
        i18n-name="@@stats.integrity-check-network-count.name"
        i18n="@@stats.integrity-check-network-count.comment"> <!-- IntegrityCheck -->
        Number of networks that include integrity check.
        <!-- Aantal netwerken met integriteitscontroles. -->
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="IntegrityCheckCount"
        name="IntegrityCheck"
        i18n-name="@@stats.integrity-check.name"
        i18n="@@stats.integrity-check.comment"> <!-- IntegrityCheck -->
        Number of nodes with integrity check.
        <!-- Aantal knooppunten met integriteitscontrole. -->
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="IntegrityCheckFailedCount"
        name="IntegrityCheckFailed"
        i18n-name="@@stats.integrity-check-failed.name"
        i18n="@@stats.integrity-check-failed.comment"
        [linkFunction]="factDetailCounts">
        Number of failed integrity checks.
        <!-- Aantal falende integriteitscontroles. -->
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="IntegrityCheckPassRate"
        name="IntegrityCheckPassRate"
        i18n-name="@@stats.integrity-check-pass-rate.name"
        i18n="@@stats.integrity-check-pass-rate.comment"> <!-- Slaagpercentage -->
        Integrity check pass rate (percentage of ok checks).
        <!-- Slaagpercentage van de integriteitscontroles. -->
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="IntegrityCheckCoverage"
        name="IntegrityCheckCoverage"
        i18n-name="@@stats.integrity-check-coverage.name"
        i18n="@@stats.integrity-check-coverage.comment"> <!-- Dekkingsgraad -->
        Integrity check coverage (percentage of nodes that do have integrity check information).
        <!-- Dekkingsgraad van de integriteitscontroles (percentage van knooppunten met integriteitscontrole informatie). -->
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="RouteUnusedSegmentsCount"
        name="RouteUnusedSegments"
        i18n-name="@@stats.route-unused-segments.name"
        i18n="@@stats.route-unused-segments.comment"
        [linkFunction]="factDetailCounts">
        Number of routes where one or more of the ways (or part of ways) are not used in the
        forward or backward path or in one of the tentacles.
        <!-- Aantal routes waarbij niet alle wegen (of stukken van wegen) gebruikt worden in de heen of -->
        <!-- terugweg of in een van de tentakels. -->
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="RouteNodeMissingInWaysCount"
        name="RouteNodeMissingInWays"
        i18n-name="@@stats.route-node-missing-in-ways.name"
        i18n="@@stats.route-node-missing-in-ways.comment"
        [linkFunction]="factDetailCounts">
        Number of routes for which the end node and/or the begin node is not found in any of the ways of the route.
        <!-- Aantal routes waarvan de begin en/of eind knooppunten niet voorkomen in een van de wegen van deze route. -->
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="RouteRedundantNodesCount"
        name="RouteRedundantNodes"
        i18n-name="@@stats.route-redundant-nodes.name"
        i18n="@@stats.route-redundant-nodes.comment"
        [linkFunction]="factDetailCounts">
        Number of routes where the ways of the route contain extra nodes other than the start and end nodes.
        <!-- Aantal routes waarin er zich naast de begin en eind knooppunten nog andere vreemde knooppunten -->
        <!-- bevinden in de wegen van de route. -->
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="RouteWithoutWaysCount"
        name="RouteWithoutWays"
        i18n-name="@@stats.route-without-ways.name"
        i18n="@@stats.route-without-ways.comment"
        [markdownEnabled]="true"
        [linkFunction]="factDetailCounts">
        Routes without ways (a route is expected to have at least 1 way).
        <!-- Aantal routes zonder wegen. We verwachten tenmiste 1 weg _("way")_. -->
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="RouteNameMissingCount"
        name="RouteNameMissing"
        i18n-name="@@stats.route-name-missing.name"
        i18n="@@stats.route-name-missing.comment"
        [markdownEnabled]="true"
        [linkFunction]="factDetailCounts">
        Routes without a _"note"_ tag with the route name.
        <!-- Aantal routes zonder tag met sleutel _"note"_ met de route naam in de route relatie. -->
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="RouteTagMissingCount"
        name="RouteTagMissing"
        i18n-name="@@stats.route-tag-missing.name"
        i18n="@@stats.route-tag-missing.comment"
        [markdownEnabled]="true"
        [linkFunction]="factDetailCounts">
        The route relation does not contain a _"route"_ tag.
        <!-- De _"route"_ tag ontbreekt in de route relatie. -->
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="RouteTagInvalidCount"
        name="RouteTagInvalid"
        i18n-name="@@stats.route-tag-invalid.name"
        i18n="@@stats.route-tag-invalid.comment"
        [markdownEnabled]="true"
        [linkFunction]="factDetailCounts">
        The value in the _"route"_ tag in the route relation is unexpected.
        <!-- De waarde in de _"route"_ tag in de route relatie is onverwacht. -->
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="RouteUnexpectedNodeCount"
        name="RouteUnexpectedNode"
        i18n-name="@@stats.route-unexpected-node.name"
        i18n="@@stats.route-unexpected-node.comment"
        [markdownEnabled]="true"
        [linkFunction]="factDetailCounts"> <!-- xxx -->
        Number of routes with one or more unexpected node members.\\
        In route relations we expect only nodes with tag _"rwn_ref"_ or _"rcn_ref"_.
        <!-- Aantal routes met 1 of meer overwachte knopen. -->
        <!-- In route relaties verwachten we enkel knopen met een tag met sleutel _"rwn_ref"_ of _"rcn_ref"_. -->
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="RouteUnexpectedRelationCount"
        name="RouteUnexpectedRelation"
        i18n-name="@@stats.route-unexpected-relation.name"
        i18n="@@stats.route-unexpected-relation.comment"
        [markdownEnabled]="true"
        [linkFunction]="factDetailCounts"> <!-- xxx -->
        Number of routes with one or more unexpected members.\\In route relations we expect
        only members of type _"way"_, or members of type _"node"_ with a
        tag _"rwn_ref"_ or _"rcn_ref"_.
        <!-- Aantal routes met 1 of meer overwachte relaties. In route relaties verwachten -->
        <!-- we enkel onderdelen van het type _"way"_, of onderdelen van het type _"node"_ met een tag -->
        <!-- met sleutel _"rwn_ref"_ of _"rcn_ref"_. -->
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="NetworkExtraMemberNodeCount"
        name="NetworkExtraMemberNode"
        i18n-name="@@stats.network-extra-member-node.name"
        i18n="@@stats.network-extra-member-node.comment"
        [markdownEnabled]="true"
        [linkFunction]="factDetailCounts">
        Number of network relation members of type _"node"_ that are unexpected (expect only
        [network nodes](glossary#node) or [information maps](glossary#info-map) as members
        in the network relation).
        <!--Aantal onverwachte knopen in netwerkrelaties (we verwachten enkel knopen dit ook echt een 
        [knooppunt](glossary#node) definitie zijn, of [informatie kaarten](glossary#info-map)
        in de netwerk relatie). -->
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="NetworkExtraMemberWayCount"
        name="NetworkExtraMemberWay"
        i18n-name="@@stats.network-extra-member-way.name"
        i18n="@@stats.network-extra-member-way.comment"
        [markdownEnabled]="true"
        [linkFunction]="factDetailCounts">
        Number of network relation members of type _"way"_ (expect only route relations
        or network nodes as members in the node network relation).
        <!-- Overwachte wegen _("ways")_ in netwerk relaties (in network relaties verwachten we 
        enkel route relaties of knooppunten, geen wegen). -->
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="NetworkExtraMemberRelationCount"
        name="NetworkExtraMemberRelation"
        i18n-name="@@stats.network-extra-member-relation.name"
        i18n="@@stats.network-extra-member-relation.comment"
        [markdownEnabled]="true"
        [linkFunction]="factDetailCounts">
        Number of network relation members of type _"relation"_ that are unexpected (expect only
        valid route relations or network nodes as members in the node network relation).
        <!-- Aantal overwachte relaties in network relaties. In network relaties verwachten we -->
        <!-- enkel route relaties of knooppunten, geen relaties anders dan route relaties. -->
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="NodeMemberMissingCount"
        name="NodeMemberMissing"
        i18n-name="@@stats.node-member-missing.name"
        i18n="@@stats.node-member-missing.comment"
        [linkFunction]="factDetailCounts">
        Number of nodes that are not included in the network relation
        <!-- Aantal knooppunten dat niet is opgenomen als lid in een netwerk relatie. -->
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="NameMissingCount"
        name="NameMissing"
        i18n-name="@@stats.name-missing.name"
        i18n="@@stats.name-missing.comment"
        [linkFunction]="factDetailCounts"> <!-- xxx -->
        Number of networks without _"name"_ tag in the network relation.
        <!-- Aantal netwerken zonder tag met sleutel _"name"_ in de netwerk relatie. -->
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="RouteUnaccessibleCount"
        name="RouteUnaccessible"
        i18n-name="@@stats.route-unaccessible.name"
        i18n="@@stats.route-unaccessible.comment"
        [markdownEnabled]="true"
        [linkFunction]="factDetailCounts"> <!-- xxx -->
        Number of [unaccessible](glossary#accessible) routes.
        <!-- Aantal [ontoegangkelijke](glossary#accessible) routes. -->
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="RouteInvalidSortingOrderCount"
        name="RouteInvalidSortingOrder"
        i18n-name="@@stats.route-invalid-sorting-order.name"
        i18n="@@stats.route-invalid-sorting-order.comment"
        [linkFunction]="factDetailCounts">
        Number of routes with ways in wrong sorting order.
        <!-- Aantal routes met wegen in verkeerde volgorde. -->
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="RouteReversedCount"
        name="RouteReversed"
        i18n-name="@@stats.route-reversed.name"
        i18n="@@stats.route-reversed.comment"
        [linkFunction]="factDetailCounts">
        Number of routes where the ways are in reverse order (from high node number to low node number).
        <!-- Aantal routes met de wegen in omgekeerde volgorde (van hoog knooppunt nummer naar laag knooppunt nummer). -->
      </kpn-statistic-configuration>

      <kpn-statistic-configuration
        id="RouteNodeNameMismatchCount"
        name="RouteNodeNameMismatch"
        i18n-name="@@stats.route-node-name-mismatch.name"
        i18n="@@stats.route-node-name-mismatch.comment"
        [linkFunction]="factDetailCounts">
        Routes where the route name does not match with the names of the start node and the end node. The
        route name is expected to contain the start node name and the end node name, separated by a dash. The
        start node is expected to have a lower number than the end node.
        <!-- Aantal routes waarvan de route naam niet overeenkomt met de namen van de start en eind nodes.
        We verwachten dat de route naam de namen van de start en eindknooppunten bevat, gescheiden door
        een koppelteken. Het knooppunt met het laagste nummer verwachten we vooraan. -->
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

  readonly networks = (id: string, subset: Subset) => "networks/" + subset.key();
  readonly orphanNodes = (id: string, subset: Subset) => "orphan-nodes/" + subset.key();
  readonly orphanRoutes = (id: string, subset: Subset) => "orphan-routes/" + subset.key();
  readonly factDetailCounts = (id: string, subset: Subset) => id + "/" + subset.key();

  @ViewChildren(StatisticConfigurationComponent) children: StatisticConfigurationComponent[];

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
