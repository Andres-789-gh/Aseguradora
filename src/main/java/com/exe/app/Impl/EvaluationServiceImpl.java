package com.exe.app.Impl;

import com.exe.app.Dto.EvaluationDto;
import com.exe.app.Repository.AdjusterRepository;
import com.exe.app.Repository.ClaimRepository;
import com.exe.app.Repository.EvaluationRepository;
import com.exe.app.Service.EvaluationService;
import com.exe.app.entity.Adjuster;
import com.exe.app.entity.Claim;
import com.exe.app.entity.Evaluation;
import com.exe.app.exception.NotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EvaluationServiceImpl implements EvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final ClaimRepository claimRepository;
    private final AdjusterRepository adjusterRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public EvaluationServiceImpl(EvaluationRepository evaluationRepository, ClaimRepository claimRepository,
                                 AdjusterRepository adjusterRepository, ModelMapper modelMapper) {
        this.evaluationRepository = evaluationRepository;
        this.claimRepository = claimRepository;
        this.adjusterRepository = adjusterRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<EvaluationDto> getAllEvaluations() {
        return evaluationRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public EvaluationDto getEvaluationById(Long evaluationId) {
        Evaluation evaluation = evaluationRepository.findById(evaluationId)
                .orElseThrow(() -> new NotFoundException("Evaluacion no encontrada con el id: " + evaluationId));
        return convertToDto(evaluation);
    }

    @Override
    public EvaluationDto createEvaluation(EvaluationDto evaluationDto) {
        Claim claim = claimRepository.findById(evaluationDto.getClaimId())
                .orElseThrow(() -> new NotFoundException("Siniestro no encontrado con el id: " + evaluationDto.getClaimId()));

        Adjuster adjuster = adjusterRepository.findById(evaluationDto.getAdjusterId())
                .orElseThrow(() -> new NotFoundException("Ajustador no encontrado con el id: " + evaluationDto.getAdjusterId()));

        Evaluation evaluation = new Evaluation();
        evaluation.setClaim(claim);
        evaluation.setAdjuster(adjuster);
        evaluation.setEvaluationDate(evaluationDto.getEvaluationDate());
        evaluation.setVerdict(evaluationDto.getVerdict());
        evaluation.setApprovedAmount(evaluationDto.getApprovedAmount());

        Evaluation saved = evaluationRepository.save(evaluation);
        return convertToDto(saved);
    }

    @Override
    public EvaluationDto updateEvaluation(Long evaluationId, EvaluationDto evaluationDto) {
        Evaluation existingEvaluation = evaluationRepository.findById(evaluationId)
                .orElseThrow(() -> new NotFoundException("Evaluacion no encontrada con el id: " + evaluationId));

        if (evaluationDto.getClaimId() != null) {
            Claim claim = claimRepository.findById(evaluationDto.getClaimId())
                    .orElseThrow(() -> new NotFoundException("Siniestro no encontrado con el id: " + evaluationDto.getClaimId()));
            existingEvaluation.setClaim(claim);
        }

        if (evaluationDto.getAdjusterId() != null) {
            Adjuster adjuster = adjusterRepository.findById(evaluationDto.getAdjusterId())
                    .orElseThrow(() -> new NotFoundException("Ajustador no encontrado con el id: " + evaluationDto.getAdjusterId()));
            existingEvaluation.setAdjuster(adjuster);
        }

        if (evaluationDto.getEvaluationDate() != null) existingEvaluation.setEvaluationDate(evaluationDto.getEvaluationDate());
        if (evaluationDto.getVerdict() != null) existingEvaluation.setVerdict(evaluationDto.getVerdict());
        if (evaluationDto.getApprovedAmount() != null) existingEvaluation.setApprovedAmount(evaluationDto.getApprovedAmount());

        Evaluation updated = evaluationRepository.save(existingEvaluation);
        return convertToDto(updated);
    }

    @Override
    public boolean deleteEvaluation(Long evaluationId) {
        if (!evaluationRepository.existsById(evaluationId)) {
            throw new NotFoundException("Evaluacion no encontrada con el id: " + evaluationId);
        }
        evaluationRepository.deleteById(evaluationId);
        return true;
    }

    private EvaluationDto convertToDto(Evaluation evaluation) {
        EvaluationDto dto = new EvaluationDto();
        dto.setId(evaluation.getId());
        dto.setClaimId(evaluation.getClaim().getId());
        dto.setClaimDescription(evaluation.getClaim().getDescription());
        dto.setAdjusterId(evaluation.getAdjuster().getId());
        dto.setAdjusterName(evaluation.getAdjuster().getName());
        dto.setEvaluationDate(evaluation.getEvaluationDate());
        dto.setVerdict(evaluation.getVerdict());
        dto.setApprovedAmount(evaluation.getApprovedAmount());
        return dto;
    }
}