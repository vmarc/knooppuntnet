import { viewChild } from '@angular/core';
import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatButton } from '@angular/material/button';
import { MatDivider } from '@angular/material/divider';
import { MatAccordion } from '@angular/material/expansion';
import { MatExpansionPanelHeader } from '@angular/material/expansion';
import { MatExpansionPanelContent } from '@angular/material/expansion';
import { MatExpansionPanel } from '@angular/material/expansion';
import { RouterLink } from '@angular/router';
import { SubsetFactDetailsPage } from '@api/common/subset';
import { EditParameters } from '@app/analysis/components/edit';
import { Facts } from '@app/analysis/fact';
import { EditService } from '@app/components/shared';
import { IconNetworkComponent } from '@app/components/shared/icon';
import { IconNodeComponent } from '@app/components/shared/icon';
import { IconRelationComponent } from '@app/components/shared/icon';
import { IconRouteComponent } from '@app/components/shared/icon';
import { IconWayComponent } from '@app/components/shared/icon';
import { LinkNodeComponent } from '@app/components/shared/link';
import { LinkRouteComponent } from '@app/components/shared/link';
import { OsmLinkNodeComponent } from '@app/components/shared/link';
import { OsmLinkRelationComponent } from '@app/components/shared/link';
import { OsmLinkWayComponent } from '@app/components/shared/link';
import { ActionButtonNetworkComponent } from '../../components/action/action-button-network.component';
import { ActionButtonNodeComponent } from '../../components/action/action-button-node.component';
import { ActionButtonRelationComponent } from '../../components/action/action-button-relation.component';
import { ActionButtonRouteComponent } from '../../components/action/action-button-route.component';
import { ActionButtonWayComponent } from '../../components/action/action-button-way.component';

@Component({
  selector: 'kpn-subset-fact-details',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (page().networks.length > 0) {
      <div class=" kpn-button-group kpn-spacer-above kpn-spacer-below">
        <button
          mat-stroked-button
          class="location-button"
          (click)="expandAll()"
          i18n="@@location.tree.expand-all"
        >
          Expand all
        </button>
        <button
          mat-stroked-button
          class="location-button"
          (click)="collapseAll()"
          i18n="@@location.tree.collapse-all"
        >
          Collapse all
        </button>

      </div>

      @if (fact(); as fact) {
        <mat-accordion class="example-headers-align" multi>
          @for (networkFactRefs of page().networks; track networkFactRefs) {
            <mat-expansion-panel togglePosition="before">
              <mat-expansion-panel-header>
                <div class="kpn-align-center">
                  <kpn-icon-network />
                  @if (networkFactRefs.networkId === 0) {
                    <!-- TODO add button to load all routes -->
                    <span i18n="@@subset-facts.orphan-routes" class="free-route-indent">Free routes</span>
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
                      <span i18n="@@subset-facts.nodes">
                        {networkFactRefs.factRefs.length, plural, one {1 node} other {{{ networkFactRefs.factRefs.length }} nodes}}
                      </span>
                    }
                    @if (fact.hasRouteRefs()) {
                      <span i18n="@@subset-facts.routes">
                        {networkFactRefs.factRefs.length, plural, one {1 route} other
                          {{{ networkFactRefs.factRefs.length }} routes}}
                      </span>
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
                        <kpn-link-node
                          [nodeId]="ref.id"
                          [nodeName]="ref.name"
                        />
                      }
                      @if (fact.hasRouteRefs()) {
                        <kpn-icon-route />
                        <kpn-action-button-route [relationId]="ref.id" />
                        <kpn-link-route
                          [routeId]="ref.id"
                          [routeName]="ref.name"
                          [networkType]="page().subsetInfo.networkType"
                        />
                      }
                      @if (fact.hasOsmNodeRefs()) {
                        <kpn-icon-node />
                        <kpn-action-button-node [nodeId]="ref.id" />
                        <kpn-osm-link-node
                          [nodeId]="ref.id"
                          [title]="ref.name"
                        />
                      }
                      @if (fact.hasOsmWayRefs()) {
                        <kpn-icon-way />
                        <kpn-action-button-way [wayId]="ref.id" />
                        <kpn-osm-link-way
                          [wayId]="ref.id"
                          [title]="ref.name"
                        />
                      }
                      @if (fact.hasOsmRelationRefs()) {
                        <kpn-icon-relation />
                        <kpn-action-button-relation [relationId]="ref.id" />
                        <kpn-osm-link-relation
                          [relationId]="ref.id"
                          [title]="ref.name"
                        />
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
  styleUrl: './_subset-fact-details-page.component.scss',
  standalone: true,
  imports: [
    IconNetworkComponent,
    IconNodeComponent,
    IconRelationComponent,
    IconRouteComponent,
    IconWayComponent,
    LinkNodeComponent,
    LinkRouteComponent,
    MatAccordion,
    MatButton,
    MatDivider,
    MatExpansionPanel,
    MatExpansionPanelContent,
    MatExpansionPanelHeader,
    OsmLinkNodeComponent,
    OsmLinkRelationComponent,
    OsmLinkWayComponent,
    RouterLink,
    ActionButtonNodeComponent,
    ActionButtonNetworkComponent,
    ActionButtonRouteComponent,
    ActionButtonNodeComponent,
    ActionButtonWayComponent,
    ActionButtonRelationComponent,
  ],
})
export class SubsetFactDetailsComponent {
  private readonly editService = inject(EditService);

  page = input.required<SubsetFactDetailsPage>();
  protected readonly fact = computed(() => Facts.facts.get(this.page().fact));

  private readonly accordion = viewChild(MatAccordion);

  edit(): void {
    const elementIds = this.page()
      .networks.flatMap((n) => n.factRefs)
      .map((ref) => ref.id);

    let editParameters: EditParameters = null;

    if (this.fact().hasNodeRefs() || this.fact().hasOsmNodeRefs()) {
      editParameters = {
        nodeIds: elementIds,
      };
    } else if (this.fact().hasOsmWayRefs()) {
      editParameters = {
        wayIds: elementIds,
      };
    } else if (this.fact().hasOsmRelationRefs() || this.fact().hasRouteRefs()) {
      editParameters = {
        relationIds: elementIds,
        fullRelation: true,
      };
    }
    this.editService.edit(editParameters);
  }

  expandAll(): void {
    this.accordion().openAll();
  }

  collapseAll(): void {
    this.accordion().closeAll();
  }
}
