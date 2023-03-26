import { NgModule } from '@angular/core';
import { CockpitComponent } from './cockpit/cockpit.component';
import { SharedModule } from "../shared/shared.module";
import { RouterModule, Routes } from "@angular/router";
import { AdminHeaderComponent } from "./admin-header/admin-header.component";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { CreateProductComponent } from './create-product/create-product.component';
import { JwtHelperService } from "@auth0/angular-jwt";
import { AdminGuard } from "./admin.guard";
import { CreateCategoryComponent } from './create-category/create-category.component';
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { AsyncPipe, CommonModule, NgOptimizedImage } from "@angular/common";
import { UpdateCategoryComponent } from './update-category/update-category.component';
import { CategoryComponent } from './category/category.component';
import { CreateMediaComponent } from './create-media/create-media.component';
import { MediaComponent } from './media/media.component';
import { PositiveNumberDirective } from "../directive/positive.number.directive";
import { ProductComponent } from './product/product.component';
import { UpdateProductComponent } from './update-product/update-product.component';
import { EffectsModule } from "@ngrx/effects";
import { CategoryEffects } from "../store/effect/category.effect";
import { MediaEffects } from "../store/effect/media.effect";
import { ProductEffects } from "../store/effect/product.effect";
import { MatPaginatorModule } from "@angular/material/paginator";
import { UserComponent } from './user/user.component';

const routes: Routes = [
  {
    path: '', canActivateChild: [AdminGuard], children: [
      { path: 'cockpit', component: CockpitComponent },
      { path: 'categories', component: CategoryComponent },
      { path: 'category/create', component: CreateCategoryComponent },
      { path: 'medias', component: MediaComponent },
      { path: 'products', component: ProductComponent },
      { path: 'product/create', component: CreateProductComponent },
      { path: 'media/create', component: CreateMediaComponent },
      { path: 'categories/:identifier', component: UpdateCategoryComponent },
      { path: 'users', component: UserComponent }
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
    CategoryComponent,
    CreateMediaComponent,
    MediaComponent,
    PositiveNumberDirective,
    ProductComponent,
    UpdateProductComponent,
    UserComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    SharedModule,
    FontAwesomeModule,
    RouterModule.forChild(routes),
    FormsModule,
    AsyncPipe,
    ReactiveFormsModule,
    NgOptimizedImage,
    MatPaginatorModule,
    EffectsModule.forFeature(CategoryEffects, MediaEffects, ProductEffects)
  ],
  providers: [JwtHelperService]
})
export class AdministrationModule {
}
