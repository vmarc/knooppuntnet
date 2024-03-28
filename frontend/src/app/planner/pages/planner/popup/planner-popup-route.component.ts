import { signal } from '@angular/core';
import { effect } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MapRouteDetail } from '@api/common/route';
import { ApiResponse } from '@api/custom';
import { LinkRouteComponent } from '@app/components/shared/link';
import { ApiService } from '@app/services';
import { Coordinate } from 'ol/coordinate';
import { PlannerPopupService } from '../../../domain/context/planner-popup-service';
import { PlannerStateService } from '../planner-state.service';
import { PlannerService } from '../planner.service';

@Component({
  selector: 'kpn-planner-popup-route',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (response(); as response) {
      <div>
        @if (!!response.result) {
          <h2>
            <span i18n="@@map.route-popup.title">Route</span>
            {{ response.result.name }}
          </h2>
          <div>
            @if (response.result.networkReferences.length === 1) {
              <span class="kpn-label" i18n="@@map.route-popup.network">Network</span>
            }
            @if (response.result.networkReferences.length !== 1) {
              <span class="kpn-label" i18n="@@map.route-popup.networks">Networks</span>
            }
            @if (response.result.networkReferences.length === 0) {
              <span i18n="@@map.route-popup.no-networks">None</span>
            }
            @for (ref of response.result.networkReferences; track ref) {
              <div class="reference">
                <a [routerLink]="'/analysis/network/' + ref.id">{{ ref.name }}</a>
              </div>
            }
          </div>
          <p class="more-details">
            <kpn-link-route
              [routeId]="response.result.id"
              [routeName]="response.result.name"
              [networkType]="networkType()"
              title="More details"
              i18n-title="@@map.route-popup.more-details"
            />
          </p>
        }
      </div>
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
  imports: [RouterLink, LinkRouteComponent],
})
export class PlannerPopupRouteComponent {
  private readonly service = inject(PlannerPopupService);
  private readonly plannerStateService = inject(PlannerStateService);
  private readonly apiService = inject(ApiService);
  private readonly plannerService = inject(PlannerService);

  protected response = signal<ApiResponse<MapRouteDetail>>(null);
  protected networkType = this.plannerStateService.networkType;

  constructor() {
    effect(() => {
      const routeClick = this.service.routeClick();
      if (routeClick !== null) {
        this.apiService.mapRouteDetail(routeClick.route.routeId).subscribe((response) => {
          if (response.result) {
            this.response.set(response);
            this.openPopup(routeClick.coordinate);
          }
        });
      }
    });
  }

  private openPopup(coordinate: Coordinate): void {
    setTimeout(() => this.plannerService.context.plannerPopup.setPosition(coordinate, -12), 0);
  }
}
