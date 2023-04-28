import { NgIf } from '@angular/common';
import { NgFor } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { Subsets } from '@app/kpn/common';
import { MarkdownModule } from 'ngx-markdown';
import { Stat } from '../domain/stat';
import { OverviewValueComponent } from './overview-value.component';

@Component({
  selector: 'kpn-overview-table-row',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <td>
      {{ stat.configuration.name }}
    </td>
    <td class="value-cell">
      {{ total() }}
    </td>
    <td class="value-cell" *ngFor="let subset of subsets()">
      <kpn-overview-value [stat]="stat" [subset]="subset" />
    </td>
    <td class="comment-cell">
      <markdown *ngIf="stat.configuration.markdown" [data]="comment()" />
      <p *ngIf="!stat.configuration.markdown">
        {{ stat.configuration.comment }}
      </p>
    </td>
  `,
  styles: [
    `
      :host {
        display: table-row;
      }

      ::ng-deep .comment-cell p:first-child {
        margin-top: 0;
      }

      ::ng-deep .comment-cell p:last-child {
        margin-bottom: 0;
      }

      ::ng-deep .comment-cell {
        min-width: 15em;
      }

      .value-cell {
        white-space: nowrap;
        text-align: right;
      }
    `,
  ],
  standalone: true,
  imports: [NgFor, OverviewValueComponent, NgIf, MarkdownModule],
})
export class OverviewTableRowComponent {
  @Input() stat: Stat;

  subsets() {
    return Subsets.all;
  }

  total() {
    return this.stat.total();
  }

  comment() {
    return this.stat.configuration.comment.replace('\\', '\n\n');
  }
}
