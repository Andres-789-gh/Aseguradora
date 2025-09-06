package com.exe.app.Impl;

import com.exe.app.Dto.PolicyDto;
import com.exe.app.Repository.ClientRepository;
import com.exe.app.Repository.PolicyRepository;
import com.exe.app.Service.PolicyService;
import com.exe.app.entity.Client;
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
public class PolicyServiceImpl implements PolicyService {

    private final PolicyRepository policyRepository;
    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public PolicyServiceImpl(PolicyRepository policyRepository, ClientRepository clientRepository, ModelMapper modelMapper) {
        this.policyRepository = policyRepository;
        this.clientRepository = clientRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<PolicyDto> getAllPolicies() {
        return policyRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PolicyDto getPolicyById(Long policyId) {
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new NotFoundException("Póliza no encontrada con el id: " + policyId));
        return convertToDto(policy);
    }



    @Override
    public PolicyDto createPolicy(PolicyDto policyDto) {
        Client client = clientRepository.findById(policyDto.getClientId())
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado con el id: " + policyDto.getClientId()));

        if (policyRepository.existsByPolicyNumber(policyDto.getPolicyNumber())) {
            throw new IllegalArgumentException("Número de póliza ya existe.");
        }

        Policy policy = new Policy();
        policy.setClient(client);
        policy.setPolicyNumber(policyDto.getPolicyNumber());
        policy.setPolicyType(policyDto.getPolicyType());
        policy.setPremium(policyDto.getPremium());
        policy.setValidFrom(policyDto.getValidFrom());
        policy.setValidUntil(policyDto.getValidUntil());

        Policy saved = policyRepository.save(policy);
        return convertToDto(saved);
    }

    @Override
    public PolicyDto updatePolicy(Long policyId, PolicyDto policyDto) {
        Policy existingPolicy = policyRepository.findById(policyId)
                .orElseThrow(() -> new NotFoundException("Póliza no encontrada con el id: " + policyId));

        if (policyDto.getClientId() != null) {
            Client client = clientRepository.findById(policyDto.getClientId())
                    .orElseThrow(() -> new NotFoundException("Cliente no encontrado con el id: " + policyDto.getClientId()));
            existingPolicy.setClient(client);
        }

        if (policyDto.getPolicyNumber() != null && !policyDto.getPolicyNumber().equals(existingPolicy.getPolicyNumber())
                && policyRepository.existsByPolicyNumber(policyDto.getPolicyNumber())) {
            throw new IllegalArgumentException("Número de póliza ya existe.");
        }

        if (policyDto.getPolicyNumber() != null) existingPolicy.setPolicyNumber(policyDto.getPolicyNumber());
        if (policyDto.getPolicyType() != null) existingPolicy.setPolicyType(policyDto.getPolicyType());
        if (policyDto.getPremium() != null) existingPolicy.setPremium(policyDto.getPremium());
        if (policyDto.getValidFrom() != null) existingPolicy.setValidFrom(policyDto.getValidFrom());
        if (policyDto.getValidUntil() != null) existingPolicy.setValidUntil(policyDto.getValidUntil());

        Policy updated = policyRepository.save(existingPolicy);
        return convertToDto(updated);
    }

    @Override
    public boolean deletePolicy(Long policyId) {
        if (!policyRepository.existsById(policyId)) {
            throw new NotFoundException("Póliza no encontrada con el id: " + policyId);
        }
        policyRepository.deleteById(policyId);
        return true;
    }

    private PolicyDto convertToDto(Policy policy) {
        PolicyDto dto = new PolicyDto();
        dto.setId(policy.getId());
        dto.setClientId(policy.getClient().getId());
        dto.setClientName(policy.getClient().getName());
        dto.setPolicyNumber(policy.getPolicyNumber());
        dto.setPolicyType(policy.getPolicyType());
        dto.setPremium(policy.getPremium());
        dto.setValidFrom(policy.getValidFrom());
        dto.setValidUntil(policy.getValidUntil());
        return dto;
    }
}