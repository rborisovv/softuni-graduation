import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  ElementRef,
  OnDestroy,
  ViewChild,
  ViewChildren,
  ViewEncapsulation
} from '@angular/core';
import {ProductSharedFunctionality} from "../item.product.index";
import {Media} from "../../interface/media";
import {catchError, map, Observable, Subscription} from "rxjs";
import {MediaService} from "../../service/media.service";
import {AbstractControl, AsyncValidatorFn, FormControl, FormGroup, ValidationErrors, Validators} from "@angular/forms";
import {ProductService} from "../../service/product.service";
import {positiveNumberValidator} from "../../directive/positive.number.directive";
import {CategoryService} from "../../service/category.service";
import {Category} from "../../interface/category";
import {Product} from "../../interface/product";
import {NotificationType} from "../../enumeration/notification-enum";
import {NotifierService} from "angular-notifier";
import {Router} from "@angular/router";
import {createFormData} from "../../service/service.index";

@Component({
  selector: 'app-create-product',
  templateUrl: './create-product.component.html',
  styleUrls: ['./create-product.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None
})
export class CreateProductComponent extends ProductSharedFunctionality implements OnDestroy {
  constructor(protected override readonly changeDetectorRef: ChangeDetectorRef,
              private readonly mediaService: MediaService,
              private productService: ProductService,
              private readonly categoryService: CategoryService,
              private readonly notifier: NotifierService,
              private readonly router: Router) {
    super(changeDetectorRef);
  }

  @ViewChild('mediaSearchInput') mediaSearchInput: ElementRef;
  @ViewChild('previewProductMedia') previewProductMedia: ElementRef;
  @ViewChildren('mediaRow') modalMediaRows: ElementRef[];
  @ViewChildren('categoryRow') modalCategoryRows: ElementRef[];

  selectedMediaFromPickup: Media;
  filteredMedias$: Observable<Media[]>;
  filteredCategories$: Observable<Category[]>;
  selectedCategoryFromPickup: Category;

  subscriptions: Subscription[] = [];

  createProductFormGroup = new FormGroup({
    name: new FormControl('', [Validators.required, Validators.minLength(4),
      Validators.maxLength(120)], [nameValidator.validateName(this.productService)]),
    identifier: new FormControl('', [Validators.required,
      Validators.minLength(4), Validators.maxLength(10)], [identifierValidator.validateIdentifier(this.productService)]),
    description: new FormControl(''),
    price: new FormControl(0, [Validators.required, positiveNumberValidator]),
    media: new FormControl(undefined),
    pkOfFile: new FormControl('')
  });

  public selectCategoryMedia(selectedRow: HTMLTableRowElement, media: Media): void {
    this.modalMediaRows.forEach(x => {
      x.nativeElement.closest('tr').classList.remove('selected-media');
    });

    selectedRow.classList.add('selected-media');
    this.selectedMediaFromPickup = media;
  }

  public submitMediaSelection(): void {
    this.previewProductMedia.nativeElement.src = this.selectedMediaFromPickup.mediaUrl;
    this.mediaPath = this.selectedMediaFromPickup.mediaUrl;
    document.getElementById('modal-close-btn').click();

    this.pkOfFile.setValue(this.selectedMediaFromPickup.pkOfFile);
    this.media.setValue('');
    this.mediaInput.nativeElement.value = '';
  }

  public filterMediaByName(): void {
    const mediaSearchInputElement = this.mediaSearchInput.nativeElement;
    if (mediaSearchInputElement.value.trim() === '') {
      return;
    }
    this.filteredMedias$ = this.mediaService.filterMediaByName(mediaSearchInputElement.value);
  }

  public filterCategory($event: Event): void {
    this.filteredCategories$ = this.categoryService.filterCategoriesByName((<HTMLInputElement>$event.target).value);
  }

  get name() {
    return this.createProductFormGroup.get('name');
  }

  get identifier() {
    return this.createProductFormGroup.get('identifier');
  }

  get media() {
    return this.createProductFormGroup.get('media');
  }

  get pkOfFile() {
    return this.createProductFormGroup.get('pkOfFile');
  }

  get price() {
    return this.createProductFormGroup.get('price');
  }

  get description() {
    return this.createProductFormGroup.get('description');
  }

  public selectCategory(selectedRow: HTMLTableRowElement, category: Category) {
    this.modalCategoryRows.forEach(x => {
      x.nativeElement.closest('tr').classList.remove('selected-category');
    });

    selectedRow.classList.add('selected-category');
    this.selectedCategoryFromPickup = category;
  }

  public submitCategorySelection(): void {
    document.getElementById('category-modal-close-btn').click();
  }

  public createProduct(): void {
    if (!this.mediaInput.nativeElement.files[0] && this.pkOfFile.value === '') {
      this.notifier.notify(NotificationType.INFO, "Please select a Category media!");
      return;
    } else if (!this.selectedCategoryFromPickup) {
      this.notifier.notify(NotificationType.INFO, "Please select a Category for the product!");
    }

    const product: Product = {
      name: this.name.value,
      identifier: this.identifier.value,
      price: this.price.value,
      description: this.description.value,
      media: (<HTMLInputElement>document.getElementById('product-media-input')).files[0],
      pkOfFile: this.pkOfFile.value,
      categoryIdentifier: this.selectedCategoryFromPickup.identifier
    };

    const subscription = this.productService.createProduct(createFormData(product))
      .pipe(
        catchError((err) => {
          this.notifier.notify(NotificationType.ERROR, err.err.message);
          throw err;
        })
      ).subscribe({
        next: (response) => {
          this.router.navigateByUrl('/admin/cockpit').then(() => {
            this.notifier.notify(NotificationType.SUCCESS, `${response.message}`);
          });
        }
      });

    this.subscriptions.push(subscription);
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(x => x.unsubscribe());
  }
}

class identifierValidator {
  static validateIdentifier(productService: ProductService): AsyncValidatorFn {
    return (control: AbstractControl): Observable<ValidationErrors> => {
      return productService.isProductByIdentifierPresent(control.value)
        .pipe(map((result: boolean) => result ? {identifierPresent: true} : null));
    }
  }
}

class nameValidator {
  static validateName(productService: ProductService): AsyncValidatorFn {
    return (control: AbstractControl): Observable<ValidationErrors> => {
      return productService.isProductByNamePresent(control.value)
        .pipe(map((result: boolean) => result ? {namePresent: true} : null));
    }
  }
}
