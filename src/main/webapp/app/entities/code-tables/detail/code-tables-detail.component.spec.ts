import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CodeTablesDetailComponent } from './code-tables-detail.component';

describe('CodeTables Management Detail Component', () => {
  let comp: CodeTablesDetailComponent;
  let fixture: ComponentFixture<CodeTablesDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CodeTablesDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ codeTables: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CodeTablesDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CodeTablesDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load codeTables on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.codeTables).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
