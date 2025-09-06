package com.exe.app.Service;

import com.exe.app.Dto.AdjusterDto;
import java.util.List;

public interface AdjusterService {
    List<AdjusterDto> getAllAdjusters();
    AdjusterDto getAdjusterById(Long adjusterId);
    AdjusterDto createAdjuster(AdjusterDto adjusterDto);
    AdjusterDto updateAdjuster(Long adjusterId, AdjusterDto adjusterDto);
    boolean deleteAdjuster(Long adjusterId);
}