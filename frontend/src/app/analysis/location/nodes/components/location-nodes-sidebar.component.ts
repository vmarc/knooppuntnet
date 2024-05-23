import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatRadioChange } from '@angular/material/radio';
import { MatRadioModule } from '@angular/material/radio';
import { SurveyParameter } from '@api/common/location';
import { BooleanParameter } from '@api/common/location/boolean-parameter';
import { LastUpdatedParameter } from '@api/common/location/last-updated-parameter';
import { Fact } from '@api/custom';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { LocationNodesPageService } from '../location-nodes-page.service';

@Component({
  selector: 'kpn-location-nodes-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      @if (store.response(); as response) {
        <div class="filter">
          <div class="title">IntegrityCheck</div>
          <mat-radio-group
            [value]="response.result.filter.integrityCheck.selected"
            (change)="integrityCheckChanged($event)"
          >
            @for (option of response.result.filter.integrityCheck.options; track option.name) {
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
          <div class="title">IntegrityCheckFailed</div>
          <mat-radio-group
            [value]="response.result.filter.integrityCheckFailed.selected"
            (change)="integrityCheckFailedChanged($event)"
          >
            @for (
              option of response.result.filter.integrityCheckFailed.options;
              track option.name
            ) {
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
          <div class="title">Referenced in routes</div>
          <mat-radio-group
            [value]="response.result.filter.referencedInRoutes.selected"
            (change)="referencedInRoutesChanged($event)"
          >
            @for (option of response.result.filter.referencedInRoutes.options; track option.name) {
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
    </kpn-sidebar>
  `,
  styles: `
    .filter {
      padding: 25px 15px 25px 25px;
    }

    .title {
      padding-bottom: 10px;
    }
  `,
  standalone: true,
  imports: [SidebarComponent, MatRadioModule],
})
export class LocationNodesSidebarComponent {
  protected readonly store = inject(LocationNodesPageService);

  integrityCheckChanged(change: MatRadioChange): void {
    if (change.value == 'all') {
      this.store.setIntegrityCheck(null);
    } else {
      const value = change.value as BooleanParameter;
      this.store.setIntegrityCheck(value);
    }
  }

  integrityCheckFailedChanged(change: MatRadioChange): void {
    if (change.value == 'all') {
      this.store.setIntegrityCheckFailed(null);
    } else {
      const value = change.value as BooleanParameter;
      this.store.setIntegrityCheckFailed(value);
    }
  }

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

  referencedInRoutesChanged(change: MatRadioChange): void {
    if (change.value == 'all') {
      this.store.setReferencedInRoutes(null);
    } else {
      const value = change.value as BooleanParameter;
      this.store.setReferencedInRoutes(value);
    }
  }
}
