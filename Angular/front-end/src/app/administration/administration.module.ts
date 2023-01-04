import {NgModule} from '@angular/core';
import {CockpitComponent} from './cockpit/cockpit.component';
import {SharedModule} from "../shared/shared.module";
import {RouterModule, Routes} from "@angular/router";
import {AdminHeaderComponent} from "./admin-header/admin-header.component";
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {CreateProductComponent} from './create-product/create-product.component';
import {JwtHelperService} from "@auth0/angular-jwt";
import {AdminGuard} from "./admin.guard";
import {CreateCategoryComponent} from './create-category/create-category.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {AsyncPipe, CommonModule} from "@angular/common";
import {UpdateCategoryComponent} from './update-category/update-category.component';
import {CategoryComponent} from './category/category.component';

const routes: Routes = [
  {
    path: '', canActivateChild: [AdminGuard], children: [
      {path: 'cockpit', component: CockpitComponent},
      {path: 'categories', component: CategoryComponent},
      {path: 'create-category', component: CreateCategoryComponent},
      {path: 'category/:identifier', component: UpdateCategoryComponent}
    ]
  }
]

@NgModule({
  declarations: [
    CockpitComponent,
    AdminHeaderComponent,
    CreateProductComponent,
    CreateCategoryComponent,
    UpdateCategoryComponent,
    CategoryComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    SharedModule,
    FontAwesomeModule,
    RouterModule.forChild(routes),
    FormsModule,
    AsyncPipe,
    ReactiveFormsModule
  ],
  providers: [JwtHelperService]
})
export class AdministrationModule {
}
