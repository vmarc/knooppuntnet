import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { LinkRouteComponent } from '@app/components/shared/link';
import { PlannerPopupService } from '../../../domain/context/planner-popup-service';
import { PlannerStateService } from '../planner-state.service';

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
            @for (ref of response.result.networkReferences; track ref) {
              <div class="reference">
                <a [routerLink]="'/analysis/network/' + ref.id">{{ ref.name }}</a>
              </div>
            } @empty {
              <span i18n="@@map.route-popup.no-networks">None</span>
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
  protected readonly response = this.service.routeDetailResponse;
  protected readonly networkType = this.plannerStateService.networkType;
}
