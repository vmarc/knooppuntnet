import {LayoutModule} from '@angular/cdk/layout';
import {HttpClientModule} from '@angular/common/http';
import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {CookieService} from "ngx-cookie-service";
import {MarkdownModule} from "ngx-markdown";
import {AppRoutingModule} from './app-routing.module';

import {AppComponent} from './app.component';
import {AppService} from './app.service';
import {PageService} from "./components/shared/page.service";
import {SharedModule} from "./components/shared/shared.module";
import {IconService} from "./icon.service";
import {KpnMaterialModule} from "./material/kpn-material.module";
import {PoiService} from "./poi.service";
import {UserService} from "./user.service";

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    MarkdownModule.forRoot(),
    BrowserModule,
    BrowserAnimationsModule,
    LayoutModule,
    KpnMaterialModule,
    HttpClientModule,
    SharedModule,
    AppRoutingModule
  ],
  providers: [
    CookieService,
    UserService,
    AppService,
    PageService,
    IconService,
    PoiService
  ],
  bootstrap: [
    AppComponent
  ]
})
export class AppModule {
}
