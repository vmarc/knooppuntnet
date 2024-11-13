import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatIconButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import { MatListItem } from '@angular/material/list';
import { MatNavList } from '@angular/material/list';
import { TryoutPanelsService } from './tryout-panels.service';

@Component({
  selector: 'kpn-tryout-panels-menu',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-nav-list>
      <mat-list-item (click)="service.gotoSearch()">
        <button mat-icon-button>
          <mat-icon svgIcon="link" />
        </button>
        <a>Search</a>
      </mat-list-item>

      <mat-list-item (click)="service.gotoPlanner()">
        <button mat-icon-button>
          <mat-icon svgIcon="location" />
        </button>
        <a>Plan a route</a>
      </mat-list-item>

      <mat-list-item (click)="service.gotoConfiguration()">
        <button mat-icon-button>
          <mat-icon svgIcon="layers" />
        </button>
        <a>Map configuration</a>
      </mat-list-item>

      <mat-list-item (click)="service.gotoAnalysis()">
        <button mat-icon-button>
          <mat-icon svgIcon="analysis" />
        </button>
        <a>Analysis</a>
      </mat-list-item>

      <mat-list-item (click)="service.gotoMonitor()">
        <button mat-icon-button>
          <mat-icon svgIcon="output" />
        </button>
        <a>Monitor</a>
      </mat-list-item>
    </mat-nav-list>
  `,
  standalone: true,
  imports: [MatIcon, MatIconButton, MatNavList, MatListItem],
})
export class TryoutPanelsMenuComponent {
  readonly service = inject(TryoutPanelsService);
}
