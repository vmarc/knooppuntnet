import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {IconsProviderModule} from './icons-provider.module';
import {NzLayoutModule} from 'ng-zorro-antd/layout';
import {NzMenuModule} from 'ng-zorro-antd/menu';
import {FormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {NZ_I18N} from 'ng-zorro-antd/i18n';
import {nl_BE} from 'ng-zorro-antd/i18n';
import {registerLocaleData} from '@angular/common';
import nl from '@angular/common/locales/nl';
import {SiderComponent} from './pages/components/sider.component';

registerLocaleData(nl);

@NgModule({
  declarations: [
    AppComponent,
    SiderComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    IconsProviderModule,
    NzLayoutModule,
    NzMenuModule,
    FormsModule,
    HttpClientModule,
    BrowserAnimationsModule
  ],
  providers: [{provide: NZ_I18N, useValue: nl_BE}],
  bootstrap: [AppComponent]
})
export class AppModule {
}
