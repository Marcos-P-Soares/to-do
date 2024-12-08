package com.aprendizado.to_do.DTO;

import java.util.List;
import java.util.UUID;

public record UserOutputDTO(UUID id, String userName, String email, List<TaskOutputDTO> tasks) {
    
}
