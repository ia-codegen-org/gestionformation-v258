import { Component, OnInit } from '@angular/core';
import { Router }             from '@angular/router';

@Component({
  selector:  'app-home',
  standalone: true,
  template:  '<div></div>'
})
export class HomeComponent implements OnInit {

  constructor(
    private readonly router: Router
  ) {}

  ngOnInit(): void {
    this.router.navigate(['/dashboard']);
  }
}
