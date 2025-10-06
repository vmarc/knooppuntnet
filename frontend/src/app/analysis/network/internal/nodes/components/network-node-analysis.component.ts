import { Signal } from '@angular/core';
import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouteScope } from '@api/common/route-scope';
import { RouteType } from '@api/common/route-type';
import { NetworkNodeRow } from '@api/common/network/network-node-row';
import { ExpectedRouteCountComponent } from '@app/shared/components/indicator/expected-route-count.component';
import { IntegrityData } from '@app/shared/components/indicator/integrity-data';
import { ConnectionNodeIncludedInNetworkRelationComponent } from './indicators/connection-node-included-in-network-relation.component';
import { NotIncludedInNetworkRelationComponent } from './indicators/not-included-in-network-relation.component';
import { NodeConnectionIndicatorComponent } from './indicators/node-connection-indicator.component';
import { ProposedIndicatorComponent } from './indicators/proposed-indicator.component';
import { RoleConnectionIndicatorComponent } from './indicators/role-connection-indicator.component';

@Component({
  selector: 'ui-network-node-analysis',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- eslint-disable @angular-eslint/template/cyclomatic-complexity -->
    @if (analysis()) {
      <div>
        @if (proposed()) {
          <ui-proposed-indicator />
        }

        @if (connection()) {
          <ui-node-connection-indicator />
        }

        @if (roleConnection()) {
          <ui-role-connection-indicator />
        }

        @if (notIncludedInNetworkRelation()) {
          <ui-not-included-in-network-relation />
        }

        @if (connectionNodeIncludedInNetworkRelation()) {
          <ui-connection-node-included-in-network-relation />
        }

        @if (expectedRouteCount()) {
          <ui-expected-route-count [data]="integrityData()" />
        }
      </div>
    }
  `,
  imports: [
    ConnectionNodeIncludedInNetworkRelationComponent,
    ExpectedRouteCountComponent,
    NodeConnectionIndicatorComponent,
    NotIncludedInNetworkRelationComponent,
    ProposedIndicatorComponent,
    RoleConnectionIndicatorComponent,
  ],
})
export class NetworkNodeAnalysisComponent {
  readonly routeType = input.required<RouteType>();
  readonly routeScope = input.required<RouteScope>();
  readonly node = input.required<NetworkNodeRow>();

  readonly notIncludedInNetworkRelation = computed(
    () => !this.node().detail.definedInRelation && !this.node().detail.connection
  );

  readonly connectionNodeIncludedInNetworkRelation = computed(
    () =>
      this.node().detail.definedInRelation &&
      this.node().detail.connection &&
      !this.node().detail.roleConnection
  );

  readonly connection = computed(() => this.node().detail.connection);
  readonly roleConnection = computed(() => this.node().detail.roleConnection);
  readonly proposed = computed(() => this.node().detail.proposed);
  readonly expectedRouteCount = computed(() => this.node().detail.expectedRouteCount);
  readonly analysis = computed(
    () =>
      this.notIncludedInNetworkRelation() ||
      this.connectionNodeIncludedInNetworkRelation() ||
      this.connection() ||
      this.roleConnection() ||
      this.proposed() ||
      this.expectedRouteCount()
  );
  readonly integrityData: Signal<IntegrityData> = computed(() => {
    return {
      routeType: this.routeType(),
      routeScope: this.routeScope(),
      actual: this.node().routeReferences.length,
      expected: this.node().detail.expectedRouteCount,
    };
  });
}
