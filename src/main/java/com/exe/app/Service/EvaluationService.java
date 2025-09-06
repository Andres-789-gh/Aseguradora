package com.exe.app.Service;

import com.exe.app.Dto.EvaluationDto;
import java.util.List;

public interface EvaluationService {
    List<EvaluationDto> getAllEvaluations();
    EvaluationDto getEvaluationById(Long evaluationId);
    EvaluationDto createEvaluation(EvaluationDto evaluationDto);
    EvaluationDto updateEvaluation(Long evaluationId, EvaluationDto evaluationDto);
    boolean deleteEvaluation(Long evaluationId);
}