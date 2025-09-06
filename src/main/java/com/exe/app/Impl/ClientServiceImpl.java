package com.exe.app.Impl;

import com.exe.app.Dto.ClientDto;
import com.exe.app.Repository.ClientRepository;
import com.exe.app.Service.ClientService;
import com.exe.app.entity.Client;
import com.exe.app.exception.NotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository, ModelMapper modelMapper) {
        this.clientRepository = clientRepository;
        this.modelMapper = modelMapper;
    }

    //get all
    @Override
    public List<ClientDto> getAllClients() {
        return clientRepository.findAll()
                .stream()
                .map(client -> modelMapper.map(client, ClientDto.class))
                .collect(Collectors.toList());
    }

    //get by id
    @Override
    public ClientDto getClientById(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado con el id: " + clientId));
        return modelMapper.map(client, ClientDto.class);
    }

    //create client
    @Override
    public ClientDto createClient(ClientDto clientDto) {
        if (clientDto.getDoc() != null && clientRepository.existByDoc(clientDto.getDoc())) {
            throw new IllegalArgumentException("Documento ya existente.");
        }
        if (clientDto.getEmail() != null && clientRepository.existByEmail(clientDto.getEmail())) {
            throw new IllegalArgumentException("Email ya existente.");
        }

        Client client = modelMapper.map(clientDto, Client.class);
        Client saved = clientRepository.save(client);
        return modelMapper.map(saved, ClientDto.class);
    }

    //update client
    @Override
    public ClientDto updateClient(Long clientId, ClientDto clientDto) {
        Client existingClient = clientRepository.findById(clientId)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado por el id: " + clientId));

        // verificar si el doc ya existe en otro cliente
        if (clientDto.getDoc() != null && !clientDto.getDoc().equals(existingClient.getDoc())
            && clientRepository.existByDoc(clientDto.getDoc())) {
            throw new IllegalArgumentException("Documento ya existente en otro cliente.");
        }

        //lo mismo con el email
        if (clientDto.getEmail() != null && !clientDto.getEmail().equals(existingClient.getEmail())
                && clientRepository.existByEmail(clientDto.getEmail())) {
            throw new IllegalArgumentException("Email ya existente en otro cliente.");
        }

        if (clientDto.getDoc() != null) existingClient.setDoc(clientDto.getDoc());
        if (clientDto.getEmail() != null) existingClient.setEmail(clientDto.getEmail());
        if (clientDto.getName() != null) existingClient.setName(clientDto.getName());

        Client updated = clientRepository.save(existingClient);
        return modelMapper.map(updated, ClientDto.class);
    }

    //delete client
    @Override
    public boolean deleteClient(Long clientId) {
        if (!clientRepository.existsById(clientId)) {
            throw new NotFoundException("Cliente no encontrado con el id: " + clientId);
        }
        clientRepository.deleteById(clientId);
        return true;
    }
}
