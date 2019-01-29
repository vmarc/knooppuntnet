import {Component} from '@angular/core';

@Component({
  selector: 'kpn-card-analysis',
  template: `
    <mat-card>
      <mat-card-header>
        <mat-card-title>
          Analysis
        </mat-card-title>
      </mat-card-header>
      <mat-divider></mat-divider>
      <mat-card-content>

      </mat-card-content>
    </mat-card>
  `,
  styles: [`
    mat-card-content {
      padding-left: 15px;
      padding-top: 15px;
      font-size: 16px;
    }

    .grid {
      display: flex;
      flex-direction: row;
    }

    .grid-column-2 {
      margin-left: 40px;
    }

    a {
      display: inline-flex;
      line-height: 20px;
      flex-direction: row;
      align-items: center;
      margin-top: 5px;
      margin-bottom: 5px;
    }

    mat-icon {
      margin-right: 10px;
      color: black;
    }

    kpn-link-overview, kpn-link-changes {
      display: block;
      line-height: 20px;
      margin-top: 5px;
      margin-bottom: 5px;
    }
  `]
})
export class CardAnalysisComponent {
}
