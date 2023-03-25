import {Component, OnInit} from '@angular/core';
import {ProductService} from "../../service/product.service";
import {Observable} from "rxjs";
import {faTrash, faWrench} from '@fortawesome/free-solid-svg-icons';
import {Store} from "@ngrx/store";
import {deleteProductAction} from "../../store/action/product.action";
import { ProductPageable } from "../../model/product.pageable";
import { PageEvent } from "@angular/material/paginator";
import { PageableData } from "../../model/pageable.data";

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.scss']
})
export class ProductComponent implements OnInit {
  faWrench = faWrench;
  faTrash = faTrash;

  products$: Observable<ProductPageable>;
  pageIndex: number = 0;
  pageSize: number = 10;

  constructor(private readonly productService: ProductService,
              private readonly store: Store) {
  }

  ngOnInit(): void {
    const data: PageableData = {
      pageIndex: 0,
      pageSize: 10
    };

    this.products$ = this.productService.loadAllProducts(data);
  }

  onPaginationChange(event: PageEvent) {
    const data: PageableData = {
      pageIndex: event.pageIndex,
      pageSize: event.pageSize
    };

    this.pageSize = event.pageSize;
    this.pageIndex = event.pageIndex;
    this.products$ = this.productService.loadAllProducts(data);
  }

  public deleteProduct(identifier: string): void {
    this.store.dispatch(deleteProductAction({identifier: identifier}));
  }
}
