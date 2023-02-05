import {Component, OnInit} from '@angular/core';
import {CategoryService} from "../../service/category.service";
import {Observable} from "rxjs";
import {Category} from 'src/app/interface/category';
import {faTrash, faWrench} from '@fortawesome/free-solid-svg-icons';
import {Store} from "@ngrx/store";
import {deleteCategoryAction} from "../../store/action/category.action";

@Component({
  selector: 'app-categories',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.scss']
})
export class CategoryComponent implements OnInit {

  faWrench = faWrench;
  faTrash = faTrash;

  categories$: Observable<Category[]>;

  constructor(private categoryService: CategoryService, private readonly store: Store) {
  }

  ngOnInit(): void {
    this.categories$ = this.categoryService.loadAllCategories();
  }

  public deleteCategory(identifier: string) {
    this.store.dispatch(deleteCategoryAction({identifier}));
    //TODO: Fix deleting category does not detect the change
  }
}
