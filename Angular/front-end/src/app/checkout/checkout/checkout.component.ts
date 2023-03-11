import { Component, OnInit } from '@angular/core';
import { Observable, take } from "rxjs";
import { Product } from "../../interface/product";
import { Store } from "@ngrx/store";
import { Router } from "@angular/router";
import { Location } from "@angular/common";
import { UserService } from "../../service/user.service";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { Checkout } from "../../interface/checkout";
import { submitCheckoutFlow } from "../../store/action/user.action";

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.scss']
})
export class CheckoutComponent implements OnInit {
  renewedBasketProducts$: Observable<Product[]>;

  checkoutFormGroup: FormGroup = new FormGroup({
    firstName: new FormControl('', [Validators.required, Validators.minLength(3),
      Validators.maxLength(30)]),
    lastName: new FormControl('', [Validators.required,
      Validators.minLength(3), Validators.maxLength(30)]),
    email: new FormControl('', [Validators.required, Validators.email]),
    city: new FormControl('', [Validators.required, Validators.minLength(3), Validators.maxLength(20)]),
    address: new FormControl('', [Validators.required, Validators.minLength(5), Validators.maxLength(150)]),
  });

  constructor(public readonly router: Router, protected readonly location: Location, private readonly store: Store,
              private readonly userService: UserService) {
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
