import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {Observable, take} from "rxjs";
import {Category} from 'src/app/interface/category';
import {CategoryService} from "../../service/category.service";
import {ActivatedRoute, ParamMap, Router} from "@angular/router";
import {faHeart} from '@fortawesome/free-regular-svg-icons';
import {Store} from "@ngrx/store";
import {addToFavourites} from "../../store/action/user.action";
import {UserService} from "../../service/user.service";

@Component({
  selector: 'app-category',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CategoryComponent implements OnInit {
  category$: Observable<Category>;

  faHeart = faHeart;

  constructor(private readonly categoryService: CategoryService, private router: Router, private route: ActivatedRoute,
              private readonly store: Store) {
  }

  ngOnInit(): void {
    let identifier = '';

    this.route.paramMap.pipe(take(1)).subscribe((params: ParamMap) => {
      identifier = params.get('category');
    });

    this.route.paramMap.subscribe(
      (params: ParamMap) => {
        const category = params.get('category');
        this.category$ = this.categoryService.loadCategoryWithBreadcrumb(category);
      });
  }

  public addToFavourites(identifier: string) {
    this.store.dispatch(addToFavourites({identifier}));
  }
}