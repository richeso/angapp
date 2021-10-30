import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICodeTables } from '../code-tables.model';

@Component({
  selector: 'jhi-code-tables-detail',
  templateUrl: './code-tables-detail.component.html',
})
export class CodeTablesDetailComponent implements OnInit {
  codeTables: ICodeTables | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ codeTables }) => {
      this.codeTables = codeTables;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
