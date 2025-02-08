import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { LocationFact } from '@api/common/location/location-fact';
import { Fact } from '@api/common/fact';
import { FactLevel } from '@api/common/fact-level';
import { Facts } from '@app/analysis/fact/components/facts';
import { FactLevelComponent } from '@app/analysis/fact/components/fact-level.component';
import { FactNameComponent } from '@app/analysis/fact/components/fact-name.component';
import { BracketsComponent } from '@app/shared/components/link/brackets.component';
import { ActionButtonLocationFactNodesComponent } from '../../../../components/action/action-button-location-fact-nodes.component';
import { ActionButtonLocationFactRoutesComponent } from '../../../../components/action/action-button-location-fact-routes.component';

@Component({
  selector: 'kpn-location-fact-panel-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (locationFact(); as locationFact) {
      <div class="kpn-line">
        @if (locationFact.elementType === 'node') {
          <kpn-action-button-location-fact-nodes [locationFact]="locationFact" />
        } @else if (locationFact.elementType === 'route') {
          <kpn-action-button-location-fact-routes [locationFact]="locationFact" />
        }
        <kpn-fact-name [fact]="locationFact.fact" />
        <kpn-brackets>{{ locationFact.refs.length }}</kpn-brackets>
        <kpn-fact-level [factLevel]="factLevel(locationFact.fact)" class="level" />
      </div>
    }
  `,
  imports: [
    ActionButtonLocationFactNodesComponent,
    ActionButtonLocationFactRoutesComponent,
    BracketsComponent,
    FactLevelComponent,
    FactNameComponent,
  ],
})
export class LocationFactPanelHeaderComponent {
  locationFact = input.required<LocationFact>();

  factLevel(fact: Fact): FactLevel {
    return Facts.factLevel(fact);
  }
}
