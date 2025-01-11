import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouteScope } from '@api/common';
import { RouteType } from '@api/common';
import { LocationNodeInfo } from '@api/common/location';
import { IntegrityIndicatorData } from '@app/components/shared/indicator';
import { IntegrityIndicatorComponent } from '@app/components/shared/indicator';
import { LocationNodeFactIndicatorComponent } from './location-node-fact-indicator.component';

@Component({
  selector: 'kpn-location-node-analysis',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="analysis">
      <kpn-location-node-fact-indicator [node]="node()" />
      <kpn-integrity-indicator [data]="integrityIndicatorData" />
    </div>
  `,
  styles: `
    .analysis {
      display: flex;
    }
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
