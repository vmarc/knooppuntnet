import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatDivider } from '@angular/material/divider';
import { LocationFact } from '@api/common/location';
import { FactInfo } from '@app/analysis/fact';
import { FactDescriptionComponent } from '@app/analysis/fact';
import { IconRouteComponent } from '@app/components/shared/icon';
import { IconNodeComponent } from '@app/components/shared/icon';
import { LinkNodeComponent } from '@app/components/shared/link';
import { LinkRouteComponent } from '@app/components/shared/link';
import { ActionButtonNodeComponent } from '../../../components/action/action-button-node.component';
import { ActionButtonRouteComponent } from '../../../components/action/action-button-route.component';

@Component({
  selector: 'kpn-location-fact-panel-contents',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (locationFact(); as locationFact) {
      <div class="description">
        <kpn-fact-description [factInfo]="factInfo(locationFact)" />
      </div>
      <div class="kpn-small-spacer-above kpn-small-spacer-below">
        <mat-divider />
      </div>
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
  standalone: true,
  imports: [
    ActionButtonNodeComponent,
    ActionButtonRouteComponent,
    FactDescriptionComponent,
    IconNodeComponent,
    IconRouteComponent,
    LinkNodeComponent,
    LinkRouteComponent,
    MatDivider,
  ],
})
export class LocationFactPanelContentsComponent {
  locationFact = input.required<LocationFact>();

  factInfo(locationFact: LocationFact): FactInfo {
    return new FactInfo(locationFact.fact);
  }
}
