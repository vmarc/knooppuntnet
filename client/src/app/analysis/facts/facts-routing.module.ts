import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AnalysisSidebarComponent } from '@app/components/shared/sidebar/analysis-sidebar.component';
import { Util } from '@app/components/shared/util';
import { FactsPageComponent } from './facts-page.component';

const routes: Routes = [
  Util.routePath('facts', FactsPageComponent, AnalysisSidebarComponent),
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class FactsRoutingModule {}
