import { computed } from '@angular/core';
import { Signal } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouteScope } from '@api/common/route-scope';
import { RouteType } from '@api/common/route-type';
import { LocationNodeInfo } from '@api/common/location/location-node-info';
import { FactsLineComponent } from '@app/analysis/fact/components/facts-line.component';
import { ExpectedRouteCountComponent } from '@app/shared/components/indicator/expected-route-count.component';
import { IntegrityData } from '@app/shared/components/indicator/integrity-data';

@Component({
  selector: 'ui-location-node-analysis',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-facts-line [facts]="node().facts" />
    @if (expectedRouteCount()) {
      <ui-expected-route-count [data]="integrityData()" />
    }
  `,
  imports: [ExpectedRouteCountComponent, FactsLineComponent],
})
export class LocationNodeAnalysisComponent {
  readonly routeType = input.required<RouteType>();
  readonly routeScope = input.required<RouteScope>();
  readonly node = input.required<LocationNodeInfo>();

  readonly expectedRouteCount = computed(() => this.node().expectedRouteCount);

  readonly integrityData: Signal<IntegrityData> = computed(() => {
    return {
      routeType: this.routeType(),
      routeScope: this.routeScope(),
      actual: this.node().routeReferences.length,
      expected: this.node().expectedRouteCount,
    };
  });
}
