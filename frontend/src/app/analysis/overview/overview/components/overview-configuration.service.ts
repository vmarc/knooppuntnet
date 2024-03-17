import { Injectable } from '@angular/core';
import { Subset } from '@api/custom';
import { Subsets } from '@app/kpn/common';
import { List } from 'immutable';
import { StatisticConfiguration } from '../../domain/statistic-configuration';

@Injectable({
  providedIn: 'root',
})
export class OverviewConfigurationService {
  statisticConfigurations: List<StatisticConfiguration> = List(this.buildStatisticConfigurations());

  private buildStatisticConfigurations(): StatisticConfiguration[] {
    const networks = (_factId: string, subset: Subset) => Subsets.key(subset) + '/networks';
    const orphanNodes = (_factId: string, subset: Subset) => Subsets.key(subset) + '/orphan-nodes';
    const orphanRoutes = (factId: string, subset: Subset) => Subsets.key(subset) + '/orphan-routes';
    const factDetailCounts = (factId: string, subset: Subset) =>
      Subsets.key(subset) + '/facts/' + factId;

    const configurations: StatisticConfiguration[] = [];

    configurations.push(
      new StatisticConfiguration(
        'Distance',
        '',
        false,
        null,
        $localize`:@@stats.km.name:Length (km)`,
        $localize`:@@stats.km.comment:Total length in kilometers.`
      )
    );

    configurations.push(
      new StatisticConfiguration(
        'NetworkCount',
        '',
        false,
        networks,
        $localize`:@@stats.network-count.name:NetworkCount`,
        $localize`:@@stats.network-count.comment:Number of networks.`
      )
    );

    configurations.push(
      new StatisticConfiguration(
        'NodeCount',
        '',
        false,
        null,
        $localize`:@@stats.node-count.name:Node count`,
        $localize`:@@stats.node-count.comment:Number of network nodes.`
      )
    );

    configurations.push(
      new StatisticConfiguration(
        'RouteCount',
        '',
        false,
        null,
        $localize`:@@stats.route-count.name:Route count`,
        $localize`:@@stats.route-count.comment:Number of routes.`
      )
    );

    configurations.push(
      new StatisticConfiguration(
        'RouteNotContiniousCount',
        'RouteNotContinious',
        false,
        factDetailCounts,
        $localize`:@@stats.route-not-continious.name:RouteNotContinious`,
        $localize`:@@stats.route-not-continious.comment:Number of broken routes.`
      )
    );

    configurations.push(
      new StatisticConfiguration(
        'RouteBrokenCount',
        '',
        false,
        null,
        $localize`:@@stats.route-broken-count.name:RouteBrokenCount`,
        $localize`:@@stats.route-broken-count.comment:Number of routes with issues.`
      )
    );

    configurations.push(
      new StatisticConfiguration(
        'RouteIncompleteCount',
        'RouteIncomplete',
        true,
        factDetailCounts,
        $localize`:@@stats.route-incomplete.name:RouteIncomplete`,
        $localize`:@@stats.route-incomplete.comment:
          Number of routes that are marked as having an incomplete definition.\\
          A route definition is explicitely marked incomplete by adding a tag *"fixme"* with
          value *"incomplete"* in the route relation.`
      )
    );

    configurations.push(
      new StatisticConfiguration(
        'RouteIncompleteOkCount',
        'RouteIncompleteOk',
        true,
        factDetailCounts,
        $localize`:@@stats.route-incomplete-ok.name:RouteIncompleteOk`,
        $localize`:@@stats.route-incomplete-ok.comment:
          Number of routes that are marked as having an incomplete definition, but
          that look ok after analysis.\\
          A route definition is explicitely marked incomplete by adding a tag *"fixme"* with
          value *"incomplete"* in the route relation.`
      )
    );

    configurations.push(
      new StatisticConfiguration(
        'RouteFixmetodoCount',
        'RouteFixmetodo',
        true,
        factDetailCounts,
        $localize`:@@stats.route-fixmetodo.name:RouteFixmetodo`,
        $localize`:@@stats.route-fixmetodo.comment:Number of routes that are marked with *"fixmetodo"*.`
      )
    );

    configurations.push(
      new StatisticConfiguration(
        'OrphanNodeCount',
        '',
        true,
        orphanNodes,
        $localize`:@@stats.orphan-node-count.name:Orphan nodes`,
        $localize`:@@stats.orphan-node-count.comment:
          Number of network nodes that do not belong to a network.\\
          The [node](https://wiki.openstreetmap.org/wiki/Knooppuntnet_analysis#node "node in glossary") was not added as a member to a valid
          [network relation](https://wiki.openstreetmap.org/wiki/Knooppuntnet_analysis#network "relation in glossary")
          and also not added as a member to a valid
          [route relation](https://wiki.openstreetmap.org/wiki/Knooppuntnet_analysis#route "route relation in glossary")
          (that itself was added as a member to a valid network relation or is a free route).`
      )
    );

    configurations.push(
      new StatisticConfiguration(
        'OrphanRouteCount',
        '',
        true,
        orphanRoutes,
        $localize`:@@stats.orphan-route-count.name:Free routes`,
        $localize`:@@stats.orphan-route-count.comment:
          Number of network routes that do not belong to a network.\\
          The route was not added as a member to a valid
          [network relation](https://wiki.openstreetmap.org/wiki/Knooppuntnet_analysis#network "network relation in glossary").`
      )
    );

    configurations.push(
      new StatisticConfiguration(
        'IntegrityCheckNetworkCount',
        '',
        false,
        null,
        $localize`:@@stats.integrity-check-network-count.name:IntegrityCheck`,
        $localize`:@@stats.integrity-check-network-count.comment:Number of networks that include integrity check.`
      )
    );

    configurations.push(
      new StatisticConfiguration(
        'IntegrityCheckCount',
        '',
        false,
        null,
        $localize`:@@stats.integrity-check.name:IntegrityCheck`,
        $localize`:@@stats.integrity-check.comment:Number of nodes with integrity check.`
      )
    );

    configurations.push(
      new StatisticConfiguration(
        'IntegrityCheckFailedCount',
        'IntegrityCheckFailed',
        false,
        factDetailCounts,
        $localize`:@@stats.integrity-check-failed.name:IntegrityCheckFailed`,
        $localize`:@@stats.integrity-check-failed.comment:Number of failed integrity checks.`
      )
    );

    configurations.push(
      new StatisticConfiguration(
        'IntegrityCheckPassRate',
        '',
        false,
        null,
        $localize`:@@stats.integrity-check-pass-rate.name:IntegrityCheckPassRate`,
        $localize`:@@stats.integrity-check-pass-rate.comment:Integrity check pass rate (percentage of ok checks).`
      )
    );

    configurations.push(
      new StatisticConfiguration(
        'IntegrityCheckCoverage',
        '',
        false,
        null,
        $localize`:@@stats.integrity-check-coverage.name:IntegrityCheckCoverage`,
        $localize`:@@stats.integrity-check-coverage.comment:
          Integrity check coverage (percentage of nodes that do have integrity check information).`
      )
    );

    configurations.push(
      new StatisticConfiguration(
        'RouteUnusedSegmentsCount',
        'RouteUnusedSegments',
        false,
        factDetailCounts,
        $localize`:@@stats.route-unused-segments.name:RouteUnusedSegments`,
        $localize`:@@stats.route-unused-segments.comment:
          Number of routes where one or more of the ways (or part of ways) are not used in the
          forward or backward path or in one of the tentacles.`
      )
    );

    configurations.push(
      new StatisticConfiguration(
        'RouteNodeMissingInWaysCount',
        'RouteNodeMissingInWays',
        false,
        factDetailCounts,
        $localize`:@@stats.route-node-missing-in-ways.name:RouteNodeMissingInWays`,
        $localize`:@@stats.route-node-missing-in-ways.comment:
          Number of routes for which there is a least one node is not found in any of the ways of the route.`
      )
    );

    configurations.push(
      new StatisticConfiguration(
        'RouteRedundantNodesCount',
        'RouteRedundantNodes',
        false,
        factDetailCounts,
        $localize`:@@stats.route-redundant-nodes.name:RouteRedundantNodes`,
        $localize`:@@stats.route-redundant-nodes.comment:
          Number of routes where the ways of the route contain extra nodes other than the start and end nodes.`
      )
    );

    configurations.push(
      new StatisticConfiguration(
        'RouteWithoutNodesCount',
        'RouteWithoutNodes',
        true,
        factDetailCounts,
        $localize`:@@stats.route-without-nodes.name:RouteWithoutNodes`,
        $localize`:@@stats.route-without-nodes.comment:
          Routes without network nodes.`
      )
    );

    configurations.push(
      new StatisticConfiguration(
        'RouteWithoutWaysCount',
        'RouteWithoutWays',
        true,
        factDetailCounts,
        $localize`:@@stats.route-without-ways.name:RouteWithoutWays`,
        $localize`:@@stats.route-without-ways.comment:
          Routes without ways (a route is expected to have at least 1 way).`
      )
    );

    configurations.push(
      new StatisticConfiguration(
        'RouteNameMissingCount',
        'RouteNameMissing',
        true,
        factDetailCounts,
        $localize`:@@stats.route-name-missing.name:RouteNameMissing`,
        $localize`:@@stats.route-name-missing.comment:Routes without the required tags from which the route name can be derived.`
      )
    );

    configurations.push(
      new StatisticConfiguration(
        'RouteTagMissingCount',
        'RouteTagMissing',
        true,
        factDetailCounts,
        $localize`:@@stats.route-tag-missing.name:RouteTagMissing`,
        $localize`:@@stats.route-tag-missing.comment:The route relation does not contain a *"route"* tag.`
      )
    );

    configurations.push(
      new StatisticConfiguration(
        'RouteTagInvalidCount',
        'RouteTagInvalid',
        true,
        factDetailCounts,
        $localize`:@@stats.route-tag-invalid.name:RouteTagInvalid`,
        $localize`:@@stats.route-tag-invalid.comment:
          The value in the *"route"* tag in the route relation is unexpected.`
      )
    );

    configurations.push(
      new StatisticConfiguration(
        'RouteUnexpectedNodeCount',
        'RouteUnexpectedNode',
        true,
        factDetailCounts,
        $localize`:@@stats.route-unexpected-node.name:RouteUnexpectedNode`,
        $localize`:@@stats.route-unexpected-node.comment:
          Number of routes with one or more unexpected node members.\\
          In route relations we expect only nodes with tags that make it a valid network node.`
      )
    );

    configurations.push(
      new StatisticConfiguration(
        'RouteUnexpectedRelationCount',
        'RouteUnexpectedRelation',
        true,
        factDetailCounts,
        $localize`:@@stats.route-unexpected-relation.name:RouteUnexpectedRelation`,
        $localize`:@@stats.route-unexpected-relation.comment:
          Number of routes with one or more unexpected members.\\In route relations we expect
          only members of type *"way"* or *"node"*.`
      )
    );

    configurations.push(
      new StatisticConfiguration(
        'NetworkExtraMemberNodeCount',
        'NetworkExtraMemberNode',
        true,
        factDetailCounts,
        $localize`:@@stats.network-extra-member-node.name:NetworkExtraMemberNode`,
        $localize`:@@stats.network-extra-member-node.comment:
          Number of network relation members of type *"node"* that are unexpected (expect only
          [network nodes](https://wiki.openstreetmap.org/wiki/Knooppuntnet_analysis#node "node in glossary")
          or [information maps](https://wiki.openstreetmap.org/wiki/Knooppuntnet_analysis#information-map "information maps in glossary") as members
          in the network relation).`
      )
    );

    configurations.push(
      new StatisticConfiguration(
        'NetworkExtraMemberWayCount',
        'NetworkExtraMemberWay',
        true,
        factDetailCounts,
        $localize`:@@stats.network-extra-member-way.name:NetworkExtraMemberWay`,
        $localize`:@@stats.network-extra-member-way.comment:
          Number of network relation members of type *"way"* (expect only route relations
          or network nodes as members in the node network relation).`
      )
    );

    configurations.push(
      new StatisticConfiguration(
        'NetworkExtraMemberRelationCount',
        'NetworkExtraMemberRelation',
        true,
        factDetailCounts,
        $localize`:@@stats.network-extra-member-relation.name:NetworkExtraMemberRelation`,
        $localize`:@@stats.network-extra-member-relation.comment:
          Number of network relation members of type *"relation"* that are unexpected (expect only
          valid route relations or network nodes as members in the node network relation).`
      )
    );

    configurations.push(
      new StatisticConfiguration(
        'NodeMemberMissingCount',
        'NodeMemberMissing',
        false,
        factDetailCounts,
        $localize`:@@stats.node-member-missing.name:NodeMemberMissing`,
        $localize`:@@stats.node-member-missing.comment:Number of nodes that are not included in the network relation.`
      )
    );

    configurations.push(
      new StatisticConfiguration(
        'NameMissingCount',
        'NameMissing',
        true,
        factDetailCounts,
        $localize`:@@stats.name-missing.name:NameMissing`,
        $localize`:@@stats.name-missing.comment:Number of networks without *"name"* tag in the network relation.`
      )
    );

    configurations.push(
      new StatisticConfiguration(
        'RouteInaccessibleCount',
        'RouteInaccessible',
        true,
        factDetailCounts,
        $localize`:@@stats.route-inaccessible.name:RouteInaccessible`,
        $localize`:@@stats.route-inaccessible.comment:
          Number of [inaccessible](https://wiki.openstreetmap.org/wiki/Knooppuntnet_analysis#accessible "accessible in glossary") routes.`
      )
    );

    configurations.push(
      new StatisticConfiguration(
        'RouteNodeNameMismatchCount',
        'RouteNodeNameMismatch',
        false,
        factDetailCounts,
        $localize`:@@stats.route-node-name-mismatch.name:RouteNodeNameMismatch`,
        $localize`:@@stats.route-node-name-mismatch.comment:
          Routes where the route name does not match with the names of the start node and the end node. The
          route name is expected to contain the start node name and the end node name, separated by a dash.`
      )
    );

    configurations.push(
      new StatisticConfiguration(
        'RouteNameDeprecatedNoteTagCount',
        'RouteNameDeprecatedNoteTag',
        true,
        factDetailCounts,
        $localize`:@@stats.route-name-deprecated-note-tag.name:RouteNameDeprecatedNoteTag`,
        $localize`:@@stats.route-name-deprecated-note-tag.comment:
          Routes where the route name was defined in the *"note"* tag. This is OK, but the use
          of the *"note"* tag for route names is no longer recommended (deprecated).  The idea is 
          that the *"note"* should be used for mapper notes only. The *"ref"* and *"name"* tags can 
          be used for naming routes.`
      )
    );

    return configurations;
  }
}
