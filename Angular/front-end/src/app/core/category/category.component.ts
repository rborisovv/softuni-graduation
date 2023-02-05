import {Component, OnInit} from '@angular/core';
import {take} from "rxjs";
import {Category} from 'src/app/interface/category';
import {CategoryService} from "../../service/category.service";
import {ActivatedRoute, ParamMap, Router} from "@angular/router";

@Component({
  selector: 'app-category',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.scss']
})
export class CategoryComponent implements OnInit {
  category: Category;
  breadcrumb: object;

  constructor(private readonly categoryService: CategoryService, private router: Router, private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    let identifier = '';

    this.route.paramMap.pipe(take(1)).subscribe((params: ParamMap) => {
      identifier = params.get('category');
    });

    this.route.paramMap.subscribe(
      (params: ParamMap) => {
        const category = params.get('category');

        this.categoryService.loadCategoryWithBreadcrumb(category).pipe(take(1))
          .subscribe({
            next: (category) => {
              this.category = category;
              this.breadcrumb = category.breadcrumb;
            }
          });
      });


    this.categoryService.loadCategoryWithBreadcrumb(identifier).pipe(take(1))
      .subscribe({
        next: (category) => {
          this.category = category;
          this.breadcrumb = category.breadcrumb;
        }
      });
  }
}
