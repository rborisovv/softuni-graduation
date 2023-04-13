import { Component, OnInit } from '@angular/core';
import { faHeart } from "@fortawesome/free-regular-svg-icons";
import { Observable } from "rxjs";
import { Category } from "../../model/category";
import { selectBasketProductsState, selectFavouriteProductsState } from "../../store/selector/user.selector";
import { Store } from "@ngrx/store";
import { Product } from "../../model/product";
import { addToBasket, addToFavourites } from 'src/app/store/action/user.action';
import { CategoryService } from "../../service/category.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  category$: Observable<Category>;
  favouriteProducts$: Observable<Product[]>;

  basketProducts$: Observable<Product[]>;

  constructor(private store: Store, private categoryService: CategoryService) {
  }

  ngOnInit(): void {
    const categoryIdentifier = '0001';
    this.category$ = this.categoryService.loadCategoryWithBreadcrumb(categoryIdentifier);
  }

  public addToFavourites(identifier: string) {
    this.store.dispatch(addToFavourites({ identifier }));
    this.favouriteProducts$ = this.store.select(selectFavouriteProductsState);
  }

  public addToBasket(identifier: string) {
    this.store.dispatch(addToBasket({ identifier }));
    this.basketProducts$ = this.store.select(selectBasketProductsState);
  }

  protected faHeart = faHeart;
}
