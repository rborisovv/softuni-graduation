import {Component, OnInit} from '@angular/core';
import {Store} from "@ngrx/store";
import {selectUser} from "../../store/selector/auth.selector";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  constructor(private readonly store: Store, private http: HttpClient) {
  }

  ngOnInit(): void {
    this.http.get('http://localhost:8080/home', {withCredentials: true, responseType: "text"}).subscribe(console.log);
    this.store.select(selectUser).subscribe(x => console.log(x));
    console.log('happy')
    this.store.select(selectUser).subscribe(x => console.log(x));
  }
}
