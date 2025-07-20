import { NgTemplateOutlet } from '@angular/common';
import { inject } from '@angular/core';
import { computed } from '@angular/core';
import { output } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MonitorGroupsPageGroup } from '@api/common/monitor/monitor-groups-page-group';
import { MonitorGroupActionsComponent } from '@app/monitor/internal/groups/monitor-group-actions.component';
import { ListItemComponent } from '@app/shared/components/list/list-item.component';
import { ListComponent } from '@app/shared/components/list/list.component';
import { State } from '@app/state/state';

const COMPACT_WIDTH_THRESHOLD = 500;

@Component({
  selector: 'ui-monitor-group-list',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-list [filter]="false">
      @for (group of groups(); track group.id) {
        <ui-list-item [clickable]="true" (click)="selectGroup(group)">
          <div class="group-item">
            <span class="group-name">
              <a [routerLink]="groupLink(group)" [state]="group">{{ group.name }}</a>
            </span>
            @if (!compact()) {
              <span>
                <ng-container *ngTemplateOutlet="text" />
              </span>
            }
            @if (admin()) {
              <div class="group-actions">
                <ui-monitor-group-actions [group]="group" />
              </div>
            }
          </div>
          @if (compact()) {
            <ng-container *ngTemplateOutlet="text" />
          }
          <ng-template #text>
            <span>{{ group.description }}</span>
            <span class="kpn-brackets route-count">
              {{ group.routeCount }}
              <span>routes</span>
            </span>
          </ng-template>
        </ui-list-item>
      }
    </ui-list>
  `,
  styles: `
    .group-item {
      display: flex;
      gap: 1em;
    }

    .group-actions {
      margin-left: auto;
      white-space: nowrap;
      padding-left: 1em;
    }

    .group-name {
      min-width: 8em;
    }

    .route-count {
      white-space: nowrap;
    }
  `,
  imports: [
    ListComponent,
    ListItemComponent,
    MonitorGroupActionsComponent,
    NgTemplateOutlet,
    RouterLink,
  ],
})
export class MonitorGroupListComponent {
  private readonly state = inject(State);

  readonly admin = input.required<boolean>();
  readonly groups = input.required<MonitorGroupsPageGroup[]>();
  readonly groupSelectChange = output<MonitorGroupsPageGroup>();
  readonly compact = computed(() => this.state.splitState.left() < COMPACT_WIDTH_THRESHOLD);

  groupLink(group: MonitorGroupsPageGroup): string {
    return `/monitor/groups/${group.name}`;
  }

  selectGroup(group: MonitorGroupsPageGroup) {
    this.groupSelectChange.emit(group);
  }
}
