import { viewChild } from '@angular/core';
import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatDivider } from '@angular/material/divider';
import { MatAccordion } from '@angular/material/expansion';
import { MatExpansionPanelHeader } from '@angular/material/expansion';
import { MatExpansionPanelContent } from '@angular/material/expansion';
import { MatExpansionPanel } from '@angular/material/expansion';
import { RouterLink } from '@angular/router';
import { SubsetFactDetailsPage } from '@api/common/subset/subset-fact-details-page';
import { Facts } from '@app/analysis/fact';
import { ExpandCollapseComponent } from '@app/shared/components/button/expand-collapse.component';
import { IconNetworkComponent } from '@app/shared/components/icon/icon-network.component';
import { IconNodeComponent } from '@app/shared/components/icon/icon-node.component';
import { IconRelationComponent } from '@app/shared/components/icon/icon-relation.component';
import { IconRouteComponent } from '@app/shared/components/icon/icon-route.component';
import { IconWayComponent } from '@app/shared/components/icon/icon-way.component';
import { LinkNodeComponent } from '@app/shared/components/link/link-node.component';
import { LinkRouteComponent } from '@app/shared/components/link/link-route.component';
import { OsmLinkNodeComponent } from '@app/shared/components/link/osm-link-node.component';
import { OsmLinkRelationComponent } from '@app/shared/components/link/osm-link-relation.component';
import { OsmLinkWayComponent } from '@app/shared/components/link/osm-link-way.component';
import { ActionButtonNetworkComponent } from '../../../components/action/action-button-network.component';
import { ActionButtonNodeComponent } from '../../../components/action/action-button-node.component';
import { ActionButtonRelationComponent } from '../../../components/action/action-button-relation.component';
import { ActionButtonRouteComponent } from '../../../components/action/action-button-route.component';
import { ActionButtonWayComponent } from '../../../components/action/action-button-way.component';

@Component({
  selector: 'kpn-subset-fact-details',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (page().networks.length > 0) {
      <kpn-expand-collapse [accordion]="accordion()" />
      @if (fact(); as fact) {
        <mat-accordion multi>
          @for (networkFactRefs of page().networks; track networkFactRefs) {
            <mat-expansion-panel togglePosition="before">
              <mat-expansion-panel-header>
                <div class="kpn-align-center">
                  <kpn-icon-network />
                  @if (networkFactRefs.networkId === 0) {
                    <!-- TODO add button to load all routes -->
                    <span i18n="@@subset-facts.orphan-routes" class="free-route-indent"
                      >Free routes</span
                    >
                  } @else {
                    <kpn-action-button-network [relationId]="networkFactRefs.networkId" />
                  }
                  <div class="kpn-line">
                    @if (networkFactRefs.networkId !== 0) {
                      <a [routerLink]="'/analysis/network/' + networkFactRefs.networkId">
                        {{ networkFactRefs.networkName }}
                      </a>
                    }
                    @if (fact.hasNodeRefs()) {
                      <span>{{ networkFactRefs.factRefs.length }}</span>
                      <span i18n="@@subset-facts.nodes"> node(s) </span>
                    }
                    @if (fact.hasRouteRefs()) {
                      <span>{{ networkFactRefs.factRefs.length }}</span>
                      <span i18n="@@subset-facts.routes">route(s)</span>
                    }
                  </div>
                </div>
              </mat-expansion-panel-header>

              <ng-template matExpansionPanelContent>
                <mat-divider />
                <div class="sideline">
                  @for (ref of networkFactRefs.factRefs; track ref) {
                    <div class="kpn-align-center">
                      @if (fact.hasNodeRefs()) {
                        <kpn-icon-node />
                        <kpn-action-button-node [nodeId]="ref.id" />
                        <kpn-link-node [nodeId]="ref.id" [nodeName]="ref.name" />
                      }
                      @if (fact.hasRouteRefs()) {
                        <kpn-icon-route />
                        <kpn-action-button-route
                          [routeType]="page().subsetInfo.routeType"
                          [relationId]="ref.id"
                        />
                        <kpn-link-route
                          [routeId]="ref.id"
                          [routeName]="ref.name"
                          [routeType]="page().subsetInfo.routeType"
                        />
                      }
                      @if (fact.hasOsmNodeRefs()) {
                        <kpn-icon-node />
                        <kpn-action-button-node [nodeId]="ref.id" />
                        <kpn-osm-link-node [nodeId]="ref.id" [title]="ref.name" />
                      }
                      @if (fact.hasOsmWayRefs()) {
                        <kpn-icon-way />
                        <kpn-action-button-way [wayId]="ref.id" />
                        <kpn-osm-link-way [wayId]="ref.id" [title]="ref.name" />
                      }
                      @if (fact.hasOsmRelationRefs()) {
                        <kpn-icon-relation />
                        <kpn-action-button-relation [relationId]="ref.id" />
                        <kpn-osm-link-relation [relationId]="ref.id" [title]="ref.name" />
                      }
                    </div>
                  }
                </div>
              </ng-template>
            </mat-expansion-panel>
          }
        </mat-accordion>
      }
    }
  `,
  styleUrl: '../subset-fact-details-page.component.scss',
  imports: [
    ActionButtonNetworkComponent,
    ActionButtonNodeComponent,
    ActionButtonNodeComponent,
    ActionButtonRelationComponent,
    ActionButtonRouteComponent,
    ActionButtonWayComponent,
    IconNetworkComponent,
    IconNodeComponent,
    IconRelationComponent,
    IconRouteComponent,
    IconWayComponent,
    LinkNodeComponent,
    LinkRouteComponent,
    MatAccordion,
    MatDivider,
    MatExpansionPanel,
    MatExpansionPanelContent,
    MatExpansionPanelHeader,
    OsmLinkNodeComponent,
    OsmLinkRelationComponent,
    OsmLinkWayComponent,
    RouterLink,
    ExpandCollapseComponent,
  ],
})
export class SubsetFactDetailsComponent {
  page = input.required<SubsetFactDetailsPage>();

  protected readonly fact = computed(() => Facts.facts.get(this.page().fact));
  protected readonly accordion = viewChild(MatAccordion);
}
