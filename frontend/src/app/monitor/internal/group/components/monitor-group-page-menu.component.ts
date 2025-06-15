import { computed } from '@angular/core';
import { Signal } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { ErrorComponent } from '@app/shared/components/error/error.component';
import { MenuOption } from '@app/shared/components/menu/menu-option';
import { PageMenuComponent } from '@app/shared/components/menu/page-menu.component';

@Component({
  selector: 'ui-monitor-group-page-menu',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page-menu [pageName]="pageName()" [options]="menuOptions()" />
    <ui-error />
  `,
  imports: [PageMenuComponent, ErrorComponent],
})
export class MonitorGroupPageMenuComponent {
  readonly pageName = input.required<string>();
  readonly groupName = input.required<string>();

  protected readonly menuOptions: Signal<MenuOption[]> = computed(() => {
    const link = '/monitor/groups/' + this.groupName();
    return [
      {
        pageName: 'routes',
        pageLink: link,
        label: $localize`:@@monitor.group.menu.routes:Routes`,
      },
    ];
  });
}
