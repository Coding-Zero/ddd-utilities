package com.codingzero.dddutilities.transaction.manager;

import com.codingzero.dddutilities.transaction.TransactionContext;
import com.codingzero.dddutilities.transaction.TransactionalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TransactionCoordinatorHelperTest {

    private TransactionCoordinatorHelper manager;

    @BeforeEach
    public void setUp() {
        manager = new TransactionCoordinatorHelper();
    }

    @Test
    public void testRegister() {
        TransactionalService service = mock(TransactionalService.class);
        manager.register(service);
        verify(service, times(1)).onRegister();
    }

    @Test
    public void testStart() {
        TransactionalService service = mock(TransactionalService.class);
        manager.register(service);
        manager.start();
        verify(service, times(1)).onStart(any(TransactionContext.class));
    }

    @Test
    public void testCommit() {
        TransactionalService service = mock(TransactionalService.class);
        manager.register(service);
        manager.start();
        manager.commit();
        verify(service, times(1)).onCommit(any(TransactionContext.class));
    }

    @Test
    public void testRollback() {
        TransactionalService service = mock(TransactionalService.class);
        manager.register(service);
        manager.start();
        manager.rollback();
        verify(service, times(1)).onRollback(any(TransactionContext.class));
    }
}
