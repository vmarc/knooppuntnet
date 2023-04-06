import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';
import { SharedModule } from '@app/components/shared/shared.module';
import { AnalysisComponentsModule } from '../components/analysis-components.module';
import { ChangesService } from '../components/changes/filter/changes.service';
import { ChangesRoutingModule } from './changes-routing.module';
import { ChangesPageComponent } from './page/_changes-page.component';
import { ChangesEffects } from './store/changes.effects';
import { changesReducer } from './store/changes.reducer';
import { changesFeatureKey } from './store/changes.state';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    AnalysisComponentsModule,
    ChangesRoutingModule,
    StoreModule.forFeature(changesFeatureKey, changesReducer),
    EffectsModule.forFeature([ChangesEffects]),
  ],
  declarations: [ChangesPageComponent],
  providers: [ChangesService],
})
export class ChangesModule {}
