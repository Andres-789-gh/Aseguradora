package com.exe.app.Service;

import com.exe.app.Dto.ClientDto;
import com.exe.app.entity.Client;

import java.util.List;

public interface ClientService {
    List<ClientDto> getAllClients();
    ClientDto getClientById(Long clientId);
    ClientDto createClient(ClientDto clientDto);
    ClientDto updateClient(Long clientId, ClientDto clientDto);
    boolean deleteClient(Long clientId);
}
