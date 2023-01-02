import {NgModule} from '@angular/core';
import {CockpitComponent} from './cockpit/cockpit.component';
import {SharedModule} from "../shared/shared.module";
import {RouterModule, Routes} from "@angular/router";
import {AdminHeaderComponent} from "./admin-header/admin-header.component";
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {CreateProductComponent} from './create-product/create-product.component';
import {JwtHelperService} from "@auth0/angular-jwt";
import {AdminGuard} from "./admin.guard";
import { CreateCategoryComponent } from './create-category/create-category.component';
import {FormsModule} from "@angular/forms";
import {AsyncPipe, CommonModule} from "@angular/common";

const routes: Routes = [
  {
    path: '', canActivateChild: [AdminGuard], children: [
      {path: 'cockpit', component: CockpitComponent},
      {path: 'create-category', component: CreateCategoryComponent}
    ]
  }
]

@NgModule({
  declarations: [
    CockpitComponent,
    AdminHeaderComponent,
    CreateProductComponent,
    CreateCategoryComponent
  ],
  imports: [
    SharedModule,
    SharedModule,
    FontAwesomeModule,
    RouterModule.forChild(routes),
    FormsModule,
    AsyncPipe,
    CommonModule
  ],
  providers: [JwtHelperService]
})
export class AdministrationModule {
}
