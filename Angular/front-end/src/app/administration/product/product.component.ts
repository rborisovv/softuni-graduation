import {Component, OnInit} from '@angular/core';
import {ProductService} from "../../service/product.service";
import {catchError, Observable} from "rxjs";
import {Product} from "../../interface/product";
import {faTrash, faWrench} from '@fortawesome/free-solid-svg-icons';
import {NotifierService} from "angular-notifier";
import {NotificationType} from "../../enumeration/notification-enum";
import {Router} from "@angular/router";

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.scss']
})
export class ProductComponent implements OnInit {
  faWrench = faWrench;
  faTrash = faTrash;

  products$: Observable<Product[]>;

  constructor(private readonly productService: ProductService,
              private readonly notifier: NotifierService,
              private readonly router: Router) {
  }

  ngOnInit(): void {
    this.products$ = this.productService.loadAllProducts();
  }

  public deleteProduct(identifier: string): void {
    this.productService.deleteProduct(identifier)
      .pipe(catchError((err) => {
        this.notifier.notify(NotificationType.ERROR, err.err.message);
        throw err;
      }))
      .subscribe({
        next: (response) => {
          this.router.navigateByUrl('/admin/cockpit').then(() => {
            this.notifier.notify(NotificationType.SUCCESS, response.message);
          });
        }
      });
  }
}
