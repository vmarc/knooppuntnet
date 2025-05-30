import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'ui-toolbar-title',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <a routerLink="/" i18n="@@toolbar.title">routes</a> `,
  imports: [RouterLink],
})
export class ToolbarTitleComponent {}
