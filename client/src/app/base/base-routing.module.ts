import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BaseSidebarComponent } from './base-sidebar.component';
import { AuthenticatePageComponent } from './pages/authenticate/authenticate-page.component';
import { HomePageComponent } from './pages/home/home-page.component';
import { LoginPageComponent } from './pages/login/login-page.component';
import { LogoutPageComponent } from './pages/logout/logout-page.component';
import { NotFoundPageComponent } from './pages/not-found/not-found-page.component';

const routes: Routes = [
  {
    path: '',
    component: HomePageComponent,
  },
  {
    path: '',
    component: BaseSidebarComponent,
    outlet: 'sidebar',
  },
  {
    path: 'authenticate',
    component: AuthenticatePageComponent,
  },
  {
    path: 'login',
    component: LoginPageComponent,
  },
  {
    path: 'logout',
    component: LogoutPageComponent,
  },
  {
    path: 'not-found',
    component: NotFoundPageComponent,
  },
  {
    path: '**',
    redirectTo: '',
    pathMatch: 'full',
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class BaseRoutingModule {}
