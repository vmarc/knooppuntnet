import { NgIf } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MarkdownModule } from 'ngx-markdown';
import { Stat } from '../domain/stat';
import { OverviewListStatTableComponent } from './overview-list-stat-table.component';

@Component({
  selector: 'kpn-overview-list-stat',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="item">
      <div class="header">
        <div (click)="toggleOpen()" class="title">
          <span class="expand-collapse-icon">
            <mat-icon svgIcon="expand" *ngIf="open" mat-list-icon></mat-icon>
            <mat-icon svgIcon="collapse" *ngIf="!open" mat-list-icon></mat-icon>
          </span>
          <span class="name">
            {{ stat.configuration.name }}
          </span>
        </div>
        <div class="total">
          {{ stat.total() }}
        </div>
      </div>

      <div *ngIf="open" class="body">
        <div class="comment">
          <markdown
            *ngIf="stat.configuration.markdown"
            [data]="comment(stat)"
          ></markdown>
          <p *ngIf="!stat.configuration.markdown">
            {{ stat.configuration.comment }}
          </p>
        </div>
        <kpn-overview-list-stat-table [stat]="stat" />
      </div>
    </div>
  `,
  styles: [
    `
      .item {
        padding: 10px;
        border-bottom-color: lightgray;
        border-bottom-style: solid;
        border-bottom-width: 1px;
      }

      .header {
        display: flex;
        max-width: 20em;
        align-items: center;
      }

      .title {
        flex: 0 0 auto;
        cursor: pointer;
        display: flex;
        align-items: center;
      }

      .total {
        flex: 1 0 auto;
        text-align: right;
        vertical-align: middle;
      }

      .expand-collapse-icon {
        padding-top: 5px;
      }

      ::ng-deep .expand-collapse-icon svg {
        width: 12px;
        height: 12px;
        vertical-align: middle;
      }

      .body {
        margin-left: 20px;
      }

      .comment {
        margin: 20px 20px 20px 10px;
        padding-left: 5px;
        border-left-color: lightgray;
        border-left-style: solid;
        border-left-width: 3px;
        max-width: 40em;
      }
    `,
  ],
  standalone: true,
  imports: [
    MarkdownModule,
    MatIconModule,
    NgIf,
    OverviewListStatTableComponent,
  ],
})
export class OverviewListStatComponent {
  @Input() stat: Stat;

  open = false;

  toggleOpen(): void {
    this.open = !this.open;
  }

  comment(stat: Stat) {
    return stat.configuration.comment.replace('\\', '\n\n');
  }
}
