import {ChangeDetectionStrategy} from "@angular/core";
import {Component, Input} from "@angular/core";
import {Stat} from "../domain/stat";

@Component({
  selector: "kpn-overview-list-stat",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `

    <div class="item">

      <div class="header">
        <div (click)="toggleOpen()" class="title">
          <div class="icon">
            <mat-icon svgIcon="expand" *ngIf="open" mat-list-icon></mat-icon>
            <mat-icon svgIcon="collapse" *ngIf="!open" mat-list-icon></mat-icon>
          </div>
          <div class="name">
            {{stat.configuration.name}}
          </div>
        </div>
        <div class="total">
          {{stat.total()}}
        </div>
      </div>

      <div *ngIf="open" class="body">
        <div class="comment">
          <markdown *ngIf="stat.configuration.markdown" [data]="comment(stat)"></markdown>
          <p *ngIf="!stat.configuration.markdown">
            {{stat.configuration.comment}}
          </p>
        </div>
        <kpn-overview-list-stat-table [stat]="stat"></kpn-overview-list-stat-table>
      </div>
    </div>
  `,
  styles: [`

    .item {
      padding: 10px;
      border-bottom-color: lightgray;
      border-bottom-style: solid;
      border-bottom-width: 1px;
    }

    .header {
      display: flex;
      max-width: 40em;
    }

    .title {
      flex: 0 0 auto;
      cursor: pointer;
    }

    .name {
      display: inline-block;
      vertical-align: middle;
    }

    .total {
      flex: 1 0 auto;
      text-align: right;
      vertical-align: middle;
    }

    .icon {
      display: inline-block;
    }

    ::ng-deep .icon svg {
      width: 12px;
      height: 12px;
      vertical-align: middle;
    }

    .body {
      margin-left: 20px;
    }

    .comment {
      margin-top: 20px;
      margin-bottom: 20px;
      margin-left: 10px;
      margin-right: 20px;
      padding-left: 5px;
      border-left-color: lightgray;
      border-left-style: solid;
      border-left-width: 3px;
      max-width: 40em;
    }

  `]
})
export class OverviewListStatComponent {

  @Input() stat: Stat;

  open = false;

  toggleOpen(): void {
    this.open = !this.open;
  }

  comment(stat: Stat) {
    return stat.configuration.comment.replace("\\", "\n\n");
  }

}
