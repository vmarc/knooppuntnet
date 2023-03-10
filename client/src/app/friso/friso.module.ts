import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MatDialogModule } from '@angular/material/dialog';
import { FrisoNodeDialogComponent } from '@app/friso/friso/friso-node-dialog.component';
import { SharedModule } from '../components/shared/shared.module';
import { FrisoPageComponent } from '@app/friso/friso/friso-page.component';
import { FrisoSidebarComponent } from '@app/friso/friso/friso-sidebar.component';
import { FrisoRoutingModule } from '@app/friso/friso-routing.module';
import { MatRadioModule } from '@angular/material/radio';
import { MatIconModule } from '@angular/material/icon';
import { OlModule } from '@app/components/ol/ol.module';
import { StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';
import { FrisoEffects } from '@app/friso/store/friso.effects';
import { frisoReducer } from '@app/friso/store/friso.reducer';
import { frisoFeatureKey } from '@app/friso/store/friso.state';

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
  ],
})
export class FrisoModule {}
