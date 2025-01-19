import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { TuiLink } from '@taiga-ui/core';

@Component({
  selector: 'kpn-toolbar-title',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <a tuiLink routerLink="/" i18n="@@toolbar.title">routes</a> `,
  imports: [RouterLink, TuiLink],
})
export class ToolbarTitleComponent {}
