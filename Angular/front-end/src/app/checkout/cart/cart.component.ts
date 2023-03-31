import { AfterViewInit, Component, ElementRef, OnInit, Renderer2, ViewChild } from '@angular/core';
import { UserService } from "../../service/user.service";
import { Observable, take, tap } from "rxjs";
import { Product } from "../../model/product";
import { Location } from "@angular/common";
import { Store } from "@ngrx/store";
import { removeFromBasket, updateBasketProductQuantity } from "../../store/action/user.action";
import { selectBasketProductsState } from "../../store/selector/user.selector";
import { Router } from "@angular/router";
import { BasketService } from "../../service/basket.service";
import { Voucher } from "../../model/voucher";
import { NotifierService } from "angular-notifier";
import { NotificationType } from "../../enumeration/notification-enum";

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']
})
export class CartComponent implements OnInit, AfterViewInit {
  renewedBasketProducts$: Observable<Product[]>;
  voucher$: Observable<Voucher | null>;

  totalAfterDiscount: Observable<number>;

  @ViewChild('totalPrice') totalPrice: ElementRef | undefined;
  @ViewChild('voucherInput') voucherInput: ElementRef;
  @ViewChild('voucherLabel') voucherLabel: ElementRef;

  constructor(private userService: UserService, protected location: Location,
              private store: Store, public router: Router, private basketService: BasketService,
              private renderer: Renderer2, private notifier: NotifierService) {
  }

  ngOnInit(): void {
    this.renewedBasketProducts$ = this.userService.loadBasketProducts();
  }

  removeProductFromBasket(identifier: string) {
    this.store.dispatch(removeFromBasket({ identifier }));
    this.renewedBasketProducts$ = this.store.select(selectBasketProductsState);
  }

  changeQuantityForProduct(identifier: string, event: Event) {
    const quantity = +(<HTMLSelectElement>event.target).value;
    this.store.dispatch(updateBasketProductQuantity({ identifier: identifier, quantity: quantity }));
    this.renewedBasketProducts$ = this.store.select(selectBasketProductsState);
  }

  addVoucher(labelElement: HTMLInputElement) {
    const voucher: string = labelElement.value;
    this.voucher$ = this.basketService.addVoucherToBasket(voucher).pipe(take(1));
    this.notifier.notify(NotificationType.SUCCESS, `The voucher ${voucher} has been used successfully!`);
    this.renderer.setStyle(this.voucherLabel.nativeElement, 'bottom', '100%');
  }

  ngAfterViewInit(): void {
    this.voucher$ = this.basketService.fetchVoucherIfPresent().pipe(tap((v) => {
      if (v) {
        this.voucherInput.nativeElement.value = v.name;
        this.renderer.setStyle(this.voucherLabel.nativeElement, 'bottom', '100%');
      }
    }));
  }
}
