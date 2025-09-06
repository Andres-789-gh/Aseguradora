package com.exe.app.Service;

import com.exe.app.Dto.ClaimDto;
import java.util.List;

public interface ClaimService {
    List<ClaimDto> getAllClaims();
    ClaimDto getClaimById(Long claimId);
    ClaimDto createClaim(ClaimDto claimDto);
    ClaimDto updateClaim(Long claimId, ClaimDto claimDto);
    boolean deleteClaim(Long claimId);
}