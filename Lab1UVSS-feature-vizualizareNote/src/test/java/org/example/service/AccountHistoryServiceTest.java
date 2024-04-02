package org.example.service;

import org.example.model.AccountHistory;
import org.example.model.User;
import org.example.repository.AccountHistoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AccountHistoryServiceTest {

    @Mock
    private AccountHistoryRepository accountHistoryRepository;

    @InjectMocks
    private AccountHistoryService accountHistoryService;

    @Test
    void makeNewEntry_ValidUsers_SaveEntry() {
        // Mocking
        User oldUser = new User(1, "Old", "User", "old@example.com", "oldpass", null);
        User newUser = new User(2, "New", "User", "new@example.com", "newpass", null);

        // Test
        accountHistoryService.makeNewEntry(oldUser, newUser);

        // Verification
        verify(accountHistoryRepository).save(any(AccountHistory.class));
    }
}
