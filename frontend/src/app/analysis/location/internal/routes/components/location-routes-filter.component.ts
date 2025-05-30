import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BooleanParameter } from '@api/common/location/boolean-parameter';
import { LastUpdatedParameter } from '@api/common/location/last-updated-parameter';
import { SurveyParameter } from '@api/common/location/survey-parameter';
import { Fact } from '@api/common/fact';
import { NzRadioComponent } from 'ng-zorro-antd/radio';
import { NzRadioGroupComponent } from 'ng-zorro-antd/radio';
import { LocationRoutesPageService } from '../location-routes-page.service';

@Component({
  selector: 'ui-location-routes-filter',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (store.response(); as response) {
      <div class="filter">
        <div class="title">Facts</div>
        <nz-radio-group
          [ngModel]="response.result.filter.fact.selected"
          (ngModelChange)="factChanged($event)"
        >
          @for (option of response.result.filter.fact.options; track option.name) {
            <div>
              <label nz-radio [nzValue]="option.name">
                <span>{{ option.name }}</span
                ><span class="kpn-brackets">{{ option.count }}</span>
              </label>
            </div>
          }
        </nz-radio-group>
      </div>

      <div class="filter">
        <div class="title">Survey</div>
        <nz-radio-group
          [ngModel]="response.result.filter.survey.selected"
          (ngModelChange)="surveyChanged($event)"
        >
          @for (option of response.result.filter.survey.options; track option.name) {
            <div>
              <label nz-radio [nzValue]="option.name">
                <span>{{ option.name }}</span
                ><span class="kpn-brackets">{{ option.count }}</span>
              </label>
            </div>
          }
        </nz-radio-group>
      </div>

      <div class="filter">
        <div class="title">Last Updated</div>
        <nz-radio-group
          [ngModel]="response.result.filter.lastUpdated.selected"
          (ngModelChange)="lastUpdatedChanged($event)"
        >
          @for (option of response.result.filter.lastUpdated.options; track option.name) {
            <div>
              <label nz-radio [nzValue]="option.name">
                <span>{{ option.name }}</span
                ><span class="kpn-brackets">{{ option.count }}</span>
              </label>
            </div>
          }
        </nz-radio-group>
      </div>

      <div class="filter">
        <div class="title">Proposed</div>
        <nz-radio-group
          [ngModel]="response.result.filter.proposed.selected"
          (ngModelChange)="proposedChanged($event)"
        >
          @for (option of response.result.filter.proposed.options; track option.name) {
            <div>
              <label nz-radio [nzValue]="option.name">
                <span>{{ option.name }}</span
                ><span class="kpn-brackets">{{ option.count }}</span>
              </label>
            </div>
          }
        </nz-radio-group>
      </div>
    }
  `,
  styles: `
    .filter {
      padding: 1em;
    }

    .title {
      padding-bottom: 1em;
    }
  `,
  imports: [NzRadioGroupComponent, NzRadioComponent, FormsModule],
})
export class LocationRoutesFilterComponent {
  protected readonly store = inject(LocationRoutesPageService);

  factChanged(value: string): void {
    if (value == 'all') {
      this.store.updateFact(null);
    } else {
      const fact = value as Fact;
      this.store.updateFact(fact);
    }
  }

  surveyChanged(value: string): void {
    if (value == 'all') {
      this.store.updateSurvey(null);
    } else {
      const survey = value as SurveyParameter;
      this.store.updateSurvey(survey);
    }
  }

  lastUpdatedChanged(value: string): void {
    if (value == 'all') {
      this.store.updateLastUpdated(null);
    } else {
      const lastUpdated = value as LastUpdatedParameter;
      this.store.updateLastUpdated(lastUpdated);
    }
  }

  proposedChanged(value: string): void {
    if (value == 'all') {
      this.store.updateProposed(null);
    } else {
      const proposed = value as BooleanParameter;
      this.store.updateProposed(proposed);
    }
  }
}
