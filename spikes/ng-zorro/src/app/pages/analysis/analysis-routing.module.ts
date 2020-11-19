import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {FactsComponent} from './facts.component';
import {RoutesComponent} from './routes.component';
import {NodesComponent} from './nodes.component';
import {AnalysisComponent} from './analysis.component';

const routes: Routes = [
  {path: '', component: AnalysisComponent},
  {path: 'nodes', component: NodesComponent},
  {path: 'routes', component: RoutesComponent},
  {path: 'facts', component: FactsComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AnalysisRoutingModule {
}
