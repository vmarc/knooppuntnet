import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Validators } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { MonitorGroup } from '@api/common/monitor/monitor-group';
import { Store } from '@ngrx/store';
import { tap } from 'rxjs/operators';
import { AppState } from '../../../../core/core.state';
import { actionMonitorGroupUpdateInit } from '../../../store/monitor.actions';
import { actionMonitorGroupUpdate } from '../../../store/monitor.actions';
import { selectMonitorGroupPage } from '../../../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-admin-group-add-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/monitor">Monitor</a></li>
      <li>Group</li>
    </ul>

    <h1>Monitor</h1>

    <h2>Update group</h2>

    <div *ngIf="response$ | async as response" class="kpn-form">
      <div *ngIf="!response.result">
        <p>Group not found</p>
      </div>
      <div *ngIf="response.result">
        <form [formGroup]="form">
          <p>Name: {{ _id.value }}</p>

          <p>
            <mat-form-field class="description">
              <mat-label>Description</mat-label>
              <input matInput [formControl]="description" />
            </mat-form-field>
          </p>

          <div class="kpn-form-buttons">
            <button mat-stroked-button (click)="add()">Update group</button>
            <a routerLink="/monitor">Cancel</a>
          </div>
        </form>
      </div>
    </div>
  `,
  styles: [
    `
      .description {
        width: 40em;
      }
    `,
  ],
})
export class MonitorAdminGroupUpdatePageComponent implements OnInit {
  readonly _id = new FormControl('');
  readonly description = new FormControl('', [Validators.required]);

  readonly form = new FormGroup({
    _id: this._id,
    description: this.description,
  });

  readonly response$ = this.store.select(selectMonitorGroupPage).pipe(
    tap((response) => {
      if (response?.result) {
        this.form.reset({
          _id: response.result.groupName,
          description: response.result.groupDescription,
        });
      }
    })
  );

  constructor(private store: Store<AppState>) {}

  ngOnInit(): void {
    this.store.dispatch(actionMonitorGroupUpdateInit());
  }

  add(): void {
    const group: MonitorGroup = this.form.value;
    this.store.dispatch(actionMonitorGroupUpdate({ group }));
  }
}
