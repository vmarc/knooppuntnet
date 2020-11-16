import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';

/* tslint:disable:template-i18n English only */
@Component({
  selector: 'kpn-status-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
    </kpn-sidebar>
  `
})
export class StatusSidebarComponent {
}
