import {Component, OnInit} from '@angular/core';
import {ProductService} from "../../service/product.service";
import {Observable} from "rxjs";
import {Product} from "../../interface/product";
import {faTrash, faWrench} from '@fortawesome/free-solid-svg-icons';
import {Store} from "@ngrx/store";
import {deleteProductAction} from "../../store/action/product.action";

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
              private readonly store: Store) {
  }

  ngOnInit(): void {
    this.products$ = this.productService.loadAllProducts();
  }

  public deleteProduct(identifier: string): void {
    this.store.dispatch(deleteProductAction({identifier: identifier}));
  }
}
