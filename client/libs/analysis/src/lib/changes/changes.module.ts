import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { AnalysisStrategyModule } from '@app/analysis/strategy';
import { SharedModule } from '@app/components/shared';
import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';
import { AnalysisComponentsModule } from '../components';
import { ChangesService } from '../components/changes/filter';
import { ChangesRoutingModule } from './changes-routing.module';
import { ChangesPageComponent } from './page/_changes-page.component';
import { ChangesSidebarComponent } from './sidebar/changes-sidebar.component';
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
    AnalysisStrategyModule,
    ChangesPageComponent,
    ChangesSidebarComponent,
  ],
  providers: [ChangesService],
})
export class ChangesModule {}
