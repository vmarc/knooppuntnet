import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';
import {of} from 'rxjs';

@Component({
  selector: 'kpn-monitor-group-changes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `

    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/monitor">Monitor</a></li>
      <li>Group changes</li>
    </ul>

    <h1>
      {{groupDescription$ | async}}
    </h1>

    <kpn-monitor-group-page-menu pageName="changes" [groupName]="groupName$ | async"></kpn-monitor-group-page-menu>

  `,
  styles: [`
  `]
})
export class MonitorGroupChangesPageComponent {

  readonly groupName$ = of('group-1');
  readonly groupDescription$ = of('Group One');

}
