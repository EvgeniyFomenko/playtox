package org.example.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.example.pojo.Account;

import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
@AllArgsConstructor
public class AccountService {
    @Getter
    volatile private AtomicInteger countTransaction;

    public synchronized void transferMoneyFromFirstToSecondAccounts(Account first, Account second, int sum) {
        if (sum < 0) {
            log.error("Сумма перевода не может быть отрицательной");
            return;
        }

        if (first.getMoney() < sum) {
            log.error("Не достаточно денег");
            return;
        }
        log.info(String.format("Start transaction with summ = %d and one = %s to two = %s", sum, first, second));
        first.setMoney(first.getMoney() - sum);


        second.setMoney(second.getMoney() + sum);
        log.info(String.format("End transaction with one = %s and two = %s, transaction numb = %d", first, second, countTransaction.addAndGet(1)));


    }
}
