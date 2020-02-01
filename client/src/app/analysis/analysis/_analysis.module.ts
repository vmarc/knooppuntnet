import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {ReactiveFormsModule} from "@angular/forms";
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import {MatButtonModule} from "@angular/material/button";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatIconModule} from "@angular/material/icon";
import {MatInputModule} from "@angular/material/input";
import {MatRadioModule} from "@angular/material/radio";
import {MatTreeModule} from "@angular/material/tree";
import {SharedModule} from "../../components/shared/shared.module";
import {AnalysisRoutingModule} from "./_analysis-routing.module";
import {AnalysisCanoePageComponent} from "./analysis-canoe-page.component";
import {AnalysisCyclingPageComponent} from "./analysis-cycling-page.component";
import {AnalysisHikingPageComponent} from "./analysis-hiking-page.component";
import {AnalysisHorseRidingPageComponent} from "./analysis-horse-riding-page.component";
import {AnalysisInlineSkatingPageComponent} from "./analysis-inline-skating-page.component";
import {AnalysisMotorboatPageComponent} from "./analysis-motorboat-page.component";
import {AnalysisPageComponent} from "./analysis-page.component";
import {LocationNodesPageComponent} from "./location-nodes-page.component";
import {LocationSelectionPageComponent} from "./location-selection-page.component";
import {LocationSelectorComponent} from "./location-selector.component";
import {LocationTreeComponent} from "./location-tree.component";
import {AnalysisModeComponent} from "./analysis-mode.component";
import {AnalysisModeService} from "./analysis-mode.service";
import { LocationModeComponent } from './location-mode.component';
import {LocationModeService} from "./location-mode.service";

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    AnalysisRoutingModule,
    MatTreeModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatAutocompleteModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatRadioModule
  ],
  declarations: [
    AnalysisPageComponent,
    LocationTreeComponent,
    LocationSelectorComponent,
    AnalysisCyclingPageComponent,
    AnalysisHikingPageComponent,
    AnalysisInlineSkatingPageComponent,
    AnalysisMotorboatPageComponent,
    AnalysisHorseRidingPageComponent,
    AnalysisCanoePageComponent,
    LocationSelectionPageComponent,
    LocationNodesPageComponent,
    AnalysisModeComponent,
    LocationModeComponent
  ],
  providers: [
    AnalysisModeService,
    LocationModeService
  ]
})
export class AnalysisModule {
}
