import { inject, NgModule } from '@angular/core';
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
import { OrderComponent } from './order/order.component';
import { VoucherComponent } from './voucher/voucher.component';
import { CreateVoucherComponent } from './create-voucher/create-voucher.component';
import { MatInputModule } from "@angular/material/input";
import { MatDatepickerModule } from "@angular/material/datepicker";
import { MatNativeDateModule } from "@angular/material/core";
import { DateFormatPipe } from "../pipes/date.format.pipe";

const routes: Routes = [
  {
    path: '', canActivateChild: [() => inject(AdminGuard).canActivateChild()], children: [
      { path: 'cockpit', title: 'eCart | Cockpit', component: CockpitComponent },
      { path: 'categories', title: 'eCart | Categories', component: CategoryComponent },
      { path: 'category/create', title: 'eCart | Create category', component: CreateCategoryComponent },
      { path: 'medias', title: 'eCart | Medias', component: MediaComponent },
      { path: 'products', title: 'eCart | Products', component: ProductComponent },
      { path: 'product/create', title: 'eCart | Create product', component: CreateProductComponent },
      { path: 'media/create', title: 'eCart | Create media', component: CreateMediaComponent },
      { path: 'categories/:identifier', title: 'eCart | Update Category', component: UpdateCategoryComponent },
      { path: 'users', title: 'eCart | Users', component: UserComponent },
      { path: 'vouchers', title: 'eCart | Vouchers', component: VoucherComponent },
      { path: 'voucher/create', title: 'eCart | Create voucher', component: CreateVoucherComponent },
      { path: 'orders', title: 'eCart | Orders', component: OrderComponent }
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
    UserComponent,
    OrderComponent,
    VoucherComponent,
    CreateVoucherComponent,
    DateFormatPipe
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
    MatInputModule,
    MatDatepickerModule,
    MatNativeDateModule,
    EffectsModule.forFeature(CategoryEffects, MediaEffects, ProductEffects)
  ],
  exports: [
    DateFormatPipe
  ],
  providers: [JwtHelperService]
})
export class AdministrationModule {
}
