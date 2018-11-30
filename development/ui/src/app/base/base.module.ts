import {NgModule} from '@angular/core';
import {BaseSidebarComponent} from "./base-sidebar.component";
import {LoginPageComponent} from "./pages/login/login-page.component";
import {LinksPageComponent} from "./pages/links/links-page.component";
import {HomePageComponent} from "./pages/home/home-page.component";
import {NotFoundPageComponent} from "./pages/not-found/not-found-page.component";
import {LogoutPageComponent} from "./pages/logout/logout-page.component";
import {GlossaryPageComponent} from "./pages/glossary/glossary-page.component";
import {AuthenticatePageComponent} from "./pages/authenticate/authenticate-page.component";
import {AboutPageComponent} from "./pages/about/about-page.component";
import {CardAnalysisComponent} from "./pages/home/card-analysis.component";
import {CardMapComponent} from "./pages/home/card-map.component";
import {KpnMaterialModule} from "../material/kpn-material.module";
import {CommonModule} from "@angular/common";
import {SharedModule} from "../shared/shared.module";
import {BaseRoutingModule} from "./base-routing.module";

@NgModule({
  imports: [
    CommonModule,
    KpnMaterialModule,
    SharedModule,
    BaseRoutingModule
  ],
  declarations: [
    BaseSidebarComponent,
    AboutPageComponent,
    AuthenticatePageComponent,
    GlossaryPageComponent,
    HomePageComponent,
    CardMapComponent,
    CardAnalysisComponent,
    LinksPageComponent,
    LoginPageComponent,
    LogoutPageComponent,
    NotFoundPageComponent
  ],
  entryComponents: []
})
export class BaseModule {
}
