import { Component, OnInit } from '@angular/core';
import { map, Observable, of, take } from "rxjs";
import { Product } from "../../model/product";
import { Store } from "@ngrx/store";
import { Router } from "@angular/router";
import { Location } from "@angular/common";
import { UserService } from "../../service/user.service";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { Checkout } from "../../model/checkout";
import { submitCheckoutFlow } from "../../store/action/user.action";
import { selectDiscountedTotalState } from "../../store/selector/basket.selector";
import { Voucher } from "../../model/voucher";
import { BasketService } from "../../service/basket.service";

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.scss']
})
export class CheckoutComponent implements OnInit {
  renewedBasketProducts$: Observable<Product[]>;
  discountedBasketTotal$: Observable<string> = of(undefined);
  voucher$: Observable<Voucher> = of(undefined);

  checkoutFormGroup: FormGroup = new FormGroup({
    firstName: new FormControl('', [Validators.required, Validators.minLength(3),
      Validators.maxLength(30)]),
    lastName: new FormControl('', [Validators.required,
      Validators.minLength(3), Validators.maxLength(30)]),
    email: new FormControl('', [Validators.required, Validators.email]),
    city: new FormControl('', [Validators.required, Validators.minLength(3), Validators.maxLength(20)]),
    address: new FormControl('', [Validators.required, Validators.minLength(5), Validators.maxLength(150)]),
  });

  constructor(public router: Router, protected location: Location, private store: Store,
              private userService: UserService, private basketService: BasketService) {
  }

  ngOnInit(): void {
    this.renewedBasketProducts$ = this.userService.loadBasketProducts();
    this.userService.fetchCheckoutDataIfPresent().pipe(take(1))
      .subscribe(data => {
        this.checkoutFormGroup.patchValue({
          firstName: data.firstName,
          lastName: data.lastName,
          email: data.email,
          city: data.city,
          address: data.address
        })
      });

    this.voucher$ = this.basketService.fetchVoucherIfPresent();

    this.discountedBasketTotal$ = this.store.select(selectDiscountedTotalState).pipe(
      map(state => state ? (state.total <= 0 ? 'Free' : (state.total.toFixed(2) + ' lv.')) : undefined)
    );
  }

  submitCheckout() {
    if (!this.firstName.value || !this.lastName.value || !this.email.value || !this.city.value || !this.address.value) {
      return;
    }

    const checkout: Checkout = {
      firstName: this.firstName.value,
      lastName: this.lastName.value,
      email: this.email.value,
      city: this.city.value,
      address: this.address.value
    };

    this.store.dispatch(submitCheckoutFlow({ checkout }));
  }

  get firstName() {
    return this.checkoutFormGroup.get('firstName');
  }

  get lastName() {
    return this.checkoutFormGroup.get('lastName');
  }

  get email() {
    return this.checkoutFormGroup.get('email');
  }

  get city() {
    return this.checkoutFormGroup.get('city');
  }

  get address() {
    return this.checkoutFormGroup.get('address');
  }
}
