import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { PageMenuOptionComponent } from '@app/shared/components/menu/page-menu-option.component';
import { PageMenuComponent } from '@app/shared/components/menu/page-menu.component';
import { StatusLinks } from './status-links';

@Component({
  selector: 'ui-status-page-menu',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- English only-->
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <ui-page-menu>
      <ui-page-menu-option [link]="links().hour" [active]="periodType() === 'hour'">
        Hour
      </ui-page-menu-option>
      <ui-page-menu-option [link]="links().day" [active]="periodType() === 'day'">
        Day
      </ui-page-menu-option>
      <ui-page-menu-option [link]="links().week" [active]="periodType() === 'week'">
        Week
      </ui-page-menu-option>
      <ui-page-menu-option [link]="links().month" [active]="periodType() === 'month'">
        Month
      </ui-page-menu-option>
      <ui-page-menu-option [link]="links().year" [active]="periodType() === 'year'">
        Year
      </ui-page-menu-option>
    </ui-page-menu>
  `,
  imports: [PageMenuComponent, PageMenuOptionComponent],
})
export class StatusPageMenuComponent {
  readonly periodType = input.required<string>();
  readonly links = input.required<StatusLinks>();
}
