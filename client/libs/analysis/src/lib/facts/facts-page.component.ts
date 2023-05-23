import { NgFor } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FactDescriptionComponent } from '@app/analysis/fact';
import { FactNameComponent } from '@app/analysis/fact';
import { ItemComponent } from '@app/components/shared/items';
import { ItemsComponent } from '@app/components/shared/items';
import { PageComponent } from '@app/components/shared/page';
import { AnalysisSidebarComponent } from '../analysis/analysis-sidebar.component';
import { FactInfo } from '../fact';
import { Facts } from '../fact';

@Component({
  selector: 'kpn-facts-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <h1 i18n="@@fact-page.title">All facts</h1>

      <kpn-items>
        <kpn-item
          *ngFor="let factName of allFactNames(); let i = index"
          [index]="i"
        >
          <p>
            <kpn-fact-name [fact]="factName" />
          </p>
          <kpn-fact-description [factInfo]="factInfo(factName)" />
        </kpn-item>
      </kpn-items>
      <kpn-analysis-sidebar sidebar />
    </kpn-page>
  `,
  standalone: true,
  imports: [
    AnalysisSidebarComponent,
    FactDescriptionComponent,
    FactNameComponent,
    ItemComponent,
    ItemsComponent,
    NgFor,
    PageComponent,
  ],
})
export class FactsPageComponent {
  allFactNames() {
    return Facts.allFactNames;
  }

  factInfo(factName: string): FactInfo {
    return new FactInfo(factName);
  }
}
