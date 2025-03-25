package com.dynamika.library.service;

import com.dynamika.library.model.exception.NotFoundException;
import com.dynamika.library.entity.Client;
import com.dynamika.library.repository.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    public Client addClient(Client client) {
        return clientRepository.save(client);
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Client getClientById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Клиент не найдена: " + id));
    }

    public Client updateClient(Long id, Client clientDetails) {
        return clientRepository.findById(id)
                .map(client -> {
                    client.setFullName(clientDetails.getFullName());
                    client.setBirthDate(clientDetails.getBirthDate());
                    return clientRepository.save(client);
                })
                .orElseThrow(() -> new NotFoundException("Клиент не найдена: " + id));
    }

    public void deleteClient(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new NotFoundException("Клиент не найдена: " + id);
        }
        clientRepository.deleteById(id);
    }
}