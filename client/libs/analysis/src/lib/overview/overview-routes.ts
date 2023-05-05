import { Routes } from '@angular/router';
import { Util } from '@app/components/shared';
import { OverviewPageComponent } from './overview/_overview-page.component';
import { OverviewSidebarComponent } from './overview/overview-sidebar.component';

export const overviewRoutes: Routes = [
  Util.routePath('', OverviewPageComponent, OverviewSidebarComponent),
];
