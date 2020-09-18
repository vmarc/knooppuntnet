import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {SharedModule} from "../components/shared/shared.module";
import {SettingsRoutingModule} from "./settings-routing.module";
import {SettingsPageComponent} from "./settings-page.component";
import {SettingsSidebarComponent} from "./settings-sidebar.component";
import {MatSlideToggleModule} from "@angular/material/slide-toggle";

@NgModule({
  declarations: [
    SettingsPageComponent,
    SettingsSidebarComponent,
  ],
  imports: [
    CommonModule,
    SettingsRoutingModule,
    SharedModule,
    MatSlideToggleModule
  ]
})
export class SettingsModule {
}
