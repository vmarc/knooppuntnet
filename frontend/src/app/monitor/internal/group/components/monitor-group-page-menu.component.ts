import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { ErrorComponent } from '@app/shared/components/error/error.component';
import { PageMenuOptionComponent } from '@app/shared/components/menu/page-menu-option.component';
import { PageMenuComponent } from '@app/shared/components/menu/page-menu.component';

@Component({
  selector: 'ui-monitor-group-page-menu',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page-menu>
      <ui-page-menu-option
        [link]="'/monitor/groups/' + groupName()"
        [active]="pageName() === 'routes'"
        i18n="@@monitor.group.menu.routes"
      >
        Routes
      </ui-page-menu-option>
    </ui-page-menu>
    <ui-error />
  `,
  imports: [PageMenuComponent, PageMenuOptionComponent, ErrorComponent],
})
export class MonitorGroupPageMenuComponent {
  readonly pageName = input.required<string>();
  readonly groupName = input.required<string>();
}
