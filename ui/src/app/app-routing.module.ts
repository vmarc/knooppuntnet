import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AboutPageComponent} from "./pages/about/_page/about-page.component";
import {AuthenticatePageComponent} from "./pages/authenticate/_page/authenticate-page.component";
import {NotFoundPageComponent} from "./pages/not-found/_page/not-found-page.component";
import {LogoutPageComponent} from "./pages/logout/_page/logout-page.component";
import {LinksPageComponent} from "./pages/links/_page/links-page.component";
import {HomePageComponent} from "./pages/home/_page/home-page.component";
import {LoginPageComponent} from "./pages/login/_page/login-page.component";
import {GlossaryPageComponent} from "./pages/glossary/_page/glossary-page.component";

export const routes: Routes = [
  {
    path: 'analysis',
    loadChildren: './analysis/analysis.module#AnalysisModule'
  },
  {
    path: 'about',
    component: AboutPageComponent
  },
  {
    path: 'authenticate',
    component: AuthenticatePageComponent
  },
  {
    path: 'glossary',
    component: GlossaryPageComponent
  },
  {
    path: 'home',
    component: HomePageComponent
  },
  {
    path: 'links',
    component: LinksPageComponent
  },
  {
    path: 'login',
    component: LoginPageComponent
  },
  {
    path: 'logout',
    component: LogoutPageComponent
  },
  {
    path: 'not-found',
    component: NotFoundPageComponent
  },
  {
    path: '**',
    redirectTo: '/not-found',
    pathMatch: 'full'
  }
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class AppRoutingModule {
}
