import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { Validators } from '@angular/forms';
import { Store } from '@ngrx/store';
import { tap } from 'rxjs/operators';
import { AppState } from '../../../core/core.state';
import { MonitorService } from '../../monitor.service';
import { actionMonitorGroupUpdateDestroy } from '../../store/monitor.actions';
import { actionMonitorGroupUpdateInit } from '../../store/monitor.actions';
import { actionMonitorGroupUpdate } from '../../store/monitor.actions';
import { selectMonitorGroupPage } from '../../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-group-update-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-monitor-group-breadcrumb />

    <h1 i18n="@@monitor.group.update.title">Monitor - update group</h1>

    <div *ngIf="response$ | async as response" class="kpn-form">
      <div *ngIf="!response.result">
        <p i18n="@@monitor.group.update.group-not-found">Group not found</p>
      </div>
      <div *ngIf="response.result">
        <form [formGroup]="form" #ngForm="ngForm">
          <kpn-monitor-group-name [ngForm]="ngForm" [name]="name" />
          <kpn-monitor-group-description
            [ngForm]="ngForm"
            [description]="description"
          />

          <div class="kpn-form-buttons">
            <button
              mat-stroked-button
              (click)="update(response.result.groupId)"
              i18n="@@monitor.group.update.action"
            >
              Update group
            </button>
            <a routerLink="/monitor" i18n="@@action.cancel">Cancel</a>
          </div>
        </form>
      </div>
    </div>
  `,
})
export class MonitorGroupUpdatePageComponent implements OnInit, OnDestroy {
  private initialName = '';
  readonly name = new FormControl<string>('', {
    validators: [Validators.required, Validators.maxLength(15)],
    asyncValidators: this.monitorService.asyncGroupNameUniqueValidator(
      () => this.initialName
    ),
    // updateOn: 'blur',
  });
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
        this.initialName = response.result.groupName;
        this.form.reset({
          name: response.result.groupName,
          description: response.result.groupDescription,
        });
      }
    })
  );

  constructor(
    private monitorService: MonitorService,
    private store: Store
  ) {}

  ngOnInit(): void {
    this.store.dispatch(actionMonitorGroupUpdateInit());
  }

  ngOnDestroy(): void {
    this.store.dispatch(actionMonitorGroupUpdateDestroy());
  }

  update(groupId: string): void {
    if (this.form.valid) {
      this.store.dispatch(
        actionMonitorGroupUpdate({ groupId, properties: this.form.value })
      );
    }
  }
}
