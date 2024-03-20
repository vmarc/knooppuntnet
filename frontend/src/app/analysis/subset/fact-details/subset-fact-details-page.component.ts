import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatCardTitle } from '@angular/material/card';
import { MatCardHeader } from '@angular/material/card';
import { MatCardContent } from '@angular/material/card';
import { MatCard } from '@angular/material/card';
import { FactLevelComponent } from '@app/analysis/fact';
import { FactInfo } from '@app/analysis/fact';
import { FactDescriptionComponent } from '@app/analysis/fact';
import { FactNameComponent } from '@app/analysis/fact';
import { ErrorComponent } from '@app/components/shared/error';
import { PageComponent } from '@app/components/shared/page';
import { RouterService } from '../../../shared/services/router.service';
import { SubsetPageHeaderBlockComponent } from '../components/subset-page-header-block.component';
import { SubsetSidebarComponent } from '../subset-sidebar.component';
import { SubsetFactDetailsSummaryComponent } from './components/subset-fact-details-summary.component';
import { SubsetFactDetailsComponent } from './components/subset-fact-details.component';
import { SubsetFactDetailsPageService } from './subset-fact-details-page.service';

@Component({
  selector: 'kpn-subset-fact-details-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <kpn-subset-page-header-block
        pageName="facts"
        pageTitle="Facts"
        i18n-pageTitle="@@subset-facts.title"
      />
      <mat-card class="kpn-spacer-above">
        <mat-card-header>
          <mat-card-title>
            <div class="kpn-line">
              <kpn-fact-name [fact]="service.factDefinition().name" />
              <kpn-fact-level [factLevel]="service.factDefinition().level"></kpn-fact-level>
            </div>
          </mat-card-title>
        </mat-card-header>
        <mat-card-content>
          <kpn-fact-description [factInfo]="factInfo()" />
          @if (service.page(); as page) {
            <kpn-subset-fact-details-summary [page]="page" />
          }
        </mat-card-content>
      </mat-card>
      <kpn-error />
      @if (service.response(); as response) {
        <div>
          @if (response.result) {
            <div>
              <kpn-subset-fact-details [page]="response.result" />
            </div>
          }
        </div>
      }
      <kpn-subset-sidebar sidebar />
    </kpn-page>
  `,
  styleUrl: './subset-fact-details-page.component.scss',
  providers: [SubsetFactDetailsPageService, RouterService],
  standalone: true,
  imports: [
    ErrorComponent,
    FactDescriptionComponent,
    FactNameComponent,
    MatCard,
    MatCardContent,
    MatCardHeader,
    MatCardTitle,
    PageComponent,
    SubsetFactDetailsComponent,
    SubsetFactDetailsSummaryComponent,
    SubsetPageHeaderBlockComponent,
    SubsetSidebarComponent,
    FactLevelComponent,
  ],
})
export class SubsetFactDetailsPageComponent implements OnInit {
  protected readonly service = inject(SubsetFactDetailsPageService);

  ngOnInit(): void {
    this.service.onInit();
  }

  factInfo(): FactInfo {
    return new FactInfo(this.service.subsetFact().factName);
  }
}
