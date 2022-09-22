import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  constructor(private http: HttpClient) {
  }

  ngOnInit(): void {
  }

  buttonClicked() {
    this.http.get("http://localhost:8080/user/something").subscribe(
      {
        next: value => console.log(value)
      }
    );
  }
}
