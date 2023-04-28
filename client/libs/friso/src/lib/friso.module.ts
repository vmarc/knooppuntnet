import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatRadioModule } from '@angular/material/radio';
import { OlModule } from '@app/components/ol';
import { SharedModule } from '@app/components/shared';
import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';
import { FrisoRoutingModule } from './friso-routing.module';
import { FrisoMapComponent } from './friso/friso-map.component';
import { FrisoMapService } from './friso/friso-map.service';
import { FrisoNodeDialogComponent } from './friso/friso-node-dialog.component';
import { FrisoPageComponent } from './friso/friso-page.component';
import { FrisoSidebarComponent } from './friso/friso-sidebar.component';
import { FrisoEffects } from './store/friso.effects';
import { frisoReducer } from './store/friso.reducer';
import { frisoFeatureKey } from './store/friso.state';

@NgModule({
  imports: [
    CommonModule,
    StoreModule.forFeature(frisoFeatureKey, frisoReducer),
    EffectsModule.forFeature([FrisoEffects]),
    FrisoRoutingModule,
    SharedModule,
    MatRadioModule,
    MatIconModule,
    OlModule,
    MatDialogModule,
    FrisoPageComponent,
    FrisoSidebarComponent,
    FrisoNodeDialogComponent,
    FrisoMapComponent,
  ],
  providers: [FrisoMapService],
})
export class FrisoModule {}
