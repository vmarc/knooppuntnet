import {OnInit} from '@angular/core';
import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';
import {FormGroup} from '@angular/forms';
import {Validators} from '@angular/forms';
import {FormControl} from '@angular/forms';
import {MonitorGroup} from '@api/common/monitor/monitor-group';
import {Store} from '@ngrx/store';
import {tap} from 'rxjs/operators';
import {AppState} from '../../../../core/core.state';
import {actionMonitorGroupUpdateInit} from '../../../store/monitor.actions';
import {actionMonitorUpdateRouteGroup} from '../../../store/monitor.actions';
import {selectMonitorAdminRouteGroupPage} from '../../../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-admin-group-add-page',
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
        Update group
      </span>
    </kpn-page-menu>

    <div *ngIf="response$ | async as response">
      <div *ngIf="!response.result">
        <p>
          Group not found
        </p>
      </div>
      <div *ngIf="response.result">

        <form [formGroup]="form">

          <p>
            Name: {{name.value}}
          </p>

          <p>
            <mat-form-field class="description">
              <mat-label>Description</mat-label>
              <input matInput [formControl]="description">
            </mat-form-field>
          </p>

          <div class="kpn-button-group">
            <button mat-stroked-button (click)="add()">Update group</button>
            <a routerLink="/monitor">Cancel</a>
          </div>

        </form>
      </div>
    </div>
  `,
  styles: [`
    .description {
      width: 40em;
    }

    .kpn-button-group {
      padding-top: 3em;
    }
  `]
})
export class MonitorAdminGroupUpdatePageComponent implements OnInit {

  readonly name = new FormControl('');
  readonly description = new FormControl('', [Validators.required]);

  readonly form = new FormGroup({
    name: this.name,
    description: this.description
  });

  readonly response$ = this.store.select(selectMonitorAdminRouteGroupPage).pipe(
    tap(response => {
      if (response?.result) {
        this.form.reset({
          name: response.result.groupName,
          description: response.result.groupDescription,
        });
      }
    })
  );

  constructor(private store: Store<AppState>) {
  }

  ngOnInit(): void {
    this.store.dispatch(actionMonitorGroupUpdateInit());
  }

  add(): void {
    const group: MonitorGroup = this.form.value;
    this.store.dispatch(actionMonitorUpdateRouteGroup({group}));
  }
}
