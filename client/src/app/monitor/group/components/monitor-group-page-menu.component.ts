import {ChangeDetectionStrategy} from '@angular/core';
import {Component, Input} from '@angular/core';

@Component({
  selector: 'kpn-monitor-group-page-menu',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page-menu>
      <kpn-page-menu-option
        [link]="'/monitor/groups/' + groupName"
        [active]="pageName === 'routes'">
        Routes
      </kpn-page-menu-option>
      <kpn-page-menu-option
        [link]="'/monitor/groups/' + groupName + '/changes'"
        [active]="pageName === 'changes'">
        Changes
      </kpn-page-menu-option>
    </kpn-page-menu>

    <kpn-error></kpn-error>
  `
})
export class MonitorGroupPageMenuComponent {

  @Input() pageName: string;
  @Input() groupName: string;

}
