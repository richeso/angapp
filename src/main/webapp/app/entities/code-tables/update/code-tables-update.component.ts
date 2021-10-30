import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICodeTables, CodeTables } from '../code-tables.model';
import { CodeTablesService } from '../service/code-tables.service';

@Component({
  selector: 'jhi-code-tables-update',
  templateUrl: './code-tables-update.component.html',
})
export class CodeTablesUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    tableName: [null, [Validators.required, Validators.minLength(3)]],
  });

  constructor(protected codeTablesService: CodeTablesService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ codeTables }) => {
      this.updateForm(codeTables);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const codeTables = this.createFromForm();
    if (codeTables.id !== undefined) {
      this.subscribeToSaveResponse(this.codeTablesService.update(codeTables));
    } else {
      this.subscribeToSaveResponse(this.codeTablesService.create(codeTables));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICodeTables>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(codeTables: ICodeTables): void {
    this.editForm.patchValue({
      id: codeTables.id,
      tableName: codeTables.tableName,
    });
  }

  protected createFromForm(): ICodeTables {
    return {
      ...new CodeTables(),
      id: this.editForm.get(['id'])!.value,
      tableName: this.editForm.get(['tableName'])!.value,
    };
  }
}
