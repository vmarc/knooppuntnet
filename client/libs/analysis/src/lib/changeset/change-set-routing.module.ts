import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Routes } from '@angular/router';
import { Util } from '@app/components/shared';
import { AnalysisSidebarComponent } from '@app/components/shared/sidebar';
import { ChangeSetPageComponent } from './page/_change-set-page.component';

const routes: Routes = [
  Util.routePath(
    ':changeSetId/:replicationNumber',
    ChangeSetPageComponent,
    AnalysisSidebarComponent
  ),
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ChangeSetRoutingModule {}
