import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatRadioChange } from '@angular/material/radio';
import { MatRadioModule } from '@angular/material/radio';
import { LocationNodesType } from '@api/custom';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { LocationNodesPageService } from '../location-nodes-page.service';

@Component({
  selector: 'kpn-location-nodes-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      @if (store.response(); as response) {
        <div class="filter">
          <div class="title" i18n="@@location-nodes-sidebar.filter.title">Filter</div>
          <mat-radio-group
            [value]="locationNodesType.all"
            (change)="locationNodesTypeChanged($event)"
          >
            <div>
              <mat-radio-button [value]="locationNodesType.all">
                <span i18n="@@location-nodes-sidebar.filter.all">All</span
                ><span class="kpn-brackets">{{ response.result.allNodeCount }}</span>
              </mat-radio-button>
            </div>
            <div>
              <mat-radio-button [value]="locationNodesType.facts">
                <span i18n="@@location-nodes-sidebar.filter.facts">Facts</span
                ><span class="kpn-brackets">{{ response.result.factsNodeCount }}</span>
              </mat-radio-button>
            </div>
            <div>
              <mat-radio-button [value]="locationNodesType.survey">
                <span i18n="@@location-nodes-sidebar.filter.survey">Survey</span
                ><span class="kpn-brackets">{{ response.result.surveyNodeCount }}</span>
              </mat-radio-button>
            </div>
            <div>
              <mat-radio-button [value]="locationNodesType.integrityCheckFailed">
                <span i18n="@@location-nodes-sidebar.filter.integrity-check-failed"
                  >Unexpected route count</span
                ><span class="kpn-brackets">{{
                  response.result.integrityCheckFailedNodeCount
                }}</span>
              </mat-radio-button>
            </div>
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
  protected readonly locationNodesType = LocationNodesType;

  locationNodesTypeChanged(change: MatRadioChange): void {
    const locationNodesType = change.value as LocationNodesType;
    this.store.setPageType(locationNodesType);
  }
}
