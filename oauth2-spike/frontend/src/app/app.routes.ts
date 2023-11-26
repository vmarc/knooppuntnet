import { Routes } from '@angular/router';
import { AuthenticatedComponent } from "./pages/authenticated.component";
import { HomeComponent } from "./pages/home.component";
import { LoginComponent } from "./pages/login.component";
import { Page1Component } from "./pages/page1.component";
import { Page2Component } from "./pages/page2.component";
import { Page3Component } from "./pages/page3.component";

export const routes: Routes = [
  {
    path: 'page1',
    component: Page1Component,
  },
  {
    path: 'page2',
    component: Page2Component,
  },
  {
    path: 'page3',
    component: Page3Component,
  },
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: 'authenticated',
    component: AuthenticatedComponent,
  },
  {
    path: '',
    component: HomeComponent,
  },
];
