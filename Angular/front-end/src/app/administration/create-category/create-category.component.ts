import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  ElementRef,
  ViewChild,
  ViewChildren,
  ViewEncapsulation
} from '@angular/core';
import {CategoryService} from "../../service/category.service";
import {Router} from "@angular/router";
import {NotifierService} from "angular-notifier";
import {NotificationType} from "../../enumeration/notification-enum";
import {AbstractControl, AsyncValidatorFn, FormControl, FormGroup, ValidationErrors, Validators} from "@angular/forms";
import {map, Observable} from "rxjs";
import {MediaService} from "../../service/media.service";
import {Media} from "../../interface/media";
import {Category} from "../../interface/category";
import {createFormData} from "../../service/service.index";
import {CategorySharedFunctionality} from "../item.category.index";
import {Store} from "@ngrx/store";
import {createCategoryAction} from "../../store/action/category.action";

@Component({
  selector: 'app-create-categories',
  templateUrl: './create-category.component.html',
  styleUrls: ['./create-category.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None
})
export class CreateCategoryComponent extends CategorySharedFunctionality {
  constructor(private categoryService: CategoryService, private router: Router,
              private notifier: NotifierService, private mediaService: MediaService,
              protected override changeDetectorRef: ChangeDetectorRef,
              private readonly store: Store) {
    super(changeDetectorRef);
  }

  createCategoryFormGroup = new FormGroup({
    name: new FormControl('', [Validators.required, Validators.minLength(4),
      Validators.maxLength(40)], [nameValidator.validateName(this.categoryService)]),
    identifier: new FormControl('', [Validators.required,
      Validators.minLength(4), Validators.maxLength(10)], identifierValidator.validateIdentifier(this.categoryService)),
    superCategoryIdentifier: new FormControl('', [Validators.maxLength(30), Validators.minLength(4)]),
    media: new FormControl(undefined),
    pkOfFile: new FormControl('')
  });

  @ViewChild('mediaSearchInput') mediaSearchInput: ElementRef;
  @ViewChild('previewCategoryMedia') previewCategoryMedia: ElementRef;
  @ViewChildren('row') modalMediaRows: ElementRef[];

  filteredMedias$: Observable<Media[]>;
  selectedMediaFromPickup: Media;


  public filterMediaByName(): void {
    const mediaSearchInputElement = this.mediaSearchInput.nativeElement;
    if (mediaSearchInputElement.value.trim() === '') {
      return;
    }
    this.filteredMedias$ = this.mediaService.filterMediaByName(mediaSearchInputElement.value);
  }

  createCategory(): void {
    if (!this.mediaInput.nativeElement.files[0] && this.pkOfFIle.value === '') {
      this.notifier.notify(NotificationType.INFO, "Please select a Category media!");
      return;
    }

    const categoryData: Category = {
      name: this.name.value.trim(),
      identifier: this.identifier.value.trim(),
      superCategoryIdentifier: this.superCategory.value.trim(),
      media: this.mediaInput.nativeElement.files[0],
      pkOfFile: this.pkOfFIle.value
    }

    this.store.dispatch(createCategoryAction({formData: createFormData(categoryData)}));
  }

  get name() {
    return this.createCategoryFormGroup.get('name');
  }

  get identifier() {
    return this.createCategoryFormGroup.get('identifier');
  }

  get superCategory() {
    return this.createCategoryFormGroup.get('superCategoryIdentifier');
  }

  get media() {
    return this.createCategoryFormGroup.get('media');
  }

  get pkOfFIle() {
    return this.createCategoryFormGroup.get('pkOfFile');
  }

  protected override onMediaUpload(event: any) {
    super.onMediaUpload(event);
    this.selectedMediaFromPickup = null;
  }

  public selectCategoryMedia(selectedRow: HTMLTableRowElement, media: Media): void {
    this.modalMediaRows.forEach(x => {
      x.nativeElement.closest('tr').classList.remove('selected-media');
    });

    (selectedRow).classList.add('selected-media');
    this.selectedMediaFromPickup = media;
  }

  public submitMediaSelection(): void {
    this.previewCategoryMedia.nativeElement.src = this.selectedMediaFromPickup.mediaUrl;
    this.mediaPath = this.selectedMediaFromPickup.mediaUrl;
    document.getElementById('modal-close-btn').click();

    this.pkOfFIle.setValue(this.selectedMediaFromPickup.pkOfFile);
    this.media.setValue('');
    this.mediaInput.nativeElement.value = '';
  }
}

class identifierValidator {
  static validateIdentifier(categoryService: CategoryService): AsyncValidatorFn {
    return (control: AbstractControl): Observable<ValidationErrors> => {
      return categoryService.isCategoryByIdentifierPresent(control.value)
        .pipe(map((result: boolean) => result ? {identifierPresent: true} : null));
    }
  }
}

class nameValidator {
  static validateName(categoryService: CategoryService): AsyncValidatorFn {
    return (control: AbstractControl): Observable<ValidationErrors> => {
      return categoryService.isCategoryByNamePresent(control.value)
        .pipe(map((result: boolean) => result ? {namePresent: true} : null));
    }
  }
}
