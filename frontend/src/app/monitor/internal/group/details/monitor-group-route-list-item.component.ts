import { NgTemplateOutlet } from '@angular/common';
import { inject } from '@angular/core';
import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MonitorRouteDetail } from '@api/common/monitor/monitor-route-detail';
import { MonitorGroupRouteActionsComponent } from '@app/monitor/internal/group/details/monitor-group-route-actions.component';
import { DistancePipe } from '@app/shared/components/format/distance.pipe';
import { TimestampDayPipe } from '@app/shared/components/format/timestamp-day.pipe';
import { TimestampPipe } from '@app/shared/components/format/timestamp-pipe';
import { IconHappyComponent } from '@app/shared/components/icon/icon-happy.component';
import { State } from '@app/state/state';
import { SymbolComponent } from '@app/symbol/symbol.component';
import { ActionButtonRelationComponent } from '@app/analysis/components/action/action-button-relation.component';
import { NzTableModule } from 'ng-zorro-antd/table';
import { NzTooltipDirective } from 'ng-zorro-antd/tooltip';

const COMPACT_WIDTH_THRESHOLD = 500;

@Component({
  selector: 'ui-monitor-group-route-list-item',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- eslint-disable @angular-eslint/template/cyclomatic-complexity -->
    @let route = monitorRouteDetail();
    <div class="kpn-separated route-item">
      <span class="kpn-dot">{{ route.rowIndex + 1 }}</span>
      <ui-action-button-relation [relationId]="route.relationId" />
      <a [routerLink]="routeLink()" [state]="route">{{ route.name }}</a>
      @if (!compact()) {
        <span>
          <ng-container *ngTemplateOutlet="text" />
        </span>
      }
      @if (admin()) {
        <span class="route-actions">
          <ui-monitor-group-route-actions [groupName]="groupName()" [route]="route" />
        </span>
      }
    </div>
    @if (compact()) {
      <ng-container *ngTemplateOutlet="text" />
    }
    <div class="reference kpn-line">
      <span class="reference-type">
        {{ route.referenceType }}
      </span>

      <span
        nz-tooltip
        nzTooltipPlacement="right"
        [nzTooltipTitle]="route.referenceTimestamp | yyyymmddhhmm"
        class="kpn-nowrap"
      >
        {{ route.referenceTimestamp | yyyymmdd }}
      </span>
      @if (route.referenceType) {
        <span class="kpn-nowrap">
          {{ route.referenceDistance | distance }}
        </span>
      }
    </div>
    @if (route.referenceType && route.deviationCount > 0) {
      <div class="kpn-line">
        <a [routerLink]="deviationsLink()" [state]="route" class="kpn-space-separated">
          <span>{{ route.deviationCount }}</span>
          @if (route.deviationCount > 1) {
            <span>deviations</span>
          } @else {
            <span>deviation</span>
          }
        </a>
        <span class="kpn-nowrap">{{ route.deviationDistance | distance }}</span>
      </div>
    }
    @if (route.osmSegmentCount > 1) {
      <a [routerLink]="segmentsLink()" [state]="route" class="kpn-space-separated">
        <span>{{ route.osmSegmentCount }}</span>
        <span>segments</span>
      </a>
    }

    <ng-template #text>
      <span class="kpn-space-separated">
        @if (route.symbol) {
          <span class="symbol">
            <ui-symbol [description]="route.symbol" [width]="25" [height]="25" />
          </span>
        }
        <span>{{ route.description }}</span>

        @if (route.happy) {
          <ui-icon-happy />
        }
      </span>
    </ng-template>
  `,
  styles: `
    .route-item {
      display: flex;
    }

    .route-actions {
      margin-left: auto;
      white-space: nowrap;
      padding-left: 1em;
    }

    .symbol {
      width: 25px;
      height: 25px;
      margin-right: 2em;
    }

    .reference {
      height: 4em;
    }

    .reference-type {
      font-style: italic;
      margin-right: 1em;
      padding: 0.3em 0.8em 0.3em 0.5em;
      border-radius: 0.5em;
      border: 1px solid lightgray;
    }
  `,
  imports: [
    ActionButtonRelationComponent,
    DistancePipe,
    IconHappyComponent,
    MonitorGroupRouteActionsComponent,
    NgTemplateOutlet,
    NzTableModule,
    NzTooltipDirective,
    RouterLink,
    SymbolComponent,
    TimestampDayPipe,
    TimestampPipe,
  ],
})
export class MonitorGroupRouteListItemComponent {
  private readonly state = inject(State);

  readonly admin = input.required<boolean>();
  readonly groupName = input.required<string>();
  readonly monitorRouteDetail = input.required<MonitorRouteDetail>();
  readonly compact = computed(() => this.state.splitState.left() < COMPACT_WIDTH_THRESHOLD);

  protected readonly routeLink = computed(() => {
    const prefix = this.admin() ? 'admin/' : '';
    return `/monitor/${prefix}groups/${this.groupName()}/routes/${this.monitorRouteDetail().name}`;
  });

  protected readonly deviationsLink = computed(() => this.routeLink() + '/deviations');
  protected readonly segmentsLink = computed(() => this.routeLink() + '/segments');
}
