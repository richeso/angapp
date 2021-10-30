import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICodeValues, CodeValues } from '../code-values.model';
import { CodeValuesService } from '../service/code-values.service';
import { ICodeTables } from 'app/entities/code-tables/code-tables.model';
import { CodeTablesService } from 'app/entities/code-tables/service/code-tables.service';

@Component({
  selector: 'jhi-code-values-update',
  templateUrl: './code-values-update.component.html',
})
export class CodeValuesUpdateComponent implements OnInit {
  isSaving = false;

  codeTablesSharedCollection: ICodeTables[] = [];

  editForm = this.fb.group({
    id: [],
    key: [null, [Validators.required, Validators.minLength(3)]],
    value: [],
    codeTables: [],
  });

  constructor(
    protected codeValuesService: CodeValuesService,
    protected codeTablesService: CodeTablesService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ codeValues }) => {
      this.updateForm(codeValues);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const codeValues = this.createFromForm();
    if (codeValues.id !== undefined) {
      this.subscribeToSaveResponse(this.codeValuesService.update(codeValues));
    } else {
      this.subscribeToSaveResponse(this.codeValuesService.create(codeValues));
    }
  }

  trackCodeTablesById(index: number, item: ICodeTables): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICodeValues>>): void {
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

  protected updateForm(codeValues: ICodeValues): void {
    this.editForm.patchValue({
      id: codeValues.id,
      key: codeValues.key,
      value: codeValues.value,
      codeTables: codeValues.codeTables,
    });

    this.codeTablesSharedCollection = this.codeTablesService.addCodeTablesToCollectionIfMissing(
      this.codeTablesSharedCollection,
      codeValues.codeTables
    );
  }

  protected loadRelationshipsOptions(): void {
    this.codeTablesService
      .query()
      .pipe(map((res: HttpResponse<ICodeTables[]>) => res.body ?? []))
      .pipe(
        map((codeTables: ICodeTables[]) =>
          this.codeTablesService.addCodeTablesToCollectionIfMissing(codeTables, this.editForm.get('codeTables')!.value)
        )
      )
      .subscribe((codeTables: ICodeTables[]) => (this.codeTablesSharedCollection = codeTables));
  }

  protected createFromForm(): ICodeValues {
    return {
      ...new CodeValues(),
      id: this.editForm.get(['id'])!.value,
      key: this.editForm.get(['key'])!.value,
      value: this.editForm.get(['value'])!.value,
      codeTables: this.editForm.get(['codeTables'])!.value,
    };
  }
}
