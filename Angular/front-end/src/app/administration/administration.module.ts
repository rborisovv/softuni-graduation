import {NgModule} from '@angular/core';
import {CockpitComponent} from './cockpit/cockpit.component';
import {SharedModule} from "../shared/shared.module";
import {RouterModule, Routes} from "@angular/router";
import {AdminHeaderComponent} from "./admin-header/admin-header.component";
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";

const routes: Routes = [
  {
    path: '', children: [
      {path: 'cockpit', component: CockpitComponent}
    ]
  }
]

@NgModule({
  declarations: [
    CockpitComponent,
    AdminHeaderComponent
  ],
  imports: [
    SharedModule,
    SharedModule,
    FontAwesomeModule,
    RouterModule.forChild(routes)
  ]
})
export class AdministrationModule {
}
