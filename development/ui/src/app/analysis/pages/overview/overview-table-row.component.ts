import {Component, Input} from '@angular/core';
import {Stat} from "./stat";
import {Subsets} from "../../../kpn/common/subsets";

@Component({
  selector: 'kpn-overview-table-row',
  template: `
    <td>
      {{stat.configuration.name}}
    </td>
    <td class="value-cell">
      {{total()}}
    </td>
    <td class="value-cell" *ngFor="let subset of subsets()">
      <kpn-overview-table-cell [stat]="stat" [subset]="subset"></kpn-overview-table-cell>
    </td>
    <td class="comment-cell">
      <markdown *ngIf="stat.configuration.markdown" [data]="comment()"></markdown>
      <p *ngIf="!stat.configuration.markdown">
        {{stat.configuration.comment}}
      </p>
    </td>
  `,
  styles: [`
    :host {
      display: table-row;
    }

    /deep/ .comment-cell p:first-child {
      margin-top: 0;
    }

    /deep/ .comment-cell p:last-child {
      margin-bottom: 0;
    }
  `]
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
    return this.stat.configuration.comment.replace("\\", "\n\n");
  }

}
