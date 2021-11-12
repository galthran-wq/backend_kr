package com.resitplatform.application.query;

import com.resitplatform.api.dto.ResitDto;
import com.resitplatform.api.query.GetResits;
import com.resitplatform.api.query.GetResitsResult;
import com.resitplatform.application.ResitAssembler;
import com.resitplatform.bus.QueryHandler;
import com.resitplatform.domain.model.Resit;
import com.resitplatform.domain.repository.ResitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GetResitsHandler implements QueryHandler<GetResitsResult, GetResits> {

    private final ResitRepository resitRepository;

    // todo test, not sure how it works with several fields empty
    @Override
    public GetResitsResult handle(GetResits query) {
//        List<Resit> resitList = resitRepository
//                .findByFilters(query.getName(), query.getTeacherName(), query.getLimit(), query.getOffset());
//
//        List<ResitDto> results = new ArrayList<>();
//
//        resitList.forEach(resit -> results.add(ResitAssembler.assemble(resit)));

//        return new GetResitsResult(results, results.size());
        return new GetResitsResult(new ArrayList<>(), 0);
    }
}
