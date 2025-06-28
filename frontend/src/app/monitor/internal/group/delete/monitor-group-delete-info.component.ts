import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MonitorGroupPage } from '@api/common/monitor/monitor-group-page';
import { IconWarningComponent } from '@app/shared/components/icon/icon-warning.component';
import { NavService } from '@app/shared/components/nav.service';
import { MonitorGroupDeletePageService } from './monitor-group-delete-page.service';

@Component({
  selector: 'ui-monitor-group-delete-info',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-form">
      <p>
        <span class="kpn-label" i18n="@@monitor.group.delete.name">Name</span>
        {{ page().groupName }}
      </p>

      <p>
        <span class="kpn-label" i18n="@@monitor.group.delete.description">Description</span>
        {{ page().groupDescription }}
      </p>

      @if (page().routes.length; as routeCount) {
        @if (routeCount > 0) {
          <div class="kpn-line">
            <ui-icon-warning />
            <span i18n="@@monitor.group.delete.warning">
              The information of all routes ({{ routeCount }} route(s)) in the group will also be
              deleted!
            </span>
          </div>
        }
      }
    </div>
  `,
  providers: [MonitorGroupDeletePageService, NavService],
  imports: [IconWarningComponent],
})
export class MonitorGroupDeleteInfoComponent {
  readonly page = input.required<MonitorGroupPage>();
}
