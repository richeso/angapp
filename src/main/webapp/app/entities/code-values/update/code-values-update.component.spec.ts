jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CodeValuesService } from '../service/code-values.service';
import { ICodeValues, CodeValues } from '../code-values.model';
import { ICodeTables } from 'app/entities/code-tables/code-tables.model';
import { CodeTablesService } from 'app/entities/code-tables/service/code-tables.service';

import { CodeValuesUpdateComponent } from './code-values-update.component';

describe('CodeValues Management Update Component', () => {
  let comp: CodeValuesUpdateComponent;
  let fixture: ComponentFixture<CodeValuesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let codeValuesService: CodeValuesService;
  let codeTablesService: CodeTablesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CodeValuesUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CodeValuesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CodeValuesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    codeValuesService = TestBed.inject(CodeValuesService);
    codeTablesService = TestBed.inject(CodeTablesService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call CodeTables query and add missing value', () => {
      const codeValues: ICodeValues = { id: 456 };
      const codeTables: ICodeTables = { id: 74890 };
      codeValues.codeTables = codeTables;

      const codeTablesCollection: ICodeTables[] = [{ id: 76835 }];
      jest.spyOn(codeTablesService, 'query').mockReturnValue(of(new HttpResponse({ body: codeTablesCollection })));
      const additionalCodeTables = [codeTables];
      const expectedCollection: ICodeTables[] = [...additionalCodeTables, ...codeTablesCollection];
      jest.spyOn(codeTablesService, 'addCodeTablesToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ codeValues });
      comp.ngOnInit();

      expect(codeTablesService.query).toHaveBeenCalled();
      expect(codeTablesService.addCodeTablesToCollectionIfMissing).toHaveBeenCalledWith(codeTablesCollection, ...additionalCodeTables);
      expect(comp.codeTablesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const codeValues: ICodeValues = { id: 456 };
      const codeTables: ICodeTables = { id: 78522 };
      codeValues.codeTables = codeTables;

      activatedRoute.data = of({ codeValues });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(codeValues));
      expect(comp.codeTablesSharedCollection).toContain(codeTables);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CodeValues>>();
      const codeValues = { id: 123 };
      jest.spyOn(codeValuesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ codeValues });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: codeValues }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(codeValuesService.update).toHaveBeenCalledWith(codeValues);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CodeValues>>();
      const codeValues = new CodeValues();
      jest.spyOn(codeValuesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ codeValues });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: codeValues }));
      saveSubject.complete();

      // THEN
      expect(codeValuesService.create).toHaveBeenCalledWith(codeValues);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CodeValues>>();
      const codeValues = { id: 123 };
      jest.spyOn(codeValuesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ codeValues });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(codeValuesService.update).toHaveBeenCalledWith(codeValues);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCodeTablesById', () => {
      it('Should return tracked CodeTables primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCodeTablesById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
