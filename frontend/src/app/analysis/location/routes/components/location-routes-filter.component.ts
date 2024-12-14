import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatRadioChange } from '@angular/material/radio';
import { MatRadioModule } from '@angular/material/radio';
import { BooleanParameter } from '@api/common/location/boolean-parameter';
import { LastUpdatedParameter } from '@api/common/location/last-updated-parameter';
import { SurveyParameter } from '@api/common/location/survey-parameter';
import { Fact } from '@api/common';
import { LocationRoutesPageService } from '../location-routes-page.service';

@Component({
  selector: 'kpn-location-routes-filter',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (store.response(); as response) {
      <div class="filter">
        <div class="title">Facts</div>
        <mat-radio-group
          [value]="response.result.filter.fact.selected"
          (change)="factChanged($event)"
        >
          @for (option of response.result.filter.fact.options; track option.name) {
            <div>
              <mat-radio-button [value]="option.name">
                <span>{{ option.name }}</span
                ><span class="kpn-brackets">{{ option.count }}</span>
              </mat-radio-button>
            </div>
          }
        </mat-radio-group>
      </div>

      <div class="filter">
        <div class="title">Survey</div>
        <mat-radio-group
          [value]="response.result.filter.survey.selected"
          (change)="surveyChanged($event)"
        >
          @for (option of response.result.filter.survey.options; track option.name) {
            <div>
              <mat-radio-button [value]="option.name">
                <span>{{ option.name }}</span
                ><span class="kpn-brackets">{{ option.count }}</span>
              </mat-radio-button>
            </div>
          }
        </mat-radio-group>
      </div>

      <div class="filter">
        <div class="title">Last Updated</div>
        <mat-radio-group
          [value]="response.result.filter.lastUpdated.selected"
          (change)="lastUpdatedChanged($event)"
        >
          @for (option of response.result.filter.lastUpdated.options; track option.name) {
            <div>
              <mat-radio-button [value]="option.name">
                <span>{{ option.name }}</span
                ><span class="kpn-brackets">{{ option.count }}</span>
              </mat-radio-button>
            </div>
          }
        </mat-radio-group>
      </div>

      <div class="filter">
        <div class="title">Proposed</div>
        <mat-radio-group
          [value]="response.result.filter.proposed.selected"
          (change)="proposedChanged($event)"
        >
          @for (option of response.result.filter.proposed.options; track option.name) {
            <div>
              <mat-radio-button [value]="option.name">
                <span>{{ option.name }}</span
                ><span class="kpn-brackets">{{ option.count }}</span>
              </mat-radio-button>
            </div>
          }
        </mat-radio-group>
      </div>
    }
  `,
  styles: `
    .filter {
      padding: 25px 15px 25px 25px;
    }

    .title {
      padding-bottom: 10px;
    }
  `,
  imports: [MatRadioModule],
})
export class LocationRoutesFilterComponent {
  protected readonly store = inject(LocationRoutesPageService);

  factChanged(change: MatRadioChange): void {
    if (change.value == 'all') {
      this.store.setFact(null);
    } else {
      const value = change.value as Fact;
      this.store.setFact(value);
    }
  }

  surveyChanged(change: MatRadioChange): void {
    if (change.value == 'all') {
      this.store.setSurvey(null);
    } else {
      const value = change.value as SurveyParameter;
      this.store.setSurvey(value);
    }
  }

  lastUpdatedChanged(change: MatRadioChange): void {
    if (change.value == 'all') {
      this.store.setLastUpdated(null);
    } else {
      const value = change.value as LastUpdatedParameter;
      this.store.setLastUpdated(value);
    }
  }

  proposedChanged(change: MatRadioChange): void {
    if (change.value == 'all') {
      this.store.setProposed(null);
    } else {
      const value = change.value as BooleanParameter;
      this.store.setProposed(value);
    }
  }
}
