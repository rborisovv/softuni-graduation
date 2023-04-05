import { Component, OnInit } from '@angular/core';
import { CategoryService } from "../../service/category.service";
import { Observable, take } from "rxjs";
import { faTrash, faWrench } from '@fortawesome/free-solid-svg-icons';
import { Store } from "@ngrx/store";
import { deleteCategoryAction } from "../../store/action/category.action";
import { PageableData } from "../../model/pageable.data";
import { CategoryPageable } from "../../model/category.pageable";
import { PageEvent } from "@angular/material/paginator";

@Component({
  selector: 'app-categories',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.scss']
})
export class CategoryComponent implements OnInit {

  faWrench = faWrench;
  faTrash = faTrash;

  categories$: Observable<CategoryPageable>;
  pageIndex: number = 0;
  pageSize: number = 10;

  constructor(private categoryService: CategoryService, private store: Store) {
  }

  ngOnInit(): void {
    const data: PageableData = {
      pageIndex: 0,
      pageSize: 10
    };

    this.categories$ = this.categoryService.loadAllCategories(data).pipe(take(1));
  }

  public deleteCategory(identifier: string) {
    this.store.dispatch(deleteCategoryAction({ identifier }));
    //TODO: Fix deleting category does not detect the change
  }

  onPaginationChange(event: PageEvent) {
    const data: PageableData = {
      pageIndex: event.pageIndex,
      pageSize: event.pageSize
    };

    this.pageSize = event.pageSize;
    this.pageIndex = event.pageIndex;
    this.categories$ = this.categoryService.loadAllCategories(data).pipe(take(1));
  }
}
