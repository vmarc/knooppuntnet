import { Routes } from '@angular/router';
import { Util } from '@app/components/shared';
import { AnalysisSidebarComponent } from '@app/components/shared/sidebar';
import { ChangeSetPageComponent } from './page/_change-set-page.component';

export const changeSetRoutes: Routes = [
  Util.routePath(
    ':changeSetId/:replicationNumber',
    ChangeSetPageComponent,
    AnalysisSidebarComponent
  ),
];
