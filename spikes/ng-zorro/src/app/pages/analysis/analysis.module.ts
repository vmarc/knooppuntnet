import {NgModule} from '@angular/core';
import {NzButtonModule} from 'ng-zorro-antd/button';
import {NzTabsModule} from 'ng-zorro-antd/tabs';
import {NodesComponent} from './nodes.component';
import {RoutesComponent} from './routes.component';
import {FactsComponent} from './facts.component';
import {TabsComponent} from './tabs.component';
import {AnalysisRoutingModule} from './analysis-routing.module';
import {AnalysisComponent} from './analysis.component';
import {NzTableModule} from 'ng-zorro-antd/table';
import {CommonModule} from '@angular/common';
import {NzBreadCrumbModule} from 'ng-zorro-antd/breadcrumb';
import {NzIconModule} from 'ng-zorro-antd/icon';
import {HeaderComponent} from './header.component';
import {NzCascaderModule} from 'ng-zorro-antd/cascader';
import {FormsModule} from '@angular/forms';
import {NzBadgeModule} from "ng-zorro-antd/badge";
import {BadgeComponent} from "./badge.component";

@NgModule({
  imports: [
    CommonModule,
    AnalysisRoutingModule,
    NzButtonModule,
    NzTabsModule,
    NzTableModule,
    NzBreadCrumbModule,
    NzIconModule,
    NzCascaderModule,
    FormsModule,
    NzBadgeModule
  ],
  declarations: [
    AnalysisComponent,
    NodesComponent,
    RoutesComponent,
    FactsComponent,
    TabsComponent,
    HeaderComponent,
    BadgeComponent,
  ],
  exports: [
    AnalysisComponent
  ]
})
export class AnalysisModule {
}
