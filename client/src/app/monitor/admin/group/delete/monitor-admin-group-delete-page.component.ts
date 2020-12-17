import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';

@Component({
  selector: 'kpn-monitor-admin-group-delete-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/monitor">Monitor</a></li>
      <li>Group</li>
    </ul>

    <h1>
      Monitor
    </h1>

    <kpn-page-menu>
      <span>
        Delete group
      </span>
    </kpn-page-menu>

    <p>
      Name: group-1
    </p>

    <p>
      Description: Group One
    </p>

    <div class="kpn-button-group">
      <button mat-stroked-button (click)="delete()">
        <span class="delete">Delete group</span>
      </button>
      <a routerLink="/monitor">Cancel</a>
    </div>
  `,
  styles: [`
    .delete {
      color: red;
    }
  `]
})
export class MonitorAdminGroupDeletePageComponent {

  delete(): void {
    console.log('Dispatch group delete action');
  }
}
