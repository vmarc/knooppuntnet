import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatRadioModule } from '@angular/material/radio';
import { SurveyParameter } from '@api/common/location';
import { BooleanParameter } from '@api/common/location/boolean-parameter';
import { LastUpdatedParameter } from '@api/common/location/last-updated-parameter';
import { Fact } from '@api/custom';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { LocationFilterFactComponent } from '../../components/filter/location-filter-fact';
import { LocationFilterGroupComponent } from '../../components/filter/location-filter-group';
import { LocationNodesPageService } from '../location-nodes-page.service';

@Component({
  selector: 'kpn-location-nodes-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      @if (store.response(); as response) {
        <kpn-location-filter-group
          title="integrityCheck"
          [filterGroup]="response.result.filter.integrityCheck"
          (changed)="integrityCheckChanged($event)"
        />

        <kpn-location-filter-group
          title="integrityCheckFailed"
          [filterGroup]="response.result.filter.integrityCheckFailed"
          (changed)="integrityCheckFailedChanged($event)"
        />

        <kpn-location-filter-fact
          title="fact"
          [filterGroup]="response.result.filter.fact"
          (changed)="factChanged($event)"
        />

        <kpn-location-filter-group
          title="referencedInRoute"
          [filterGroup]="response.result.filter.referencedInRoutes"
          (changed)="referencedInRoutesChanged($event)"
        />

        <kpn-location-filter-group
          title="survey"
          [filterGroup]="response.result.filter.survey"
          (changed)="surveyChanged($event)"
        />

        <kpn-location-filter-group
          title="lastUpdated"
          [filterGroup]="response.result.filter.lastUpdated"
          (changed)="lastUpdatedChanged($event)"
        />

        <kpn-location-filter-group
          title="proposed"
          [filterGroup]="response.result.filter.proposed"
          (changed)="proposedChanged($event)"
        />
      }
    </kpn-sidebar>
  `,
  standalone: true,
  imports: [
    LocationFilterFactComponent,
    LocationFilterGroupComponent,
    MatRadioModule,
    SidebarComponent,
  ],
})
export class LocationNodesSidebarComponent {
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
