import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HomeComponent} from './home/home.component';
import {StoreModule} from '@ngrx/store';
import {environment} from '../environments/environment';
import {StoreDevtoolsModule} from '@ngrx/store-devtools';
import {CounterComponent} from './counter/counter.component';
import {NewIssueComponent} from './issue/new-issue.component';
import {ReactiveFormsModule} from '@angular/forms';
import {IssueListComponent} from './issue/issue-list.component';
import {IssuesComponent} from './issue/issues.component';
import {IssueDetailComponent} from './issue/issue-detail.component';
import {reducers} from './store/app.state';
import {NodeComponent} from './node/node.component';
import {AppService} from './app.service';
import {HttpClientModule} from '@angular/common/http';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    CounterComponent,
    IssuesComponent,
    NewIssueComponent,
    IssueListComponent,
    IssueDetailComponent,
    NodeComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    StoreModule.forRoot(
      reducers,
      {
        runtimeChecks: {
          strictStateImmutability: true,
          strictActionImmutability: true,
          strictStateSerializability: true,
          strictActionSerializability: true,
          strictActionWithinNgZone: true,
          strictActionTypeUniqueness: true
        }
      }
    ),
    environment.production
      ? []
      : StoreDevtoolsModule.instrument({
        name: 'ngrx test'
      }),
    StoreDevtoolsModule.instrument({maxAge: 25, logOnly: environment.production}),
    ReactiveFormsModule,
  ],
  providers: [
    AppService
  ],
  bootstrap: [
    AppComponent
  ]
})
export class AppModule {
}
