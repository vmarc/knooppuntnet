import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { LinkRouteComponent } from '@app/components/shared/link';
import { TimestampComponent } from '@app/components/shared/timestamp';
import { PlannerPopupService } from '../../../domain/context/planner-popup-service';

@Component({
  selector: 'kpn-planner-popup-node',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (response(); as response) {
      @if (response.result) {
        <h2>
          <ng-container i18n="@@map.node-popup.title">Node</ng-container>
          {{ response.result.name }}
        </h2>
        <p>
          <span class="kpn-label" i18n="@@map.node-popup.last-updated">Last updated</span>
          <kpn-timestamp [timestamp]="response.result.lastUpdated" />
        </p>
        <div>
          @if (response.result.networkReferences.length === 1) {
            <span class="kpn-label" i18n="@@map.node-popup.network">Network</span>
          }
          @if (response.result.networkReferences.length !== 1) {
            <span class="kpn-label" i18n="@@map.node-popup.networks">Networks</span>
          }
          @for (ref of response.result.networkReferences; track ref) {
            <div class="reference">
              <a [routerLink]="'/analysis/network/' + ref.id">{{ ref.name }}</a>
            </div>
          } @empty {
            <span i18n="@@map.node-popup.no-networks">None</span>
          }
        </div>
        @if (response.result.routeReferences.length > 0) {
          <div>
            <span class="kpn-label" i18n="@@map.node-popup.routes">Routes</span>
            @for (ref of response.result.routeReferences; track ref) {
              <div class="reference">
                <kpn-link-route
                  [routeId]="ref.id"
                  [routeName]="ref.name"
                  [networkType]="ref.networkType"
                />
              </div>
            } @empty {
              <span i18n="@@map.node-popup.routes.none">None</span>
            }
          </div>
        }
        <div class="more-details">
          <a
            [routerLink]="'/analysis/node/' + response.result.id"
            i18n="@@map.node-popup.more-details"
          >
            More details
          </a>
        </div>
      }
    }
  `,
  styles: `
    .reference {
      margin: 0.5em 0 0.5em 1em;
    }

    .more-details {
      margin-top: 2em;
    }
  `,
  standalone: true,
  imports: [LinkRouteComponent, RouterLink, TimestampComponent],
})
export class PlannerPopupNodeComponent {
  private readonly service = inject(PlannerPopupService);
  protected readonly response = this.service.nodeDetailResponse;
}
