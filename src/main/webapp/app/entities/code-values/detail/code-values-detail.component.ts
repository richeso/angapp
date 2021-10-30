import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICodeValues } from '../code-values.model';

@Component({
  selector: 'jhi-code-values-detail',
  templateUrl: './code-values-detail.component.html',
})
export class CodeValuesDetailComponent implements OnInit {
  codeValues: ICodeValues | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ codeValues }) => {
      this.codeValues = codeValues;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
