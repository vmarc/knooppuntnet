import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {HttpClient, HttpClientModule} from '@angular/common/http';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {AppComponent} from './app.component';
import {AppRoutingModule} from "./module/app-routing.module";
import {AngularMaterialModule} from "./module/angular-material.module";
import {TranslateLoader, TranslateModule} from "@ngx-translate/core";
import {TranslateHttpLoader} from "@ngx-translate/http-loader";
import {DragDropModule} from '@angular/cdk/drag-drop';
import {NgxSpinnerModule} from 'ngx-spinner';
import {ToastrModule} from 'ngx-toastr';
import {PdfResolver} from "./resolver/pdf.resolver";
import {
  ContainerComponent,
  ExportCompactComponent,
  ExportPdfComponent,
  ExportRouteComponent,
  FaqContainerComponent,
  HeaderContainerComponent,
  MapContainerComponent,
  MapRouteContainerComponent,
  PoiinformationComponent,
  RouteContainerComponent,
  RouteDetailsComponent,
  SelectionDetailsComponent
} from './container';


export function createTranslateLoader(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

@NgModule({
  declarations: [
    AppComponent,
    MapContainerComponent,
    RouteContainerComponent,
    HeaderContainerComponent,
    ContainerComponent,
    MapRouteContainerComponent,
    FaqContainerComponent,
    RouteDetailsComponent,
    PoiinformationComponent,
    SelectionDetailsComponent,
    ExportRouteComponent,
    ExportCompactComponent,
    ExportPdfComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    DragDropModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    NgxSpinnerModule,
    AngularMaterialModule,
    ToastrModule.forRoot({
      timeOut: 3000,
      positionClass: 'toast-top-right',
      preventDuplicates: true,
    }),
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: (createTranslateLoader),
        deps: [HttpClient]
      }
    })
  ],
  providers: [PdfResolver],
  bootstrap: [AppComponent]
})
export class AppModule {
}
