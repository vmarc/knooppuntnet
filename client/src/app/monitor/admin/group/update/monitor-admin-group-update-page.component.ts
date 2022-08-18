import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { Validators } from '@angular/forms';
import { Store } from '@ngrx/store';
import { tap } from 'rxjs/operators';
import { AppState } from '../../../../core/core.state';
import { actionMonitorGroupUpdateInit } from '../../../store/monitor.actions';
import { actionMonitorGroupUpdate } from '../../../store/monitor.actions';
import { selectMonitorGroupPage } from '../../../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-admin-group-update-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-monitor-admin-group-breadcrumb></kpn-monitor-admin-group-breadcrumb>

    <h1>Monitor - update group</h1>

    <div *ngIf="response$ | async as response" class="kpn-form">
      <div *ngIf="!response.result">
        <p>Group not found</p>
      </div>
      <div *ngIf="response.result">
        <form [formGroup]="form" #ngForm="ngForm">
          <p>Name: {{ name.value }}</p>

          <kpn-monitor-admin-group-description
            [ngForm]="ngForm"
            [description]="description"
          ></kpn-monitor-admin-group-description>

          <div class="kpn-form-buttons">
            <button
              mat-stroked-button
              (click)="update(response.result.groupId)"
            >
              Update group
            </button>
            <a routerLink="/monitor">Cancel</a>
          </div>
        </form>
      </div>
    </div>
  `,
})
export class MonitorAdminGroupUpdatePageComponent implements OnInit {
  readonly name = new FormControl<string>('');
  readonly description = new FormControl<string>('', [
    Validators.required,
    Validators.maxLength(100),
  ]);

  readonly form = new FormGroup({
    name: this.name,
    description: this.description,
  });

  readonly response$ = this.store.select(selectMonitorGroupPage).pipe(
    tap((response) => {
      if (response?.result) {
        this.form.reset({
          name: response.result.groupName,
          description: response.result.groupDescription,
        });
      }
    })
  );

  constructor(private store: Store<AppState>) {}

  ngOnInit(): void {
    this.store.dispatch(actionMonitorGroupUpdateInit());
  }

  update(groupId: string): void {
    if (this.form.valid) {
      this.store.dispatch(
        actionMonitorGroupUpdate({ groupId, properties: this.form.value })
      );
    }
  }
}
