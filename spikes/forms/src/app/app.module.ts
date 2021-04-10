import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatRadioModule } from '@angular/material/radio';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MAT_SNACK_BAR_DEFAULT_OPTIONS } from '@angular/material/snack-bar';
import { MatTableModule } from '@angular/material/table';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';
import { StoreDevtoolsModule } from '@ngrx/store-devtools';
import { environment } from '../environments/environment';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { Page1Component } from './page1/page1.component';
import { Page2Component } from './page2/page2.component';
import { Page3Component } from './page3/page3.component';
import { Page4Component } from './page4/page4.component';
import { Sub1Component } from './page4/sub1.component';
import { Sub2Component } from './page4/sub2.component';
import { Sub3Component } from './page4/sub3.component';
import { Page5Component } from './page5/page5.component';
import { Sub5Component } from './page5/sub5.component';
import { MessageService } from './shared/message.service';
import { SharedModule } from './shared/shared.module';
import { reducers } from './store/app.state';

@NgModule({
  imports: [
    BrowserModule,
    SharedModule,
    ReactiveFormsModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatTableModule,
    MatSnackBarModule,
    MatRadioModule,
    MatCheckboxModule,

    StoreModule.forRoot(reducers, {
      runtimeChecks: {
        strictStateImmutability: true,
        strictActionImmutability: true,
        strictStateSerializability: false,
        strictActionSerializability: false,
        strictActionWithinNgZone: true,
        strictActionTypeUniqueness: true,
      },
    }),
    EffectsModule.forRoot([]),
    environment.production
      ? []
      : StoreDevtoolsModule.instrument({
          name: 'forms-spike',
        }),
  ],
  declarations: [
    AppComponent,
    Page1Component,
    Page2Component,
    Page3Component,
    Page4Component,
    Page5Component,
    Sub1Component,
    Sub2Component,
    Sub3Component,
    Sub5Component,
  ],
  providers: [
    MessageService,
    {
      provide: MAT_SNACK_BAR_DEFAULT_OPTIONS,
      useValue: {
        duration: 1500,
        verticalPosition: 'top',
        horizontalPosition: 'right',
      },
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
