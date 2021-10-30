import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CodeTablesService } from '../service/code-tables.service';

import { CodeTablesComponent } from './code-tables.component';

describe('CodeTables Management Component', () => {
  let comp: CodeTablesComponent;
  let fixture: ComponentFixture<CodeTablesComponent>;
  let service: CodeTablesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CodeTablesComponent],
    })
      .overrideTemplate(CodeTablesComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CodeTablesComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CodeTablesService);

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
    expect(comp.codeTables?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
