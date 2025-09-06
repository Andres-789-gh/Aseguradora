package com.exe.app.Service;

import com.exe.app.Dto.PolicyDto;

import java.util.List;

public interface PolicyService {
    List<PolicyDto> getAllPolicies();
    PolicyDto getPolicyById(Long policyId);
    PolicyDto createPolicy(PolicyDto policyDto);
    PolicyDto updatePolicy(Long policyId, PolicyDto policyDto);
    boolean deletePolicy(Long policyId);
}
