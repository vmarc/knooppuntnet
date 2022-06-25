import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { Subsets } from '../../../kpn/common/subsets';
import { Stat } from '../domain/stat';

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
      <kpn-overview-value [stat]="stat" [subset]="subset"></kpn-overview-value>
    </td>
    <td class="comment-cell">
      <markdown
        *ngIf="stat.configuration.markdown"
        [data]="comment()"
      ></markdown>
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

      .value-cell {
        white-space: nowrap;
        text-align: right;
      }
    `,
  ],
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
