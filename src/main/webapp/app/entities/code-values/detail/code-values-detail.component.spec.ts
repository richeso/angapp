import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CodeValuesDetailComponent } from './code-values-detail.component';

describe('CodeValues Management Detail Component', () => {
  let comp: CodeValuesDetailComponent;
  let fixture: ComponentFixture<CodeValuesDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CodeValuesDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ codeValues: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CodeValuesDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CodeValuesDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load codeValues on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.codeValues).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
