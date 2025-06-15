import { computed } from '@angular/core';
import { Signal } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MenuOption } from '@app/shared/components/menu/menu-option';
import { PageMenuOptionComponent } from '@app/shared/components/menu/page-menu-option.component';
import { PageMenuComponent } from '@app/shared/components/menu/page-menu.component';
import { StatusLinks } from './status-links';

@Component({
  selector: 'ui-status-page-menu',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page-menu [pageName]="periodType()" [options]="menuOptions()" />
  `,
  imports: [PageMenuComponent, PageMenuOptionComponent],
})
export class StatusPageMenuComponent {
  readonly periodType = input.required<string>();
  readonly links = input.required<StatusLinks>();

  protected readonly menuOptions: Signal<MenuOption[]> = computed(() => {
    return [
      {
        pageName: 'hour',
        pageLink: this.links().hour,
        label: 'Hour',
      },
      {
        pageName: 'day',
        pageLink: this.links().day,
        label: 'Day',
      },
      {
        pageName: 'week',
        pageLink: this.links().week,
        label: 'Week',
      },
      {
        pageName: 'month',
        pageLink: this.links().month,
        label: 'Month',
      },
      {
        pageName: 'year',
        pageLink: this.links().year,
        label: 'Year',
      },
    ];
  });
}
