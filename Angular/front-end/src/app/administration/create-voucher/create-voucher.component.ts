import { Component, ElementRef, ViewChild, ViewChildren, ViewEncapsulation } from '@angular/core';
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { faCalendar } from '@fortawesome/free-solid-svg-icons';
import { positiveNumberValidator } from "../../directive/positive.number.directive";
import { Observable } from "rxjs";
import { User } from "../../model/user";
import { UserService } from "../../service/user.service";
import { Category } from "../../model/category";
import { CategoryService } from "../../service/category.service";
import { Voucher } from "../../model/voucher";
import { Store } from "@ngrx/store";
import { createVoucher } from "../../store/action/auth.action";
import { NotifierService } from "angular-notifier";
import { NotificationType } from "../../enumeration/notification-enum";
import { INVALID_EXPIRATION_DATE } from "../../common/messages";

@Component({
  selector: 'app-create-voucher',
  templateUrl: './create-voucher.component.html',
  styleUrls: ['./create-voucher.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class CreateVoucherComponent {
  faCalendar = faCalendar;

  users$: Observable<User[]>;
  categories$: Observable<Category[]>;
  selectedUserFromPickup: User;
  selectedCategoryFromPickup: Category;

  @ViewChild('userSearchInput') userInput: ElementRef | undefined;
  @ViewChildren('userRow') suggestedUsers: ElementRef[] | undefined;
  @ViewChildren('categoryRow') suggestedCategories: ElementRef[] | undefined;

  constructor(private userService: UserService, private categoryService: CategoryService, private store: Store,
              private notifier: NotifierService) {
  }

  createVoucherFormGroup: FormGroup = new FormGroup({
    name: new FormControl('', [Validators.required,
      Validators.minLength(3), Validators.maxLength(20)]),
    type: new FormControl('ABSOLUTE', [Validators.required, Validators.minLength(8),
      Validators.maxLength(8)]),
    discount: new FormControl(0, [Validators.required, positiveNumberValidator()]),
    expirationDate: new FormControl(new Date(), Validators.required)
  })

  get today(): any {
    return new Date();
  }

  findUserByUsername() {
    const username = this.userInput.nativeElement.value;
    if (username.trim() === '') {
      return;
    }

    this.users$ = this.userService.findUserByUsernameLike(username);
  }

  selectUser(selectedUserRow: HTMLTableRowElement, user: User) {
    this.suggestedUsers.forEach(x => {
      x.nativeElement.closest('tr').classList.remove('selected-user');
    });

    selectedUserRow.classList.add('selected-user');
    this.selectedUserFromPickup = user;
  }

  selectCategory(selectedCategoryRow: HTMLTableRowElement, category: Category) {
    this.suggestedCategories.forEach(x => {
      x.nativeElement.closest('tr').classList.remove('selected-category');
    });

    selectedCategoryRow.classList.add('selected-category');
    this.selectedCategoryFromPickup = category;
  }

  submitUserSelection() {
    document.getElementById('user-modal-close-btn').click();
  }

  public findCategoryByName(event: Event): void {
    const categoryName = (<HTMLInputElement>event.target).value;
    if (categoryName.trim() === '') {
      return;
    }

    this.categories$ = this.categoryService.filterCategoriesByName(categoryName);
  }

  submitCategorySelection() {
    document.getElementById('category-modal-close-btn').click();
  }

  get name() {
    return this.createVoucherFormGroup.get('name');
  }

  get type() {
    return this.createVoucherFormGroup.get('type');
  }

  get discount() {
    return this.createVoucherFormGroup.get('discount');
  }

  get expirationDate() {
    return this.createVoucherFormGroup.get('expirationDate');
  }

  createVoucher() {
    if (this.expirationDate.value <= new Date().getTime()) {
      this.notifier.notify(NotificationType.INFO, INVALID_EXPIRATION_DATE);
      return;
    }

    const voucher: Voucher = {
      name: this.name.value,
      type: this.type.value,
      discount: this.discount.value,
      expirationDate: this.expirationDate.value,
      user: this.selectedUserFromPickup,
      category: this.selectedCategoryFromPickup
    };

    this.store.dispatch(createVoucher({ voucher }));
  }
}
