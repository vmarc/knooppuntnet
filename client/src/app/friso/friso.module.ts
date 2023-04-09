import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatRadioModule } from '@angular/material/radio';
import { OlModule } from '@app/components/ol/ol.module';
import { FrisoRoutingModule } from '@app/friso/friso-routing.module';
import { FrisoMapComponent } from '@app/friso/friso/friso-map.component';
import { FrisoNodeDialogComponent } from '@app/friso/friso/friso-node-dialog.component';
import { FrisoPageComponent } from '@app/friso/friso/friso-page.component';
import { FrisoSidebarComponent } from '@app/friso/friso/friso-sidebar.component';
import { FrisoEffects } from '@app/friso/store/friso.effects';
import { frisoReducer } from '@app/friso/store/friso.reducer';
import { frisoFeatureKey } from '@app/friso/store/friso.state';
import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';
import { SharedModule } from '../components/shared/shared.module';
import { FrisoMapService } from './friso/friso-map.service';

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
  ],
  declarations: [
    FrisoPageComponent,
    FrisoSidebarComponent,
    FrisoNodeDialogComponent,
    FrisoMapComponent,
  ],
  providers: [FrisoMapService],
})
export class FrisoModule {}
