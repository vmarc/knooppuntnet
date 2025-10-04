import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { SurveyParameter } from '@api/common/location/survey-parameter';
import { BooleanParameter } from '@api/common/location/boolean-parameter';
import { LastUpdatedParameter } from '@api/common/location/last-updated-parameter';
import { Fact } from '@api/common/fact';
import { LocationFilterFactComponent } from '../../components/filter/location-filter-fact';
import { LocationFilterGroupComponent } from '../../components/filter/location-filter-group';
import { LocationNodesPageService } from '../location-nodes-page.service';

@Component({
  selector: 'ui-location-nodes-filter',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (store.response(); as response) {
      <ui-location-filter-group
        title="integrityCheck"
        [filterGroup]="response.result.filter.integrityCheck"
        (changed)="integrityCheckChanged($event)"
      />

      <ui-location-filter-group
        title="integrityCheckFailed"
        [filterGroup]="response.result.filter.integrityCheckFailed"
        (changed)="integrityCheckFailedChanged($event)"
      />

      <ui-location-filter-fact
        title="fact"
        [filterGroup]="response.result.filter.fact"
        (changed)="factChanged($event)"
      />

      <ui-location-filter-group
        title="referencedInRoute"
        [filterGroup]="response.result.filter.referencedInRoutes"
        (changed)="referencedInRoutesChanged($event)"
      />

      <ui-location-filter-group
        title="survey"
        [filterGroup]="response.result.filter.survey"
        (changed)="surveyChanged($event)"
      />

      <ui-location-filter-group
        title="lastUpdated"
        [filterGroup]="response.result.filter.lastUpdated"
        (changed)="lastUpdatedChanged($event)"
      />

      <ui-location-filter-group
        title="proposed"
        [filterGroup]="response.result.filter.proposed"
        (changed)="proposedChanged($event)"
      />
    }
  `,
  imports: [LocationFilterFactComponent, LocationFilterGroupComponent],
})
export class LocationNodesFilterComponent {
  protected readonly store = inject(LocationNodesPageService);

  integrityCheckChanged(integrityCheck: string): void {
    this.store.setIntegrityCheck(integrityCheck as BooleanParameter);
  }

  integrityCheckFailedChanged(integrityCheckFailed: string): void {
    this.store.setIntegrityCheckFailed(integrityCheckFailed as BooleanParameter);
  }

  factChanged(fact: Fact): void {
    this.store.setFact(fact);
  }

  surveyChanged(survey: string): void {
    this.store.setSurvey(survey as SurveyParameter);
  }

  lastUpdatedChanged(lastUpdated: string): void {
    this.store.setLastUpdated(lastUpdated as LastUpdatedParameter);
  }

  proposedChanged(proposed: string): void {
    this.store.setProposed(proposed as BooleanParameter);
  }

  referencedInRoutesChanged(referencedInRoutes: string): void {
    this.store.setReferencedInRoutes(referencedInRoutes as BooleanParameter);
  }
}
