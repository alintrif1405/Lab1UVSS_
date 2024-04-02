package org.example.service;

import org.example.model.AccountHistory;
import org.example.model.User;
import org.example.repository.AccountHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AccountHistoryService {
    private final AccountHistoryRepository accountHistoryRepository;

    @Autowired
    public AccountHistoryService(AccountHistoryRepository accountHistoryRepository){
        this.accountHistoryRepository = accountHistoryRepository;
    }

    public boolean makeNewEntry(User oldUser, User newUser){
        AccountHistory newEntry = new AccountHistory();
        newEntry.setOldEmail(oldUser.getEmail());
        newEntry.setOldFirstname(oldUser.getFirstname());
        newEntry.setOldLastname(oldUser.getLastname());
        newEntry.setOldPassword(oldUser.getPassword());
        newEntry.setOldRole(oldUser.getRole());
        newEntry.setNewEmail(newUser.getEmail());
        newEntry.setNewFirstname(newUser.getFirstname());
        newEntry.setNewLastname(newUser.getLastname());
        newEntry.setNewPassword(newUser.getPassword());
        newEntry.setNewRole(newUser.getRole());
        newEntry.setDateModified(LocalDate.now());
        this.accountHistoryRepository.save(newEntry);
        return true;
    }
}
