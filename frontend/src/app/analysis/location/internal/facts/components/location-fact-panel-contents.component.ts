import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { LocationFact } from '@api/common/location';
import { FactInfo } from '@app/analysis/fact';
import { FactDescriptionComponent } from '@app/analysis/fact';
import { DividerComponent } from '@app/shared/components/divider.component';
import { IconNodeComponent } from '@app/shared/components/icon/icon-node.component';
import { IconRouteComponent } from '@app/shared/components/icon/icon-route.component';
import { LinkNodeComponent } from '@app/shared/components/link/link-node.component';
import { LinkRouteComponent } from '@app/shared/components/link/link-route.component';
import { ActionButtonNodeComponent } from '../../../../components/action/action-button-node.component';
import { ActionButtonRouteComponent } from '../../../../components/action/action-button-route.component';

@Component({
  selector: 'kpn-location-fact-panel-contents',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (locationFact(); as locationFact) {
      <div class="description">
        <kpn-fact-description [factInfo]="factInfo(locationFact)" />
      </div>
      <kpn-divider />
      <div class="sideline">
        @if (locationFact.elementType === 'route') {
          @for (ref of locationFact.refs; track ref) {
            <div class="kpn-align-center">
              <kpn-icon-route />
              <kpn-action-button-route [relationId]="ref.id" />
              <kpn-link-route [routeId]="ref.id" [routeName]="ref.name" />
            </div>
          }
        }
        @if (locationFact.elementType === 'node') {
          @for (ref of locationFact.refs; track ref) {
            <div class="kpn-align-center">
              <kpn-icon-node />
              <kpn-action-button-node [nodeId]="ref.id" />
              <kpn-link-node [nodeId]="ref.id" [nodeName]="ref.name" />
            </div>
          }
        }
      </div>
    }
  `,
  styles: `
    .description {
      max-width: 60em;
    }

    .sideline {
      margin-top: 1em;
      margin-bottom: 1em;
      margin-left: 0.8em;
      padding-left: 0.8em;
      border-left: 1px solid lightgray;
    }
  `,
  imports: [
    ActionButtonNodeComponent,
    ActionButtonRouteComponent,
    DividerComponent,
    FactDescriptionComponent,
    IconNodeComponent,
    IconRouteComponent,
    LinkNodeComponent,
    LinkRouteComponent,
  ],
})
export class LocationFactPanelContentsComponent {
  locationFact = input.required<LocationFact>();

  factInfo(locationFact: LocationFact): FactInfo {
    return new FactInfo(locationFact.fact);
  }
}
