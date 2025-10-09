import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { Ref } from '@api/common/common/ref';
import { SubsetFactDetailsPage } from '@api/common/subset/subset-fact-details-page';
import { FactDefinition } from '@app/analysis/fact/components/facts';
import { IconNodeComponent } from '@app/shared/components/icon/icon-node.component';
import { IconRelationComponent } from '@app/shared/components/icon/icon-relation.component';
import { IconRouteComponent } from '@app/shared/components/icon/icon-route.component';
import { IconWayComponent } from '@app/shared/components/icon/icon-way.component';
import { LinkNodeComponent } from '@app/shared/components/link/link-node.component';
import { LinkRouteComponent } from '@app/shared/components/link/link-route.component';
import { OsmLinkNodeComponent } from '@app/shared/components/link/osm-link-node.component';
import { OsmLinkRelationComponent } from '@app/shared/components/link/osm-link-relation.component';
import { OsmLinkWayComponent } from '@app/shared/components/link/osm-link-way.component';
import { ActionButtonNodeComponent } from '../../../../components/action/action-button-node.component';
import { ActionButtonRelationComponent } from '../../../../components/action/action-button-relation.component';
import { ActionButtonRouteComponent } from '../../../../components/action/action-button-route.component';
import { ActionButtonWayComponent } from '../../../../components/action/action-button-way.component';

@Component({
  selector: 'ui-subset-fact-detail-panel-fact',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @let ref = factRef();
    <div class="kpn-line">
      @if (fact().hasNodeRefs()) {
        <ui-icon-node />
        <ui-action-button-node [nodeId]="ref.id" />
        <ui-link-node [nodeId]="ref.id" [nodeName]="ref.name" />
      }
      @if (fact().hasRouteRefs()) {
        <ui-icon-route />
        <ui-action-button-route [routeType]="page().subsetInfo.routeType" [relationId]="ref.id" />
        <ui-link-route
          [routeId]="ref.id"
          [routeName]="ref.name"
          [routeType]="page().subsetInfo.routeType"
        />
      }
      @if (fact().hasOsmNodeRefs()) {
        <ui-icon-node />
        <ui-action-button-node [nodeId]="ref.id" />
        <ui-osm-link-node [nodeId]="ref.id" [title]="ref.name" />
      }
      @if (fact().hasOsmWayRefs()) {
        <ui-icon-way />
        <ui-action-button-way [wayId]="ref.id" />
        <ui-osm-link-way [wayId]="ref.id" [title]="ref.name" />
      }
      @if (fact().hasOsmRelationRefs()) {
        <ui-icon-relation />
        <ui-action-button-relation [relationId]="ref.id" />
        <ui-osm-link-relation [relationId]="ref.id" [title]="ref.name" />
      }
    </div>
  `,
  styleUrl: '../subset-fact-details-page.component.scss',
  imports: [
    ActionButtonNodeComponent,
    ActionButtonRelationComponent,
    ActionButtonRouteComponent,
    ActionButtonWayComponent,
    IconNodeComponent,
    IconRelationComponent,
    IconRouteComponent,
    IconWayComponent,
    LinkNodeComponent,
    LinkRouteComponent,
    OsmLinkNodeComponent,
    OsmLinkRelationComponent,
    OsmLinkWayComponent,
  ],
})
export class SubsetFactDetailPanelFactComponent {
  readonly page = input.required<SubsetFactDetailsPage>();
  readonly fact = input.required<FactDefinition>();
  readonly factRef = input.required<Ref>();
}
