import { computed } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { NetworkFact } from '@api/common/network-fact';
import { RouteType } from '@api/common/route-type';
import { FactDescriptionComponent } from '@app/analysis/fact/components/fact-description.component';
import { NetworkFactElementsComponent } from '@app/analysis/network/internal/facts/components/network-fact-elements.component';
import { DividerComponent } from '@app/shared/components/divider.component';
import { NetworkFactChecksComponent } from './network-fact-checks.component';

@Component({
  selector: 'ui-network-fact-contents',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="description">
      <ui-fact-description [fact]="fact()" />
    </div>
    <ui-divider />
    <div class="sideline">
      <ui-network-fact-elements [routeType]="routeType()" [networkFact]="networkFact()" />
      @if (hasChecks()) {
        <ui-network-fact-checks [checks]="networkFact().checks" />
      }
    </div>
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
    DividerComponent,
    FactDescriptionComponent,
    NetworkFactChecksComponent,
    NetworkFactElementsComponent,
  ],
})
export class NetworkFactContentsComponent {
  readonly routeType = input.required<RouteType>();
  readonly networkFact = input.required<NetworkFact>();
  protected readonly fact = computed(() => this.networkFact().fact);
  protected readonly hasChecks = computed(
    () => this.networkFact().checks && this.networkFact().checks.length > 0
  );
}
