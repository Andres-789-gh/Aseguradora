package com.exe.app.Impl;

import com.exe.app.Dto.ClaimDto;
import com.exe.app.Repository.ClaimRepository;
import com.exe.app.Repository.PolicyRepository;
import com.exe.app.Service.ClaimService;
import com.exe.app.entity.Claim;
import com.exe.app.entity.Policy;
import com.exe.app.exception.NotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClaimServiceImpl implements ClaimService {

    private final ClaimRepository claimRepository;
    private final PolicyRepository policyRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ClaimServiceImpl(ClaimRepository claimRepository, PolicyRepository policyRepository, ModelMapper modelMapper) {
        this.claimRepository = claimRepository;
        this.policyRepository = policyRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ClaimDto> getAllClaims() {
        return claimRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ClaimDto getClaimById(Long claimId) {
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new NotFoundException("Siniestro no encontrado con el id: " + claimId));
        return convertToDto(claim);
    }

    @Override
    public ClaimDto createClaim(ClaimDto claimDto) {
        Policy policy = policyRepository.findById(claimDto.getPolicyId())
                .orElseThrow(() -> new NotFoundException("Poliza no encontrada con el id: " + claimDto.getPolicyId()));

        Claim claim = new Claim();
        claim.setPolicy(policy);
        claim.setClaimDate(claimDto.getClaimDate());
        claim.setDescription(claimDto.getDescription());
        claim.setEstimatedAmount(claimDto.getEstimatedAmount());
        claim.setStatus(claimDto.getStatus());

        Claim saved = claimRepository.save(claim);
        return convertToDto(saved);
    }

    @Override
    public ClaimDto updateClaim(Long claimId, ClaimDto claimDto) {
        Claim existingClaim = claimRepository.findById(claimId)
                .orElseThrow(() -> new NotFoundException("Siniestro no encontrado con el id: " + claimId));

        if (claimDto.getPolicyId() != null) {
            Policy policy = policyRepository.findById(claimDto.getPolicyId())
                    .orElseThrow(() -> new NotFoundException("Poliza no encontrada con el id: " + claimDto.getPolicyId()));
            existingClaim.setPolicy(policy);
        }

        if (claimDto.getClaimDate() != null) existingClaim.setClaimDate(claimDto.getClaimDate());
        if (claimDto.getDescription() != null) existingClaim.setDescription(claimDto.getDescription());
        if (claimDto.getEstimatedAmount() != null) existingClaim.setEstimatedAmount(claimDto.getEstimatedAmount());
        if (claimDto.getStatus() != null) existingClaim.setStatus(claimDto.getStatus());

        Claim updated = claimRepository.save(existingClaim);
        return convertToDto(updated);
    }

    @Override
    public boolean deleteClaim(Long claimId) {
        if (!claimRepository.existsById(claimId)) {
            throw new NotFoundException("Siniestro no encontrado con el id: " + claimId);
        }
        claimRepository.deleteById(claimId);
        return true;
    }

    private ClaimDto convertToDto(Claim claim) {
        ClaimDto dto = new ClaimDto();
        dto.setId(claim.getId());
        dto.setPolicyId(claim.getPolicy().getId());
        dto.setPolicyNumber(claim.getPolicy().getPolicyNumber());
        dto.setClaimDate(claim.getClaimDate());
        dto.setDescription(claim.getDescription());
        dto.setEstimatedAmount(claim.getEstimatedAmount());
        dto.setStatus(claim.getStatus());
        return dto;
    }
}