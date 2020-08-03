import {ChangeDetectionStrategy} from "@angular/core";
import {AfterViewInit, ChangeDetectorRef, Component, ElementRef} from "@angular/core";
import {I18nService} from "./i18n.service";

@Component({
  selector: "kpn-i18n",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="!isRegistryUpdated()">

      <span id="@@country.nl" i18n="@@country.nl">The Netherlands</span>
      <span id="@@country.be" i18n="@@country.be">Belgium</span>
      <span id="@@country.de" i18n="@@country.de">Germany</span>
      <span id="@@country.fr" i18n="@@country.fr">France</span>
      <span id="@@country.at" i18n="@@country.at">Austria</span>

      <span id="@@network-type.hiking" i18n="@@network-type.hiking">Hiking</span>
      <span id="@@network-type.cycling" i18n="@@network-type.cycling">Cycling</span>
      <span id="@@network-type.horse-riding" i18n="@@network-type.horse-riding">Horse riding</span>
      <span id="@@network-type.motorboat" i18n="@@network-type.motorboat">Motorboat</span>
      <span id="@@network-type.canoe" i18n="@@network-type.canoe">Canoe</span>
      <span id="@@network-type.inline-skating" i18n="@@network-type.inline-skating">Inline Skating</span>

      <span id="@@subset.in" i18n="@@subset.in">in</span>

      <span id="@@map.start-node" i18n="@@map.start-node">Start node</span>
      <span id="@@map.end-node" i18n="@@map.end-node">End node</span>
      <span id="@@map.start-tentacle-node" i18n="@@map.start-tentacle-node">Start tentacle node</span>
      <span id="@@map.end-tentacle-node" i18n="@@map.end-tentacle-node">End tentacle node</span>
      <span id="@@map.redundant-node" i18n="@@map.redundant-node">Redundant node</span>

      <span id="@@map.layer.forward-route" i18n="@@map.layer.forward-route">Forward route</span>
      <span id="@@map.layer.backward-route" i18n="@@map.layer.backward-route">Backward route</span>
      <span id="@@map.layer.start-tentacle" i18n="@@map.layer.start-tentacle">Start tentacle</span>
      <span id="@@map.layer.end-tentacle" i18n="@@map.layer.end-tentacle">End tentacle</span>
      <span id="@@map.layer.unused" i18n="@@map.layer.unused">Unused</span>
      <span id="@@map.layer.nodes" i18n="@@map.layer.nodes">Nodes</span>
      <span id="@@map.layer.network" i18n="@@map.layer.network">Network</span>
      <span id="@@map.layer.networks" i18n="@@map.layer.networks">Networks</span>

      <span id="@@map.layer.unchanged" i18n="@@map.layer.unchanged">Unchanged</span>
      <span id="@@map.layer.added" i18n="@@map.layer.added">Added</span>
      <span id="@@map.layer.deleted" i18n="@@map.layer.deleted">Deleted</span>

      <span id="@@map.layer.osm" i18n="@@map.layer.osm">OpenStreetMap</span>
      <span id="@@map.layer.gpx" i18n="@@map.layer.gpx">Your GPX trace</span>
      <span id="@@map.layer.tile-names" i18n="@@map.layer.tile-names">Tilenames</span>
      <span id="@@map.layer.other-routes" i18n="@@map.layer.other-routes">Other routes</span>
      <span id="@@map.layer.node" i18n="@@map.layer.node">Node</span>
      <span id="@@map.layer.cycling" i18n="@@map.layer.cycling">Cycling</span>
      <span id="@@map.layer.hiking" i18n="@@map.layer.hiking">Hiking</span>
      <span id="@@map.layer.horse-riding" i18n="@@map.layer.horse-riding">Horse riding</span>
      <span id="@@map.layer.motorboat" i18n="@@map.layer.motorboat">Motorboat</span>
      <span id="@@map.layer.canoe" i18n="@@map.layer.canoe">Canoe</span>
      <span id="@@map.layer.inline-skating" i18n="@@map.layer.inline-skating">Inline Skating</span>
      <span id="@@map.layer.nodes-and-routes" i18n="@@map.layer.nodes-and-routes">Nodes and routes</span>
      <span id="@@map.layer.boundary" i18n="@@map.layer.boundary">Boundary</span>

      <span id="@@filter.all" i18n="@@filter.all">all</span>
      <span id="@@filter.yes" i18n="@@filter.yes">yes</span>
      <span id="@@filter.no" i18n="@@filter.no">no</span>
      <span id="@@filter.unknown" i18n="@@filter.unknown">unknown</span>
      <span id="@@filter.definedInNetworkRelation"
            i18n="@@filter.definedInNetworkRelation">Defined in network relation</span>
      <span id="@@filter.definedInRouteRelation" i18n="@@filter.definedInRouteRelation">Defined in route relation</span>
      <span id="@@filter.referencedInRoute" i18n="@@filter.referencedInRoute">Referenced in route</span>
      <span id="@@filter.integrityCheck" i18n="@@filter.integrityCheck">Integrity check</span>
      <span id="@@filter.integrityCheckFailed" i18n="@@filter.integrityCheckFailed">Integrity check failed</span>
      <span id="@@filter.connection" i18n="@@filter.connection">Connection</span>
      <span id="@@filter.investigate" i18n="@@filter.investigate">Investigate</span>
      <span id="@@filter.accessible" i18n="@@filter.accessible">Accessible</span>
      <span id="@@filter.lastUpdated" i18n="@@filter.lastUpdated">Last updated</span>
      <span id="@@filter.lastSurvey" i18n="@@filter.lastSurvey">Last survey</span>
      <span id="@@filter.lastWeek" i18n="@@filter.lastWeek">last week</span>
      <span id="@@filter.lastMonth" i18n="@@filter.lastMonth">last month</span>
      <span id="@@filter.lastYear" i18n="@@filter.lastYear">last year</span>
      <span id="@@filter.lastHalfYear" i18n="@@filter.lastHalfYear">last half year</span>
      <span id="@@filter.lastTwoYears" i18n="@@filter.lastTwoYears">last two years</span>
      <span id="@@filter.older" i18n="@@filter.older">older</span>
      <span id="@@filter.roleConnection" i18n="@@filter.roleConnection">Role connection</span>

      <span id="@@wiki.home" i18n="@@wiki.home">Knooppuntnet</span>
      <span id="@@wiki.planner" i18n="@@wiki.planner">Knooppuntnet_planner#What_do_you_see.3F</span>
      <span id="@@wiki.planner.edit" i18n="@@wiki.planner">Knooppuntnet_planner#Edit_route</span>
      <span id="@@wiki.login-page" i18n="@@wiki.login-page">Knooppuntnet_analysis#Login</span>
      <span id="@@wiki.logout-page" i18n="@@wiki.logout-page">Knooppuntnet_analysis#Logout</span>
      <span id="@@wiki.node-page" i18n="@@wiki.node-page">Knooppuntnet_analysis#Node</span>
      <span id="@@wiki.route-page" i18n="@@wiki.route-page">Knooppuntnet_analysis#Route</span>
      <span id="@@wiki.network-page" i18n="@@wiki.network-page">Knooppuntnet_analysis#Network</span>
      <span id="@@wiki.changes-page" i18n="@@wiki.changes-page">Knooppuntnet_analysis#Changes</span>
      <span id="@@wiki.location-page" i18n="@@wiki.location-page">Knooppuntnet_analysis#Location</span>
      <span id="@@wiki.overview-in-numbers-page" i18n="@@wiki.overview-in-numbers-page">Knooppuntnet_analysis#Overview_in_numbers</span>
      <span id="@@wiki.subset-networks-page" i18n="@@wiki.subset-networks-page">Knooppuntnet_analysis#Subset_networks</span>
      <span id="@@wiki.subset-facts-page" i18n="@@wiki.subset-facts-page">Knooppuntnet_analysis#Subset_facts</span>
      <span id="@@wiki.subset-orphan-nodes-page" i18n="@@wiki.subset-orphan-nodes-page">Knooppuntnet_analysis#Subset_orphan_nodes</span>
      <span id="@@wiki.subset-orphan-routes-page" i18n="@@wiki.subset-orphan-routes-page">Knooppuntnet_analysis#Subset_orphan_routes</span>
      <span id="@@wiki.subset-map-page" i18n="@@wiki.subset-map-page">Knooppuntnet_analysis#Subset_map</span>
      <span id="@@wiki.subset-changes-page" i18n="@@wiki.subset-changes-page">Knooppuntnet_analysis#Subset_Changes</span>

    </div>
  `,
  styles: [`
    :host {
      display: none;
    }
  `]
})
export class I18nComponent implements AfterViewInit {

  constructor(private element: ElementRef,
              private i18nService: I18nService,
              private cdr: ChangeDetectorRef) {
  }

  ngAfterViewInit(): void {
    const divElement = this.element.nativeElement.children[0];
    if (divElement != null) {
      const elements = divElement.children;
      this.i18nService.updateRegistry(elements);
      this.cdr.detectChanges();
    }
  }

  isRegistryUpdated(): boolean {
    return this.i18nService.isRegistryUpdated();
  }
}
