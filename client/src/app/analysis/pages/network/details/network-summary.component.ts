import {Component, Input} from "@angular/core";
import {NetworkDetailsPage} from "../../../../kpn/shared/network/network-details-page";

@Component({
  selector: "kpn-network-summary",
  template: `
    <p>
      {{page.attributes.km}} km
    </p>
    <p>
      {{page.networkSummary.nodeCount}}
      <ng-container i18n="@@network-details.nodes">nodes</ng-container>
    </p>
    <p>
      {{page.networkSummary.routeCount}}
      <ng-container i18n="@@network-details.routes">routes</ng-container>
    </p>
    <p>
      <kpn-network-type [networkType]="page.attributes.networkType"></kpn-network-type>
    </p>
    <p>
      <kpn-osm-link-relation [relationId]="page.attributes.id"></kpn-osm-link-relation>
      <kpn-josm-relation [relationId]="page.attributes.id"></kpn-josm-relation>
    </p>
    
    <p *ngIf="page.attributes.brokenRouteCount > 0" class="kpn-line">
      <mat-icon svgIcon="warning"></mat-icon>
      <span i18n="@@network-details.contains-broken-routes">This network contains broken (non-continuous) routes.</span>
      <!--@@ Dit network bevat verbindingen die niet geheel aaneensluitend zijn. -->
    </p>
    
    <p *ngIf="!page.active" i18n="@@network-details.not-active">
      Not active      
    </p>
    
  `
})
export class NetworkSummaryComponent {
  @Input() page: NetworkDetailsPage;
}
