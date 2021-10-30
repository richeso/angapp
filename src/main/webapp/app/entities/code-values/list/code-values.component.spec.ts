import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CodeValuesService } from '../service/code-values.service';

import { CodeValuesComponent } from './code-values.component';

describe('CodeValues Management Component', () => {
  let comp: CodeValuesComponent;
  let fixture: ComponentFixture<CodeValuesComponent>;
  let service: CodeValuesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CodeValuesComponent],
    })
      .overrideTemplate(CodeValuesComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CodeValuesComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CodeValuesService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.codeValues?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
