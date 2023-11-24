import { Routes } from '@angular/router';
import { AuthenticatedComponent } from "./authenticated.component";
import { HomeComponent } from "./home.component";

export const routes: Routes = [
  {
    path: 'authenticated',
    component: AuthenticatedComponent,
  },
  {
    path: '',
    component: HomeComponent,
  },
];
