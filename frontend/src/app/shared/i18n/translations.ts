export class Translations {
  private static readonly translations = new Map<string, string>([
    ['country.nl', $localize`:@@country.nl:The Netherlands`],
    ['country.be', $localize`:@@country.be:Belgium`],
    ['country.de', $localize`:@@country.de:Germany`],
    ['country.fr', $localize`:@@country.fr:France`],
    ['country.at', $localize`:@@country.at:Austria`],
    ['country.es', $localize`:@@country.es:Spain`],
    ['country.dk', $localize`:@@country.dk:Denmark`],
    //
    ['network-type.hiking', $localize`:@@network-type.hiking:Hiking`],
    ['network-type.cycling', $localize`:@@network-type.cycling:Cycling`],
    ['network-type.horse-riding', $localize`:@@network-type.horse-riding:Horse riding`],
    ['network-type.motorboat', $localize`:@@network-type.motorboat:Motorboat`],
    ['network-type.canoe', $localize`:@@network-type.canoe:Canoe`],
    ['network-type.inline-skating', $localize`:@@network-type.inline-skating:Inline skating`],
    //
    ['network-scope.local', $localize`:@@network-scope.local:local`],
    ['network-scope.regional', $localize`:@@network-scope.regional:regional`],
    ['network-scope.national', $localize`:@@network-scope.national:national`],
    ['network-scope.international', $localize`:@@network-scope.international:international`],
    //
    ['subset.in', $localize`:@@subset.in:in`],
    //
    ['map.start-node', $localize`:@@map.start-node:Start node`],
    ['map.end-node', $localize`:@@map.end-node:End node`],
    ['map.start-tentacle-node', $localize`:@@map.start-tentacle-node:Start tentacle node`],
    ['map.end-tentacle-node', $localize`:@@map.end-tentacle-node:End tentacle node`],
    ['map.redundant-node', $localize`:@@map.redundant-node:Redundant node`],
    //
    ['filter.all', $localize`:@@filter.all:all`],
    ['filter.yes', $localize`:@@filter.yes:yes`],
    ['filter.no', $localize`:@@filter.no:no`],
    ['filter.unknown', $localize`:@@filter.unknown:unknown`],
    ['filter.proposed', $localize`:@@filter.proposed:Proposed`],
    [
      'filter.definedInNetworkRelation',
      $localize`:@@filter.definedInNetworkRelation:Defined in network relation`,
    ],
    ['filter.referencedInRoute', $localize`:@@filter.referencedInRoute:Referenced in route`],
    ['filter.integrityCheck', $localize`:@@filter.integrityCheck:Integrity check`],
    [
      'filter.integrityCheckFailed',
      $localize`:@@filter.integrityCheckFailed:Integrity check failed`,
    ],
    ['filter.connection', $localize`:@@filter.connection:Connection`],
    ['filter.investigate', $localize`:@@filter.investigate:Investigate`],
    ['filter.accessible', $localize`:@@filter.accessible:Accessible`],
    ['filter.lastUpdated', $localize`:@@filter.lastUpdated:Last updated`],
    ['filter.lastSurvey', $localize`:@@filter.lastSurvey:Survey`],
    ['filter.lastWeek', $localize`:@@filter.lastWeek:last week`],
    ['filter.lastMonth', $localize`:@@filter.lastMonth:last month`],
    ['filter.lastYear', $localize`:@@filter.lastYear:last year`],
    ['filter.lastHalfYear', $localize`:@@filter.lastHalfYear:last half year`],
    ['filter.lastTwoYears', $localize`:@@filter.lastTwoYears:last two years`],
    ['filter.older', $localize`:@@filter.older:older`],
    ['filter.roleConnection', $localize`:@@filter.roleConnection:Role connection`],
    //
    ['wiki.home', $localize`:@@wiki.home:Knooppuntnet`],
    ['wiki.planner', $localize`:@@wiki.planner:Knooppuntnet_planner#What_do_you_see.3F`],
    ['wiki.planner.edit', $localize`:@@wiki.planner.edit:Knooppuntnet_planner#Edit_route`],
    ['wiki.login-page', $localize`:@@wiki.login-page:Knooppuntnet_analysis#Login`],
    ['wiki.logout-page', $localize`:@@wiki.logout-page:Knooppuntnet_analysis#Logout`],
    ['wiki.node-page', $localize`:@@wiki.node-page:Knooppuntnet_analysis#Node`],
    ['wiki.route-page', $localize`:@@wiki.route-page:Knooppuntnet_analysis#Route`],
    ['wiki.network-page', $localize`:@@wiki.network-page:Knooppuntnet_analysis#Network`],
    ['wiki.changes-page', $localize`:@@wiki.changes-page:Knooppuntnet_analysis#Changes`],
    ['wiki.location-page', $localize`:@@wiki.location-page:Knooppuntnet_analysis#Location`],
    [
      'wiki.overview-in-numbers-page',
      $localize`:@@wiki.overview-in-numbers-page:Knooppuntnet_analysis#Overview_in_numbers`,
    ],
    [
      'wiki.subset-networks-page',
      $localize`:@@wiki.subset-networks-page:Knooppuntnet_analysis#Subset_networks`,
    ],
    [
      'wiki.subset-facts-page',
      $localize`:@@wiki.subset-facts-page:Knooppuntnet_analysis#Subset_facts`,
    ],
    [
      'wiki.subset-orphan-nodes-page',
      $localize`:@@wiki.subset-orphan-nodes-page:Knooppuntnet_analysis#Subset_orphan_nodes`,
    ],
    [
      'wiki.subset-orphan-routes-page',
      $localize`:@@wiki.subset-orphan-routes-page:Knooppuntnet_analysis#Subset_orphan_routes`,
    ],
    ['wiki.subset-map-page', $localize`:@@wiki.subset-map-page:Knooppuntnet_analysis#Subset_map`],
    [
      'wiki.subset-changes-page',
      $localize`:@@wiki.subset-changes-page:Knooppuntnet_analysis#Subset_Changes`,
    ],
    //
    ['action.cancel', $localize`:@@action.cancel:Cancel`],
  ]);

  static get(key: string): string {
    return this.translations.get(key);
  }
}
