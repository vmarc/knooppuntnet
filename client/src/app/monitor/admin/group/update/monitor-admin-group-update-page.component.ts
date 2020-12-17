import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';
import {FormGroup} from '@angular/forms';
import {Validators} from '@angular/forms';
import {FormControl} from '@angular/forms';

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
        Add group
      </span>
    </kpn-page-menu>

    <form [formGroup]="form">
      <p>
        <mat-form-field>
          <mat-label>Name</mat-label>
          <input matInput [formControl]="name">
        </mat-form-field>
      </p>

      <p>
        <mat-form-field>
          <mat-label>Description</mat-label>
          <input matInput [formControl]="description">
        </mat-form-field>
      </p>

      <div class="kpn-button-group">
        <button mat-stroked-button (click)="add()">Update group</button>
        <a routerLink="/monitor">Cancel</a>
      </div>

    </form>
  `,
  styles: [`
    .kpn-button-group {
      padding-top: 3em;
    }
  `]
})
export class MonitorAdminGroupUpdatePageComponent {

  readonly name = new FormControl('', [Validators.required]);
  readonly description = new FormControl('', [Validators.required]);

  readonly form = new FormGroup({
    name: this.name,
    description: this.description
  });

  add(): void {
    console.log('Dispatch update group action');
  }
}
