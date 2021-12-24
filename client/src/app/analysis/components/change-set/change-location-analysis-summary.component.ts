import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input, OnInit } from '@angular/core';
import { ChangeSetNetwork } from '@api/common/change-set-network';
import { ChangeSetSummaryInfo } from '@api/common/change-set-summary-info';
import { ChangeSetNetworkAction } from './components/change-set-network.component';

@Component({
  selector: 'kpn-change-location-analysis-summary',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="change-set">
      <kpn-change-header
        [changeKey]="changeSet.key"
        [happy]="changeSet.happy"
        [investigate]="changeSet.investigate"
        [comment]="changeSet.comment"
      >
      </kpn-change-header>

      <pre>
        {{ json }}
      </pre
      >
    </div>
  `,
  styles: [
    `
      .change-set {
        margin-top: 5px;
        margin-bottom: 5px;
      }
    `,
  ],
})
export class ChangeLocationAnalysisSummaryComponent implements OnInit {
  @Input() changeSet: ChangeSetSummaryInfo;

  json: string;

  ngOnInit(): void {
    this.json = JSON.stringify(this.changeSet.location, null, 2);
  }
}
