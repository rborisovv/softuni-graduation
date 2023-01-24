import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  ElementRef, OnDestroy,
  OnInit,
  ViewChild
} from '@angular/core';
import {CategoryService} from "../../service/category.service";
import {ActivatedRoute, ParamMap, Router} from "@angular/router";
import {NotifierService} from "angular-notifier";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {createFormData} from "../../service/service.index";
import {NotificationType} from "../../enumeration/notification-enum";
import {Subscription} from "rxjs";
import {CategoryUpdate} from "../../interface/category.update";

@Component({
  selector: 'app-update-categories',
  templateUrl: './update-category.component.html',
  styleUrls: ['./update-category.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class UpdateCategoryComponent implements OnInit, OnDestroy {
  private subscriptions: Subscription[] = [];
  private oldName: string = '';
  private oldCategoryIdentifier: string = '';

  constructor(private categoryService: CategoryService, private router: Router, private notifier: NotifierService,
              private activatedRoute: ActivatedRoute, private changeDetectorRef: ChangeDetectorRef) {
  }

  ngOnInit(): void {
    let identifier: string = '';
    const routerSubscription = this.activatedRoute.paramMap.subscribe((params: ParamMap) => {
      identifier = params.get('identifier');
    });
    this.oldCategoryIdentifier = identifier;

    const subscription = this.categoryService.loadCategory(identifier)
      .subscribe({
        next: (response) => {
          this.oldName = response.name;
          this.setName(response.name);
          this.setIdentifier(response.identifier);
          this.setProductPrefix(response.productNamePrefix);
          this.imageSrc = response.mediaUrl;
          this.changeDetectorRef.markForCheck();
        }
      });

    this.subscriptions.push(routerSubscription);
    this.subscriptions.push(subscription);
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(x => x.unsubscribe());
  }

  createCategoryFormGroup = new FormGroup({
    name: new FormControl('', [Validators.required, Validators.minLength(5),
      Validators.maxLength(40)]),
    identifier: new FormControl('', [Validators.required,
      Validators.minLength(4), Validators.maxLength(10)]),
    productNamePrefix: new FormControl('', [Validators.maxLength(30)]),
    media: new FormControl('')
  });

  @ViewChild('media') mediaInput: ElementRef;

  mediaPath: string;
  imageSrc: string;


  public handleMediaUploadClick(): void {
    this.mediaInput.nativeElement.click();
  }

  public onMediaUpload(event: any) {
    this.mediaPath = this.mediaInput.nativeElement.value;
    this.readURL(event);
  }

  readURL(event: any): void {
    if (event.target.files && event.target.files[0]) {
      const file = event.target.files[0];

      const reader = new FileReader();
      reader.onload = () => {
        this.imageSrc = reader.result as string;
        this.changeDetectorRef.markForCheck();
      };

      reader.readAsDataURL(file);
    }
  }

  updateCategory(): void {
    const categoryData: CategoryUpdate = {
      name: this.name.value,
      oldName: this.oldName,
      identifier: this.identifier.value,
      oldCategoryIdentifier: this.oldCategoryIdentifier,
      productNamePrefix: this.productNamePrefix.value,
      media: this.mediaInput.nativeElement.files[0]
    }

    const subscription = this.categoryService.updateCategory(createFormData(categoryData))
      .subscribe({
        next: (response) => {
          this.router.navigateByUrl('/admin/cockpit').then(() => {
            this.notifier.notify(NotificationType.SUCCESS, `${response.message}`);
          });
        }
      });

    this.subscriptions.push(subscription);
  }

  get name() {
    return this.createCategoryFormGroup.get('name');
  }

  get identifier() {
    return this.createCategoryFormGroup.get('identifier');
  }

  get productNamePrefix() {
    return this.createCategoryFormGroup.get('productNamePrefix');
  }

  get media() {
    return this.createCategoryFormGroup.get('media');
  }

  public setName(name: string) {
    this.createCategoryFormGroup.controls['name'].setValue(name);
  }

  public setIdentifier(identifier: string) {
    this.createCategoryFormGroup.controls['identifier'].setValue(identifier);
  }

  public setProductPrefix(productPrefix: string) {
    this.createCategoryFormGroup.controls['productNamePrefix'].setValue(productPrefix);
  }
}

//TODO: Create validations for user input (Lower or Uppercase)
