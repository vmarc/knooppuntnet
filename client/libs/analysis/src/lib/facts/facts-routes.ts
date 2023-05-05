import { Routes } from '@angular/router';
import { Util } from '@app/components/shared';
import { AnalysisSidebarComponent } from '@app/components/shared/sidebar';
import { FactsPageComponent } from './facts-page.component';

export const factsRoutes: Routes = [
  Util.routePath('facts', FactsPageComponent, AnalysisSidebarComponent),
];
