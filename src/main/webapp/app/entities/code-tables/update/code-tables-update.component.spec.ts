jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CodeTablesService } from '../service/code-tables.service';
import { ICodeTables, CodeTables } from '../code-tables.model';

import { CodeTablesUpdateComponent } from './code-tables-update.component';

describe('CodeTables Management Update Component', () => {
  let comp: CodeTablesUpdateComponent;
  let fixture: ComponentFixture<CodeTablesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let codeTablesService: CodeTablesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CodeTablesUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CodeTablesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CodeTablesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    codeTablesService = TestBed.inject(CodeTablesService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const codeTables: ICodeTables = { id: 456 };

      activatedRoute.data = of({ codeTables });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(codeTables));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CodeTables>>();
      const codeTables = { id: 123 };
      jest.spyOn(codeTablesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ codeTables });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: codeTables }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(codeTablesService.update).toHaveBeenCalledWith(codeTables);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CodeTables>>();
      const codeTables = new CodeTables();
      jest.spyOn(codeTablesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ codeTables });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: codeTables }));
      saveSubject.complete();

      // THEN
      expect(codeTablesService.create).toHaveBeenCalledWith(codeTables);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CodeTables>>();
      const codeTables = { id: 123 };
      jest.spyOn(codeTablesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ codeTables });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(codeTablesService.update).toHaveBeenCalledWith(codeTables);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
