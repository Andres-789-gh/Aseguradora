package com.exe.app.Impl;

import com.exe.app.Dto.AdjusterDto;
import com.exe.app.Repository.AdjusterRepository;
import com.exe.app.Service.AdjusterService;
import com.exe.app.entity.Adjuster;
import com.exe.app.exception.NotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdjusterServiceImpl implements AdjusterService {

    private final AdjusterRepository adjusterRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public AdjusterServiceImpl(AdjusterRepository adjusterRepository, ModelMapper modelMapper) {
        this.adjusterRepository = adjusterRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<AdjusterDto> getAllAdjusters() {
        return adjusterRepository.findAll()
                .stream()
                .map(adjuster -> modelMapper.map(adjuster, AdjusterDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public AdjusterDto getAdjusterById(Long adjusterId) {
        Adjuster adjuster = adjusterRepository.findById(adjusterId)
                .orElseThrow(() -> new NotFoundException("Ajustador no encontrado con el id: " + adjusterId));
        return modelMapper.map(adjuster, AdjusterDto.class);
    }

    @Override
    public AdjusterDto createAdjuster(AdjusterDto adjusterDto) {
        if (adjusterDto.getEmail() != null && adjusterRepository.existsByEmail(adjusterDto.getEmail())) {
            throw new IllegalArgumentException("Email ya existente.");
        }

        Adjuster adjuster = modelMapper.map(adjusterDto, Adjuster.class);
        Adjuster saved = adjusterRepository.save(adjuster);
        return modelMapper.map(saved, AdjusterDto.class);
    }

    @Override
    public AdjusterDto updateAdjuster(Long adjusterId, AdjusterDto adjusterDto) {
        Adjuster existingAdjuster = adjusterRepository.findById(adjusterId)
                .orElseThrow(() -> new NotFoundException("Ajustador no encontrado con el id: " + adjusterId));

        if (adjusterDto.getEmail() != null && !adjusterDto.getEmail().equals(existingAdjuster.getEmail())
                && adjusterRepository.existsByEmail(adjusterDto.getEmail())) {
            throw new IllegalArgumentException("Email ya existente en otro ajustador.");
        }

        if (adjusterDto.getName() != null) existingAdjuster.setName(adjusterDto.getName());
        if (adjusterDto.getEmail() != null) existingAdjuster.setEmail(adjusterDto.getEmail());
        if (adjusterDto.getPhoneNumber() != null) existingAdjuster.setPhoneNumber(adjusterDto.getPhoneNumber());

        Adjuster updated = adjusterRepository.save(existingAdjuster);
        return modelMapper.map(updated, AdjusterDto.class);
    }

    @Override
    public boolean deleteAdjuster(Long adjusterId) {
        if (!adjusterRepository.existsById(adjusterId)) {
            throw new NotFoundException("Ajustador no encontrado con el id: " + adjusterId);
        }
        adjusterRepository.deleteById(adjusterId);
        return true;
    }
}