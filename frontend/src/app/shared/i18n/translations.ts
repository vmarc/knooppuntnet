import { RouteType } from '@api/common/route-type';

export class Translations {
  static readonly countryNl = $localize`:@@country.nl:The Netherlands`;
  static readonly countryBe = $localize`:@@country.be:Belgium`;
  static readonly countryDe = $localize`:@@country.de:Germany`;
  static readonly countryFr = $localize`:@@country.fr:France`;
  static readonly countryAt = $localize`:@@country.at:Austria`;
  static readonly countryEs = $localize`:@@country.es:Spain`;
  static readonly countryDk = $localize`:@@country.dk:Denmark`;

  static readonly routeTypeHiking = $localize`:@@route-type.hiking:Hiking`;
  static readonly routeTypeCycling = $localize`:@@route-type.cycling:Cycling`;
  static readonly routeTypeHorseRiding = $localize`:@@route-type.horse-riding:Horse riding`;
  static readonly routeTypeMotorboat = $localize`:@@route-type.motorboat:Motorboat`;
  static readonly routeTypeCanoe = $localize`:@@route-type.canoe:Canoe`;
  static readonly routeTypeInlineSkating = $localize`:@@route-type.inline-skating:Inline skating`;

  static routeTypeLabel(routeType: RouteType): string {
    let label = '';
    switch (routeType) {
      case 'hiking':
        label = Translations.routeTypeHiking;
        break;
      case 'cycling':
        label = Translations.routeTypeCycling;
        break;
      case 'horse-riding':
        label = Translations.routeTypeHorseRiding;
        break;
      case 'motorboat':
        label = Translations.routeTypeMotorboat;
        break;
      case 'canoe':
        label = Translations.routeTypeCanoe;
        break;
      case 'inline-skating':
        label = Translations.routeTypeInlineSkating;
        break;
    }
    return label;
  }

  private static readonly translations = new Map<string, string>([
    ['country.nl', this.countryNl],
    ['country.be', this.countryBe],
    ['country.de', this.countryDe],
    ['country.fr', this.countryFr],
    ['country.at', this.countryAt],
    ['country.es', this.countryEs],
    ['country.dk', this.countryDk],
    //
    ['route-type.hiking', this.routeTypeHiking],
    ['route-type.cycling', this.routeTypeCycling],
    ['route-type.horse-riding', this.routeTypeHorseRiding],
    ['route-type.motorboat', this.routeTypeMotorboat],
    ['route-type.canoe', this.routeTypeCanoe],
    ['route-type.inline-skating', this.routeTypeInlineSkating],
    //
    ['route-scope.local', $localize`:@@route-scope.local:local`],
    ['route-scope.regional', $localize`:@@route-scope.regional:regional`],
    ['route-scope.national', $localize`:@@route-scope.national:national`],
    ['route-scope.international', $localize`:@@route-scope.international:international`],
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
    ['filter.survey', $localize`:@@filter.survey:Last survey`],
    ['filter.fact', $localize`:@@filter.facts:Facts`],
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
    const translated = this.translations.get(key);
    if (translated) {
      return translated;
    }
    return '?' + key + '?';
  }
}
