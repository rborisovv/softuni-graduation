import {Component, OnInit} from '@angular/core';
import {CategoryService} from "../../service/category.service";
import {Observable} from "rxjs";
import {Category} from 'src/app/interface/category';
import {faTrash, faWrench} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-category',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.scss']
})
export class CategoryComponent implements OnInit {

  faWrench = faWrench;
  faTrash = faTrash;

  categories$: Observable<Category[]>;

  constructor(private categoryService: CategoryService) {
  }

  ngOnInit(): void {
    this.categories$ = this.categoryService.loadAllCategories();
  }
}
