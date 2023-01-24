import {ChangeDetectionStrategy, Component, OnDestroy, OnInit} from '@angular/core';
import {CategoryService} from "../../service/category.service";
import {Observable, Subscription} from "rxjs";
import {Category} from 'src/app/interface/category';
import {faTrash, faWrench} from '@fortawesome/free-solid-svg-icons';
import {NotifierService} from "angular-notifier";
import {NotificationType} from "../../enumeration/notification-enum";
import {Router} from "@angular/router";

@Component({
  selector: 'app-categories',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CategoryComponent implements OnInit, OnDestroy {

  faWrench = faWrench;
  faTrash = faTrash;

  categories$: Observable<Category[]>;

  private subscriptions$: Subscription[] = [];

  constructor(private categoryService: CategoryService, private notifier: NotifierService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.categories$ = this.categoryService.loadAllCategories();
  }

  public deleteCategory(identifier: string) {
    const subscription = this.categoryService.deleteCategory(identifier)
      .subscribe({
        next: (response) => {
          this.router.navigateByUrl('/admin/cockpit').then(() => {
            this.notifier.notify(NotificationType.SUCCESS, response.message);
          });
        }
      });

    this.subscriptions$.push(subscription);
  }

  ngOnDestroy(): void {
    this.subscriptions$.forEach(x => x.unsubscribe());
  }
}
