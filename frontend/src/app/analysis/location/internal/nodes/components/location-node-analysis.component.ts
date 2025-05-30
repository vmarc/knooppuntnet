import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouteScope } from '@api/common/route-scope';
import { RouteType } from '@api/common/route-type';
import { LocationNodeInfo } from '@api/common/location/location-node-info';
import { IntegrityIndicatorData } from '@app/shared/components/indicator/integrity-indicator-data';
import { IntegrityIndicatorComponent } from '@app/shared/components/indicator/integrity-indicator.component';
import { LocationNodeFactIndicatorComponent } from './location-node-fact-indicator.component';

@Component({
  selector: 'ui-location-node-analysis',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-location-node-fact-indicator [node]="node()" />
    <ui-integrity-indicator [data]="integrityIndicatorData" />
  `,
  imports: [LocationNodeFactIndicatorComponent, IntegrityIndicatorComponent],
})
export class LocationNodeAnalysisComponent implements OnInit {
  routeType = input.required<RouteType>();
  routeScope = input.required<RouteScope>();
  node = input.required<LocationNodeInfo>();

  integrityIndicatorData: IntegrityIndicatorData;

  ngOnInit(): void {
    this.integrityIndicatorData = new IntegrityIndicatorData(
      this.routeType(),
      this.routeScope(),
      this.node().routeReferences.length,
      this.node().expectedRouteCount
    );
  }
}
