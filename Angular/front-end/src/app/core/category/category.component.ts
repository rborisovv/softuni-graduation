import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { Observable, take, tap } from "rxjs";
import { Category } from 'src/app/model/category';
import { CategoryService } from "../../service/category.service";
import { ActivatedRoute, ParamMap, Router } from "@angular/router";
import { faHeart } from '@fortawesome/free-regular-svg-icons';
import { Store } from "@ngrx/store";
import { addToBasket, addToFavourites } from "../../store/action/user.action";
import {
  selectBasketProductsState,
  selectFavouriteProductsState
} from "../../store/selector/user.selector";
import { Product } from "../../model/product";
import { Title } from "@angular/platform-browser";

@Component({
  selector: 'app-category',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CategoryComponent implements OnInit {
  category$: Observable<Category>;

  favouriteProducts$: Observable<Product[]>;

  basketProducts$: Observable<Product[]>;

  faHeart = faHeart;

  constructor(private categoryService: CategoryService, private router: Router, private route: ActivatedRoute,
              private store: Store, private titleService: Title) {
  }

  ngOnInit(): void {
    let identifier = '';

    this.route.paramMap.pipe(take(1)).subscribe((params: ParamMap) => {
      identifier = params.get('category');
    });

    this.route.paramMap.subscribe(
      (params: ParamMap) => {
        const category = params.get('category');
        this.category$ = this.categoryService.loadCategoryWithBreadcrumb(category)
          .pipe(tap((category) => {
            this.titleService.setTitle("eCart | " + category.name);
          }));
      });
  }

  public addToFavourites(identifier: string) {
    this.store.dispatch(addToFavourites({ identifier }));
    this.favouriteProducts$ = this.store.select(selectFavouriteProductsState);
  }

  public addToBasket(identifier: string) {
    this.store.dispatch(addToBasket({ identifier }));
    this.basketProducts$ = this.store.select(selectBasketProductsState);
  }
}
