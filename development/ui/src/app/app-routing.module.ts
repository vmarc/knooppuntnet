import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AboutPageComponent} from "./pages/about/about-page.component";
import {AuthenticatePageComponent} from "./pages/authenticate/authenticate-page.component";
import {NotFoundPageComponent} from "./pages/not-found/not-found-page.component";
import {LogoutPageComponent} from "./pages/logout/logout-page.component";
import {LinksPageComponent} from "./pages/links/links-page.component";
import {HomePageComponent} from "./pages/home/home-page.component";
import {LoginPageComponent} from "./pages/login/login-page.component";
import {GlossaryPageComponent} from "./pages/glossary/glossary-page.component";

export const routes: Routes = [
  {
    path: 'analysis',
    loadChildren: './analysis/analysis.module#AnalysisModule'
  },
  {
    path: 'translations',
    loadChildren: './translations/translations.module#TranslationsModule'
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
