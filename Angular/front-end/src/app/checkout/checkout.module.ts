import { inject, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CartComponent } from './cart/cart.component';
import { RouterModule, Routes } from "@angular/router";
import { PageGuard } from "../guard/page.guard";
import { SharedModule } from "../shared/shared.module";
import { CartBalancePipe } from "../pipes/cart.balance.pipe";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { CheckoutComponent } from './checkout/checkout.component';
import { ReactiveFormsModule } from "@angular/forms";
import { FinalizeComponent } from './finalize/finalize.component';
import { PaymentSuccessfulComponent } from './payment-successful/payment-successful.component';
import { CheckoutGuard } from "../guard/checkout.guard";
import { OrderFlowGuard } from "../guard/order.flow.guard";
import { CheckoutHeaderComponent } from './checkout-header/checkout-header.component';
import { BasketDiscountedTotalPipe } from "../pipes/basket.discounted.total.pipe";
import { BasketService } from "../service/basket.service";

const routes: Routes = [
  {
    path: '',
    canActivate: [() => inject(PageGuard).canActivate()],
    children: [
      {
        path: 'cart',
        component: CartComponent,
        title: 'eCart | Basket',
      },
      {
        path: 'checkout',
        component: CheckoutComponent,
        title: 'eCart | Checkout',
        canActivate: [() => inject(CheckoutGuard).canActivate()]
      },
      {
        path: 'finalize-order',
        component: FinalizeComponent,
        title: 'eCart | Finalize order',
        canActivate: [() => inject(OrderFlowGuard).canActivate()]
      },
      {
        path: 'order-created',
        title: 'eCart | Order created',
        component: PaymentSuccessfulComponent
      }
    ]
  }
];

@NgModule({
  declarations: [
    CartComponent,
    CartBalancePipe,
    CheckoutComponent,
    FinalizeComponent,
    PaymentSuccessfulComponent,
    CheckoutHeaderComponent,
    BasketDiscountedTotalPipe
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    SharedModule,
    FontAwesomeModule,
    ReactiveFormsModule
  ],
  providers: [CartBalancePipe, BasketDiscountedTotalPipe, CheckoutGuard, OrderFlowGuard, BasketService]
})
export class CheckoutModule {
}
