import { Injectable } from '@angular/core';
import { Map } from 'immutable';

@Injectable()
export class I18nService {
  private translations: Map<string, string> = this.buildTranslations();

  translation(key: string): string {
    return this.translations.get(key);
  }

  private buildTranslations(): Map<string, string> {
    const keysAndValues: Array<[string, string]> = [];

    keysAndValues.push([
      '@@country.nl',
      $localize`:@@country.nl:The Netherlands`,
    ]);
    keysAndValues.push(['@@country.be', $localize`:@@country.be:Belgium`]);
    keysAndValues.push(['@@country.de', $localize`:@@country.de:Germany`]);
    keysAndValues.push(['@@country.fr', $localize`:@@country.fr:France`]);
    keysAndValues.push(['@@country.at', $localize`:@@country.at:Austria`]);
    keysAndValues.push(['@@country.es', $localize`:@@country.es:Spain`]);

    keysAndValues.push([
      '@@network-type.hiking',
      $localize`:@@network-type.hiking:Hiking`,
    ]);
    keysAndValues.push([
      '@@network-type.cycling',
      $localize`:@@network-type.cycling:Cycling`,
    ]);
    keysAndValues.push([
      '@@network-type.horse-riding',
      $localize`:@@network-type.horse-riding:Horse riding`,
    ]);
    keysAndValues.push([
      '@@network-type.motorboat',
      $localize`:@@network-type.motorboat:Motorboat`,
    ]);
    keysAndValues.push([
      '@@network-type.canoe',
      $localize`:@@network-type.canoe:Canoe`,
    ]);
    keysAndValues.push([
      '@@network-type.inline-skating',
      $localize`:@@network-type.inline-skating:Inline skating`,
    ]);

    keysAndValues.push([
      '@@network-scope.local',
      $localize`:@@network-scope.local:local`,
    ]);
    keysAndValues.push([
      '@@network-scope.regional',
      $localize`:@@network-scope.regional:regional`,
    ]);
    keysAndValues.push([
      '@@network-scope.national',
      $localize`:@@network-scope.national:national`,
    ]);
    keysAndValues.push([
      '@@network-scope.international',
      $localize`:@@network-scope.international:international`,
    ]);

    keysAndValues.push(['@@subset.in', $localize`:@@subset.in:in`]);

    keysAndValues.push([
      '@@map.start-node',
      $localize`:@@map.start-node:Start node`,
    ]);
    keysAndValues.push(['@@map.end-node', $localize`:@@map.end-node:End node`]);
    keysAndValues.push([
      '@@map.start-tentacle-node',
      $localize`:@@map.start-tentacle-node:Start tentacle node`,
    ]);
    keysAndValues.push([
      '@@map.end-tentacle-node',
      $localize`:@@map.end-tentacle-node:End tentacle node`,
    ]);
    keysAndValues.push([
      '@@map.redundant-node',
      $localize`:@@map.redundant-node:Redundant node`,
    ]);

    keysAndValues.push([
      '@@map.layer.free-path',
      $localize`:@@map.layer.free-path:Path`,
    ]);
    keysAndValues.push([
      '@@map.layer.forward-route',
      $localize`:@@map.layer.forward-route:Forward route`,
    ]);
    keysAndValues.push([
      '@@map.layer.backward-route',
      $localize`:@@map.layer.backward-route:Backward route`,
    ]);
    keysAndValues.push([
      '@@map.layer.start-tentacle',
      $localize`:@@map.layer.start-tentacle:Start tentacle`,
    ]);
    keysAndValues.push([
      '@@map.layer.end-tentacle',
      $localize`:@@map.layer.end-tentacle:End tentacle`,
    ]);
    keysAndValues.push([
      '@@map.layer.unused',
      $localize`:@@map.layer.unused:Unused`,
    ]);
    keysAndValues.push([
      '@@map.layer.nodes',
      $localize`:@@map.layer.nodes:Nodes`,
    ]);
    keysAndValues.push([
      '@@map.layer.network',
      $localize`:@@map.layer.network:Network`,
    ]);
    keysAndValues.push([
      '@@map.layer.networks',
      $localize`:@@map.layer.networks:Networks`,
    ]);

    keysAndValues.push([
      '@@map.layer.unchanged',
      $localize`:@@map.layer.unchanged:Unchanged`,
    ]);
    keysAndValues.push([
      '@@map.layer.added',
      $localize`:@@map.layer.added:Added`,
    ]);
    keysAndValues.push([
      '@@map.layer.deleted',
      $localize`:@@map.layer.deleted:Deleted`,
    ]);

    keysAndValues.push([
      '@@map.layer.osm',
      $localize`:@@map.layer.osm:OpenStreetMap`,
    ]);
    keysAndValues.push([
      '@@map.layer.background',
      $localize`:@@map.layer.background:Background`,
    ]);
    keysAndValues.push([
      '@@map.layer.gpx',
      $localize`:@@map.layer.gpx:Your GPX trace`,
    ]);
    keysAndValues.push([
      '@@map.layer.tile-256-names',
      $localize`:@@map.layer.tile-256-names:Tilenames (256)`,
    ]);
    keysAndValues.push([
      '@@map.layer.tile-512-names',
      $localize`:@@map.layer.tile-512-names:Tilenames (512)`,
    ]);
    keysAndValues.push([
      '@@map.layer.other-routes',
      $localize`:@@map.layer.other-routes:Other routes`,
    ]);
    keysAndValues.push(['@@map.layer.node', $localize`:@@map.layer.node:Node`]);
    keysAndValues.push([
      '@@map.layer.cycling',
      $localize`:@@map.layer.cycling:Cycling`,
    ]);
    keysAndValues.push([
      '@@map.layer.hiking',
      $localize`:@@map.layer.hiking:Hiking`,
    ]);
    keysAndValues.push([
      '@@map.layer.horse-riding',
      $localize`:@@map.layer.horse-riding:Horse riding`,
    ]);
    keysAndValues.push([
      '@@map.layer.motorboat',
      $localize`:@@map.layer.motorboat:Motorboat`,
    ]);
    keysAndValues.push([
      '@@map.layer.canoe',
      $localize`:@@map.layer.canoe:Canoe`,
    ]);
    keysAndValues.push([
      '@@map.layer.inline-skating',
      $localize`:@@map.layer.inline-skating:Inline Skating`,
    ]);
    keysAndValues.push([
      '@@map.layer.nodes-and-routes',
      $localize`:@@map.layer.nodes-and-routes:Nodes and routes`,
    ]);
    keysAndValues.push([
      '@@map.layer.boundary',
      $localize`:@@map.layer.boundary:Boundary`,
    ]);
    keysAndValues.push([
      '@@map.layer.poi-areas',
      $localize`:@@map.layer.poi-areas:Poi areas`,
    ]);

    keysAndValues.push(['@@filter.all', $localize`:@@filter.all:all`]);
    keysAndValues.push(['@@filter.yes', $localize`:@@filter.yes:yes`]);
    keysAndValues.push(['@@filter.no', $localize`:@@filter.no:no`]);
    keysAndValues.push([
      '@@filter.unknown',
      $localize`:@@filter.unknown:unknown`,
    ]);
    keysAndValues.push([
      '@@filter.proposed',
      $localize`:@@filter.proposed:Proposed`,
    ]);
    keysAndValues.push([
      '@@filter.definedInNetworkRelation',
      $localize`:@@filter.definedInNetworkRelation:Defined in network relation`,
    ]);
    keysAndValues.push([
      '@@filter.definedInRouteRelation',
      $localize`:@@filter.definedInRouteRelation:Defined in route relation`,
    ]);
    keysAndValues.push([
      '@@filter.referencedInRoute',
      $localize`:@@filter.referencedInRoute:Referenced in route`,
    ]);
    keysAndValues.push([
      '@@filter.integrityCheck',
      $localize`:@@filter.integrityCheck:Integrity check`,
    ]);
    keysAndValues.push([
      '@@filter.integrityCheckFailed',
      $localize`:@@filter.integrityCheckFailed:Integrity check failed`,
    ]);
    keysAndValues.push([
      '@@filter.connection',
      $localize`:@@filter.connection:Connection`,
    ]);
    keysAndValues.push([
      '@@filter.investigate',
      $localize`:@@filter.investigate:Investigate`,
    ]);
    keysAndValues.push([
      '@@filter.accessible',
      $localize`:@@filter.accessible:Accessible`,
    ]);
    keysAndValues.push([
      '@@filter.lastUpdated',
      $localize`:@@filter.lastUpdated:Last updated`,
    ]);
    keysAndValues.push([
      '@@filter.lastSurvey',
      $localize`:@@filter.lastSurvey:Survey`,
    ]);
    keysAndValues.push([
      '@@filter.lastWeek',
      $localize`:@@filter.lastWeek:last week`,
    ]);
    keysAndValues.push([
      '@@filter.lastMonth',
      $localize`:@@filter.lastMonth:last month`,
    ]);
    keysAndValues.push([
      '@@filter.lastYear',
      $localize`:@@filter.lastYear:last year`,
    ]);
    keysAndValues.push([
      '@@filter.lastHalfYear',
      $localize`:@@filter.lastHalfYear:last half year`,
    ]);
    keysAndValues.push([
      '@@filter.lastTwoYears',
      $localize`:@@filter.lastTwoYears:last two years`,
    ]);
    keysAndValues.push(['@@filter.older', $localize`:@@filter.older:older`]);
    keysAndValues.push([
      '@@filter.roleConnection',
      $localize`:@@filter.roleConnection:Role connection`,
    ]);

    keysAndValues.push(['@@wiki.home', $localize`:@@wiki.home:Knooppuntnet`]);
    keysAndValues.push([
      '@@wiki.planner',
      $localize`:@@wiki.planner:Knooppuntnet_planner#What_do_you_see.3F`,
    ]);
    keysAndValues.push([
      '@@wiki.planner.edit',
      $localize`:@@wiki.planner.edit:Knooppuntnet_planner#Edit_route`,
    ]);
    keysAndValues.push([
      '@@wiki.login-page',
      $localize`:@@wiki.login-page:Knooppuntnet_analysis#Login`,
    ]);
    keysAndValues.push([
      '@@wiki.logout-page',
      $localize`:@@wiki.logout-page:Knooppuntnet_analysis#Logout`,
    ]);
    keysAndValues.push([
      '@@wiki.node-page',
      $localize`:@@wiki.node-page:Knooppuntnet_analysis#Node`,
    ]);
    keysAndValues.push([
      '@@wiki.route-page',
      $localize`:@@wiki.route-page:Knooppuntnet_analysis#Route`,
    ]);
    keysAndValues.push([
      '@@wiki.network-page',
      $localize`:@@wiki.network-page:Knooppuntnet_analysis#Network`,
    ]);
    keysAndValues.push([
      '@@wiki.changes-page',
      $localize`:@@wiki.changes-page:Knooppuntnet_analysis#Changes`,
    ]);
    keysAndValues.push([
      '@@wiki.location-page',
      $localize`:@@wiki.location-page:Knooppuntnet_analysis#Location`,
    ]);
    keysAndValues.push([
      '@@wiki.overview-in-numbers-page',
      $localize`:@@wiki.overview-in-numbers-page:Knooppuntnet_analysis#Overview_in_numbers`,
    ]);
    keysAndValues.push([
      '@@wiki.subset-networks-page',
      $localize`:@@wiki.subset-networks-page:Knooppuntnet_analysis#Subset_networks`,
    ]);
    keysAndValues.push([
      '@@wiki.subset-facts-page',
      $localize`:@@wiki.subset-facts-page:Knooppuntnet_analysis#Subset_facts`,
    ]);
    keysAndValues.push([
      '@@wiki.subset-orphan-nodes-page',
      $localize`:@@wiki.subset-orphan-nodes-page:Knooppuntnet_analysis#Subset_orphan_nodes`,
    ]);
    keysAndValues.push([
      '@@wiki.subset-orphan-routes-page',
      $localize`:@@wiki.subset-orphan-routes-page:Knooppuntnet_analysis#Subset_orphan_routes`,
    ]);
    keysAndValues.push([
      '@@wiki.subset-map-page',
      $localize`:@@wiki.subset-map-page:Knooppuntnet_analysis#Subset_map`,
    ]);
    keysAndValues.push([
      '@@wiki.subset-changes-page',
      $localize`:@@wiki.subset-changes-page:Knooppuntnet_analysis#Subset_Changes`,
    ]);

    return Map<string, string>(keysAndValues);
  }
}
