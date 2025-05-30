import { output } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AnalysisStrategy } from '@app/shared/core/preferences/analysis-strategy';
import { NzRadioGroupComponent } from 'ng-zorro-antd/radio';
import { NzRadioComponent } from 'ng-zorro-antd/radio';
import { AnalysisStrategyService } from './analysis-strategy.service';

@Component({
  selector: 'ui-analysis-strategy',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div>Strategy</div>
    <nz-radio-group [ngModel]="strategy()" (ngModelChange)="onStrategyChange($event)">
      <li>
        <label nz-radio nzValue="location" i18n="@@analysis.by-location">Explore by location</label>
      </li>
      <li>
        <label nz-radio nzValue="network" i18n="@@analysis.by-network">Explore by network</label>
      </li>
    </nz-radio-group>
  `,
  styles: `
    :host {
      display: block;
    }
  `,
  imports: [NzRadioComponent, NzRadioGroupComponent, FormsModule],
})
export class AnalysisStrategyComponent {
  readonly strategyChange = output<AnalysisStrategy>();

  private readonly service = inject(AnalysisStrategyService);
  protected readonly strategy = this.service.strategy;

  onStrategyChange(value: AnalysisStrategy) {
    this.service.setStrategy(value);
    this.strategyChange.emit(value);
  }
}
