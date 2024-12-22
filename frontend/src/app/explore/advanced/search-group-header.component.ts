import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatIconButton } from '@angular/material/button';
import { MatButtonToggle } from '@angular/material/button-toggle';
import { MatButtonToggleGroup } from '@angular/material/button-toggle';
import { MatIcon } from '@angular/material/icon';
import { MatMenuItem } from '@angular/material/menu';
import { MatMenuTrigger } from '@angular/material/menu';
import { MatMenu } from '@angular/material/menu';
import { ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'kpn-search-group-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div>
      <mat-button-toggle-group name="operator">
        <mat-button-toggle value="and">and</mat-button-toggle>
        <mat-button-toggle value="or">or</mat-button-toggle>
      </mat-button-toggle-group>

      <button mat-icon-button [matMenuTriggerFor]="menu">
        <mat-icon svgIcon="add"></mat-icon>
      </button>
      <mat-menu #menu="matMenu">
        <button mat-menu-item>add condition</button>
        <button mat-menu-item>add group</button>
      </mat-menu>
      <button mat-icon-button>
        <mat-icon svgIcon="remove"></mat-icon>
      </button>
    </div>
  `,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    MatIcon,
    MatButtonToggleGroup,
    MatButtonToggle,
    MatIconButton,
    MatMenu,
    MatMenuTrigger,
    MatMenuItem,
  ],
})
export class SearchGroupHeaderComponent {}
